package swingx.table;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ErrorHandlingMain implements Runnable {
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ErrorHandlingMain());
	}

	public void run() {
		// Push you Error handling event queue
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(
				new ErrorHandlingEventQueue());
		
		JFrame frame = new JFrame("Error Testing Window");
		frame.setLayout(new BorderLayout());
		JButton errorButton = new JButton("throw new RuntimeException()");
		errorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				throw new RuntimeException("Error While Action is Performed !");
			}
		});
		
		final JCheckBox catchErrorCheck = new JCheckBox("Use the Error Handling EventQueue");
		catchErrorCheck.setSelected(true);
		catchErrorCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (catchErrorCheck.isSelected()) {
					Toolkit.getDefaultToolkit().getSystemEventQueue().push(
							new ErrorHandlingEventQueue());
				} else {
					Toolkit.getDefaultToolkit().getSystemEventQueue().push(
							new EventQueue());
				}
			}
		});
		JPanel panel = new JPanel();
		panel.add(catchErrorCheck);
		frame.add(panel, BorderLayout.NORTH);
		frame.add(errorButton, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(JOptionPane.getRootFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
