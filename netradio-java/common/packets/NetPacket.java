package common.packets;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Logger;
import common.exceptions.BadPacketException;
import common.utils.RegexUtil;

/**
 * Type pour les packets qui sont reçus et envoyés par l'entité
 */
public abstract class NetPacket {

	private final PacketID packetID;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;

	public NetPacket(PacketID packetID) {
		this.packetID = packetID;
		this.socket = null;
	}

	public NetPacket(PacketID packetID, String string) {
		this(packetID);
		
		// On va vérifier que la syntaxe est correcte et qu'il est ASCII 128
		if (!syntaxIsCorrect(string) || !RegexUtil.regexMatch(string, "[\\x00-\\x7F]+")) {
			Logger.log.warning("The syntax of the package does not respect the protocol.");
			throw new BadPacketException(packetID.name());
		}
	}

	/**
	 * Verifie que la syntaxe du message envoyé correspond bien au packet
	 * 
	 * @return bien syntaxé (vrai) ou mal syntaxé (faux)
	 */
	public abstract boolean syntaxIsCorrect(String entry);

	/**
	 * Structure le packet en une string qui pourrait être envoyé a une autre entité
	 * 
	 * @return chaine de caractère avec toutes les informations stockées dans le
	 *         paquet
	 */
	public abstract String toString();

	/**
	 * @return l'ID du packet
	 * @see PacketID
	 */
	public PacketID getPacketID() {
		return packetID;
	}

	/**
	 * Définit les variables sur lesquelles ont communique via le paquet
	 * 
	 * @param socket la socket de communication
	 * @param reader le flux d'entrée
	 * @param writer le flux de sortie
	 * 
	 * @see Socket
	 * @see BufferedReader
	 * @see PrintWriter
	 */
	public void setSocket(Socket socket, BufferedReader reader, PrintWriter writer) {
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
	}

	/**
	 * @return la socket de communication stockée
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @return le flux d'entrée
	 */
	public BufferedReader getReader() {
		return reader;
	}

	/**
	 * @return le flux de sortie
	 */
	public PrintWriter getWriter() {
		return writer;
	}
}
