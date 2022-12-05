package diffuser.handlers.messages;

import common.utils.NormalizationUtil;

/**
 * Stocke les informations des message a diffuser
 */
public class Messages {

	private final String id;
	private final String message;

	public Messages(String id, String message) {
		this.id = id;
		this.message = NormalizationUtil.normalizeWithSize(message, 140);
	}

	/**
	 * @return l'identifiant de l'entité ayant enregistré le message
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return le contenu du message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "id=" + getId() + " msg=" + getMessage();
	}

}
