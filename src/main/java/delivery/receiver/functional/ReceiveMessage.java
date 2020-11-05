package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import main.DataStream;

public interface ReceiveMessage {
    void receiveMessage(Packet receivedMessagePacket, Receiver receiver, DataStream dataStream);
}
