package models.requests;

@SuppressWarnings("unused")
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
