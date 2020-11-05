package delivery.packet.messages;

import main.Invariables;

import java.util.UUID;

public class UUIDMessage {
    private final String data;
    private final UUID uuid;
    private final Long timeOfCreation;
    private Long timeOfConfirm;

    public UUIDMessage(String data) {
        this.data = data;
        this.uuid = UUID.randomUUID();
        this.timeOfCreation = System.currentTimeMillis();
        this.timeOfConfirm = null;
    }

    public UUIDMessage(String uuid, String data) {
        this.uuid = UUID.fromString(uuid);
        this.data = data;
        this.timeOfCreation = System.currentTimeMillis();
        timeOfConfirm = null;
    }

    public UUIDMessage(UUID uuid, String data) {
        this.uuid = uuid;
        this.data = data;
        this.timeOfCreation = System.currentTimeMillis();
        timeOfConfirm = null;
    }

    public Long getTimeOfCreation() {
        return timeOfCreation;
    }

    public String getMessage() {
        return uuid + Invariables.SPACE + data;
    }

    public void confirmDelivery() {
        this.timeOfConfirm = System.currentTimeMillis();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Long getTimeOfConfirm() {
        return timeOfConfirm;
    }

    public void print() {
        System.out.println("        " + uuid + " " + data);
    }

    @Override
    public String toString() {
        return uuid + Invariables.SPACE + data;
    }

    public String getData() {
        return data;
    }
}
