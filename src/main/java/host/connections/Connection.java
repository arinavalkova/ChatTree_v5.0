package host.connections;

import main.Invariables;
import delivery.packet.Packet;
import delivery.packet.messages.UUIDMessage;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Connection {
    private ConnectionInfo connectionInfo;
    private final ConnectionType connectionType;
    private final Connections connections;
    private Long pingConfirmTime;
    private Long askPingConfirmTime;
    private final Map<UUID, UUIDMessage> messageBuffer;

    public Connection(ConnectionInfo connectionInfo, ConnectionType connectionType,
                      Connections connections) {
        this.connectionInfo = connectionInfo;
        this.connectionType = connectionType;
        this.connections = connections;
        this.pingConfirmTime = System.currentTimeMillis();
        this.askPingConfirmTime = System.currentTimeMillis();
        this.messageBuffer = new ConcurrentHashMap<>();
    }

    public void putUUIDMessageToBuffer(UUIDMessage uuidMessage) {
        synchronized (messageBuffer) {
            messageBuffer.put(uuidMessage.getUUID(), uuidMessage);
        }
    }

    public void updateUUIDMessageConfirmTime(UUID uuid) {
        messageBuffer.get(uuid).confirmDelivery();
    }

    @Override
    public String toString() {
        return connectionInfo + Invariables.SPACE + connectionType;
    }

    public Connections getConnections() {
        return connections;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void forwardMessage(UUIDMessage uuidMessage) {
        connections.getForwardMessage().start(uuidMessage, this);
    }

    public InetSocketAddress getAddress() {
        return connectionInfo.getAddress();
    }

    public void refuseConnection() {
        connections.disconnect(this);
    }

    public void connect() {
        System.out.println("Trying to establish connection with " + connectionInfo.getAddress() + "...");
        System.out.println("Starting exchanging host info...");
        connections.getDataStream().send(new Packet(Invariables.CHILD,
                connections.getHost().getConnectionInfo().toString(), connectionInfo.getAddress()));
    }

    public void sendMSG(String typeOfMSG, String MSG) {
        connections.getDataStream().send(new Packet(typeOfMSG, MSG, connectionInfo.getAddress()));
    }

    public boolean isTimedOut() {
        boolean pingConfirmTimedOut = (System.currentTimeMillis() - pingConfirmTime) > Invariables.PING_TIME_OUT;
        boolean pingAskTimedOut = (System.currentTimeMillis() - askPingConfirmTime) > Invariables.PING_TIME_OUT;
        boolean isDeputy = getConnectionType() == ConnectionType.PARENT_DEPUTY;

        return pingConfirmTimedOut || (pingAskTimedOut && !isDeputy);
    }

    public void updatePingConfirmTime(Long newTime) {
        pingConfirmTime = newTime;
    }

    public void updateAskPingConfirmTime(Long newTime) {
        askPingConfirmTime = newTime;
    }

    public void updateConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }
}

