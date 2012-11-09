package wait;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 16, 2007
 * Time: 2:56:53 PM
 */
public class StopAction extends AbstractAction {

    private Thread t;

    public StopAction(Thread t) {
        putValue(Action.NAME, "Stop");
        putValue(Action.SHORT_DESCRIPTION, "Stop");
        putValue(Action.LONG_DESCRIPTION, "Stop current task");
        putValue(Action.MNEMONIC_KEY, new Integer('S'));
        putValue(Action.SMALL_ICON, new ImageIcon(StopAction.class.getResource("stop.gif")));
        this.t = t;
    }

    public void actionPerformed(ActionEvent e) {
        t.interrupt();
    }
}
