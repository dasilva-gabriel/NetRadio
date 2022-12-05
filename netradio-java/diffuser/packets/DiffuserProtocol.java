package diffuser.packets;

import common.Logger;
import common.protocols.NetProtocol;
import diffuser.Diffuser;
import diffuser.handlers.messages.Messages;
import diffuser.handlers.messages.MessagesHandler;
import diffuser.tasks.ConsoleTask;
import diffuser.tasks.DiffusionTask;
import diffuser.tasks.TCPTask;

/**
 * Protocol du Diffuser
 * 
 * @see TCPTask
 * @see DiffusionTask
 * @see ConsoleTask
 * @see MessagesHandler
 */
public class DiffuserProtocol extends NetProtocol {

	private Diffuser diffuser;

	public DiffuserProtocol(Diffuser diffuser) {
		super(diffuser);

		this.diffuser = diffuser;
	}

	public void init() {

		Logger.log.info("(Protocol) Starting TCP Thread...");

		Thread tcp = new Thread(new TCPTask(this));
		tcp.start();

		Logger.log.info("(Protocol) Starting Diffusion Thread...");

		Thread diffuse = new Thread(new DiffusionTask(diffuser));
		diffuse.start();

		Logger.log.info("(Protocol) Starting Console Thread...");

		Thread console = new Thread(new ConsoleTask(diffuser));
		console.start();

		diffuser.getMessagesHandler().addMessage(new Messages(diffuser.getName(), "Je suis le message basique n 1"));
		diffuser.getMessagesHandler().addMessage(new Messages(diffuser.getName(), "Je suis le message basique n 2"));

	}

}
