package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.packet.messages.UUIDMessage;
import delivery.receiver.Receiver;
import host.connections.Connection;
import main.DataStream;
import main.Invariables;

public class ConfirmReceive implements ReceiveMessage{
    @Override
    public void receiveMessage(Packet receivedMessagePacket, Receiver receiver, DataStream dataStream) {
        String[] receivedMessageArray = receivedMessagePacket.getMSG().trim().split(Invariables.SPACE);
        UUIDMessage receivedUUIDMessage = new UUIDMessage(receivedMessageArray[Invariables.FIRST],
                receivedMessageArray[Invariables.SECOND]);
        System.out.println(receivedMessagePacket.getAddress() + " say that " + receivedUUIDMessage
                + " gotten successfully...");
        Connection connection = receiver.getHost().getConnections().
                findConnection(receivedMessagePacket.getAddress());
        connection.updateUUIDMessageConfirmTime(receivedUUIDMessage.getUUID());
        //receiver.getHost().getHostWork().confirmMSGDelivery(receivedUUIDMessage.getUUID());
    }
}
