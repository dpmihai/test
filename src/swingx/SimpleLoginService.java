package swingx;

import org.jdesktop.swingx.auth.LoginService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 30, 2007
 * Time: 2:16:13 PM
 */
public class SimpleLoginService extends LoginService {

    private List<Server> servers = new ArrayList<Server>();

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    private Server getServer(String name) {
        for (Server server : servers) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    public boolean authenticate(String name, char[] password, String server)
            throws Exception {

        if ( ("mihai".equals(name) || "one".equals(name)) && "123".equals(String.valueOf(password))) {
            Thread.sleep(3000);
            Server serv = getServer(server);
            System.out.println("Server used = " + serv);

            return true;
        } else {
            return false;
        }
    }
   
}