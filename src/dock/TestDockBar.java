package dock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestDockBar {
    public static String icons[] = new String[]{
            "document-128x128.png", "k-user-128x128.png",
            "desktop-128x128.png", "edit-paste-128x128.png",
            "date-128x128.png", "agent-128x128.png",
            "x-calc-128x128.png"};

    public static void main(String arg[]) {
        final JFrame frame = new JFrame();
        DockBar dockBar = new DockBar();
        for (int i = 0; i < 10; ++i) {
            System.out.println("path=" + new File(".").getAbsolutePath());
            URL url = DockBar.class.getResource("/dock/img/" + icons[i % icons.length]);
            System.out.println("url = " + url);
            Image img = Toolkit.getDefaultToolkit().getImage(url);
            ImageIcon imageIcon = new ImageIcon(img);
            Image image = imageIcon.getImage();
            BufferedImage b = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = b.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            dockBar.addApplication("App " + i, b, null);
        }

        frame.add(BorderLayout.CENTER, new JButton());
        JPanel dockBarPanel = new JPanel();
        dockBarPanel.add(dockBar);
        frame.add(BorderLayout.SOUTH, dockBarPanel);
        dockBar.setVisible(true);
        dockBar.setFrameParent();
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int state = frame.getExtendedState();
        state |= JFrame.MAXIMIZED_BOTH;
        frame.setExtendedState(state);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

}
