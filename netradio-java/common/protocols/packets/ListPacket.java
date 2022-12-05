package common.protocols.packets;

import common.packets.NetPacketWithoutArgument;
import common.packets.PacketID;

public class ListPacket extends NetPacketWithoutArgument {

	public ListPacket() {
		super(PacketID.LIST);
	}

	public ListPacket(String s) {
		this();
	}

}
