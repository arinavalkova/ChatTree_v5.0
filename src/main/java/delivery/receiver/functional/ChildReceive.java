package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import host.connections.ConnectionInfo;
import host.connections.ConnectionType;
import main.DataStream;
import main.Invariables;

import java.net.InetSocketAddress;

public class ChildReceive implements ReceiveMessage {
    @Override
    public void receiveMessage(Packet receivedChildHostInfoPacket, Receiver receiver, DataStream dataStream) {
        ConnectionInfo childInfo = new ConnectionInfo(receivedChildHostInfoPacket.getMSG().trim());
        System.out.println("Gotten new child " + childInfo + "...");
        receiver.getHost().getConnections().addConnection(childInfo, ConnectionType.CHILD);
        System.out.println("Added new child to network...");

        InetSocketAddress childAddress = receivedChildHostInfoPacket.getAddress();
        System.out.println("Sending my host info to child " + childAddress + "...");
        dataStream.send(new Packet(Invariables.PARENT, receiver.getHost().getConnectionInfo().toString(), childAddress));

        ConnectionInfo deputyInfo = receiver.getHost().getConnections().findAndSetMyDeputy();

        if (deputyInfo != null ) {
            System.out.println("Sending my deputy to child...");
            dataStream.send(new Packet(Invariables.PARENT_DEPUTY, deputyInfo.toString(), childAddress));
        } else {
            System.out.println("No deputy for me...");
        }
    }
}
