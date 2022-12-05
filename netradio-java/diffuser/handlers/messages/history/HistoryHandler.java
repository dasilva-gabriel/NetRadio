package diffuser.handlers.messages.history;

import java.util.LinkedList;

import diffuser.handlers.messages.Messages;
import diffuser.listeners.HistoryListener;
import diffuser.tasks.DiffusionTask;

/**
 * Gestionnaire qui s'occupe de l'historique des messages ayant déjà été
 * diffusés
 * 
 * @see DiffusionTask
 * @see HistoryListener listener qui gère les packets lié a l'historique
 */
public class HistoryHandler {

	/**
	 * Stocke le contenu de l'historique
	 * 
	 * @see HistoryHandler
	 */
	public class HistoryContent {
		private final Messages msg;
		private final int historyID;

		public HistoryContent(Messages msg, int historyID) {
			this.msg = msg;
			this.historyID = historyID;
		}

		/**
		 * @return le message stocké
		 */
		public Messages getMsg() {
			return msg;
		}

		/**
		 * @return le numéro du message dans l'historique
		 */
		public int getHistoryID() {
			return historyID;
		}

	}

	private LinkedList<HistoryContent> history;
	private int numMessCount;

	public HistoryHandler() {
		history = new LinkedList<HistoryContent>();
		numMessCount = 0;
	}

	/**
	 * @param s un message qui va être diffusé
	 * @return sa position dans l'historique
	 */
	public synchronized int addMessageToHistory(Messages s) {
		if (numMessCount > 9999) {
			numMessCount = 0;
			history.removeLast();
		}
		history.addFirst(new HistoryContent(s, numMessCount));
		return numMessCount++;
	}

	/**
	 * @return le contenu de l'historique
	 */
	public synchronized LinkedList<HistoryContent> getHistory() {
		return history;
	}

}
