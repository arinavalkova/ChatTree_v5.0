package host;

import delivery.receiver.Receiver;
import delivery.sender.HostWork;
import host.connections.Connection;
import host.connections.ConnectionInfo;
import host.connections.ConnectionType;
import host.connections.Connections;
import main.DataStream;

public class Host {
    private final ConnectionInfo connectionInfo;
    private final HostWork hostWork;
    private final Connections connections;
    private final Receiver receiver;

    public Host(ConnectionInfo connectionInfo, DataStream dataStream) {
        this.connectionInfo = connectionInfo;
        this.connections = new Connections(dataStream, this);//host->connection info
        this.hostWork = new HostWork(dataStream, this, connections);
        this.receiver = new Receiver(dataStream, this);
    }

    public void start() {
        connections.start();
        hostWork.start();
        receiver.start();
    }

    public void printAllInfo() {
        System.out.println("****************************************************");
        System.out.println("Host info: ");
        System.out.println("    Name: " + connectionInfo.getName());
        System.out.println("    Address: " + connectionInfo.getAddress());
        System.out.println("    Loss: " + connectionInfo.getLoss());
        System.out.println("    Neighbours info:");
        connections.printAllConnections();
        System.out.println("    My deputy info:");
        System.out.println("        " + connections.getMyDeputy());
        System.out.println("    Parent deputy info:");
        System.out.println("        " + connections.getParentDeputy());
        System.out.println("    Received messages: ");
        System.out.println("        UUID                                 MSG");
        receiver.printReceivedMessages();
        System.out.println("    Sent messages: ");
        System.out.println("        UUID                                 MSG");
        hostWork.printSentMessages();
        System.out.println("****************************************************");
    }

    public void establishConnection(ConnectionInfo connectionInfo, ConnectionType type) {
        Connection connection = new Connection(connectionInfo, type, connections);
        connections.addConnection(connectionInfo, type);
        connection.connect();
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public Connections getConnections() {
        return connections;
    }

    public HostWork getHostWork() {
        return hostWork;
    }
}
