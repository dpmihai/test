package swingx.table;
import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class RepaintErrorMain implements Runnable{
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RepaintErrorMain());
	}

	public void run() {
		// Push you Error handling event queue
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(
				new ErrorHandlingEventQueue());
		
		JCheckBox errorInRepaint = new JCheckBox("If you select this you are dead ...") {
			@Override
			public void repaint() {
				super.repaint();
				if (isSelected()) {
					throw new RuntimeException("I told you ...");
				}
			}
		};
		JPanel panel = new JPanel();
		panel.add(errorInRepaint);
		JFrame frame = new JFrame("Error Testing Window");
		frame.add(errorInRepaint, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(JOptionPane.getRootFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
}
