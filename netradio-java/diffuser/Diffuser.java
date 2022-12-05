package diffuser;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Logger;
import common.NetRadioArguments;
import common.NetRadioInstance;
import common.packets.PacketID;
import common.protocols.NetProtocol;
import common.protocols.listener.PacketListenerHandler;
import common.utils.NetPatterns;
import common.utils.NormalizationUtil;
import diffuser.handlers.messages.MessagesHandler;
import diffuser.handlers.messages.history.HistoryHandler;
import diffuser.listeners.HistoryListener;
import diffuser.listeners.MessageListener;
import diffuser.packets.DiffuserProtocol;

/**
 * Classe principale qui instancie l'entité "diffuseur".
 */
public class Diffuser implements NetRadioInstance {

	private String name, listenPort, castPort, castIP, diffusingTime, ipMachineSurLaquelleTes;

	private DiffuserProtocol diffuserProtocol;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;

	private MessagesHandler messagesHandler;
	private HistoryHandler historyHandler;

	/**
	 * @return le type de l'entité instancié
	 */
	@Override
	public NetRadioInstanceType getInstanceType() {
		return NetRadioInstanceType.DIFFUSER;
	}

	/**
	 * Démarrer l'entité
	 * 
	 * @param les arguments entrés par l'utilisateur
	 */
	@Override
	public void start(String[] args) {

		Logger.log.info("Diffuser starting...");

		// Lecture et parsing des arguments
		this.name = NormalizationUtil.normalizeWithSize(args[0], 8, true);
		this.castPort = args[1];
		this.castIP = NormalizationUtil.normalizeIPv4(args[2]);
		this.listenPort = args[3];
		this.diffusingTime = args[4];
		Logger.log.setDebugMode(Boolean.parseBoolean(args[5]));

		// Instanciation du DiffuserProtocol (threads etc...)
		this.diffuserProtocol = new DiffuserProtocol(this);

		// Instanciation des handlers
		this.messagesHandler = new MessagesHandler();
		this.historyHandler = new HistoryHandler();

		// Définition des packets gérés par l'entité
		registerPackets();

		// Instanciation et enregistrement des listeners
		PacketListenerHandler handler = this.diffuserProtocol.getPacketListenerHandler();
		handler.registerPacketListener(new HistoryListener(this));
		handler.registerPacketListener(new MessageListener(this));

		// Lancement du DiffuserProtocol
		this.diffuserProtocol.init();
	}

	/**
	 * Enregistrement et définition des packets qui sont traités, envoyés et peuvent
	 * être reçus par l'entité
	 */
	private void registerPackets() {
		this.diffuserProtocol.registerPackets(PacketID.DIFF, PacketID.MESS, PacketID.REGI, PacketID.TIME, PacketID.IMOK,
			PacketID.LAST, PacketID.OLDM, PacketID.ENDM, PacketID.ACKM);
	}

	/**
	 * Définition des arguments nécéssaires pour lancer l'entité. Les arguments sont
	 * au format regex.
	 */
	@Override
	public NetRadioArguments getArguments() {
		return new NetRadioArguments().addArgument(NetPatterns.USER_ID.getPattern())
			.addArgument(NetPatterns.VALID_PORT.getPattern()).addArgument(NetPatterns.IPv4.getPattern())
			.addArgument(NetPatterns.VALID_PORT.getPattern()).addArgument(NetPatterns.DIFFUSING_TIME.getPattern())
			.addArgument(NetPatterns.DEBUG.getPattern());
	}

	/**
	 * @return le protocole (dans ce cas, le DiffuserProtocol)
	 */
	@Override
	public NetProtocol getProtocol() {
		return this.diffuserProtocol;
	}

	/**
	 * Définition des informations du diffuseur (utilisé par les gestionnaire)
	 * 
	 * @param id    nom de l'entité
	 * @param ip1   IP de multi cast
	 * @param port1 port de multi cast
	 * @param ip2   IP de la machine
	 * @param port2 port de communication avec le diffuseur
	 */
	public void setInfo(String id, String ip1, int port1, String ip2, int port2) {
		this.name = id;
		this.listenPort = "" + port2;
		this.castPort = "" + port1;
		this.castIP = ip1;
		this.ipMachineSurLaquelleTes = ip2;
	}

	public void setSocket(Socket socket, BufferedReader reader, PrintWriter writer) {
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public MessagesHandler getMessagesHandler() {
		return messagesHandler;
	}

	public HistoryHandler getHistoryHandler() {
		return historyHandler;
	}

	public String getName() {
		return name;
	}

	public String getListenPort() {
		return listenPort;
	}

	public String getCastPort() {
		return castPort;
	}

	public String getCastIP() {
		return castIP;
	}

	public String getDiffusingTime() {
		return diffusingTime;
	}

	public String getIpMachine() {
		return ipMachineSurLaquelleTes;
	}

	public void setDiffusingTime(String diffusingTime) {
		this.diffusingTime = diffusingTime;
	}

	@Override
	public String toString() {
		return "ITEM " + getName() + " " + getCastIP() + " " + getCastPort() + " " + getIpMachine() + " "
			+ getListenPort();
	}

}
