package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class ImokPacket extends NetPacketWithoutArgument {

	public ImokPacket() {
		super(PacketID.IMOK);
	}

}
