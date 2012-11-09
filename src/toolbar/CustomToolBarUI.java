package toolbar;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 11:48:03 AM
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
import javax.swing.plaf.metal.MetalToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.*;

public class CustomToolBarUI extends BasicToolBarUI {
  public final static String FRAME_IMAGEICON = "ToolBar.frameImageIcon";


  protected JFrame createFloatingWindow(JToolBar toolbar) {
    JFrame frame = new JFrame(toolbar.getName());
    frame.setResizable(false);
    Icon icon = UIManager.getIcon(FRAME_IMAGEICON);
    if (icon instanceof ImageIcon) {
      Image iconImage = ((ImageIcon)icon).getImage();
      frame.setIconImage(iconImage);
    }
    WindowListener windowListener = createFrameListener();
    frame.addWindowListener(windowListener);
    return frame;
  }
}