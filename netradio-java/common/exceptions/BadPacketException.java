package common.exceptions;

/**
 * Erreur créé lorsque le packet n'a pas été reconnu
 *
 * @see NetPacket
 */

@SuppressWarnings("serial")
public class BadPacketException extends RuntimeException {

	public BadPacketException(String packet) {
		super("The package \"" + packet + "\" is not correctly recognized.");
	}

}
