package common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import common.Logger;

/**
 * Utilitaire pour la gestion des fichier
 */
public class FileUtil {

	/**
	 * Vérifie que le chemin donné pointe bien vers un fichier
	 * 
	 * @return vrai (si fichier) ou faux (si pas fichier)
	 */
	public static boolean stringIsFile(String path) {
		File f = new File(path);
		return f.exists() && !f.isDirectory();
	}

	/**
	 * Lit le fichier afin de remplir le tableau
	 * 
	 * @param path   le chemin du fichier
	 * @param config le tableau a remplir
	 */
	public static void getConfig(String path, String[] config) {
		File file = new File(path);
		Scanner myReader;
		try {
			myReader = new Scanner(file);
			int i = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				config[i++] = data;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			Logger.log.warning("The file could not be read correctly. (" + e.getMessage() + ")");
		}
	}

}
