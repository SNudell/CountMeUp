package models.responses;


import models.Counter;

public class CounterResponse {
    private long counterStatus;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(long counterStatus) {
        this.counterStatus = counterStatus;
    }

    public CounterResponse (long counterStatus, String name) {
        this.counterStatus = counterStatus;
        this.name = name;
    }

    public CounterResponse (Counter counter) {
        this.counterStatus = counter.get();
        this.name = counter.getName();
    }

}
