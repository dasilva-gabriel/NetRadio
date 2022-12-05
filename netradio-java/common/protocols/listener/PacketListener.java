package common.protocols.listener;

import java.lang.reflect.Method;

import common.packets.PacketID;

/**
 * Stocke les méthodes qui sont taguées par l'annotation.
 *
 * @see ListeningPacket
 */
public class PacketListener {

	private final Object object;
	private final Method method;
	private final PacketID packetID;

	protected PacketListener(Object object, Method method, PacketID packetID) {
		this.object = object;
		this.method = method;
		this.packetID = packetID;
	}

	/**
	 * @return l'objet o`u est stocké la méthode
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @return la méthode qui est taguée
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @return le packet qui est écouté par la méthode
	 */
	public PacketID getPacketID() {
		return packetID;
	}

}
