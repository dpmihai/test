/*
 * ValidationOverlay.java
 *
 * Created on May 10, 2006, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package magnifyglass;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author jm158417
 */
public class ValidationOverlay extends JComponent {
    private Painter painter;
    private List<JComponent>targets;
    
    /** Creates a new instance of ValidationOverlay */
    public ValidationOverlay() {
        targets = new ArrayList<JComponent>();
    }
    
    public void addTarget(JComponent comp) {
        targets.add(comp);
        repaint();
    }
    public void removeTarget(JComponent comp) {
        targets.remove(comp);
        repaint();
    }
    
    protected void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        if(getPainter() != null) {
            for(JComponent target : targets) {
                Point offset = calcOffset(target);
                g.translate(offset.x,offset.y);
                //getPainter().paint(g,target);
                g.translate(-offset.x,-offset.y);
            }
        }
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

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {        
        this.painter = painter;
        repaint();
    }
}
