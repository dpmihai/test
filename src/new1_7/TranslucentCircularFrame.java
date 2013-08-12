package new1_7;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TranslucentCircularFrame extends JFrame {

  /**
   * Creates a frame containing a text area and a button. The frame has a
   * circular shape and a 75% opacity.
   * 
   * http://www.javacodegeeks.com/2013/07/java-7-swing-creating-translucent-and-shaped-windows.html
   */
  public TranslucentCircularFrame() {
    super("Translucent Circular Frame");
    setLayout(new GridBagLayout());
    final JTextArea textArea = new JTextArea(3, 50);
    textArea.setBackground(Color.GREEN);
    add(textArea);
    setUndecorated(true);

    // set the window's shape in the componentResized method, so
    // that if the window is resized, the shape will be recalculated
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        setShape(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
      }
    });

    // make the window translucent
    setOpacity(0.75f);

    setLocationRelativeTo(null);
    setSize(250, 250);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main(String[] args) {

    // Create the GUI on the event-dispatching thread
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // check if the OS supports translucency
        if (ge.getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
          new TranslucentCircularFrame();
        }
      }
    });
  }
}