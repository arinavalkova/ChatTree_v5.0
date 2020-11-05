package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import main.DataStream;
import main.Invariables;

public class AskPingReceive implements ReceiveMessage {
    @Override
    public void receiveMessage(Packet receivedPacket, Receiver receiver, DataStream dataStream) {
        dataStream.send(new Packet(Invariables.PING, null, receivedPacket.getAddress()));
        var connection = receiver.getHost().getConnections().findConnection(receivedPacket.getAddress());
        if (connection == null)
            return;
        connection.updateAskPingConfirmTime(System.currentTimeMillis());
    }
}