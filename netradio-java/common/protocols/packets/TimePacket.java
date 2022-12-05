package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class TimePacket extends NetPacket {

	private long time;

	public TimePacket(long time) {
		super(PacketID.TIME);
		this.time = time;
	}

	public TimePacket(String string) {
		super(PacketID.TIME, string);

		String[] splitted = string.split(" ");

		this.time = Long.valueOf(splitted[1]);
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry, NetPatterns.DIFFUSING_TIME.getPattern());
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + time;
	}

	public long getTime() {
		return time;
	}

}
