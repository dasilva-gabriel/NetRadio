package common.protocols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Logger;
import common.NetRadioInstance;
import common.packets.NetPacket;
import common.packets.PacketID;
import common.protocols.listener.PacketListenerHandler;

public abstract class NetProtocol {

	private final NetRadioInstance instance;
	private ArrayList<PacketID> registeredPackets;
	private PacketListenerHandler packetListenerHandler;

	public NetProtocol(NetRadioInstance instance) {
		this.instance = instance;
		registeredPackets = new ArrayList<PacketID>();
		packetListenerHandler = new PacketListenerHandler();
	}

	public abstract void init();

	public NetPacket receivingPacket(String entry) {
		List<PacketID> packetID = getRegisteredPackets().stream().filter(p -> entry.startsWith(p.name()))
			.collect(Collectors.toList());

		if (packetID.size() == 0) return null;

		PacketID packet = packetID.get(0);

		try {
			return (NetPacket) Class.forName(packet.getPacketClass().getName()).getConstructor(String.class)
				.newInstance(entry);
		} catch (Exception e) {
			Logger.log.warning("Error reading the packet. The packet may be poorly formed. (" + e.toString() + ")");
		}

		return null;
	}

	public void registerPacket(PacketID id) {
		if (!registeredPackets.contains(id)) registeredPackets.add(id);
	}

	public void registerPackets(PacketID... ids) {
		for (PacketID object : ids)
			registerPacket(object);
	}

	public void unregisterPacket(PacketID id) {
		if (registeredPackets.contains(id)) registeredPackets.remove(id);
	}

	public PacketListenerHandler getPacketListenerHandler() {
		return packetListenerHandler;
	}

	public ArrayList<PacketID> getRegisteredPackets() {
		return registeredPackets;
	}

	public List<PacketID> fromString(String s) {
		return registeredPackets.stream().filter(p -> s.startsWith(p.name())).collect(Collectors.toList());
	}

	public NetRadioInstance getInstance() {
		return instance;
	}
}
