package swingx.table;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class ErrorHandlingEventQueue extends EventQueue {

	@Override
	protected void dispatchEvent(AWTEvent event) {
		try {
			super.dispatchEvent(event);
		} catch (Throwable t) {
			Component comp = JOptionPane.getRootFrame();
			try {
				comp = (Component) event.getSource();
			} catch (ClassCastException e) { }
			StringWriter strWriter = new StringWriter();
			PrintWriter writer = new PrintWriter(strWriter);
			t.printStackTrace(writer);
			JOptionPane.showMessageDialog(
					comp, 
					strWriter.toString(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}