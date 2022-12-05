package common;

import java.util.LinkedList;

import common.utils.RegexUtil;

/**
 * Gère les arguments nécéssaire au lancement d'une entitée
 */

public class NetRadioArguments {

	public LinkedList<String> arguments = new LinkedList<String>();

	public NetRadioArguments() {
	}

	/**
	 * Ajoute un regex a la liste des arguments nécéssaires
	 * 
	 * @param pattern le regex
	 * @return la liste des arguments
	 */
	public NetRadioArguments addArgument(String pattern) {
		arguments.addLast(pattern);
		return this;
	}

	/**
	 * Vérifie que la liste des arguments en entrée correspond aux arguments
	 * attendus
	 * 
	 * @param entry la liste des arguents en entrée
	 * @return null (si aucune erreur) ou une chaine de caractère (s'il y a un
	 *         problème)
	 */
	public String verify(String[] entry) {

		if (arguments.size() > (entry.length))
			return "There are missing arguments ... Refer to the documentation. (needed: " + arguments.size() + ")";
		if (arguments.size() < (entry.length))
			return "There are too many arguments ... Refer to the documentation. (needed: " + arguments.size() + ")";

		int index = -1;

		for (String args : arguments) {
			index++;
			if (RegexUtil.regexMatch(entry[index], args)) continue;

			return "There is a problem with the argument \"" + entry[index] + "\" (index: " + index
				+ "). The expected argument is of the form: "
				+ ((index == 0) ? args + ". Or file does not exist" : args);

		}

		return null;

	}

}
