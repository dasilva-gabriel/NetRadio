package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class MessPacket extends NetPacket {

	private final String id;
	private final String message;

	public MessPacket(String id, String message) {
		super(PacketID.MESS);

		this.id = id;
		this.message = message;
	}

	public MessPacket(String string) {
		super(PacketID.MESS, string);

		this.id = string.substring(5, 13);
		this.message = string.substring(14, 154);
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry, getPacketID().name() + " " + NetPatterns.USER_ID.getPattern() + " .{140}");
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + id + " " + message;
	}

}
