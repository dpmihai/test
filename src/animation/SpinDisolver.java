package animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 3:03:57 PM
 */
class SpinDissolver extends Dissolver {

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        // draw the screen, offset in case the window isn't at 0,0
        g.drawImage(screen_buffer,-fullscreen.getX( ),
            -fullscreen.getY( ),null);

        // save the current transform
        AffineTransform old_trans = g2.getTransform( );

        // move to the upper-lefthand corner of the frameanimation
        g2.translate(frame.getX( ), frame.getY( ));

        // move the frameanimation off toward the left
        g2.translate(-((count+1) * (frame.getX( )+frame.getWidth( ))/20),0);

        // shrink the frameanimation
        float scale = 1f / ((float)count+1);
        g2.scale(scale,scale);

        // rotate around the center
        g2.rotate(((float)count)/3.14/1.3,
            frame.getWidth( )/2, frame.getHeight( )/2);

        // finally draw the frameanimation
        g2.drawImage(frame_buffer,0,0,null);

        // restore the current transform
        g2.setTransform(old_trans);
    }

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Dissolve Hack");
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new SpinDissolver().dissolveExit(frame);
            }
        });

        frame.getContentPane().add(quit);
        frame.pack();
        frame.setLocation(300, 300);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
