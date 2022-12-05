package diffuser.tasks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import common.Logger;
import common.utils.NetPatterns;
import common.utils.NormalizationUtil;
import common.utils.RegexUtil;
import diffuser.Diffuser;
import diffuser.handlers.ManagerInfo;

/**
 * Thread qui s'occupe de lire l'entrée dans la console.
 * 
 * @see Runnable
 * @see RegisterTask
 */
public class ConsoleTask implements Runnable {

	private Diffuser diffuser;

	public ConsoleTask(Diffuser m) {
		this.diffuser = m;
	}

	@Override
	public void run() {

		try (Scanner scanner = new Scanner(System.in)) {
			String line = "";

			while (!(line = scanner.nextLine()).equalsIgnoreCase("quit")) {

				// Si le message entré est pour s'enregistrer a un gestionnaire
				if (line.startsWith("R")) {

					String managerIP = "";
					int port = -1;
					try {
						managerIP = line.split(" ")[1];
						port = Integer.valueOf(line.split(" ")[2]);
					} catch (Exception e) {}

					// On vérifie que le port est dans les bonnes bornes
					if (port < 1024 || port > 9999) {
						Logger.log.warning("Error: Invalid port.");

						// On vérifie que l'ip est dans le bon format (peut être pas formatté comme
						// demandé dans le protocole)
					} else if (!RegexUtil.regexMatch(managerIP, NetPatterns.IPv4.getPattern())) {
						Logger.log.warning("Error: Invalid IPv4.");
					} else {

						// On normalise l'IP
						managerIP = NormalizationUtil.normalizeIPv4(managerIP);

						ManagerInfo info = null;
						try {
							// On créé la socket
							Socket s1 = new Socket();
							//s1.setSoTimeout(1000);

							info = new ManagerInfo(port, s1, null, null);

							s1.connect(new InetSocketAddress(managerIP, port), 200);

						} catch (UnknownHostException e) {
							Logger.log.warning("Unknown host: Information is not recognized.");
						} catch (IOException e) {
							Logger.log.warning("Error creating socket. (" + e.getMessage() + ")");
						}

						if (info != null) {

							// On fait appel au thread qui va s'occuper de traiter l'enregistrement...
							RegisterTask reg = new RegisterTask(this.diffuser, managerIP, port, info);
							Thread registerThread = new Thread(reg);

							// ... et on le lance
							registerThread.start();
						}

					}

				} else {
					Logger.log.warning("You entered something that could not be interpreted by the entity.");
				}

				line = "";

			}

			Logger.log.success("Bye :)");
			
			System.exit(0);

		}

	}

}
