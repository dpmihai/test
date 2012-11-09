package wait;

import java.awt.*;
import javax.swing.*;


public final class JWaitDialog extends JDialog {

    public static final Cursor WAIT=Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	public static final Cursor NORMAL=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private JProgressBar pbar;
	private JDialog owner;
	private JFrame frameOwner;
	private boolean ownerIsFrame=false;
    private boolean stop = false;

    private static String HEADER = "Wait";

    /** Creates a new JWaitDialog.
	* @param owner The frame or dialog that this wait dialog is for.
    * @param title title
	* @param msg The message to display on the wait dialog -
    * @param tasks number of tasks
	*/
	public JWaitDialog(JFrame owner, String title, String msg, int tasks){
		super(owner,HEADER,false);
        this.frameOwner=owner;
		ownerIsFrame=true;
        init(title, msg, tasks);        		
	}
    
    /** Creates a new JWaitDialog.
	* @param owner The frame or dialog that this wait dialog is for.
    * @param title title
	* @param msg The message to display on the wait dialog -
    * @param tasks number of tasks
	* this is a key in the language bundle and will be translated.*/
	public JWaitDialog(JDialog owner, String title, String msg, int tasks){
		super(owner,HEADER,false);
        this.owner=owner;
        init(title, msg, tasks);
	}

    private void init(String title, String msg, int tasks) {
        stop = false;
        JLabel titleLabel = new JLabel("<HTML><font size=4>" + title + "</font></HTML>");
        pbar = new JProgressBar(0, tasks);
        pbar.setValue(0);
        pbar.setStringPainted(true);
        pbar.setString(msg);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        panel.add(pbar, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 10, 10, 10), 0, 0));

        add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        this.setCursor(WAIT);
		pack();
    }

    /** Shows the wait dialog.
	* The owner will be disabled and have it's cursor set to
    * WAIT cursor.
    **/
	public void setVisible(boolean visible){		
		if(ownerIsFrame){
			if(frameOwner!=null){
				setLocationRelativeTo(frameOwner);
				frameOwner.getGlassPane().setVisible(true);
				frameOwner.setCursor(WAIT);
				frameOwner.setEnabled(false);
			}
			else{
				setLocationRelativeTo(null);
			}
		}
		else{
			if(owner!=null){
				setLocationRelativeTo(owner);
				owner.getGlassPane().setVisible(true);
				owner.setCursor(WAIT);
				owner.setEnabled(false);
			}
			else{
				setLocationRelativeTo(null);
			}
		}
		super.setVisible(visible);
	}

    /**Update progress bar
     *
     * @param message progress bar message
     */
    public void updateProgress(String message) {
        if ((message != null) && (!message.trim().equals(""))) {
            pbar.setString(message);
        }
        pbar.setValue(pbar.getValue()+1);
        if (pbar.getMaximum() == pbar.getValue()) {
            stop = true;
        } 
    }

    /**
     * Test to see if process was finished
     * @return true if process was finished
     */
    public boolean isStop() {
        return stop;
    }
 
    /** Dispose of the wait dialog.
	 * The owner of the wait dialog will be re-enabled and have it's cursor set to
     * NORMAL cursor.
     **/
	public void dispose(){
		if(ownerIsFrame){
			if(frameOwner!=null){
				frameOwner.setEnabled(true);
				frameOwner.setCursor(NORMAL);
				frameOwner.getGlassPane().setVisible(false);
			}
		}
		else{
			if(owner!=null){
				owner.setEnabled(true);
				owner.setCursor(NORMAL);
				owner.getGlassPane().setVisible(false);
			}
		}
		super.dispose();
	}

}