package diffuser.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import common.Logger;
import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.ReaderUtil;
import diffuser.packets.DiffuserProtocol;

/**
 * Thread qui traite les messages reçu. Dès qu'il a reçu un message il l'analyse
 * pour trouver qui a été envoyé.
 *
 * @see NetProtocol s'occupe d'associer un message un packet
 * @see NetPacket packet qui est créé
 * @see PacketListenerHandler s'occupe de faire appel a tout les listeners
 */

public class ReceptionRunnable implements Runnable {

	public Socket socket;
	private DiffuserProtocol diffuser;

	public ReceptionRunnable(DiffuserProtocol m, Socket s) {
		this.socket = s;
		this.diffuser = m;
	}

	public void run() {
		try {

			String tampon;

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (!socket.isClosed() && (tampon = ReaderUtil.getMessage(reader)) != null) {
				Logger.log.debug("Reading: \""+tampon+"\"");
				/*
				 * On associe le message reçu a un packet a deux conditions: - Si le packet
				 * existe - Si le packet a enregistré dans le NetProtocol
				 */
				NetPacket packet = diffuser.receivingPacket(tampon);

				// Si le packet existe et qu'il n'est pas RUOK (car géré ailleurs)
				if (packet != null && !packet.getPacketID().equals(PacketID.RUOK)) {

					// On définit le socket dans le NetPacket pour pouvoir l'utiliser dans les
					// listeners
					packet.setSocket(this.socket, reader, null);

					// On fait appel a nos listeners qui traiteront le packet
					diffuser.getPacketListenerHandler().packetReception(packet);

				} else {
					Logger.log
						.warning("The reception of the packet coming from the manager could not be recognized. (RUOK)");
				}
			}
			reader.close();
		} catch (Exception e) {
			Logger.log.warning("Not receving ruok");
			Logger.log.debug(e.toString());
		} finally {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					Logger.log.warning("Can't close socket");
					Logger.log.debug(e.toString());
				}
			}
		}
	}
}
