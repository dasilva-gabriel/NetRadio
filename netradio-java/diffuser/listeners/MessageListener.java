package diffuser.listeners;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import common.Logger;
import common.packets.NetPacket;
import common.packets.PacketID;
import common.protocols.listener.ListeningPacket;
import common.protocols.listener.PacketListenerHandler.IPacketListener;
import common.protocols.packets.AckmPacket;
import common.protocols.packets.MessPacket;
import common.protocols.packets.TimePacket;
import diffuser.Diffuser;
import diffuser.handlers.messages.Messages;
import diffuser.handlers.messages.MessagesHandler;

/**
 * Listener qui gère les messages a diffuser
 *
 * @see MessagesHandler gestionnaire des messages a diffuser
 */
public class MessageListener implements IPacketListener {

	private Diffuser diffuser;

	public MessageListener(Diffuser diffuser) {
		this.diffuser = diffuser;
	}

	@ListeningPacket(packetID = PacketID.MESS)
	public void mess(MessPacket packet) {

		// On créer le message qu'on pourrait stocker
		Messages msg = new Messages(packet.getId(), packet.getMessage());

		// On verifie le packet (pas obligatoire car fait dans le NetPacket)
		if (msg.getMessage().length() == 140 && msg.getId().length() == 8) {

			// On ajoute le message
			this.diffuser.getMessagesHandler().addMessage(msg);

			Logger.log.info("Registred message : " + msg.toString());

			try {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(packet.getSocket().getOutputStream()));

				// On créer le packet ACKM...
				NetPacket ackm = new AckmPacket();

				// ... et on le print
				writer.print(ackm.toString());
				writer.flush();
				writer.close();

				packet.getSocket().close();
			} catch (IOException e) {
				Logger.log.warning("Error message packet");
				Logger.log.debug(e.toString());
			}

		} else {
			Logger.log.warning("The client does not respect the protocol...");
		}

	}

	@ListeningPacket(packetID = PacketID.TIME)
	public void time(TimePacket packet) {

		// On définit le temps de diffusion a la valeur entrée
		this.diffuser.setDiffusingTime(String.valueOf(packet.getTime()));

		Logger.log.info("Time for diffusing is now at " + this.diffuser.getDiffusingTime());
	}

}
