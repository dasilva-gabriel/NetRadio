package diffuser.tasks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import common.Logger;
import diffuser.Diffuser;
import diffuser.handlers.ReceptionRunnable;
import diffuser.packets.DiffuserProtocol;

/**
 * Thread qui s'occupe de la communication sur le port TCP. Lors de la reception
 * d'une connexion un thread est lancé pour traiter les messages envoyés.
 * 
 * @see Runnable
 * @see ReceptionRunnable
 */
public class TCPTask implements Runnable {

	private DiffuserProtocol diffuser;

	public TCPTask(DiffuserProtocol m) {
		this.diffuser = m;
	}

	@Override
	public void run() {

		try (ServerSocket serverSocket = new ServerSocket(
				Integer.valueOf(((Diffuser) this.diffuser.getInstance()).getListenPort()))) {

			while (true) {
				try {
					Socket socket = serverSocket.accept();

					Logger.log.debug("New connexion from " + socket.getInetAddress().getHostName() + " ("
							+ socket.getInetAddress().getHostAddress() + ")...");

					// Lance un thread pour traiter le message reçu.
					ReceptionRunnable serv = new ReceptionRunnable(diffuser, socket);
					Thread th = new Thread(serv);
					th.start();

				} catch (Exception e) {
					Logger.log.warning("There was an error processing the received message. (" + e.getMessage() + ")");
				}
			}

		} catch (NumberFormatException | IOException e1) {
			Logger.log.warning("Connection could not be made correctly. (" + e1.getMessage() + ")");
			Logger.log.info("Shutdown...");
			System.exit(0);
		}
	}

}
