package mouse;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 25, 2005 Time: 9:47:07 AM
 */
public class ScrollGlassPane extends JPanel implements ActionListener, MouseInputListener, SwingConstants{
       private static final Image img = Toolkit.getDefaultToolkit().createImage(EnhancedScrollingDemo.class.getResource("mouse.gif"));
    
       Component oldGlassPane = null;
       public Point location = null;

       private Timer movingTimer;
       private Point mouseLocation;
       private JViewport viewport;

       public ScrollGlassPane(Component oldGlassPane, JViewport viewport, Point location){
           this.oldGlassPane = oldGlassPane;
           this.viewport = viewport;
           this.location = mouseLocation = location;

           setOpaque(false);

           ScrollGestureRecognizer.getInstance().stop();
           addMouseListener(this);
           addMouseMotionListener(this);

           movingTimer = new Timer(100, this);
           movingTimer.setRepeats(true);
           movingTimer.start();
       }

       protected void paintComponent(Graphics g){
          g.drawImage(img, location.x-15, location.y-15, this);
       }

       /*-------------------------------------------------[ ActionListener ]---------------------------------------------------*/

       public void actionPerformed(ActionEvent e) {
           int deltax = (mouseLocation.x - location.x)/4;
           int deltay = (mouseLocation.y - location.y)/4;


           Point p = viewport.getViewPosition();
           p.translate(deltax, deltay);

           if(p.x<0)
               p.x=0;
           else if(p.x>=viewport.getView().getWidth()-viewport.getWidth())
               p.x = viewport.getView().getWidth()-viewport.getWidth();

           if(p.y<0)
               p.y = 0;
           else if(p.y>=viewport.getView().getHeight()-viewport.getHeight())
               p.y = viewport.getView().getHeight()-viewport.getHeight();

           viewport.setViewPosition(p);
       }

       /*-------------------------------------------------[ MouseListener ]---------------------------------------------------*/

       public void mousePressed(MouseEvent e) {
           movingTimer.stop();
           setVisible(false);
           JRootPane rootPane = SwingUtilities.getRootPane(this);
           rootPane.setGlassPane(oldGlassPane);
           ScrollGestureRecognizer.getInstance().start();
       }

       public void mouseClicked(MouseEvent e){
           mousePressed(e);
       }

       public void mouseMoved(MouseEvent e) {
           mouseLocation = e.getPoint();
       }

       public void mouseDragged(MouseEvent e){}
       public void mouseEntered(MouseEvent e){}
       public void mouseExited(MouseEvent e){}
       public void mouseReleased(MouseEvent e){}
   }