package util;


/*************************************************************************
 *  Compilation:  javac Draw.java
 *
 *  Simple graphics library.
 *
 *  For documentation, see http://www.cs.princeton.edu/introcs/24inout
 *
 *  Todo
 *  ------------------
 *     -  Add support for CubicCurve2D or QudarticCurve2D or Arc2D
 *     -  Add support for gradient fill, etc.
 *     -  keep XOR mode?
 *     -  Remove support for non-centered spot
 *
 *  Remarks
 *  -------
 *     -  lines are much faster than spots?
 *     -  can't use AffineTransform since it inverts images and strings
 *     -  careful using setFont in inner loop within an animation -
 *        it can cause flicker
 *
 *************************************************************************/


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Draw {

    private BufferedImage offscreenImage;    // double buffered image
    private BufferedImage onscreenImage;     // double buffered image
    private Graphics2D offscreen;
    private Graphics2D onscreen;
    protected JFrame f;
    private double x = 0.0, y = 0.0;         // turtle is at coordinate (x, y)
    private double orientation = 0.0;        // facing this many degrees counterclockwise
    private int width, height;               // size of drawing area in pixels
    private Color background = Color.white;  // background color
    private Color foreground = Color.black;  // foreground color
    private boolean penDown = false;         // is the pen up or down?
    private boolean fill = true;             // fill in circles and rectangles?
    private boolean center = true;           // center spot at (x, y) or make (x, y) lower left
    private double xmin, xmax, ymin, ymax;   // boundary of (x, y) coordinates
    private Insets insets;
    private String title = "Untitled";       // title of the frameanimation in the menubar

    private Font font = new Font("Serif", Font.PLAIN, 16);


    // create a new drawing region of given dimensions
    public Draw(int width, int height) {
        this.width  = width;
        this.height = height;
        if (width <= 0 || height <= 0) throw new RuntimeException("Illegal dimension");
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        offscreen = (Graphics2D) offscreenImage.getGraphics();
        onscreen  = (Graphics2D) onscreenImage.getGraphics();
        setScale(0.0, 0.0, width, height);
        clear();
    }


    // accessor methods
    public double x()           { return x;           }
    public double y()           { return y;           }
    public double orientation() { return orientation; }
    public int width()          { return width;       }
    public int height()         { return height;      }

    // simple state changing methods
    public void setTitle(String s) { title = s; }

    public void penDown()       { penDown = true;     }
    public void penUp()         { penDown = false;    }
    public void fillOn()        { fill    = true;     }
    public void fillOff()       { fill    = false;    }
    public void centerOn()      { center  = true;     }
    public void centerOff()     { center  = false;    }

    public void XOROn()         { offscreen.setXORMode(background); }
    public void XOROff()        { offscreen.setPaintMode();         }

    // rotate counterclockwise in degrees
    public void rotate(double angle) { orientation += angle; }


   /***********************************************************************************
    *  Affine transform
    ***********************************************************************************/

    // change the user coordinate system
    public void setScale(double xmin, double ymin, double xmax, double ymax) {

        // update (x, y) so that they stay at same screen position ???
        // update orientation so that it stays the same relative to screen coorindates ???

        // there may be some bugs when using scaling with images ???


        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    // scale from user coordinates to screen coordinates
    public double scaleX(double x)  { return width  * (x - xmin) / (xmax - xmin); }
    public double scaleY(double y)  { return height * (ymax - y) / (ymax - ymin); }
    public double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    public double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }


    // scale from screen coordinates to user coordinates
    public double toUserX(double x)  { return (xmax - xmin) * (x - insets.left) / width  + xmin; }
    public double toUserY(double y)  { return (ymax - ymin) * (height - y + insets.top) / height + ymin; }


   /***********************************************************************************
    *  Background and foreground colors
    ***********************************************************************************/

    // clear the background
    public void clear() {
        offscreen.setColor(background);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(foreground);
    }

    // clear the background with a new color
    public void clear(Color color) {
        background = color;
        clear();
    }

    // set the foreground color
    public void setColor(Color color) {
        foreground = color;
        offscreen.setColor(foreground);
    }

    // set the foreground color using red-green-blue (inputs between 0 and 255)
    public void setColorRGB(int r, int g, int b) {
        setColor(new Color(r, g, b));
    }

    // set the foreground color using hue-saturation-brightness (inputs between 0 and 255)
    public void setColorHSB(int h, int s, int b) {
        setColor(Color.getHSBColor(1.0f * h / 255, 1.0f * s / 255, 1.0f * b / 255));
    }

    // set the foreground color using hue-saturation-brightness (inputs between 0.0 and 1.0)
    public void setColorHSB(double h, double s, double b) {
        setColor(Color.getHSBColor((float) h, (float) s, (float) b));
    }

    // set the foreground color to a random color
    public void setColorRandom() {
        setColorHSB((int) (Math.random() * 256), 255, 255);
    }


   /***********************************************************************************
    *  Move the turtle
    ***********************************************************************************/

    // go to (x, y), drawing if the pen is down
    public void go(double x, double y) {
        if (penDown) offscreen.draw(new Line2D.Double(scaleX(this.x), scaleY(this.y), scaleX(x), scaleY(y)));
        this.x = x;
        this.y = y;
    }

    // walk forward, drawing if the pen is down
    public void goForward(double d) {
        double oldx = x;
        double oldy = y;
        x += d * Math.cos(Math.toRadians(orientation));
        y += d * Math.sin(Math.toRadians(orientation));
        if (penDown) offscreen.draw(new Line2D.Double(scaleX(x), scaleY(y), scaleX(oldx), scaleY(oldy)));
    }



   /***********************************************************************************
    *  Draw spots
    ***********************************************************************************/
    // draw pixel at current location
    public void spot() { spot(0.0); }

    // draw circle of diameter d, centered at current location; degenerate to pixel if small
    public void spot(double d) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(d);
        double hs = factorY(d);
        if (ws <= 1 && hs <= 1)    offscreen.fillRect((int) xs, (int) ys, 1, 1);
        else if ( fill && !center) offscreen.fill(new Ellipse2D.Double(xs, ys - hs, ws, hs));
        else if (!fill && !center) offscreen.draw(new Ellipse2D.Double(xs, ys - hs, ws, hs));
        else if ( fill &&  center) offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        else if (!fill &&  center) offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }


    // draw w-by-h rectangle, centered at current location; degenerate to single pixel if too small
    public void spot(double w, double h) {
        // screen coordinates
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws <= 1 && hs <= 1)    offscreen.fillRect((int) xs, (int) ys, 1, 1);
        else if ( fill && !center) offscreen.fill(new Rectangle2D.Double(xs, ys - hs, ws, hs));
        else if (!fill && !center) offscreen.draw(new Rectangle2D.Double(xs, ys - hs, ws, hs));
        else if ( fill &&  center) offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        else if (!fill &&  center) offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    // get an image from the given filename
    private Image getImage(String filename) {
        URL url = Draw.class.getResource(filename);
        if (url == null) throw new RuntimeException("image " + filename + " not found");
        ImageIcon icon = new ImageIcon(url);
        return icon.getImage();
    }

    // draw spot using gif - fix to be centered at (x, y)
    public void spot(String s) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);

        // center of rotation is (xs, ys)
        if (center) {
            offscreen.rotate(Math.toRadians(orientation), xs, ys);
            offscreen.drawImage(image, (int) (xs - ws/2.0), (int) (ys - hs/2.0), null);
            offscreen.rotate(Math.toRadians(-orientation), xs, ys);
        }
        else { /// ?????
            offscreen.rotate(Math.toRadians(orientation), xs + ws/2.0, ys - hs/2.0);
            offscreen.drawImage(image, (int) (xs), (int) (ys - hs), null);
            offscreen.rotate(Math.toRadians(-orientation), xs + ws/2.0, ys - hs/2.0);
        }
    }

    // draw spot using gif, centered on (x, y), scaled of size w-by-h
    // center vs. !center
    public void spot(String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(w);
        double hs = factorY(h);
        // center of rotation is (xs, ys)
        offscreen.rotate(Math.toRadians(orientation), xs, ys);
        if (center) offscreen.drawImage(image, (int) (xs - ws/2.0), (int) (ys - hs/2.0), (int) ws, (int) hs, null);
        else        offscreen.drawImage(image, (int) (xs - ws)    , (int) (ys - hs)    , (int) ws, (int) hs, null);
        offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }


   /***********************************************************************************
    *  Writing text
    ***********************************************************************************/

    // write the given string in the current font
    public void setFont(Font font) {
        this.font = font;
    }

    // write the given string in the current font, center on the current location
    public void write(String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.rotate(Math.toRadians(orientation), xs, ys);
        if (center) offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        else        offscreen.drawString(s, (float) (xs)         , (float) (ys));
        offscreen.rotate(Math.toRadians(-orientation), xs, ys);
    }


   /***********************************************************************************
    *  Display the image on screen or save to file
    ***********************************************************************************/
    // wait for a short while
    public void pause(int delay) {
        show();
        try { Thread.currentThread().sleep(delay); }
        catch (InterruptedException e) { }
    }

    // view on-screen, creating new frameanimation if necessary
    public void show() {

        // create the GUI for viewing the image if needed
        if (f == null) {
            f = new JFrame();
            ImageIcon icon = new ImageIcon(onscreenImage);
            f.setContentPane(new JLabel(icon));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
            // f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
            f.setTitle(title);
            f.setResizable(false);
            f.pack();
            f.setVisible(true);
            insets = f.getInsets();    // must be after frameanimation is rendered
        }

        // draw
        onscreen.drawImage(offscreenImage, 0, 0, null);
        f.setTitle(title);
        f.repaint();
    }


    // save to file - suffix can be png, jpg, or gif
    public void save(String filename) {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        try { ImageIO.write(offscreenImage, suffix, file); }
        catch (IOException e) { e.printStackTrace(); }
    }

    // for emebedding into a JPanel
    public JLabel getJLabel() {
        if (offscreenImage == null) return null;         // no image available
        ImageIcon icon = new ImageIcon(offscreenImage);
        JLabel jlabel = new JLabel(icon);
        jlabel.setAlignmentX(0.5f);
        return jlabel;
    }


   /***********************************************************************************
    *  Sound
    ***********************************************************************************/

    // play a wav or midi sound
    public void play(String s) {
        URL url = Draw.class.getResource(s);
        if (url == null) throw new RuntimeException("audio " + s + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }







    // test client
    public static void main(String args[]) {
        Draw t = new Draw(600, 600);
//        t.setScale(0, 600, 600, 0);
        t.clear(Color.gray);
        t.penUp();
        t.go(100, 300);
        t.penDown();
        t.setColor(Color.blue);
        t.spot(30);
        t.rotate(30);
        t.setColor(Color.green);
        t.goForward(200);
        t.rotate(-30);
        t.setColor(Color.red);
        t.spot(30, 30);
        t.pause(1000);

        t.penUp();
        t.go(200, 100);
        t.spot("img.png");
        t.spot(10);
        t.setColor(Color.magenta);
        t.go(200, 175);
        t.spot(10);
        Font font = new Font("Arial", Font.BOLD, 30);
        t.setFont(font);
        t.write("Joker");
        t.pause(1000);

        t.go(400, 100);
        t.rotate(90);
        t.spot("img.png");
        t.spot(10);
        t.go(475, 100);
        t.spot(10);
        t.write("Kingpin");
        t.show();   // don't forget to repaint at end
    }

}

