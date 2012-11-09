package tabbedpanel3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 7, 2008
 * Time: 12:02:58 PM
 */
public class CloseTabButton extends JPanel implements ActionListener {

    private JTabbedPane pane;

    public CloseTabButton(JTabbedPane pane, int index) {
        this.pane = pane;
        setOpaque(false);
        add(new JLabel(pane.getTitleAt(index), pane.getIconAt(index), JLabel.LEFT));
        Icon closeIcon = new CloseIcon();
        JButton btClose = new JButton(closeIcon);
        btClose.setPreferredSize(new Dimension(closeIcon.getIconWidth(), closeIcon.getIconHeight()));
        add(btClose);
        btClose.addActionListener(this);
        pane.setTabComponentAt(index, this);
    }

    public void actionPerformed(ActionEvent e) {
        int i = pane.indexOfTabComponent(this);
        if (i != -1) {
            pane.remove(i);
        }
    }
}
