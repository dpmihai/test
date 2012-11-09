package magnify;

import javax.swing.*;
import java.io.File;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 5:53:36 PM
 */
public class TestDetachedMagnifyingGlass {

    public TestDetachedMagnifyingGlass(File f) {
        // image frame
        ImageIcon i = new ImageIcon(f.getPath());
        JLabel l = new JLabel(i);

        JFrame imgFrame = new JFrame("Image");
        imgFrame.getContentPane().add(l);
        imgFrame.pack();
        imgFrame.setVisible(true);
        // magnifying glass frame
        JFrame magFrame = new JFrame("Mag");
        DetachedMagnifyingGlass mag = new DetachedMagnifyingGlass(l, new Dimension(150, 150), 2.0);
        magFrame.getContentPane().add(mag);
        magFrame.pack();
        magFrame.setLocation(new Point(
                imgFrame.getLocation().x + imgFrame.getWidth(),
                imgFrame.getLocation().y));
        magFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        new TestDetachedMagnifyingGlass(f);
    }
}

