/*
 * Main.java
 *
 * Created on May 9, 2006, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package magnifyglass;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;

/**
 *
 * @author jm158417
 */
public class Main {
    
    ValidationOverlay validationOverlay;
    MagGlassOverlay magGlassOverlay;
    /** Creates a new instance of Main */ 
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                TestFrame frame = new TestFrame();
                Main main = new Main();
                frame.main = main;
                main.validationOverlay = new ValidationOverlay();
                //frame.setGlassPane(main.validationOverlay);
                main.magGlassOverlay = new MagGlassOverlay();
                frame.setGlassPane(main.magGlassOverlay);
                
                //frame.setGlassPane(new PainterOverlay(frame.overlayed, 
                        //new CheckerboardPainter(Color.blue,new Color(0,0,0,0))));
                //frame.setGlassPane(new PainterOverlay(frame.subpanel, 
                //        new MattePainter(new Color(255,0,0,100))));
                frame.getGlassPane().setVisible(true);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
        
    
}
