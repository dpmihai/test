package swingx;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 11, 2008
 * Time: 1:50:37 PM
 */
public class Server implements Serializable {

    private static final long serialVersionUID = -5712345608947514617L;

    private String name;
    private String ip;
    private String port;

    public Server(String name, String ip, String port) {        
        if (ip == null) {
            throw new IllegalArgumentException("Ip cannot be null!");
        }
        if (port == null) {
            throw new IllegalArgumentException("Port cannot be null!");
        }

        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        if (name == null) {
            return ip;
        } else {
            return name;
        }
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (ip != null ? !ip.equals(server.ip) : server.ip != null) return false;
        if (port != null ? !port.equals(server.port) : server.port != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        return result;
    }


    public String toString() {
        return "Server{" +
                "name='" + getName() + '\'' +
                ", ip='" + getIp() + '\'' +
                ", port='" + getPort() + '\'' +
                '}';
    }


}
