package common.protocols.listener;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Logger;
import common.packets.NetPacket;
import common.packets.PacketID;

/**
 * Gestionnaire qui traite les paquets reçus et fait l'appel aux méthode qui
 * écoutent ces derniers
 */
public class PacketListenerHandler {

	/**
	 * Permet juse de taguer les class qui contiennent des méthodes de type Listener
	 */
	public interface IPacketListener {}

	private final List<PacketListener> packetListeners;

	public PacketListenerHandler() {
		packetListeners = new ArrayList<>();
	}

	/**
	 * @return la liste des méthodes qui écoutent un packet
	 */
	public List<PacketListener> getPacketListeners() {
		return packetListeners;
	}

	/**
	 * On va récupérer une classe, et on va parcourir chacune des méthodes taguées
	 * par l'annotation, et ainsi savoir qu'elle méthode écoute quel packet.
	 * 
	 * @see ListeningPacket
	 * 
	 * @param packetListener une classe qui contient des méthodes qui écoutent un
	 *                       packet
	 */
	public void registerPacketListener(IPacketListener packetListener) {

		// Parcours toutes les méthodes de la classe
		for (Method method : packetListener.getClass().getDeclaredMethods()) {

			// Vérifie si l'annotaiton ListeningPacket est présente
			if (method.isAnnotationPresent(ListeningPacket.class)) registerMethod(packetListener, method);

		}

	}

	/**
	 * On récupère une méthode taguée et on la stocke comme étant en écoute
	 * 
	 * @param packetListener l'objet qui contient la méthode
	 * @param method         la méthode qui écoute
	 */
	protected void registerMethod(IPacketListener packetListener, Method method) {
		ListeningPacket annotation = method.getAnnotation(ListeningPacket.class);

		// On force la méthode a être accessible
		method.setAccessible(true);

		// On déclare un PacketListener correspodant a la méthode...
		PacketListener register = new PacketListener(packetListener, method, annotation.packetID());

		// ... et on l'ajoute a la liste
		this.packetListeners.add(register);
	}

	/**
	 * On appelle cette fonction a la lecture d'un packet pour invoquer les méthodes
	 * qui écoutent ce paquet
	 * 
	 * @param packet le packet reçu
	 */
	public void packetReception(NetPacket packet) {

		Logger.log.info("Listener invocations for the " + packet.getPacketID().name() + " packet...");

		List<PacketListener> listener = getPacketListenersIfExist(packet.getPacketID());

		if (listener != null) listener.forEach(li -> invokeMethod(li, packet));

	}

	/**
	 * Permet d'invoquer la méthode qui écoute le packet qui est en paramètre
	 * 
	 * @param listener la méthode
	 * @param packet   le packet
	 */
	private PacketListener invokeMethod(PacketListener listener, NetPacket packet) {

		Parameter[] parameters = listener.getMethod().getParameters();
		Object[] objects = new Object[parameters.length];

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getType() == listener.getPacketID().getPacketClass()) {
				objects[i] = packet;
			}

		}

		try {
			listener.getMethod().invoke(listener.getObject(), objects);
		} catch (Exception e) {
			Logger.log.warning("Error while invoking listeners. (" + e.getMessage() + ")");
		}

		return listener;
	}

	/**
	 * Permet de récupérer la liste des méthodes qui écoutent le packet donné
	 * 
	 * @param id l'ID du packet dont on souhaite les méthodes
	 * @return la liste des méthodes qui écoutent le packet
	 */
	private List<PacketListener> getPacketListenersIfExist(PacketID id) {
		List<PacketListener> listeners = getPacketListeners().stream().filter(li -> li.getPacketID().equals(id))
			.collect(Collectors.toList());

		if (listeners.isEmpty()) return null;

		return listeners;
	}

}
