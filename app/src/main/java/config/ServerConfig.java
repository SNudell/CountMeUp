package config;

public class ServerConfig {

    private String ip;
    private String port;

    public ServerConfig(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }


    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    private String getServerAddress() {
        return "http://" + ip + ":" + port;
    }

    public String getFullCounterEndpoint() {
        String counterEndpoint = "/counter";
        return getServerAddress()+ counterEndpoint;
    }

    public String getFullIncrementEndpoint() {
        String incrementEndpoint = "/increment";
        return getFullCounterEndpoint()+ incrementEndpoint;
    }

    public String getFullDecrementEndpoint() {
        String decrementEndpoint = "/decrement";
        return getFullCounterEndpoint()+ decrementEndpoint;
    }
}
