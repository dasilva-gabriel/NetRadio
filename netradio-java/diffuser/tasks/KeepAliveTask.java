package diffuser.tasks;

import java.io.*;

import common.Logger;
import common.packets.PacketID;
import common.utils.ReaderUtil;
import diffuser.Diffuser;
import diffuser.handlers.ManagerInfo;

/**
 * Threas qui s'occupe du packet IMOK permettant au gestionnaire de s'assurer
 * que l'entité est encore "vivante".
 */
public class KeepAliveTask implements Runnable {

	private ManagerInfo info;

	public KeepAliveTask(Diffuser m, ManagerInfo info) {
		this.info = info;
	}

	@Override
	public void run() {

		Logger.log.info("Starting KeepAlive thread for manager [" + info.getSocket().getInetAddress().getHostAddress()
			+ ":" + info.getPort() + "]");

		while (!info.getSocket().isClosed()) {

			String s = null;
			try {
				s = ReaderUtil.getMessage(info.getReader());
			} catch (IOException e) {
				Logger.log.warning("The reception of the packet coming from the manager could not be recognized. ("
					+ e.getMessage() + ")");
			}
			if (!info.getSocket().isClosed() && s != null) {

				if (s.equalsIgnoreCase(PacketID.RUOK.name())) {
					String st = "IMOK\r\n";

					info.getWriter().print(st);
					info.getWriter().flush();
				}
			}else {
				try {
					info.getSocket().close();
					info.getReader().close();
					info.getWriter().close();
				} catch (IOException e) {
					Logger.log.info("Can't close the socket");
				}
			}

		}

	}

}
