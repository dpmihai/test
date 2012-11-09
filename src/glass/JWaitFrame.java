package glass;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 7, 2005 Time: 12:48:51 PM
 */
public class JWaitFrame extends JFrame {

    private WaitGlassPane glass;
    private int defaultClose;
    private boolean resizable;

    public JWaitFrame() {
        this.glass = new WaitGlassPane();
        this.setGlassPane(glass);
    }

    public JWaitFrame(String title) {
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        glass.start();
    }

    public void activate() {
        glass.stop();
        setResizable(resizable);
        setDefaultCloseOperation(defaultClose);
    }
}
