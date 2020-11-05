package main;

import delivery.packet.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DataStream {
    private final DatagramSocket socket;

    public DataStream(DatagramSocket socket) {
        this.socket = socket;
    }

    public void send(Packet packet) {
        String message = packet.getPacketMSG();
        InetSocketAddress address = packet.getAddress();
        try {
            byte[] sendingDataBuffer = message.getBytes();
            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length,
                    InetAddress.getByName(address.getHostName()), address.getPort());

            socket.send(sendingPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet receive() throws IOException {
        byte[] receivingDataBuffer = new byte[Invariables.BUFF_SIZE];
        DatagramPacket receivingPacket;
        receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        socket.receive(receivingPacket);

        String receiveMessage = new String(receivingDataBuffer);
        InetSocketAddress receivedAddress = new InetSocketAddress(receivingPacket.getAddress(), receivingPacket.getPort());

        return new Packet(receiveMessage, receivedAddress);
    }

    public void close() {
        socket.close();
    }
}
