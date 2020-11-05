package main;

import host.Host;
import host.connections.ConnectionInfo;
import host.connections.ConnectionType;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser(args);
        System.out.println("I am " + argsParser.getAddress());
        try {
            DatagramSocket socket = new DatagramSocket(argsParser.getAddress().getPort());
            ConnectionInfo connectionInfo = new ConnectionInfo(argsParser.getHostName(), argsParser.getLoss(),
                    argsParser.getAddress());

            DataStream dataStream = new DataStream(socket);

            Host host = new Host(connectionInfo, dataStream);
            host.start();

            InetSocketAddress parentAddress = argsParser.getParentAddress();
            if (parentAddress != null) {
                host.establishConnection(new ConnectionInfo(null, null, parentAddress),
                        ConnectionType.PARENT);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
