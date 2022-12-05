import common.Logger;
import common.NetRadioInstance;
import common.utils.FileUtil;
import diffuser.Diffuser;
import manager.Manager;

/**
 * Class principale qui execute soit le diffuseur soit le gestionnaire et qui
 * traites les arguments d'exécutions
 */
public class NetRadio {

	public static void main(String[] args) {
		
		NetRadioInstance instance = null;

		if (args.length > 0 && args[0].equalsIgnoreCase("DIFFUSER")) instance = new Diffuser();
		if (args.length > 0 && args[0].equalsIgnoreCase("MANAGER")) instance = new Manager();

		if (instance != null) {
			String[] config;
			
			if (args.length>1 && FileUtil.stringIsFile("../../" + args[1])) {
				config = new String[instance.getArguments().arguments.size()];
				FileUtil.getConfig("../../" + args[1], config);
			} else {
				config = new String[args.length-1];
				for (int i = 0; i < args.length - 1; i++) {
					config[i] = args[i + 1];
				}
			}

			String argsProblem = instance.getArguments().verify(config);

			if (argsProblem == null) instance.start(config);
			else
				Logger.log.warning(argsProblem);
		} else {
			Logger.log.warning("The first argument must be: DIFFUSER or MANAGER.");
		}
		
		/*String[] args_diff = {
			"12345678",
			"5252",
			"225.10.20.30",
			"5151",
			"1000",
			"true"
		};
		String[] args_gest = {
			"4242",
			"10",
			"1000",
			"false",
		};
		
		NetRadioInstance dif_instance = new Diffuser();
		NetRadioInstance man_instance = new Manager();
			
		String argsProblemDif = dif_instance.getArguments().verify(args_diff);
		if (argsProblemDif == null) dif_instance.start(args_diff);
		else
			Logger.log.warning(argsProblemDif);
		String argsProblemMan = man_instance.getArguments().verify(args_gest);
		if (argsProblemMan == null) man_instance.start(args_gest);
		else
			Logger.log.warning(argsProblemMan);
		*/
	}
	
}
