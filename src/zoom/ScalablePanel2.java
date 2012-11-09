package zoom;

import sun.awt.dnd.SunDropTargetEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * User: mihai.panaitescu
 * Date: 02-Nov-2009
 * Time: 11:37:22
 */
public class ScalablePanel2 extends JScrollPane  {

    private float zoom = 1;
    private float factor = 1;
    private JPanel zoomPanel;
    private Dimension initialSize;

    public ScalablePanel2(JPanel zoomPanel) {
        this.zoomPanel = zoomPanel;
        this.addMouseListener(mouseListenerTranslator);
        getViewport().setView(zoomPanel);
        setPreferredSize(new Dimension(300, 300));
        initialSize =new Dimension(300, 300);
    }


    private void scaleFonts(Component[] components) {
        for (Component c : components) {
            if (c instanceof JPanel) {
                scaleFonts(((JPanel)c).getComponents());
            }
            Font f = c.getFont();
            f = f.deriveFont(f.getSize2D() * factor);
            c.setFont(f);
            c.invalidate();
        }
        repaint();
    }

    public void setZoom(float zoom) {
        factor = zoom / this.zoom;
        this.zoom = zoom;
        System.out.println("set="+this.zoom + " factor="+factor);
        zoom();
    }

    public void zoom() {

        scaleFonts(zoomPanel.getComponents());

//        // Resize panel
        //final Dimension currSize = zoomPanel.getSize();
        System.out.println("size="+initialSize);
        zoomPanel.setPreferredSize(new Dimension(
                (int) (initialSize.width * zoom),
                (int) (initialSize.height * zoom)));

//        // Find out where our point on the zoom panel is now that we've resized it
//        final Point newViewPos = new Point();
//
//        newViewPos.x = (int) ((zoom * initialSize.width) * (1/ (double) initialSize.width));
//        newViewPos.y = (int) ((zoom * initialSize.height) * (1 / (double) initialSize.height));
//
//        System.out.println("newViewPos=" + newViewPos);
//
//        // Move the viewport to the new position to keep the area our mouse was in the same spot
//        getViewport().setViewPosition(newViewPos);

        zoomPanel.revalidate();
    }
    private MouseListener mouseListenerTranslator = new MouseListener() {
            public void mouseClicked( MouseEvent e ) {
                translateEvent( e );
            }

            public void mousePressed( MouseEvent e ) {
                translateEvent( e );
            }

            public void mouseReleased( MouseEvent e ) {
                translateEvent( e );
            }

            public void mouseEntered( MouseEvent e ) {} //FIX! Support rollovers by implementing this

            public void mouseExited( MouseEvent e ) {} //FIX! Support rollovers by implementing this
        };


        MouseMotionListener mouseMotionTranslator = new MouseMotionListener() {
            public void mouseDragged( MouseEvent e ) {
                translateEvent( e );
            }

            public void mouseMoved( MouseEvent e ) {
                translateEvent( e );
            }
        };

        private void translateEvent( MouseEvent e ) {
            e.translatePoint( (int)(e.getX() / zoom - e.getX()), (int)(e.getY() / zoom - e.getY()) );
            Component realComponent = getMouseEventTargetImpl( e.getX(), e.getY(), zoomPanel);
            retargetMouseEvent( realComponent, e.getID(), e ); //FIX!!! e.getID() should probably be something else?
        }

        //Copied and pasted and modified from the private method in java.awt.Container. Modified to only work with Swing components. --jsb
        /**
         * A private version of getMouseEventTarget which has three additional
         * controllable behaviors. This method searches for the top-most
         * descendant of this container that contains the given coordinates
         * and is accepted by the given filter. The search will be constrained to
         * descendants of only lightweight children or only heavyweight children
         * of this container depending on searchHeavyweightChildren. The search will
         * be constrained to only lightweight descendants of the searched children
         * of this container if searchHeavyweightDescendants is <code>false</code>.
         */
        private Component getMouseEventTargetImpl(int x, int y,
                                                  // EventTargetFilter filter,
                                                  JComponent startingComponent
        ) {
            int ncomponents = startingComponent.getComponentCount();
            //Component component[] = this.component;

            for (int i = 0 ; i < ncomponents ; i++) {

                Component comp = startingComponent.getComponent( i );
                if (comp != null && comp.isVisible() && comp.contains(x - comp.getX(), y - comp.getY() ) ) {

                    // found a component that intersects the point, see if there is
                    // a deeper possibility.
                    if (comp instanceof JComponent) {
                        //Container child = (Container) comp;
                        Component deeper = getMouseEventTargetImpl(x - comp.getX(),
                                y - comp.getY(),
                                //filter,
                                (JComponent)comp );
                        if (deeper != null) {
                            return deeper;
                        }
                    } else {
                        return comp;
                    }
                }
            }

            boolean	isMouseOverMe = contains(x,y);
            System.out.println("x="+x + "  y="+y + " over="+isMouseOverMe);

            // didn't find a child target, return this component if it's a possible
            // target
            if (isMouseOverMe ) { //&& filter.accept(this)) {
                return startingComponent;
            }
            // no possible target
            return null;
        }

        //Copied and pasted and modified from Container --jsb
        /**
         * Sends a mouse event to the current mouse event recipient using
         * the given event (sent to the windowed host) as a srcEvent.  If
         * the mouse event target is still in the component tree, the
         * coordinates of the event are translated to those of the target.
         * If the target has been removed, we don't bother to send the
         * message.
         */
        private void retargetMouseEvent(Component target, int id, MouseEvent e) {
            if (target == null) {
                return; // mouse is over another hw component or target is disabled
            }

            int x = e.getX(), y = e.getY();
            Component component;

            for(component = target;  component != ScalablePanel2.this ; component = component.getParent()) {
                x -= component.getX();
                y -= component.getY();
            }
            MouseEvent retargeted;
            if (e instanceof SunDropTargetEvent) {
                retargeted = new SunDropTargetEvent(target, id, x, y, ((SunDropTargetEvent)e).getDispatcher());
            } else if (id == MouseEvent.MOUSE_WHEEL) {
                retargeted = new MouseWheelEvent(target,
                        id, e.getWhen(), e.getModifiersEx() | e.getModifiers(), x, y,
                        e.getClickCount(), e.isPopupTrigger(),
                        ((MouseWheelEvent)e).getScrollType(),
                        ((MouseWheelEvent)e).getScrollAmount(),
                        ((MouseWheelEvent)e).getWheelRotation());
            }
            else {
                retargeted = new MouseEvent(target,
                        id, e.getWhen(), e.getModifiersEx() | e.getModifiers(), x, y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
            }

            //((AWTEvent)e).copyPrivateDataInto(retargeted);

            target.dispatchEvent(retargeted);
        }


}
