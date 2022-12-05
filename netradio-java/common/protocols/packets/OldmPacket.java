package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class OldmPacket extends NetPacket {

	private final int numMess;
	private final int id;
	private final String message;

	public OldmPacket(int numMess, int id, String message) {
		super(PacketID.OLDM);
		this.numMess = numMess;
		this.id = id;
		this.message = message;
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry,
			getPacketID().name() + " [0-9]{4} " + NetPatterns.USER_ID.getPattern() + " .{140}");
	}

	public int getNumMess() {
		return numMess;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + numMess + " " + id + " " + message;
	}

}
