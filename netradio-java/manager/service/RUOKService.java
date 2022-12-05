package manager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import common.Logger;
import common.protocols.packets.ImokPacket;
import common.protocols.packets.RuokPacket;
import common.utils.ReaderUtil;
import diffuser.Diffuser;
import manager.Manager;
import manager.packets.ManagerProtocol;

/**
 * Lance les demandes RUOK aux diffuseurs
 */
public class RUOKService implements Runnable {

	private ManagerProtocol managerProtocol;

	public RUOKService(ManagerProtocol managerProtocol) {
		this.managerProtocol = managerProtocol;
	}

	@Override
	public void run() {
		while (true) {
			try {
				List<Diffuser> list = ((Manager) managerProtocol.getInstance()).getDiffuser_list();
				Logger.log.debug("Sending RUOK to " + list.size() + " diffusers");
				// Envoie les RUOK
				for (int i = 0; i < list.size(); i++) {
					Diffuser diffuser = list.get(i);
					Socket socket = diffuser.getSocket();
					BufferedReader br = diffuser.getReader();
					PrintWriter pw = diffuser.getWriter();
					if (socket.isClosed()) {
						((Manager) managerProtocol.getInstance()).getDiffuser_list().remove(diffuser);
					} else {
						RuokPacket ruok = new RuokPacket();
						pw.print(ruok.toString());
						pw.flush();
						// Attente de IMOK
						Logger.log.debug("Waiting IMOK");
						String mess = ReaderUtil.getMessage(br);
						if (mess == null || socket.isClosed()) {
							// Recu un IMOK
							Logger.log.info("Closing " + diffuser.getName() + " Connection...");
							diffuser.getReader().close();
							diffuser.getWriter().close();
							diffuser.getSocket().close();
							((Manager) managerProtocol.getInstance()).getDiffuser_list().remove(diffuser);
							i--;

						} else {
							// Pas recu un IMOK
							ImokPacket imok = new ImokPacket();
							if (!mess.equals(imok.getPacketID().name())) {
								Logger.log.info("Don't receive IMOK");
								Logger.log.info("Closing " + diffuser.getName() + " Connection...");
								diffuser.getReader().close();
								diffuser.getWriter().close();
								diffuser.getSocket().close();
								((Manager) managerProtocol.getInstance()).getDiffuser_list().remove(diffuser);
								i--;
							}
						}
					}
				}
				// Attente
				Thread.sleep(((Manager) managerProtocol.getInstance()).getRuok_time());
			} catch (InterruptedException | IOException e) {
				Logger.log.warning("RUOK SERVICE CAN'T CONTINUE, STOPPING MANAGER");
				Logger.log.debug(e.toString());
				System.exit(0);
			}
		}

	}

}
