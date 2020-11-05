package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.packet.messages.UUIDMessage;
import delivery.receiver.Receiver;
import main.DataStream;
import main.Invariables;

import java.net.InetSocketAddress;

public class MsgReceive implements ReceiveMessage {
    @Override
    public void receiveMessage(Packet receivedMessagePacket, Receiver receiver, DataStream dataStream) {
        String[] receivedMessageArray = receivedMessagePacket.getMSG().trim().split(Invariables.SPACE);
        UUIDMessage receivedUUIDMessage = new UUIDMessage(receivedMessageArray[Invariables.FIRST],
                receivedMessageArray[Invariables.SECOND]);
        InetSocketAddress receivedMessageAddress = receivedMessagePacket.getAddress();

        dataStream.send(new Packet(Invariables.CONFIRM, receivedUUIDMessage.toString(), receivedMessageAddress));
        System.out.println("Gotten " + receivedUUIDMessage + " from " + receivedMessageAddress + "...");

        receiver.addReceivedMSG(receivedUUIDMessage);
        receiver.getHost().getHostWork().addSentMSG(receivedUUIDMessage);
        receiver.getHost().getConnections().sendUUIDMessage(receivedUUIDMessage, receivedMessageAddress);
    }
}