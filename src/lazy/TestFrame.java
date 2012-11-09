package lazy;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: May 4, 2005 Time: 10:07:58 AM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestFrame extends Frame {

    public TestFrame() {
        setLayout(new BorderLayout());
        add(nextButton, "South");

        framePanel.setLayout(layout);
        for (int i = 0; i < 30; ++i)
            framePanel.add(new BusyPanel(8, 8), "");
        add(framePanel, "Center");

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                layout.next(framePanel);
            }
        });

        setSize(400, 300);
    }

    private CardLayout layout = new CardLayout();
    private JButton nextButton = new JButton("Next Panel");
    private JPanel framePanel = new JPanel();

    static public void main(String args[]) {
        (new TestFrame()).show();
    }
}