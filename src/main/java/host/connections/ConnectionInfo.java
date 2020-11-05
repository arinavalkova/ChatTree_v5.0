package host.connections;

import main.Invariables;

import java.net.InetSocketAddress;

public class ConnectionInfo {
    private final String name;
    private final Integer loss;
    private final InetSocketAddress address;

    public ConnectionInfo(String name, Integer loss, InetSocketAddress address) {
        this.name = name;
        this.loss = loss;
        this.address = address;
    }

    public ConnectionInfo(String dataLine) {
        String[] dataArray = dataLine.trim().split(Invariables.SPACE);
        this.name = dataArray[Invariables.FIRST];
        this.loss = Integer.parseInt(dataArray[Invariables.SECOND]);
        this.address = new InetSocketAddress(dataArray[Invariables.THIRD],
                Integer.parseInt(dataArray[Invariables.FOURTH]));
    }

    public String getName() {
        return name;
    }

    public int getLoss() {
        return loss;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return name + Invariables.SPACE + loss + Invariables.SPACE
                + address.getHostName() + Invariables.SPACE + address.getPort();
    }
}

