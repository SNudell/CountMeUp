package config;

public class ServerConfig {

    private String ip;
    private String port;
    private String counterEndpoint = "/counter";
    private String incrementEndpoint = "/increment";
    private String decrementEndpoint = "/decrement";

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

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerAddress() {
        return "https://" + ip + ":" + port;
    }

    public String getFullCounterEndpoint() {
        return getServerAddress()+counterEndpoint;
    }

    public String getFullIncrementEndpoint() {
        return getFullCounterEndpoint()+incrementEndpoint;
    }

    public String getFullDecrementEndpoint() {
        return getFullCounterEndpoint()+decrementEndpoint;
    }
}
