package host.connections.refuse;

import host.Host;
import main.DataStream;

public interface ConnectionRefuse {
    void changeConnection(Host host, DataStream dataStream);
}

