package animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 3:01:16 PM
 */
class Dissolver extends JComponent implements Runnable {
    Frame frame;
    Window fullscreen;
    int count;
    BufferedImage frame_buffer;
    BufferedImage screen_buffer;

    public Dissolver() {
    }

    public void dissolveExit(JFrame frame) {
        try {
            this.frame = frame;
            Robot robot = new Robot();

            // cap screen w/ frameanimation to frameanimation buffer
            Rectangle frame_rect = frame.getBounds();
            frame_buffer = robot.createScreenCapture(frame_rect);

            // hide frameanimation
            frame.setVisible(false);

            // cap screen w/o frameanimation
            Dimension screensize = Toolkit.getDefaultToolkit()
                    .getScreenSize();
            Rectangle screen_rect = new Rectangle(0, 0,
                    screensize.width, screensize.height);
            screen_buffer = robot.createScreenCapture(screen_rect);

            // create big window w/o decorations
            fullscreen = new Window(new JFrame());
            fullscreen.setSize(screensize);
            fullscreen.add(this);
            this.setSize(screensize);
            fullscreen.setVisible(true);

            // start animation
            new Thread(this).start();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            count = 0;
            Thread.currentThread().sleep(100);
            for (int i = 0; i < 20; i++) {
                count = i;
                fullscreen.repaint();
                Thread.currentThread().sleep(100);
            }
        } catch (InterruptedException ex) {
        }
        System.exit(0);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // draw the screen, offset in case the window isn't at 0,0
        g.drawImage(screen_buffer, -fullscreen.getX(),
                -fullscreen.getY(), null);

        // draw the frameanimation
        Composite old_comp = g2.getComposite();
        Composite fade = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f - ((float) count) / 20f);
        g2.setComposite(fade);
        g2.drawImage(frame_buffer, frame.getX(), frame.getY(), null);
        g2.setComposite(old_comp);
    }

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Dissolve Hack");
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new Dissolver().dissolveExit(frame);
            }
        });

        frame.getContentPane().add(quit);
        frame.pack();
        frame.setLocation(300, 300);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }


}
