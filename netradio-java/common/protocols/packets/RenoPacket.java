package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class RenoPacket extends NetPacketWithoutArgument {

	public RenoPacket() {
		super(PacketID.RENO);
	}

}
