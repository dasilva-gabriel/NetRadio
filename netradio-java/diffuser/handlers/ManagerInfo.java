package diffuser.handlers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Stocke les informations des gestionnaires.
 */
public class ManagerInfo {

	private int port;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	public ManagerInfo(int port, Socket socket, PrintWriter writer, BufferedReader reader) {
		super();
		this.port = port;
		this.socket = socket;
		this.writer = writer;
		this.reader = reader;
	}

	/**
	 * @return le port de communication
	 */
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return le scoket utilisé
	 */
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return le stream de sortie pour print des messages au gestionnaire
	 * @see PrintWriter
	 */
	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	/**
	 * @return le stream d'entrée pour read des messages venant du gestionnaire
	 * @see BufferedReader
	 */
	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

}