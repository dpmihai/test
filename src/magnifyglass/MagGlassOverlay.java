/*
 * MagGlassOverlay.java
 *
 * Created on May 12, 2006, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package magnifyglass;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


/**
 *
 * @author jm158417
 */
public class MagGlassOverlay extends JComponent {
    private int magx = 0;
    private BufferedImage lensImage;
    /** Creates a new instance of MagGlassOverlay */

    public MagGlassOverlay() {
        MouseInputAdapter mia = new InputHandler(this);
        this.addMouseListener(mia);
        this.addMouseMotionListener(mia);
        try {
            lensImage = ImageIO.read(MagGlassOverlay.class.getResourceAsStream("lens.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setMagLocation(int x) {
        this.magx = x;
        repaint();
    }
    
    double srad = 39;
    double lrad = 75;
    BufferedImage buff = new BufferedImage((int)srad*2+50,(int)srad*2+50,BufferedImage.TYPE_INT_ARGB);
    Area glass = new Area();
    
    protected void genGlass() {
        Ellipse2D le = new Ellipse2D.Double(mx,my,lrad*2+10,lrad*2+10);
        
        Point2D pt = createAnglePoint(new Point2D.Double(
                mx+lrad, my+lrad),
                Math.PI/2/2, lrad+srad+6);

        Ellipse2D se = new Ellipse2D.Double(
                pt.getX()-srad, pt.getY()-srad,
                srad*2, srad*2);

        Area sa = new Area();
        sa.add(new Area(se));
        sa.add(new Area(le));
        
        glass = sa;
    }

    protected void paintComponent(Graphics gs) {
        Graphics2D g = (Graphics2D)gs;
        g = (Graphics2D)g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        
        genGlass();
        g.setColor(Color.red);

        // regen the mag buffer
        if(glass.intersects(g.getClipBounds())) {
            JFrame frame = (JFrame)this.getParent().getParent();
            Container content = frame.getContentPane();
            Graphics2D gfx = buff.createGraphics();
            gfx.translate(-mx-lrad-21, -my-lrad-21);
            content.paint(gfx);
            gfx.dispose();
        }
        
        drawBigGlass(g);
        drawSmallGlass(g);

        g.drawImage(lensImage,mx-70,my-70,null);
    }

    private void drawBigGlass(final Graphics2D g) {
        // big glass
        Graphics2D g2 = (Graphics2D)g.create();
        g2.translate(mx+4+lrad,my+4+lrad);
        double irad = lrad-3;
        Ellipse2D crop = new Ellipse2D.Double(-irad,-irad,irad*2,irad*2);
        g2.setClip(crop);
        int scale = 2;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(buff, -buff.getWidth()*scale/2, -buff.getHeight()*scale/2, 
                            buff.getWidth()*scale, buff.getHeight()*scale, null);
        g2.setColor(new Color(0,0,255,120));
        g2.drawLine(0, (int)-lrad,0,(int)lrad);
        g2.drawLine((int)-lrad,0,(int)lrad,0);
        g2.dispose();
    }

    private void drawSmallGlass(Graphics2D g) {
        Ellipse2D crop;
        double irad;
        
        g = (Graphics2D)g.create();
        // small glass
        g.translate(mx+lrad+5+80,my+lrad+5+80);
        
        irad = srad-6; 
        // draw everything centered around the origin
        crop = new Ellipse2D.Double(-irad,-irad,irad*2,irad*2);
        g.setClip(crop);
        g.drawImage(buff, -buff.getWidth()/2, -buff.getHeight()/2, null);
        //g.draw(crop);
        // small glass cross
        g.setColor(new Color(0,0,255,120));
        g.drawLine(0, (int)-srad,  0, (int)srad);
        g.drawLine((int)-srad, 0, (int)srad, 0);
    }

    
    public static Point2D createAnglePoint(Point2D src, double angle, double distance) {
        double xoff = Math.cos(angle)*distance;
        double yoff = Math.sin(angle)*distance;
        Point2D dst = new Point2D.Double(src.getX()+xoff,src.getY()+yoff);
        return dst;
    }
        
    boolean isInside(Point pt) {
        if(glass.contains(pt)) {
            return true;
        }
        return false;
    }
    
    int mx, my;
    
    protected void setOffset(int x, int y) {
        this.mx = x;
        this.my = y;
        repaint();
    }

    // i need to abstract this out to a reusable class
    private class InputHandler extends MouseInputAdapter {
        Point prev;
        
        Component comp;
        public InputHandler(Component comp) {
            this.comp = comp;
        }


        public void mouseEntered(MouseEvent mouseEvent) {
            doDispatch(mouseEvent);
        }

        public void mouseMoved(MouseEvent mouseEvent) {
            doDispatch(mouseEvent);
        }
        
        public void mouseExited(MouseEvent mouseEvent) {
            doDispatch(mouseEvent);
        }


        public void mousePressed(MouseEvent mouseEvent) {
            if(isInside(mouseEvent.getPoint())) {
                prev = mouseEvent.getPoint();
            } else {
                prev = null;
                doDispatch(mouseEvent);
            }
        }

        
        public void mouseDragged(MouseEvent mouseEvent) {
            if(prev != null) {
                Point current = mouseEvent.getPoint();
                int px = current.x - prev.x;
                int py = current.y - prev.y;
                mx+=px;
                my+=py;
                repaint();
                prev = current;
            } else {
                doDispatch(mouseEvent);
            }
        }
        
        public void mouseReleased(MouseEvent mouseEvent) {
            doDispatch(mouseEvent);
        }
        
        protected Component getRealComponent(Point pt) {
            JRootPane pane = SwingUtilities.getRootPane(comp);
            // get the mouse click point relative to the content pane
            Point containerPoint = 
                SwingUtilities.convertPoint(comp, pt ,pane.getContentPane());

            // find the component that under this point
            Component component = SwingUtilities.getDeepestComponentAt(
                        pane.getContentPane(),
                        containerPoint.x,
                        containerPoint.y);
            return component;
        }
        
        protected void doDispatch(MouseEvent e) {
            // since it's not a popup we need to redispatch it.

            Component component = getRealComponent(e.getPoint());

            // return if nothing was found
            if (component == null) {
                return;
            }

            // convert point relative to the target component
            Point componentPoint = SwingUtilities.convertPoint(
                comp,
                e.getPoint(),
                component);

            // redispatch the event
            component.dispatchEvent(new MouseEvent(component,
                e.getID(),
                e.getWhen(),
                e.getModifiers(),
                componentPoint.x,
                componentPoint.y,
                e.getClickCount(),
                e.isPopupTrigger()));
        }
    }
    
}
