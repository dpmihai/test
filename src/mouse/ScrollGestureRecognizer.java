package mouse;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.AWTEventListener;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 25, 2005 Time: 9:45:38 AM
 */
public class ScrollGestureRecognizer implements AWTEventListener{
       private static ScrollGestureRecognizer instance = new ScrollGestureRecognizer();

       private ScrollGestureRecognizer(){
           start();
       }

       public static ScrollGestureRecognizer getInstance(){
           return instance;
       }

       void start(){
           Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
       }

       void stop(){
           Toolkit.getDefaultToolkit().removeAWTEventListener(this);
       }

       public void eventDispatched(AWTEvent event){
           MouseEvent me = (MouseEvent)event;
           boolean isGesture = SwingUtilities.isMiddleMouseButton(me) && me.getID()==MouseEvent.MOUSE_PRESSED;
           if(!isGesture)
               return;

           JViewport viewPort = (JViewport)SwingUtilities.getAncestorOfClass(JViewport.class, me.getComponent());
           if(viewPort==null)
               return;
           JRootPane rootPane = SwingUtilities.getRootPane(viewPort);
           if(rootPane==null)
               return;

           Point location = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), rootPane.getGlassPane());
           ScrollGlassPane glassPane = new ScrollGlassPane(rootPane.getGlassPane(), viewPort, location);
           rootPane.setGlassPane(glassPane);
           glassPane.setVisible(true);
       }
   }