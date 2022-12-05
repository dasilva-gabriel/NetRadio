package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class ReokPacket extends NetPacketWithoutArgument {

	public ReokPacket() {
		super(PacketID.REOK);
	}

}
