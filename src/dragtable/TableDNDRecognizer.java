package dragtable;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;

/**
 * @author Decebal Suiu
 */
public class TableDNDRecognizer extends MouseInputAdapter {

    private boolean recognized;
    private boolean dragged;
    private Point pressedPoint;
    
    public boolean isDragged() {
        return dragged;
    }

    public void mousePressed(MouseEvent ev) {
        pressedPoint = ev.getPoint();
    }

    public void mouseDragged(MouseEvent ev) {
        Point p = ev.getPoint();
        if (!recognized
                && ev.isShiftDown()
                && ((Math.abs(pressedPoint.x - p.x) > 5) || (Math
                        .abs(pressedPoint.y - p.y) > 5))) {
            dragged = true;
            recognized = true;
            JComponent c = (JComponent) ev.getSource();
            TransferHandler th = c.getTransferHandler();
            if (th != null) {
                th.exportAsDrag(c, ev, ev.isAltDown() ? 
                        DnDConstants.ACTION_COPY : DnDConstants.ACTION_MOVE);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        recognized = false;
        dragged = false;
        pressedPoint = null;
    }
    
}
