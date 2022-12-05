package manager.packets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

import common.Logger;
import common.protocols.NetProtocol;
import manager.Manager;
import manager.listener.ManagerListener;
import manager.service.RUOKService;
import manager.service.ServiceManager;

/**
 * Protocol du Manager
 * 
 * @see ServiceManager
 */
public class ManagerProtocol extends NetProtocol {

	public ManagerProtocol(Manager manager) {
		super(manager);
		getPacketListenerHandler().registerPacketListener(new ManagerListener(this));
	}

	/**
	 * Lancement du l'écoute du manager
	 */
	@Override
	public void init() {
		// Lance le RUOK service
		Logger.log.info("RUOK Thread Starting...");
		RUOKService ruok = new RUOKService(this);
		Thread t = new Thread(ruok);
		t.start();

		// Création du socket d'écoute
		ServerSocket server_manager = null;
		try {
			server_manager = new ServerSocket(((Manager) getInstance()).getPORT());
			Logger.log.info("Manager started on " + InetAddress.getLocalHost().toString() + ":"
				+ (((Manager) getInstance()).getPORT()));
		} catch (IOException e) {
			Logger.log.warning("Can't start Manager port already used");
			Logger.log.debug(e.toString());
			System.exit(0);
		}
		while (true) {
			try {
				// Attends des connexions
				if (server_manager != null) {
					Socket socket = server_manager.accept();
					Logger.log.info("New connection...");

					// Lance le service manager pour la connexion
					ServiceManager serv = new ServiceManager(socket, this);
					Thread th = new Thread(serv);
					th.start();
				}
			} catch (IOException e) {
				Logger.log.warning("Can't open socket for new connection");
				Logger.log.debug(e.toString());
			}
		}
	}

}
