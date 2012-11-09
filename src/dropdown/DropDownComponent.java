package dropdown;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

/**
 * User: mihai.panaitescu
 * Date: 07-Jan-2010
 * Time: 11:15:26
 */
public class DropDownComponent extends JComponent implements ActionListener, AncestorListener {

    protected JComponent drop_down_comp;
    protected JComponent visible_comp;
    protected JButton arrow;
    protected JWindow popup;

    public DropDownComponent(JComponent vcomp, JComponent ddcomp) {
        drop_down_comp = ddcomp;
        visible_comp = vcomp;

        arrow = new JButton(new MetalComboBoxIcon());
        Insets insets = arrow.getMargin();
        arrow.setMargin(new Insets(insets.top, 1, insets.bottom, 1));
        arrow.addActionListener(this);
        addAncestorListener(this);

        setupLayout();
    }

    protected void setupLayout() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(visible_comp, c);
        add(visible_comp);

        c.weightx = 0;
        c.gridx++;
        gbl.setConstraints(arrow, c);
        add(arrow);

    }

    public void actionPerformed(ActionEvent evt) {
        // build pop-up window
        popup = new JWindow(getFrame(null));
        popup.getContentPane().add(drop_down_comp);
        popup.addWindowFocusListener(new WindowAdapter() {

            public void windowLostFocus(WindowEvent evt) {
                popup.setVisible(false);
            }
        });
        popup.pack();

        // show the pop-up window
        Point pt = visible_comp.getLocationOnScreen();
        int pos = 0;
        if (visible_comp.getWidth() + arrow.getWidth() > popup.getWidth()) {
            pos = visible_comp.getWidth() + arrow.getWidth() - popup.getWidth();
        }
        pt.translate(pos, visible_comp.getHeight());
        popup.setLocation(pt);
        popup.toFront();
        popup.setVisible(true);
        popup.requestFocusInWindow();

    }

    protected Frame getFrame(Component comp) {
        if (comp == null) {
            comp = this;
        }
        if (comp.getParent() instanceof Frame) {
            return (Frame) comp.getParent();
        }
        return getFrame(comp.getParent());

    }

    public void ancestorAdded(AncestorEvent event) {
        hidePopup();
    }

    public void ancestorRemoved(AncestorEvent event) {
        hidePopup();
    }

    public void ancestorMoved(AncestorEvent event) {
        if (event.getSource() != popup) {
            hidePopup();
        }
    }

    public void hidePopup() {
        if (popup != null && popup.isVisible()) {
            popup.setVisible(false);
        }
    }


}

