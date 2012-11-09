package robot;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public abstract class TransparentWindow extends JFrame {

    private Image img;      // background image
    private Image tim;      // painted image
    private Graphics2D tig; // painted image graphics
    private Robot r;
    protected int width;
    protected int height;
    private boolean closeOnClick = false; // no interest in moving frame

    private boolean firstFocus = true;

    //  GraphicsEnvironment takes care about start bar (Toolkit does not)
    //private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

    public TransparentWindow(int width, int height) {
        this (width, height, false);
    }

    public TransparentWindow(int width, int height, boolean closeOnClick) {
        super();
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.width = width;
        this.height = height;
        this.closeOnClick = closeOnClick;

        setBounds((screenSize.width - width)/2, (screenSize.height-height)/2, width, height);
                
        try {
            r = new Robot();
        }
        catch (AWTException awe) {
            System.out.println("robot excepton occurred");
        }
        capture();

        WindowDragger dragger = new WindowDragger();
        addMouseMotionListener(dragger);
        addMouseListener(dragger);
        addFocusListener(new BackgroundRefresher());
        setVisible(true);
    }

    public void capture() {
        img = r.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
    }

    public void captureX() {
        Rectangle rect = getBounds();
        setVisible(false);
        Image xmg = r.createScreenCapture(rect);
        img.getGraphics().drawImage(xmg, rect.x, rect.y, rect.width, rect.height, null);
        setVisible(true);
    }


    public void paint(Graphics g) {
        Rectangle rect = g.getClipBounds();
        if (tim == null) {
            tim = createImage(getWidth(), getHeight());
            tig = (Graphics2D)tim.getGraphics();
        }
        if (!rect.getSize().equals(getSize())) {
            // if frame is not entirely in the scrren area
            // see WindowDragger.mouseDragged (should not happen)
            captureX();
        } else {
            paintP(g);
        }
    }

    public void paintP(Graphics g) {

        // antialising
        tig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw the background image of the "frame painted image" (what is behind this frame)
        tig.drawImage(img, 0, 0, getWidth(), getHeight(), getX(), getY(),
                getX() + getWidth(), getY() + getHeight(), null);

        paintImage(tig);

        g.drawImage(tim, 0, 0, null);
    }

    protected abstract void paintImage(Graphics2D tig);

    public void update(Graphics g) {
        this.paint(g);
    }

    private class BackgroundRefresher extends FocusAdapter {
        public void focusGained(FocusEvent e) {
            // to avoid another paint at first showing we use firstFocus variable
            if (!firstFocus) {
                setSize(0, 0);
                capture();
                setSize(width, height);
            } else {
                firstFocus = false;
            }
        }
    }

    private class WindowDragger implements MouseListener, MouseMotionListener {

        private Point mp;

        public void mouseClicked(MouseEvent e) {
            if (  closeOnClick || (e.getClickCount() == 2) ) {
                TransparentWindow.this.setVisible(false);
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (mp == null) {
                return;
            }
            Point p = e.getPoint();           
            int x = (getX() + p.x) - mp.x;
            int y = (getY() + p.y) - mp.y;
            // test frame to remain entirely in the screen area
            if ((x<0) || (y<0) || (x+width > screenSize.width) || (y+height > screenSize.height) ) {
                return;
            }
            setLocation(x, y);
            paintP(getGraphics());
        }

        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            mp = e.getPoint();
            if (closeOnClick) {
                TransparentWindow.this.setVisible(false);
            }
        }

        public void mouseReleased(MouseEvent e) {
            mp = null;
        }
    }
}
