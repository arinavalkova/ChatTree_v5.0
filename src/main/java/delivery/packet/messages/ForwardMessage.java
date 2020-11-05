package delivery.packet.messages;

import delivery.packet.Packet;
import host.connections.Connection;
import main.DataStream;
import main.Invariables;
import main.TimeOut;

public class ForwardMessage {
    private final DataStream dataStream;

    public ForwardMessage(DataStream dataStream) {
        this.dataStream = dataStream;
    }

    public void start(UUIDMessage uuidMessage, Connection connection) {
        Thread forwardingMessageThread = new Thread(() -> {
            dataStream.send(new Packet(Invariables.MSG, uuidMessage.toString(), connection.getAddress()));
            while (true) {
                new TimeOut(Invariables.RESEND_TIME).start();
                if (System.currentTimeMillis() - uuidMessage.getTimeOfCreation() > Invariables.MSG_TIMEOUT) {
                    System.out.println("Time of confirming " + uuidMessage + " is out");
                    connection.refuseConnection();
                    connection.getConnections().refuseConnection(connection);
                    break;
                } else if (uuidMessage.getTimeOfConfirm() == null) {
                    System.out.println(connection + " don't confirm getting " + uuidMessage);
                    System.out.println("Sending again " + uuidMessage + " to " + connection);
                    dataStream.send(new Packet(Invariables.MSG, uuidMessage.toString(), connection.getAddress()));
                } else {
                    break;
                }
            }
        });
        forwardingMessageThread.start();
    }
}
