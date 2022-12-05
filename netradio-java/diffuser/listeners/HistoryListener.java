package diffuser.listeners;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import common.Logger;
import common.packets.PacketID;
import common.protocols.listener.ListeningPacket;
import common.protocols.listener.PacketListenerHandler.IPacketListener;
import common.protocols.packets.LastPacket;
import common.utils.NormalizationUtil;
import diffuser.Diffuser;
import diffuser.handlers.messages.history.HistoryHandler;
import diffuser.handlers.messages.history.HistoryHandler.HistoryContent;

/**
 * Listener qui gère l'historique des messages diffusés
 *
 * @see HistoryHandler gestionnaire de l'historique
 */
public class HistoryListener implements IPacketListener {

	private Diffuser diffuser;

	public HistoryListener(Diffuser diffuser) {
		this.diffuser = diffuser;
	}

	@ListeningPacket(packetID = PacketID.LAST)
	public void diff(LastPacket packet) {
		Logger.log.info("Listening packet : " + packet.getPacketID().name());

		// Récupère la plus petite taille entre la valeur envoyé par le client et la
		// taille actuelle de l'historique
		int nbMess = Math.min(packet.getNbMess(), this.diffuser.getHistoryHandler().getHistory().size());

		PrintWriter writer;
		try {
			writer = new PrintWriter(new OutputStreamWriter(packet.getSocket().getOutputStream()));

			for (int i = 0; i < nbMess; i++) {

				// On récupère le contenu de l'historique a l'indice "i"...
				HistoryContent message = this.diffuser.getHistoryHandler().getHistory().get(i);

				// ... et on le print
				writer.print("OLDM " + NormalizationUtil.intToNum(message.getHistoryID()) + " "
					+ message.getMsg().getId() + " " + message.getMsg().getMessage() + "\r\n");
				writer.flush();
				// Thread.sleep(1);
			}

			// On transmet le packet de fin de l'historique
			writer.print("ENDM\r\n");
			writer.flush();

			writer.close();

		} catch (Exception e) {
			Logger.log.warning("Unable to send the \"OLDM\" packet correctly. (" + e.getMessage() + ")");
		}

	}

}
