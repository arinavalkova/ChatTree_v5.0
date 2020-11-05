package host.connections.refuse;

import delivery.packet.Packet;
import host.Host;
import host.connections.ConnectionInfo;
import host.connections.ConnectionType;
import main.DataStream;
import main.Invariables;

public class ParentRefuse implements ConnectionRefuse{
    @Override
    public void changeConnection(Host host, DataStream dataStream) {
        ConnectionInfo parentDeputyHostInfo = host.getConnections().getParentDeputyConnection();
        if(parentDeputyHostInfo == null) {
            System.out.println("No deputy. I am now outstanding host...");
            return;
        }
        host.establishConnection(parentDeputyHostInfo, ConnectionType.PARENT);
    }
}
