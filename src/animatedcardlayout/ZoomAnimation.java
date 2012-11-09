package animatedcardlayout;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;

import org.javadev.effects.*;

public class ZoomAnimation implements Animation {
    boolean direction = true;
    int animationDuration = 2000;
    SpecialPanel animationPanel;

    public Component animate(Component first, Component last,
                             AnimationListener listener) {
        return new SpecialPanel(first, last, listener);

/*
      For many effects, you would employ direction logic such as that shown
      below -- see CubeAnimation.java, DashboardAnimation.java, and
      SlideAnimation.java for examples. This logic does not work for
      ZoomAnimation because SlideShow keeps alternating between a pair of
      components, which causes AnimatingCardLayout to keep switching
      direction. To see the result, comment out the return statement above,
      and uncomment the return statement below.
*/

/*
      return new SpecialPanel ((direction) ? first : last,
                               (direction) ? last : first, listener);
*/
    }

    public void setAnimationDuration(int duration) {
        animationDuration = duration;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    class SpecialPanel extends JPanel {
        final static int STEP_TIME = 50;

        BufferedImage firstImage, secondImage;
        double incr, xscale, yscale;
        int maxsteps, step;
        Timer timer;

        SpecialPanel(Component component1, Component component2,
                     final AnimationListener listener) {
            // Take a snapshot of the first component.

            firstImage = new BufferedImage(component1.getSize().width,
                    component1.getSize().height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = firstImage.createGraphics();
            component1.paint(g);
            g.dispose();

            // Take a snapshot of the second component.

            secondImage = new BufferedImage(component2.getSize().width,
                    component2.getSize().height,
                    BufferedImage.TYPE_INT_RGB);
            g = secondImage.createGraphics();
            component2.paint(g);
            g.dispose();

            // Calculate the maximum number of steps in the animation sequence
            // and the scaling increment used to modify the x and y scale factors
            // during each step in the sequence.

            maxsteps = animationDuration / STEP_TIME;
            incr = 1.0 / (maxsteps >> 1);

            // Create an action listener whose logic, which runs on the
            // event-dispatching thread, paints each step's image and terminates
            // the animation sequence after the last step has run.

            ActionListener al;
            al = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    repaint();

                    if (++step >= maxsteps) {
                        timer.stop();
                        listener.animationFinished();
                    }
                }
            };

            // Create and start a timer that invokes the action listener at a
            // specific interval.

            timer = new Timer(STEP_TIME, al);
            timer.start();
        }

        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            // Paint the background to remove any artifacts of previously
            // displayed image.

            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Advance the animation sequence by calculating the next scale
            // factors and drawing either the first or the second image.

            if (step < (maxsteps >> 1)) // faster than maxsteps/2
            {
                if (step == 0) {
                    xscale = 1.0;
                    yscale = 1.0;
                } else {
                    xscale -= incr;
                    yscale -= incr;
                }
                g2d.scale(xscale, yscale);
                g2d.drawImage(firstImage, 0, 0, null);
            } else {
                if (step == (maxsteps >> 1)) {
                    xscale = incr;
                    yscale = incr;
                } else {
                    xscale += incr;
                    yscale += incr;
                }
                g2d.scale(xscale, yscale);
                g2d.drawImage(secondImage, 0, 0, null);
            }
        }
    }
}
