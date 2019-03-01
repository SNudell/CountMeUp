package models;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    private final AtomicLong counter;

    public long get() {
        return counter.get();
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Counter (long initialValue, String name) {
        counter = new AtomicLong(initialValue);
        this.name = name;
    }

    public long incrementBy(long value) {
        return counter.addAndGet(value);
    }

    public long decrementBy(long value) { return counter.addAndGet(value*-1); }

}
