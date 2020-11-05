package main;

public class TimeOut {
    private final Integer time;
    public TimeOut(Integer time) {
        this.time = time;
    }

    public void start() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
