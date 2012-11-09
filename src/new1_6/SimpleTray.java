package new1_6;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 1, 2007
 * Time: 3:48:42 PM
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SimpleTray {
    public static void main(String args[]) {
        Runnable runner = new Runnable() {
            public void run() {
                if (SystemTray.isSupported()) {
                    SystemTray tray = SystemTray.getSystemTray();
                    //Image image = Toolkit.getDefaultToolkit().getImage("Hat.jpg");
                    Image image = null;
                    try {
                        image = ImageIO.read(SimpleTray.class.getResource("Hat.jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();  
                    }
                    PopupMenu popup = new PopupMenu();
                    MenuItem item = new MenuItem("Hello, World");                                        
                    popup.add(item);
                    MenuItem item2 = new MenuItem("Exit");
                    popup.add(item2);

                    final TrayIcon trayIcon = new TrayIcon(image, "Tip Text", popup);

                    item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            trayIcon.displayMessage("Good-bye", "Cruel World",
                                    TrayIcon.MessageType.WARNING);
                        }
                    });

                    item2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });

                    try {
                        tray.add(trayIcon);
                    } catch (AWTException e) {
                        System.err.println("Unable to add to system tray: " + e);
                    }
                } else {
                    System.err.println("No system tray available");
                }
            }
        };
        EventQueue.invokeLater(runner);
    }
}