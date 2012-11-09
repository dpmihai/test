
package tabbedpanel;

import java.awt.event.MouseEvent;
import java.util.EventListener;


public interface CloseListener extends EventListener {
	public void closeOperation(MouseEvent e);
}
