package vista;

import wait.UIActivator;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 13, 2007
 * Time: 2:10:37 PM
 */

/**
 * VistaAction performs the job outside EDT and can have an UI activator for disabling the
 * VistaDialog
 */
public abstract class VistaAction extends AbstractAction {

    private UIActivator activator;
    private JDialog dialog;

    /**
     * Action performed method
     * It is done on a thread outside EDT
     * @param ev action event
     */
    public void actionPerformed(ActionEvent ev) {
        Runnable runnable = new Runnable() {
            public void run() {
                perform();
                if (activator != null) {
                    activator.stop();
                }
                if (dialog != null) {                    
                    dialog.dispose();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * Set UI activator
     * @param activator UI acivator
     */
    public void setActivator(UIActivator activator) {
        this.activator = activator;
    }

    /**
     * Set Vista dialog (it is needed for dispose)
     * @param dialog vista dialog
     */
    protected void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    /**
     * Perform task
     * executed outside EDT
     */
    public abstract void perform();

}
