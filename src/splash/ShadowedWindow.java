package splash;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JWindow;
import javax.swing.Timer;

public class ShadowedWindow extends JWindow {
    private BufferedImage splash = null;

    public ShadowedWindow(BufferedImage image) {
        createShadowPicture(image);
    }

    public void paint(Graphics g) {
        if (splash != null) {
            g.drawImage(splash, 0, 0, null);
        }
    }
    
    private void createShadowPicture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        setSize(new Dimension(width + 8, height + 8));
        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();

        splash = new BufferedImage(width + 8, height + 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) splash.getGraphics();

        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture = robot.createScreenCapture(new Rectangle(windowRect.x, windowRect.y, windowRect.width + 8, windowRect.height + 8));
            g2.drawImage(capture, null, 0, 0);
        } catch (AWTException e) { }

        float ninth = 1.0f / 9.0f;
        BufferedImage shadow = new BufferedImage(width + 8, height + 8, BufferedImage.TYPE_INT_ARGB); 
        Graphics g = shadow.getGraphics();
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        g.fillRect(4, 4, width, height);

        g2.drawImage(shadow, new ConvolveOp(new Kernel(3, 3, new float[] { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth })), 0, 0);
        g2.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(ShadowedWindow.class.getResourceAsStream("splash.png"));
            ShadowedWindow window = new ShadowedWindow(image);
            window.setVisible(true);
            Timer timer = new Timer(5000, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
