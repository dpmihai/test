package glass;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 26, 2005 Time: 4:21:51 PM
 */
public class JWaitDialog extends JDialog {

    private WaitGlassPane glass;
    private int defaultClose;
    private boolean resizable;

    public JWaitDialog() {
        this.glass = new WaitGlassPane();
        this.setGlassPane(glass);
    }

    public JWaitDialog(String title) {
        this.setTitle(title);
        this.glass = new WaitGlassPane();
        this.setGlassPane(glass);
    }

    public void setGlass(WaitGlassPane gp) {
        this.setGlassPane(gp);
    }

    public void deactivate(String text, boolean glowing) {
        if ((text == null) || text.equals("")) {
            text = " ";
        }
        resizable = isResizable();
        defaultClose = getDefaultCloseOperation();
        glass.setText(text);
        glass.setGlowing(glowing);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        glass.start();
    }

    public void activate() {
        glass.stop();
        setResizable(resizable);
        setDefaultCloseOperation(defaultClose);
    }
}
