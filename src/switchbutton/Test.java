package switchbutton;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Test {
	
public static void main(String[] args) {
		
		SwitchToggleButton button = new SwitchToggleButton();								
		button.setLeftAction(new AbstractAction("", new ImageIcon(SwitchToggleButton.class.getResource("sql.gif"))) {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("--> Left selected");				
			}							
		});
		button.setRightAction(new AbstractAction("", new ImageIcon(SwitchToggleButton.class.getResource("report.gif"))) {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("--> Right selected");				
			}									
		});		
		button.setPreferredSize(new Dimension(32, 16));		
		
//		SwitchToggleButton button = new SwitchToggleButton() {
//
//			@Override
//			protected void onLeftSelected(ActionEvent e) {
//				System.out.println("%% left selected");
//			}
//
//			@Override
//			protected void onRightSelected(ActionEvent e) {
//				System.out.println("%% right selected");
//			}
//			
//		};		
//		button.setLeftText("On");
//		button.setRightText("Off");		
//		button.setPreferredSize(new Dimension(120, 22));	
		
		button.setActive(false);
		button.setLeftTooltip("Sql");
		button.setRightTooltip("Layout");
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(button);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setSize(150, 150);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 System.exit(0);
			 }
		});
	}

}
