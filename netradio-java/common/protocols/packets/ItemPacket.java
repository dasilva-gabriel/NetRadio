package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class ItemPacket extends NetPacket {

	private final int port1, port2;
	private final String id, ip1, ip2;

	public ItemPacket(String string) {
		super(PacketID.ITEM, string);

		String[] splitted = string.split(" ");

		this.id = splitted[1];
		this.ip1 = splitted[2];
		this.port1 = Integer.valueOf(splitted[3]);
		this.ip2 = splitted[4];
		this.port2 = Integer.valueOf(splitted[5]);
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry,
			getPacketID().name() + " " + NetPatterns.DIFFUSER_ID.getPattern() + " " + NetPatterns.IPv4.getPattern()
				+ " " + NetPatterns.VALID_PORT.getPattern() + " " + NetPatterns.IPv4.getPattern() + " "
				+ NetPatterns.VALID_PORT.getPattern());
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + id + " " + ip1 + " " + port1 + " " + ip2 + " " + port2 + "\r\n";
	}

}
