package common.packets;

import common.protocols.packets.AckmPacket;
import common.protocols.packets.DiffPacket;
import common.protocols.packets.EndmPacket;
import common.protocols.packets.ImokPacket;
import common.protocols.packets.ItemPacket;
import common.protocols.packets.LastPacket;
import common.protocols.packets.LinbPacket;
import common.protocols.packets.ListPacket;
import common.protocols.packets.MessPacket;
import common.protocols.packets.OldmPacket;
import common.protocols.packets.RegiPacket;
import common.protocols.packets.RenoPacket;
import common.protocols.packets.ReokPacket;
import common.protocols.packets.RuokPacket;
import common.protocols.packets.TimePacket;

/**
 * Énumère tous les packets des entités ainsi que les classes de packet associé
 */
public enum PacketID {

	DIFF(DiffPacket.class),
	MESS(MessPacket.class),
	ACKM(AckmPacket.class),
	LAST(LastPacket.class),
	OLDM(OldmPacket.class),
	ENDM(EndmPacket.class),
	REGI(RegiPacket.class),
	REOK(ReokPacket.class),
	RENO(RenoPacket.class),
	RUOK(RuokPacket.class),
	IMOK(ImokPacket.class),
	LIST(ListPacket.class),
	LINB(LinbPacket.class),
	TIME(TimePacket.class),
	ITEM(ItemPacket.class);

	private Class<?> packetClass;

	private PacketID(Class<?> packetClass) {
		this.packetClass = packetClass;
	}

	public Class<?> getPacketClass() {
		return packetClass;
	}

}
