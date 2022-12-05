package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class EndmPacket extends NetPacketWithoutArgument {

	public EndmPacket() {
		super(PacketID.ENDM);
	}

}
