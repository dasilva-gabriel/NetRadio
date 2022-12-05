package common.utils;

import java.io.BufferedReader;
import java.io.IOException;

import common.Logger;

/**
 * Class qui recupère le message d'un BufferedReader
 * et qui verifie que les \r\n existe.
 */
public class ReaderUtil {

	public static String getMessage(BufferedReader br) throws IOException {
		char[] t = new char[512];
		String mes = "";
		br.read(t);
		boolean hasR = false;
		boolean hasN = false;
		for (char c : t) {
			if (c == '\r') {
				hasR = true;
			}
			else if (c == '\n') {
				hasN = true;
			}
			else {
				mes +=c;
			}
		}
		if (!hasN || !hasR) {
			Logger.log.warning("Protocol is not correct.");
			return null;
		}
		return mes.trim();
	}

}
