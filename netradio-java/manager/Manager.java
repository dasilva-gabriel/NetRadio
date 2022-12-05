package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.Logger;
import common.NetRadioArguments;
import common.NetRadioInstance;
import common.packets.PacketID;
import common.protocols.NetProtocol;
import common.utils.NetPatterns;
import diffuser.Diffuser;
import manager.packets.ManagerProtocol;

/**
 * Classe principale qui instancie l'entité "gestionnaire".
 */
public class Manager implements NetRadioInstance {

	private int PORT;
	public static int MAX_DIFF;
	private int ruok_time;
	private volatile List<Diffuser> diffuser_list;

	private ManagerProtocol managerProtocol;

	/**
	 * @return le type de l'entité instancié
	 */
	@Override
	public NetRadioInstanceType getInstanceType() {
		return NetRadioInstanceType.MANAGER;
	}

	/**
	 * Démarrer l'entité
	 * 
	 * @param les arguments entrés par l'utilisateur
	 */
	@Override
	public void start(String[] args) {

		Logger.log.info("Manager starting...");

		// Lecture et parsing des arguments
		PORT = Integer.parseInt(args[0]);
		MAX_DIFF = Integer.parseInt(args[1]);
		ruok_time = Integer.parseInt(args[2]);
		Logger.log.setDebugMode(Boolean.parseBoolean(args[3]));
		diffuser_list = Collections.synchronizedList(new ArrayList<Diffuser>());

		// Instanciation du ManagerProtocol (threads etc...)
		this.managerProtocol = new ManagerProtocol(this);

		// Définition des packets gérés par l'entité
		registerPackets();

		// Lancement du ManagerProtocol
		this.managerProtocol.init();
	}

	/**
	 * Enregistrement et définition des packets qui sont traités, envoyés et peuvent
	 * être reçus par l'entité
	 */
	private void registerPackets() {
		this.managerProtocol.registerPackets(PacketID.REGI, PacketID.REOK, PacketID.RENO, PacketID.RUOK, PacketID.IMOK,
			PacketID.LIST, PacketID.LINB, PacketID.ITEM);
	}

	/**
	 * Définition des arguments nécéssaires pour lancer l'entité. Les arguments sont
	 * au format regex.
	 */
	@Override
	public NetRadioArguments getArguments() {
		return new NetRadioArguments().addArgument(NetPatterns.VALID_COMMUNICATION_PORT.getPattern())
			.addArgument(NetPatterns.MANAGER_MAXUSERS.getPattern()).addArgument(NetPatterns.RUOK_TIME.getPattern())
			.addArgument(NetPatterns.DEBUG.getPattern());
	}

	/**
	 * @return le protocole (dans ce cas, le ManagerProtocol)
	 */
	@Override
	public NetProtocol getProtocol() {
		return this.managerProtocol;
	}

	/**
	 * @return liste des diffuseurs
	 */
	public List<Diffuser> getDiffuser_list() {
		return diffuser_list;
	}

	/**
	 * @return le port du gestionnaire
	 */
	public int getPORT() {
		return PORT;
	}

	/**
	 * @return le temps d'attente pour les RUOK (en millisecondes)
	 */
	public int getRuok_time() {
		return ruok_time;
	}

	/**
	 * @return si les informations existent déjà
	 */
	public boolean hasInfo(String id, String ip1, int port1, String ip2, int port2) {
		for (int i = 0; i < diffuser_list.size(); i++) {
			Diffuser diffuser = diffuser_list.get(i);
			if (diffuser.getName().equals(id)
				|| (diffuser.getCastIP().equals(ip1) && diffuser.getCastPort().equals(port1 + ""))
				|| (diffuser.getIpMachine().equals(ip2) && diffuser.getListenPort().equals(port2 + ""))) {
				return true;
			}
		}
		return false;
	}

}
