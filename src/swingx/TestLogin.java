package swingx;


import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 30, 2007
 * Time: 1:59:23 PM
 */
public class TestLogin {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new PlasticLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  
        }

        //testSave();
        Login.show();
    }

    public static void testSave() {
        List<Server> servers = new LinkedList<Server>();
        Server server2 = new Server("Prod Server", "192.168.14.3", "10001");
        servers.add(server2);
        Server server = new Server("Test Server", "124.32.53.12", "323");
        servers.add(server);
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.saveServers(servers);
    }

    public static Painter getPainter() {
        //int width = getSize().width / 2;
        int width = 100;
        int height = 200;
        Color color1 = new Color(255, 255, 255, 126);
        Color color2 = new Color(132, 168, 232, 126);
        LinearGradientPaint gradientPaint =
                new LinearGradientPaint(0.0f, 0.0f, width, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{color1, color2});
        MattePainter mattePainter = new MattePainter(gradientPaint);
        return mattePainter;
    }


}
