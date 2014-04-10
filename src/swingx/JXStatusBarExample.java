package swingx;

import java.awt.Dimension;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXStatusBar;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public class JXStatusBarExample {
	
	public static void main(String[] args) {
		
		try {
            PlasticLookAndFeel.setCurrentTheme(new ExperienceBlue());
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();            
        }
		
		JXFrame frame = new JXFrame();
		frame.setPreferredSize(new Dimension(400, 300));
		
		JXStatusBar statusBar = new JXStatusBar();
        statusBar.add(new JXLabel("Status"), JXStatusBar.Constraint.ResizeBehavior.FILL);                                        
        frame.setStatusBar(statusBar);
        
        frame.setVisible(true);
	}

}
