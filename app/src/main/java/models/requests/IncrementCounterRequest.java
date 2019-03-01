package models.requests;

public class IncrementCounterRequest {
    private String name;
    private long increment;

    public String getName() {
        return name;
    }

    public long getIncrement() {
        return increment;
    }
}
