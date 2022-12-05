package manager.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import common.Logger;
import common.packets.PacketID;
import common.protocols.listener.ListeningPacket;
import common.protocols.listener.PacketListenerHandler.IPacketListener;
import common.protocols.packets.ItemPacket;
import common.protocols.packets.LinbPacket;
import common.protocols.packets.ListPacket;
import common.protocols.packets.RegiPacket;
import common.protocols.packets.RenoPacket;
import common.protocols.packets.ReokPacket;
import diffuser.Diffuser;
import manager.Manager;
import manager.packets.ManagerProtocol;

/**
 * Listener qui gère les messages recus par le gestionnaire
 */
public class ManagerListener implements IPacketListener {

	private ManagerProtocol manager;

	public ManagerListener(ManagerProtocol manager) {
		this.manager = manager;
	}

	/**
	 * Enregistre un diffuseur si c'est possible
	 */
	@ListeningPacket(packetID = PacketID.REGI)
	public void regi(RegiPacket packet) {
		Socket socket = packet.getSocket();
		BufferedReader br = packet.getReader();
		PrintWriter pw = packet.getWriter();
		Logger.log.info("Checking Infos..");
		// Verification des infos
		if (!((Manager) manager.getInstance()).hasInfo(packet.getId(), packet.getIp1(), packet.getPort1(),
			packet.getIp2(), packet.getPort2())
			&& ((Manager) manager.getInstance()).getDiffuser_list().size() < Manager.MAX_DIFF) {
			Diffuser diff = new Diffuser();
			diff.setInfo(packet.getId(), packet.getIp1(), packet.getPort1(), packet.getIp2(), packet.getPort2());
			diff.setSocket(socket, br, pw);
			Logger.log.info("Diffuser " + packet.getId() + " Registered.");

			// Ajout du à la liste de diffuseur
			((Manager) manager.getInstance()).getDiffuser_list().add(diff);

			// Envoie REOK
			ReokPacket reok = new ReokPacket();
			Logger.log.debug("Sending REOK.");
			pw.print(reok.toString());
			pw.flush();
		} else {
			Logger.log.info("Diffuser can't Register.");
			// Envoie REOK
			Logger.log.debug("Sending RENO.");
			RenoPacket reno = new RenoPacket();
			pw.print(reno.toString());
			pw.flush();
			Logger.log.info("Closing " + packet.getId() + " Connection...");
			try {
				pw.close();
				br.close();
				socket.close();
			} catch (IOException e) {
				Logger.log.info("Can't close the socket");
				Logger.log.debug(e.toString());
			}
		}
	}

	/**
	 * Envoie la liste des diffuseurs
	 */
	@ListeningPacket(packetID = PacketID.LIST)
	public void list(ListPacket packet) {
		Socket socket = packet.getSocket();
		BufferedReader br = packet.getReader();
		PrintWriter pw = packet.getWriter();
		Logger.log.info("Getting List of diffusers..");
		List<Diffuser> diffusersList = ((Manager) manager.getInstance()).getDiffuser_list();

		// Envoie LINB
		Logger.log.info("Sending LINB..");
		LinbPacket linbPacket = new LinbPacket(diffusersList.size());
		pw.print(linbPacket.toString());
		pw.flush();

		// Envoie ITEM
		Logger.log.debug("Sending " + diffusersList.size() + " ITEM");
		for (int i = 0; i < diffusersList.size(); i++) {
			Diffuser diffuser = diffusersList.get(i);
			if (diffuser != null) {
				ItemPacket itemPacket = new ItemPacket(diffuser.toString());
				pw.print(itemPacket.toString());
				pw.flush();
			}
		}
		Logger.log.info("Closing Client Connection...");
		try {
			pw.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			Logger.log.info("Can't close the socket");
			Logger.log.debug(e.toString());
		}

	}

}
