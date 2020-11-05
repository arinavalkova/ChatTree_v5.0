package delivery.receiver.functional;

import delivery.packet.Packet;
import delivery.receiver.Receiver;
import main.DataStream;

public class ServiceReceive implements ReceiveMessage {
    @Override
    public void receiveMessage(Packet receiveServiceMessage, Receiver receiver, DataStream dataStream) {
        System.out.println(receiveServiceMessage.getMSG());
    }
}
