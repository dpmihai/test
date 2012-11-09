package swingx.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingworker.SwingWorker;

public abstract class VisualSwingWorker<T, V> extends SwingWorker<T, V> {
	/**
	 * Waiting dialog displaying a simple 
	 * @author Thierry LEFORT
	 * 15 févr. 08
	 *
	 */
	private class WaitingDialog extends JDialog implements PropertyChangeListener {

		private JProgressBar progressBar = new JProgressBar();
		
		
		private WaitingDialog(Component father, String title, String message, boolean allowCancel) {
			super(JOptionPane.getFrameForComponent(father),
					title, 
					true);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			progressBar.setIndeterminate(true);
			setLayout(new BorderLayout());
			JPanel messagePanel = 
				new JPanel(new FlowLayout(FlowLayout.LEADING));
			messagePanel.add(new JLabel(message));
			add(messagePanel, BorderLayout.NORTH);
			JPanel progressPanel = new JPanel();
			progressPanel.add(progressBar);
			add(progressPanel, BorderLayout.CENTER);
			if (allowCancel) {
				addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						cancel(true);
					}
				});
				JButton cancelButton = new JButton(UIManager.getString("FileChooser.cancelButtonText"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel(true);
					}
				});
				JPanel southPanel = new JPanel();
				southPanel.add(cancelButton);
				add(southPanel, BorderLayout.SOUTH);
			}
			pack();
			//setSize(new Dimension(200, 150));
			setLocationRelativeTo(father);
			
		}
		
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress".equals(evt.getPropertyName())) {
				if (progressBar.isIndeterminate()) {
					progressBar.setIndeterminate(false);
					progressBar.setMinimum(0);
					progressBar.setMaximum(100);
				}
				progressBar.setValue((Integer) evt.getNewValue());
			}
			if ("state".equals(evt.getPropertyName())) {
				if (StateValue.DONE.equals(evt.getNewValue())) {
					beforeWaitingPopUpIsDisposed();
					dispose();
					afterWaitingPopUpIsDisposed();
				}
			}
		}
		
	}
	
	
	/**
	 * Execute the task and display a waiting pop-up
	 * @param father the father of the waiting pop-up
	 * @param title the title of the 
	 * @param message
	 */
	public void execute(
			final Component father, 
			final String title, 
			final String message,
			final boolean allowCancel) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final WaitingDialog dialog = new WaitingDialog(father, title, message, allowCancel);
				addPropertyChangeListener(dialog);
				dialog.setVisible(true);
			}
		});
		execute();
	}
	
	/**
	 * Called when the task is done before the waiting pop-up is disposed.
	 */
	public void beforeWaitingPopUpIsDisposed() {
		
	}
	/**
	 * Called when the task is done after the waiting pop-up is disposed.
	 */
	public void afterWaitingPopUpIsDisposed() {
		
	}

	
}
