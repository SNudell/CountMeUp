package config;

public class ServerConfig {

    private static String ip;
    private static String port;
    private static String counterEndpoint = "/counter";

    public static void setIp(String ip) {
        ServerConfig.ip = ip;
    }

    public static void setPort(String port) {
        ServerConfig.port = port;
    }

    public static String getServerAddress() {
        return "http://" + ip + ":" + port;
    }

    public static String getFullCounterEndpoint() {
        return getServerAddress()+counterEndpoint;
    }
}
