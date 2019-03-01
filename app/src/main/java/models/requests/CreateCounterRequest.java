package models.requests;

public class CreateCounterRequest {
    // the name the counter gets
    private String name;

    // the initial value the server is set tos
    private long value;

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }
}
