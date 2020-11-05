package delivery.receiver;

import delivery.packet.Packet;
import delivery.packet.messages.UUIDMessage;
import delivery.receiver.functional.*;
import delivery.receiver.functional.AskPingReceive;
import delivery.receiver.functional.MsgReceive;
import host.Host;
import main.DataStream;
import main.Invariables;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;

public class Receiver {
    private final DataStream dataStream;
    private final Host host;
    private final ArrayList<UUIDMessage> receivedMessages;

    public Receiver(DataStream dataStream, Host host) {
        this.dataStream = dataStream;
        this.host = host;
        this.receivedMessages = new ArrayList<>();
    }

    public void start() {
        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    Packet receivedMessagePacket = dataStream.receive();
                    String type = receivedMessagePacket.getTypeOfMSG().trim();
                    ReceiveMessage receiveMessage = receiveMap.get(type);
                    if ((Math.random() * (Invariables.MAX_NUMBER - Invariables.MIN_NUMBER)) +
                            Invariables.MIN_NUMBER < host.getConnectionInfo().getLoss() &&
                            (type.equals(Invariables.MSG))) {
                        System.out.println("I loose " + receivedMessagePacket.getMSG() +
                                " from " + receivedMessagePacket.getAddress() + " :(");
                        continue;
                    }
                    receiveMessage.receiveMessage(receivedMessagePacket, this, dataStream);
                }
            } catch (SocketException e) {
                System.out.println("Socket closed...");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        receiveThread.start();
    }

    private static final Map<String, ReceiveMessage> receiveMap = Map.of(
            Invariables.CHILD, new ChildReceive(),
            Invariables.PARENT, new ParentReceive(),
            Invariables.MSG, new MsgReceive(),
            Invariables.ASK_PING, new AskPingReceive(),
            Invariables.SERVICE, new ServiceReceive(),
            Invariables.CONFIRM, new ConfirmReceive(),
            Invariables.PARENT_DEPUTY, new DeputyReceive(),
            Invariables.PING, new PingReceive()
    );

    public void printReceivedMessages() {
        for (UUIDMessage currentUUid : receivedMessages) {
            System.out.println("        " + currentUUid);
        }
    }

    public Host getHost() {
        return host;
    }

    public void addReceivedMSG(UUIDMessage uuidMessage) {
        receivedMessages.add(uuidMessage);
    }
}
