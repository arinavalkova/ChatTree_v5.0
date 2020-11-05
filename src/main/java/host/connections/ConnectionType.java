package host.connections;

import main.Invariables;

public enum ConnectionType {
    CHILD(Invariables.CHILD), PARENT(Invariables.PARENT),
    PARENT_DEPUTY(Invariables.PARENT_DEPUTY), HOST(Invariables.HOST),
    MY_DEPUTY(Invariables.MY_DEPUTY);
    private final String hostClassification;

    ConnectionType(String hostClassification) {
        this.hostClassification = hostClassification;
    }
}
