package swingx;

import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import util.ResizeUtil;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 16, 2008
 * Time: 5:10:17 PM
 */
public abstract class DisposablePanel extends JXPanel {

    private static String CANCEL = "Cancel";

    protected Window disposableWindow;

    protected JXPanel getButtonPanel(List<JButton> buttons) {
        JXPanel buttonPanel = new JXPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        for (int i=0, size=buttons.size(); i<size; i++) {
            JButton button = buttons.get(i);
            if (i > 0) {
               buttonPanel.add(Box.createRigidArea(new Dimension(5,5)));
            }
            buttonPanel.add(button);
        }
        if (disposableWindow != null) {
            JButton cancelButton = new JButton(CANCEL);
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    disposableWindow.dispose();
                }
            });
            buttonPanel.add(Box.createRigidArea(new Dimension(5,5)));
            buttonPanel.add(cancelButton);
        }
        ResizeUtil.equalizeButtonSizes(buttonPanel);
        return buttonPanel;
    }
}
