package diffuser.handlers.messages;

import java.util.LinkedList;

import diffuser.tasks.DiffusionTask;

/**
 * Gestionnaire des messages qui doivent être diffusés
 * 
 * @see DiffusionTask thread qui envoi ces messages
 */
public class MessagesHandler {

	private LinkedList<Messages> list;
	private int count;

	public MessagesHandler() {
		list = new LinkedList<>();
		count = -1;
	}

	/**
	 * @param s de type Messages
	 * @see Messages
	 */
	public synchronized void addMessage(Messages s) {
		list.addLast(s);
		count++;
	}

	/**
	 * @return le message a diffuser
	 */
	public synchronized Messages getMessageToDiffuse() {
		count = (count >= list.size() - 1 ? 0 : count + 1);
		return list.get(count);
	}

	/**
	 * @return la liste des messages
	 */
	public synchronized LinkedList<Messages> getList() {
		return list;
	}

}
