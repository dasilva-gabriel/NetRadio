package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class RuokPacket extends NetPacketWithoutArgument {

	public RuokPacket() {
		super(PacketID.RUOK);
	}

}
