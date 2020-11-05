package delivery.packet;

import main.Invariables;

import java.net.InetSocketAddress;

public class Packet {
    private String typeOfMSG;
    private String MSG;
    private final InetSocketAddress address;

    public Packet(String typeOfMSG, String MSG, InetSocketAddress address) {
        this.typeOfMSG = typeOfMSG;
        this.MSG = MSG;
        this.address = address;
    }

    public Packet(String message, InetSocketAddress address) {
        splitPacketMessage(message);
        this.address = address;
    }

    private void splitPacketMessage(String packetMessage) {
        int packetMessageLength = packetMessage.length(), i;
        String typeOfMessage = "";
        String message = "";
        for (i = 0; i < packetMessageLength; i++) {
            char currentChar = packetMessage.charAt(i);
            if(currentChar != ' ') {
                typeOfMessage += currentChar;
            } else {
                break;
            }
        }

        for(int j = i + 1; j < packetMessageLength; j++) {
            message += packetMessage.charAt(j);
        }
        this.typeOfMSG = typeOfMessage;
        this.MSG = message;
    }

    public String getTypeOfMSG() {
        return typeOfMSG;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getMSG() {
        return MSG;
    }

    public String getPacketMSG() {
        return typeOfMSG + Invariables.SPACE + MSG;
    }
}
