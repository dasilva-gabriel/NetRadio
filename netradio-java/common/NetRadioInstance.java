package common;

import common.protocols.NetProtocol;

/**
 * Type les entités
 */
public interface NetRadioInstance {

	/**
	 * Enumère les types d'entités possibles
	 */
	public enum NetRadioInstanceType {
		DIFFUSER, MANAGER;
	}

	/**
	 * @return le type de l'entité {@link NetRadioType}
	 */
	public NetRadioInstanceType getInstanceType();

	/**
	 * @return l'instance {@link NetRadioArguments} pour les arguments
	 */
	public NetRadioArguments getArguments();

	/**
	 * @return l'instance {@link NetProtocol} pour le protocol de l'entité
	 */
	public NetProtocol getProtocol();

	/**
	 * Lance l'entité
	 */
	public void start(String[] args);

}
