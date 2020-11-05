package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import host.connections.ConnectionInfo;
import host.connections.ConnectionType;
import main.DataStream;

public class ParentReceive implements ReceiveMessage {
    @Override
    public void receiveMessage(Packet receivedHostInfoPacket, Receiver receiver, DataStream dataStream) {
        receiver.getHost().getConnections().updateConnection(new ConnectionInfo(
                receivedHostInfoPacket.getMSG().trim()), ConnectionType.PARENT);
        System.out.println("Exchanging with parent was successfully...");

        ConnectionInfo deputyInfo = receiver.getHost().getConnections().findAndSetMyDeputy();

        if (deputyInfo != null) {
            System.out.println("Sending my deputy to child...");
            receiver.getHost().getConnections().sendMyDeputyToChildren(deputyInfo);
        } else {
            System.out.println("No deputy for me...");
        }
    }
}
