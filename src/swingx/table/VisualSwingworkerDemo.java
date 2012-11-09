package swingx.table;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class VisualSwingworkerDemo implements Runnable {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new VisualSwingworkerDemo());
	}
	
	private JLabel label = new JLabel(" ");
	
	class MeaningOfLifeFinder extends VisualSwingWorker<String, Object> {
	        public String doInBackground() {
	        	for (int i = 0; i < 10; i++) {
	        		try {
						Thread.sleep(1000);
						setProgress(i * 10);
					} catch (InterruptedException e) {
						return "Interrupted !!!"; 
					}
	        	}
	        	return "I found the answer !!! 42 !!!";
	        }
	        protected void done() {
	            try { 
	               label.setText(get());
	            } catch (Exception ignore) {
	            }
	        }
	    }
	public void run() {
		final JCheckBox displayCancel = new JCheckBox("Allow Cancel");
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton findTheAnswer = new JButton("Find the meaning of life ...");
		findTheAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MeaningOfLifeFinder().execute(
						frame, 
						"Finding the Meaning of life ...", 
						"I am working on it !", 
						displayCancel.isSelected());
			}
		});
		frame.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.add(displayCancel);
		northPanel.add(findTheAnswer);
		frame.add(northPanel, BorderLayout.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}	

}
