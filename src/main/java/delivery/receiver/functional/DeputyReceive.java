package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import host.connections.ConnectionInfo;
import main.DataStream;

public class DeputyReceive implements ReceiveMessage{
    @Override
    public void receiveMessage(Packet receivedMessagePacket, Receiver receiver, DataStream dataStream) {
        ConnectionInfo deputyInfo = new ConnectionInfo(receivedMessagePacket.getMSG().trim());
        receiver.getHost().getConnections().setParentDeputy(deputyInfo);
        System.out.println("Gotten deputy " + deputyInfo + " from " +
                receivedMessagePacket.getAddress() + "...");
    }
}
