package common.packets;

/**
 * Type pour les packets qui sont reçus et envoyés par l'entité. La spécificité
 * de ce dernier est qu'il va typer les packet qui ne prennent pas d'argument.
 *
 * @see NetPacket
 */
public abstract class NetPacketWithoutArgument extends NetPacket {

	public NetPacketWithoutArgument(PacketID packetID) {
		super(packetID);
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return entry.equals(this.toString());
	}

	@Override
	public String toString() {
		return getPacketID().name() + "\r\n";
	}

}
