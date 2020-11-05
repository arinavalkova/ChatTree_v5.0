package host.connections.refuse;

import host.Host;
import host.connections.ConnectionInfo;
import main.DataStream;

public class MyDeputyRefuse implements ConnectionRefuse{
    @Override
    public void changeConnection(Host host, DataStream dataStream) {
        ConnectionInfo myDeputyConnection = host.getConnections().findAndSetMyDeputy();
        if (myDeputyConnection == null) {
            System.out.println("Can't find my new deputy...");
            return;
        }
        System.out.println("Found new my deputy " + myDeputyConnection);
        host.getConnections().sendMyDeputyToChildren(myDeputyConnection);
    }
}
