package credits;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 1, 2005 Time: 4:28:43 PM
 */
class Picture extends JPanel {

    private BufferedImage img;

    public Picture() {        
    }

    public Picture(String file) {
         setFile(file);
    }

    private void setFile(String file) {
        try {
            img = ImageIO.read(Picture.class.getResource(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (img != null) {
            g2d.drawImage(img, null, 0, 0);
        }
    }

    public void setImage(String file) {
        setFile(file);
        repaint();
    }

}
