package diffuser.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;

import common.Logger;
import common.packets.PacketID;
import common.protocols.packets.RegiPacket;
import common.utils.NormalizationUtil;
import common.utils.ReaderUtil;
import diffuser.Diffuser;
import diffuser.handlers.ManagerInfo;

/**
 * Thread qui traite l'enregistrement du diffuseur auprès du gestionnaire.
 *
 * @see RegisterSenderTask
 */

public class RegisterTask implements Runnable {

	private Diffuser diffuser;
	private String managerIP;
	private int port;
	private ManagerInfo info;

	public RegisterTask(Diffuser diffuser, String managerIP, int port, ManagerInfo info) {
		this.diffuser = diffuser;
		this.managerIP = managerIP;
		this.port = port;
		this.info = info;
	}

	@Override
	public void run() {

		// On créé un nouveau thread pour attendre la réponse du gestionnaire ...
		RegisterSenderTask reg = new RegisterSenderTask(this.diffuser, managerIP, port, info);
		Thread th = new Thread(reg);

		Logger.log.info("Sending the request to the manager [" + managerIP + ":" + port
			+ "]... (He will have a few seconds to answer it)");

		// ... et on le lance
		th.start();

		// On va attendre X secondes
		try {
			for (int time = 0; time < 10000; time++) {
				Thread.sleep(1L);
				if (th.isInterrupted() || reg.getResponse() != 0) {
					Logger.log.debug("Waiting ended in " + time + "ms.");
					break;
				}
			}
		} catch (InterruptedException e) {
			Logger.log.warning("The console playback task has been interrupted!");
		}

		boolean b = false;

		// On analyse l'état du thread pour comprendre l'état de la requête
		if (!th.isInterrupted() && reg.getResponse() == 0) {
			b = true;
			Logger.log.warning(
				"The manager [" + managerIP + ":" + port + "] did not respond on time. The request has been closed.");
		} else if (th.isInterrupted() && reg.getResponse() == 0) {
			b = true;
			Logger.log.warning(
				"The task of the request to the manager [" + managerIP + ":" + port + "] has been interrupted.");
		}

		Logger.log.debug("Register result:\nThe waiting task was interrupted (stopped/finished) > " + th.isInterrupted()+"\nTask response= " + reg.getResponse()+"\nKill socket? > " +b);

		if (b || reg.getResponse() != 2) {
			try {
				if (info.getReader() != null) info.getReader().close();
				if (info.getWriter() != null) info.getWriter().close();
			} catch (Exception e) {}
		} else {
			// On lance le thread de KeepAlive pour le packet RUOK
			Logger.log.debug("(Register) Starting the KeepAlive...");
			Thread ka = new Thread(new KeepAliveTask(diffuser, info));
			ka.start();
		}

		if (!th.isInterrupted()) {
			th.interrupt();
		}

	}

	/**
	 * Thread qui envoie et qui reçois l'enregistrement auprès du gestionnaire
	 */
	public class RegisterSenderTask implements Runnable {

		private int response;
		private Diffuser diffuser;
		private String managerIP;
		private int port;
		private ManagerInfo info;

		public RegisterSenderTask(Diffuser diffuser, String managerIP, int port, ManagerInfo info) {
			this.diffuser = diffuser;
			this.managerIP = managerIP;
			this.port = port;
			this.info = info;
			this.response = 0;
		}

		public int getResponse() {
			return response;
		}

		@Override
		public void run() {
			try {

				Logger.log.info("Request to the manager [" + managerIP + ":" + port + "] sent.");

				// Récupère l'InetAdress locale
				InetAddress ip = InetAddress.getLocalHost();

				info.setReader(new BufferedReader(new InputStreamReader(info.getSocket().getInputStream())));
				info.setWriter(new PrintWriter(new OutputStreamWriter(info.getSocket().getOutputStream())));

				// On créé le packet...
				RegiPacket regi = new RegiPacket(this.diffuser.getName(), Integer.parseInt(this.diffuser.getCastPort()),
					Integer.parseInt(this.diffuser.getListenPort()), this.diffuser.getCastIP(),
					NormalizationUtil.normalizeIPv4(ip.getHostAddress()));

				// ...et l'envoie
				Logger.log.debug("(Register) Sending the packet \"" + regi.toString() + "\"...");
				info.getWriter().print(regi.toString());
				info.getWriter().flush();

				// On attend la réponse
				Logger.log.debug("(Register) Waiting for a response...");
				String received = ReaderUtil.getMessage(info.getReader());
				if (!info.getSocket().isClosed() && received != null) {
					Logger.log.debug("(Register) Received response: " + received);
					this.response = 1;

					// Le paquet RENO a été reçu
					if (received.equalsIgnoreCase(PacketID.RENO.name())) {
						this.response = 1;
						Logger.log
							.warning("Error: The diffuser could not be registered with the manager (received RENO)");

						// Le paquet REOK a été reçu
					} else if (received.equalsIgnoreCase(PacketID.REOK.name())) {

						this.response = 2;

						Logger.log.success("The entity has been registered with the manager [" + this.managerIP + ":"
							+ this.port + "]. Launching KeepAlive....");

					}
				} else {
					Logger.log.warning(
						"Error: Invalid informations: these does not belong to any valid manager informations.");
				}

			} catch (Exception e) {
				if (!Thread.interrupted())
					Logger.log.warning("Unable to register with the manager. (" + e.getMessage() + ")");
			}

		}

	}

}
