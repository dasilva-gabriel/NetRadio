package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class AckmPacket extends NetPacketWithoutArgument {

	public AckmPacket() {
		super(PacketID.ACKM);
	}

}
