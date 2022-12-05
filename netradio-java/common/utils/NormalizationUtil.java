package common.utils;

/**
 * Utilitaire pour la normalisation des messages
 */
public class NormalizationUtil {

	/**
	 * Formatte un Integer en 4 digit
	 * 
	 * @param x le int a formatter
	 * @return une chaine de caractère avec le int formatté
	 */
	public static String intToNum(int x) {
		if (x >= 0 && x <= 9999) {

			String res = Integer.toString(x);

			if (x < 10) {
				res = "000" + res;
			} else if (x < 100) {
				res = "00" + res;
			} else if (x < 1000) {
				res = "0" + res;
			}

			return res;
		}
		return "-1";
	}

	/**
	 * Formatte une chaine de caractère avec une taille entrée
	 * 
	 * @param id   la chaine de caractère
	 * @param size la taille que doit faire la chaine formatté
	 * @return la chaine de caratère correctement formatté
	 */
	public static String normalizeWithSize(String id, int size) {
		return normalizeWithSize(id, size, false);
	}

	/**
	 * Formatte une chaine de caractère avec une taille entrée et supprime les
	 * espaces si besoin
	 * 
	 * @param id          la chaine de caractère
	 * @param size        la taille que doit faire la chaine formatté
	 * @param cleanSpaces supprimer les espaces
	 * @return la chaine de caratère correctement formatté
	 */
	public static String normalizeWithSize(String id, int size, boolean cleanSpaces) {

		String s = id;

		// Supprime les espaces
		if (cleanSpaces) {
			while (s.contains(" "))
				s.replace(" ", "");
		}

		// Si la chaine est trop grande
		if (id.length() > size) return s.substring(0, size);

		// Si la chaine est trop petite on complètre avec des '#'
		if (id.length() < size) {
			while (s.length() < size)
				s += "#";
			return s;
		}

		return s;

	}

	public static String normalizeIPv4(String id) {

		try {

			String[] blocks = id.split("\\.");
			String res = "";
			for (String s : blocks) {
				while (s.length() < 3)
					s = "0" + s;
				res += s;
				res += ".";
			}
			return res.substring(0, res.length() - 1);

		} catch (Exception e) {

		}

		return id;

	}

}
