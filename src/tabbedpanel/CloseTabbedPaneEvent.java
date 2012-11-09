
package tabbedpanel;

import java.awt.Event;
import java.awt.event.MouseEvent;

public class CloseTabbedPaneEvent extends Event {
	
	private String description;
	private MouseEvent e;
	private int overTabIndex;
	

	public CloseTabbedPaneEvent(MouseEvent e, String description, int overTabIndex){
		super(null, 0, null);
		this.e = e;
		this.description = description;
		this.overTabIndex = overTabIndex;
	}
	
	public String getDescription(){
		return description;
	}

	public MouseEvent getMouseEvent(){
		return e;
	}
	
	public int getOverTabIndex(){
		return overTabIndex;
	}
}
