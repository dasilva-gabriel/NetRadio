package common.protocols.packets;

import common.packets.NetPacket;
import common.packets.PacketID;
import common.utils.NormalizationUtil;
import common.utils.NetPatterns;
import common.utils.RegexUtil;

public class RegiPacket extends NetPacket {

	private final int port1, port2;
	private final String id, ip1, ip2;

	public RegiPacket(String id, int port1, int port2, String ip1, String ip2) {
		super(PacketID.REGI);
		this.id = id;
		this.port1 = port1;
		this.port2 = port2;
		this.ip1 = ip1;
		this.ip2 = ip2;
	}

	public RegiPacket(String string) throws IllegalArgumentException {
		super(PacketID.REGI, string);

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
			getPacketID().name() + " " + NetPatterns.DIFFUSER_ID.getPattern() + " "
				+ NetPatterns.IPv4RECEIVE.getPattern() + " " + NetPatterns.VALID_PORT.getPattern() + " "
				+ NetPatterns.IPv4RECEIVE.getPattern() + " " + NetPatterns.VALID_PORT.getPattern());
	}

	public String getId() {
		return id;
	}

	public int getPort1() {
		return port1;
	}

	public int getPort2() {
		return port2;
	}

	public String getIp1() {
		return ip1;
	}

	public String getIp2() {
		return ip2;
	}

	@Override
	public String toString() {
		return getPacketID().name() + " " + NormalizationUtil.normalizeWithSize(id, 8) + " "
			+ NormalizationUtil.normalizeIPv4(ip1) + " " + NormalizationUtil.intToNum(port1) + " "
			+ NormalizationUtil.normalizeIPv4(ip2) + " " + NormalizationUtil.intToNum(port2) + "\r\n";
	}

}
