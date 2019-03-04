package models.requests;

@SuppressWarnings("unused")
public class DecrementCounterRequest {
    private String name;
    private long decrement;

    public String getName() {
        return name;
    }

    public long getDecrement() {
        return decrement;
    }
}
