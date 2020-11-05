package host.connections;

import delivery.packet.Packet;
import host.connections.refuse.*;
import main.DataStream;
import host.Host;
import main.Invariables;
import main.TimeOut;
import delivery.packet.messages.ForwardMessage;
import delivery.packet.messages.UUIDMessage;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Connections {
    private final CopyOnWriteArrayList<Connection> connections;
    private Connection myDeputy;
    private Connection parentDeputy;
    private final ForwardMessage forwardMessage;
    private final DataStream dataStream;
    private final Host host;

    public Connections(DataStream dataStream, Host host) {
        this.host = host;
        this.dataStream = dataStream;
        this.forwardMessage = new ForwardMessage(dataStream);
        connections = new CopyOnWriteArrayList<>();
        myDeputy = null;
        parentDeputy = null;
    }

    public void addConnection(ConnectionInfo connectionInfo, ConnectionType connectionType) {
        Connection connection = new Connection(connectionInfo, connectionType, this);
        synchronized (connections) {
            connections.add(connection);
        }
    }

    public void printAllConnections() {
        synchronized (connections) {
            for (Connection currentConnection : connections) {
                System.out.println("        " + currentConnection);
            }
        }
    }

    public void disconnect(Connection connection) {
        connections.remove(connection);
    }

    public ForwardMessage getForwardMessage() {
        return forwardMessage;
    }

    public DataStream getDataStream() {
        return dataStream;
    }

    public Host getHost() {
        return host;
    }

    public void start() {
        initPingThread();
        initCheckPingRequest();
    }

    private void initPingThread() {
        Thread pingThread = new Thread(() -> {
            while (true) {
                new TimeOut(Invariables.RESEND_TIME).start();
                synchronized (connections) {
                    for (Connection currentConnection : connections) {
                        currentConnection.sendMSG(Invariables.ASK_PING, null);
                    }
                }
            }
        });
        pingThread.start();
    }

    private void initCheckPingRequest() {
        Thread checkConfirmConnection = new Thread(() -> {
            while (true) {
                new TimeOut(Invariables.CHECK_TIME).start();
                synchronized (connections) {
                    for (Connection currentConnection : connections) {
                        if (currentConnection.isTimedOut()) {
                            connections.remove(currentConnection);
                            refuseConnection(currentConnection);
                        }
                    }
                }
            }
        });
        checkConfirmConnection.start();
    }

    public void refuseConnection(Connection currentConnection) {
        ConnectionRefuse connectionRefuse;
        System.out.println("Refuse connection with " + currentConnection);
        connectionRefuse = refuseMap.get(currentConnection.getConnectionType());
        connectionRefuse.changeConnection(host, dataStream);

        if (myDeputy != null && currentConnection.getConnectionInfo().getAddress().
                equals(myDeputy.getConnectionInfo().getAddress())) {
            myDeputy = null;
            refuseMap.get(ConnectionType.MY_DEPUTY).changeConnection(host, dataStream);
        }
    }

    public void sendUUIDMessage(UUIDMessage uuidMessage, InetSocketAddress relativeConnection) {
        for (Connection connection : connections) {
            if (!connection.getAddress().equals(relativeConnection)) {
                System.out.println("Sending " + uuidMessage.getMessage() + " to " + connection);
                UUIDMessage connectionUUIDMessage = new UUIDMessage(uuidMessage.getUUID(), uuidMessage.getData());
                connection.putUUIDMessageToBuffer(connectionUUIDMessage);
                connection.forwardMessage(connectionUUIDMessage);
            }
        }
    }

    public Connection findConnection(InetSocketAddress inetSocketAddress) {
        for (Connection currentConnection : connections) {
            if (currentConnection.getAddress().equals(inetSocketAddress))
                return currentConnection;
        }
        return null;
    }

    public void updateConnection(ConnectionInfo connectionInfo, ConnectionType type) {
        for (Connection currentConnection : connections) {
            if (currentConnection.getConnectionType().equals(type)) {
                currentConnection.updateConnectionInfo(connectionInfo);
            }
        }
    }

    public ConnectionInfo findAndSetMyDeputy() {
        if (myDeputy != null) {
            return myDeputy.getConnectionInfo();
        }

        for (Connection currentConnection : connections) {
            if (currentConnection.getConnectionType() == ConnectionType.PARENT) {
                myDeputy = currentConnection;
                return currentConnection.getConnectionInfo();
            }
        }

        for (Connection currentConnection : connections) {
            if (currentConnection.getConnectionType() == ConnectionType.CHILD) {
                myDeputy = currentConnection;
                return currentConnection.getConnectionInfo();
            }
        }

        return null;
    }

    private static final Map<ConnectionType, ConnectionRefuse> refuseMap = Map.of(
            ConnectionType.CHILD, new ChildRefuse(),
            ConnectionType.PARENT_DEPUTY, new ParentDeputyRefuse(),
            ConnectionType.MY_DEPUTY, new MyDeputyRefuse(),
            ConnectionType.PARENT, new ParentRefuse()
    );

    public ConnectionInfo getParentDeputyConnection() {
        if (parentDeputy != null) {
            return parentDeputy.getConnectionInfo();
        }
        return null;
    }

    public void sendMyDeputyToChildren(ConnectionInfo connectionInfo) {
        for (Connection currentConnection : connections) {
            if (currentConnection.getConnectionType() != ConnectionType.PARENT) {
                dataStream.send(new Packet(Invariables.PARENT_DEPUTY, connectionInfo.toString(),
                        currentConnection.getAddress()));
            }
        }
    }

    public void setParentDeputy(ConnectionInfo deputyInfo) {
        if (host.getConnectionInfo().getAddress().equals(deputyInfo.getAddress())) {
            parentDeputy = null;
        } else {
            this.parentDeputy = new Connection(deputyInfo, ConnectionType.PARENT_DEPUTY, this);
        }
    }

    public Connection getMyDeputy() {
        return myDeputy;
    }

    public Connection getParentDeputy() {
        return parentDeputy;
    }
}
