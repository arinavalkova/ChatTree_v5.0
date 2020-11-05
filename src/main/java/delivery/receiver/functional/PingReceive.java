package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import main.DataStream;

public class PingReceive implements ReceiveMessage{
    @Override
    public void receiveMessage(Packet receivedMessagePacket, Receiver receiver, DataStream dataStream) {
        receiver.getHost().getConnections().findConnection(receivedMessagePacket.getAddress())
                .updatePingConfirmTime(System.currentTimeMillis());
    }
}
