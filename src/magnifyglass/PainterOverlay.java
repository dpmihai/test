/*
 * PainterOverlay.java
 *
 * Created on May 10, 2006, 3:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package magnifyglass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author jm158417
 */
public class PainterOverlay extends JComponent {
    JComponent target;
    Painter painter;
    /** Creates a new instance of PainterOverlay */
    public PainterOverlay(JComponent target, Painter painter) {
        this.target = target;
        this.painter = painter;
    }
    
    protected void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        Point offset = calcOffset(target);
        g.translate(offset.x,offset.y);
        //painter.paint(g,target);
        g.translate(-offset.x,-offset.y);
    }

    private Point calcOffset(JComponent target) {
        //u.p("calcing: " + target);
        if(target == null) {
            return new Point(0,0);
        }
        // if the parent is the top then we must be the rootpane?
        if(target.getParent() == SwingUtilities.getWindowAncestor(target)) {
            return new Point(0,0);
        }
        
        Point parent = calcOffset((JComponent)target.getParent());
        Point self = target.getLocation();
        Point loc = new Point(parent.x + self.x, parent.y + self.y);
        //u.p("loc = " + loc);
        return loc;
    }
    
    
}
