package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.RegexUtil;

public class LastPacket extends NetPacket {

	private final int nbMess;

	public LastPacket(int nbMess) {
		super(PacketID.LAST);

		this.nbMess = nbMess;
	}

	public LastPacket(String string) {
		super(PacketID.LAST, string);

		String[] splitted = string.split(" ");
		this.nbMess = Integer.valueOf(splitted[1]);
	}

	@Override
	public boolean syntaxIsCorrect(String entry) {
		return RegexUtil.regexMatch(entry, getPacketID().name() + " [0-9]{1,3}");
	}

	public int getNbMess() {
		return nbMess;
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + nbMess;
	}

}
