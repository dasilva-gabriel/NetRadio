package common.utils;

/**
 *	Enumère les regex utiles pour vérifier la syntaxe des paquets
 */
public enum NetPatterns {
	
	IPv4("(?:[0-9]{1,3}[.]){3}[0-9]{1,3}"),
	IPv4RECEIVE("(?:[0-9]{3}[.]){3}[0-9]{3}"),
	USER_ID(".{8}"),
	VALID_PORT("[0-9]{1,4}"),
	VALID_COMMUNICATION_PORT("[0-9]{1,4}"),
	MANAGER_MAXUSERS("[0-9]{1,3}"),
	DIFFUSING_TIME("[0-9]+"),
	RUOK_TIME("[0-9]+"),
	DIFFUSER_ID(".{8}"),
	DEBUG("(true|false)");
	
	private String pattern;

	private NetPatterns(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

}
