package common;

import common.utils.ChatColor;

/**
 * Utilitaire permettant de print dans la console avec le format des messages
 */
public class Logger {

	// Savoir si le mode de debug est actif ou non
	private boolean debugMode = false;

	// Intance courante du Logger
	public static Logger log = new Logger();

	/**
	 * Définit le mode de debug
	 * 
	 * @param debugMode nouveau mode de debug
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	/**
	 * @return vrai (mode debug actif) ou faux (sinon)
	 */
	public boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * Message de debug
	 * 
	 * @param s la chaine de caractère a print
	 */
	public void debug(String s) {
		if (debugMode) out("*[" + ChatColor.YELLOW + ChatColor.BOLD + "DEBUG" + ChatColor.RESET + "] " + s);

	}

	/**
	 * Message de debug (avec préfixe)
	 * 
	 * @param s préfixe
	 * @param s la chaine de caractère a print
	 */
	public void debug(String prefix, String s) {
		if (debugMode) prefixed(prefix, s);
	}

	/**
	 * Message important (grave, erreur...)
	 * 
	 * @param s la chaine de caractère a print
	 */
	public void warning(String s) {
		out("[" + ChatColor.BOLD + ChatColor.RED + "WARN" + ChatColor.RESET + "] " + s);
	}

	/**
	 * Message d'information
	 * 
	 * @param s la chaine de caractère a print
	 */
	public void info(String s) {
		out("[" + ChatColor.BOLD + ChatColor.CYAN + "INFO" + ChatColor.RESET + "] " + s);
	}

	/**
	 * Message de succès
	 * 
	 * @param s la chaine de caractère a print
	 */
	public void success(String s) {
		out("[" + ChatColor.BOLD + ChatColor.GREEN + "SUCCESS" + ChatColor.RESET + "] " + s);
	}

	/**
	 * Message a print (avec préfixe)
	 * 
	 * @param s préfixe
	 * @param s la chaine de caractère a print
	 */
	public void prefixed(String prefix, String s) {
		out("[" + prefix + ChatColor.RESET + "] " + s);
	}

	/**
	 * Print le message entré
	 * 
	 * @param s la chaine de caractère a print
	 */
	private void out(String string) {
		System.out.println(string + ChatColor.RESET);
	}

}
