package swingx;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 23, 2008
 * Time: 10:39:27 AM
 */
public class ServerHandler {

    public static String CONFIG_FILE = "E:\\Public\\test\\src\\swingx\\servers.properties";

    public List<Server> getServers() {
        List<Server> servers = new LinkedList<Server>();
        FileInputStream fin = null;
        try {
            LinkedProperties p = new LinkedProperties();
            fin = new FileInputStream(CONFIG_FILE);
            p.load(fin);
            Set<Map.Entry<Object, Object>> set = p.entrySet();
            for (Map.Entry entry : set) {
                String serverName = (String) entry.getKey();
                String value = (String) entry.getValue();
                String[] array = value.split(":");
                Server server = new Server(serverName, array[0], array[1]);
                servers.add(server);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return servers;
    }

    public List<String> getServerNames(List<Server> servers) {
        List<String> list = new LinkedList<String>();
        for (Server server : servers) {
            list.add(server.getName());
        }
        return list;
    }

    public void saveServers(List<Server> servers) {
        FileOutputStream fos = null;
        try {
            LinkedProperties p = new LinkedProperties();
            for (Server server : servers) {
                p.setProperty(server.getName(), server.getIp() + ":" + server.getPort());
            }
            fos = new FileOutputStream(CONFIG_FILE);
            p.store(fos, "Server name = ip : port");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void adjustServers(List<Server> servers, String serverName) {
        Server found = null;
        for (Iterator it = servers.iterator(); it.hasNext();) {
            Server server = (Server) it.next();
            if (server.getName().equals(serverName)) {
                found = server;
                it.remove();
                break;
            }
        }
        if (found != null) {
            System.out.println("found = " + found);
            servers.add(0, found);
        }
    }
        
}
