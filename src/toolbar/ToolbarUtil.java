package toolbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 7, 2006
 * Time: 11:45:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class ToolbarUtil {

    public static Component createExtendedToolBar(JToolBar toolbar){
        JToolBar moreToolbar = new JToolBar();
        moreToolbar.setRollover(true);
        moreToolbar.setFloatable(false);
        moreToolbar.add(new ToolbarExpandButton(toolbar));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolbar, BorderLayout.CENTER);
        panel.add(moreToolbar, BorderLayout.EAST);
        return panel;
    }

    public static void setButtonsPreferredSize(JToolBar toolbar, Dimension dim) {
        int no = toolbar.getComponentCount() - 1;
        for (int i = 0; i <= no; i++) {
            Component c = toolbar.getComponent(i);
            if (c instanceof AbstractButton) {
                c.setPreferredSize(dim);
            }
        }
    }
}
