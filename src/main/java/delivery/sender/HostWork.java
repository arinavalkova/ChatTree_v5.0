package delivery.sender;

import host.Host;
import main.DataStream;
import host.connections.Connections;
import main.Invariables;
import delivery.packet.messages.UUIDMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HostWork {
    private final DataStream dataStream;
    private final Host host;
    private final Connections connections;
    private final Map<UUID, UUIDMessage> sentMessages;

    public HostWork(DataStream dataStream, Host host, Connections connections) {
        this.dataStream = dataStream;
        this.host = host;
        this.connections = connections;
        sentMessages = new ConcurrentHashMap<>();
    }

    public void start() {
        Thread readLinesAndSendToAllConnections = new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Type message and press ENTER to send...");
                try {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    } else if (line.equals(Invariables.INFO)) {
                        host.printAllInfo();
                        continue;
                    }
                    UUIDMessage uuidMessage = new UUIDMessage(line);
                    addSentMSG(uuidMessage);
                    connections.sendUUIDMessage(uuidMessage, null);
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }
            }
            dataStream.close();
        });
        readLinesAndSendToAllConnections.start();
    }

    public void printSentMessages() {
        synchronized (sentMessages) {
            sentMessages.forEach((k, v) -> System.out.println("        " + v));//////////////
        }
    }

    public void confirmMSGDelivery(UUID uuid) {
        sentMessages.get(uuid).confirmDelivery();
    }

    public void addSentMSG(UUIDMessage uuidMessage) {
        synchronized (sentMessages) {
            sentMessages.put(uuidMessage.getUUID(), uuidMessage);
        }
    }
}
