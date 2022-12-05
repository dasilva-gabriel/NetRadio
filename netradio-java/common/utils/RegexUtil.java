package common.utils;

import java.util.regex.Pattern;

public class RegexUtil {

	/**
	 * Vérifie que la chaine de caractère respect le format Regex donné
	 * 
	 * @param s       la chaine de caractère
	 * @param pattern le regex
	 * @return vrai (la chaine de caratère correctement formatté) ou faux (sinon)
	 */
	public static boolean regexMatch(String s, String pattern) {
		return Pattern.matches(pattern, s);
	}

}
