package diffuser.tasks;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import common.Logger;
import common.protocols.packets.DiffPacket;
import diffuser.Diffuser;
import diffuser.handlers.messages.Messages;
import diffuser.handlers.messages.MessagesHandler;

/**
 * Thread qui s'occupe d'envoyer les messages en multicast.
 *
 * @see MessagesHandler gestionnaire des messages
 */
public class DiffusionTask implements Runnable {

	private Diffuser diffuser;

	public DiffusionTask(Diffuser m) {
		this.diffuser = m;
	}

	@Override
	public void run() {

		Logger.log.info("Task started...");

		try (DatagramSocket dtgrSocket = new DatagramSocket()) {

			InetSocketAddress adress = new InetSocketAddress(this.diffuser.getCastIP(),
				Integer.valueOf(this.diffuser.getCastPort()));

			while (true) {

				// On fait dormir le thread le temps indiqué dans la configuration
				Thread.sleep(Long.parseLong(this.diffuser.getDiffusingTime()));

				// On récupère le message a diffuser
				Messages msg = this.diffuser.getMessagesHandler().getMessageToDiffuse();

				// On l'ajoute a l'historique et on récupère son ID
				int id = this.diffuser.getHistoryHandler().addMessageToHistory(msg);

				// On créé le paquet
				DiffPacket diff = new DiffPacket(id, msg.getId(), msg.getMessage());

				byte[] var2 = diff.toString().getBytes();

				dtgrSocket.send(new DatagramPacket(var2, var2.length, adress));
				
				Logger.log.debug("The following message was diffused on " + adress.getAddress().getHostAddress()+":"+adress.getPort()+": \""+diff.toString()+"\"");
			}

		} catch (Exception e) {
			Logger.log.warning("[GRAVE] Message delivery could not be performed. (DIFF packet)");
			Logger.log.warning("[GRAVE] Error caused by: " + e.getMessage());
			System.exit(0);
		}
	}
}
