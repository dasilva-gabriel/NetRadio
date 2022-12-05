package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NormalizationUtil;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class DiffPacket extends NetPacket {

	private final int numMess;
	private final String message, id;

	public DiffPacket(int numMess, String id, String message) {
		super(PacketID.DIFF);

		this.numMess = numMess;
		this.id = id;
		this.message = message;
	}

	public DiffPacket(String entry) {
		super(PacketID.DIFF, entry);

		this.numMess = Integer.valueOf(entry.split(" ")[0]);
		this.id = entry.split(" ")[1];
		this.message = entry.split(" ")[2];
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry, getPacketID().name() + " [0-9]{4} " + NetPatterns.USER_ID + " .{140}");
	}

	public int getNumMess() {
		return numMess;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + NormalizationUtil.intToNum(numMess) + " "
			+ NormalizationUtil.normalizeWithSize(id, 8) + " " + NormalizationUtil.normalizeWithSize(message, 140)
			+ "\r\n";
	}

}
