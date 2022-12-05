package manager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import common.Logger;
import common.packets.NetPacket;
import common.utils.ReaderUtil;
import manager.packets.ManagerProtocol;

/**
 * Lis les messages d'une connexion
 */
public class ServiceManager implements Runnable {
	public Socket socket;
	private ManagerProtocol manager;

	public ServiceManager(Socket s, ManagerProtocol m) {
		this.socket = s;
		this.manager = m;
	}

	/**
	 * Execution après connexion
	 */
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String mess = ReaderUtil.getMessage(br);
			if (mess != null) {
				// Choisit le packet recis
				NetPacket packet = manager.receivingPacket(mess);
				if (packet != null) {
					packet.setSocket(this.socket, br, pw);

					// Execute le packet
					manager.getPacketListenerHandler().packetReception(packet);

				} else {
					Logger.log.info("Closing Random Connection...");
					this.socket.close();
					br.close();
					pw.close();
				}
			} else {
				Logger.log.info("Closing Random Connection...");
				this.socket.close();
				br.close();
				pw.close();
			}
		} catch (IOException e) {
			Logger.log.warning("Can't use the socket (Receive, Send, Close)");
			Logger.log.debug(e.toString());
		}
	}
}
