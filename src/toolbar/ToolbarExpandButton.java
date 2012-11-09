package toolbar;

import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 6, 2006
 * Time: 5:45:05 PM
 * To change this template use File | Settings | File Templates.
 */
class ToolbarExpandButton extends JToggleButton implements ActionListener {
    private JToolBar toolbar;

    public ToolbarExpandButton(final JToolBar toolbar) {
        //super(new ImageIcon(MoreButton.class.getResource("more.gif")));
        super(">>");
        this.toolbar = toolbar;
        setToolTipText("Expand");
        addActionListener(this);
        setFocusPainted(false);

        // expand button has the height of the nearest component
        setHeight((int)toolbar.getComponent(toolbar.getComponentCount() - 1).getPreferredSize().getHeight());

        // hide/see buttons and expand button
        toolbar.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                boolean visibleButton = true;
                int no = toolbar.getComponentCount() - 1;
                for (int i = 0; i <= no; i++) {
                    Component c = toolbar.getComponent(i);
                    boolean visibleComp = isVisible(c, null);
                    visibleButton = visibleButton && visibleComp;
                    c.setVisible(visibleComp);
                }
                setVisible(!visibleButton);
            }
        });
    }

    // check visibility
    private boolean isVisible(Component comp, Rectangle rect) {
        if (rect == null) {
            rect = toolbar.getVisibleRect();
        }
        return comp.getLocation().x + comp.getPreferredSize().getWidth() <= rect.getWidth();
    }

    public void actionPerformed(ActionEvent e) {
        Component[] comp = toolbar.getComponents();
        Rectangle visibleRect = toolbar.getVisibleRect();
        for (int i = 0; i < comp.length; i++) {
            if (!isVisible(comp[i], visibleRect)) {
                JPopupMenu popup = new JPopupMenu();
                for (; i < comp.length; i++) {
                    if (comp[i] instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) comp[i];
                        if (button.getAction() != null)
                            popup.add(button.getAction());
                    } else if (comp[i] instanceof JSeparator)
                        popup.addSeparator();
                }

                //on popup close make more-button unselected
                popup.addPopupMenuListener(new PopupMenuListener() {
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        setSelected(false);
                    }

                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }

                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }
                });
                popup.show(this, 0, getHeight());
            }
        }
    }

    private void setHeight(int height) {
        int width = (int)this.getPreferredSize().getWidth();
        setPreferredSize(new Dimension(width, height));
    }

}