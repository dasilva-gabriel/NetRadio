package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.RegexUtil;

public class LinbPacket extends NetPacket {

	private final int numDiff;

	public LinbPacket(int numDiff) {
		super(PacketID.LINB);

		this.numDiff = numDiff;
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry, getPacketID().name() + " [0-9]{2}");
	}


	@Override
	public String toString() {
		return getPacketID().name() + " " + ((numDiff < 10) ? "0" + numDiff : numDiff) + "\n";
	}

}
