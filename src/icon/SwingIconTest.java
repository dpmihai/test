package icon;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 19, 2006
 * Time: 9:50:58 AM
 */

import javax.swing.*;

public class SwingIconTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(layout);

        Icon baseIcon = new ImageIcon(SwingIconTest.class.getResource("2.gif"));

        Icon decoration1 = new ImageIcon(SwingIconTest.class.getResource("0.png"));
        Icon decoration2 = new ImageIcon(SwingIconTest.class.getResource("0.png"));

        Icon leftDecorIcon = new DecoratedIcon(baseIcon, decoration1);

        Icon rightDecorIcon = new DecoratedIcon(baseIcon, decoration2, DecoratedIcon.Location.LOWER_RIGHT);

        // decorate the already decorated icon.
        Icon bothIcon = new DecoratedIcon(leftDecorIcon, decoration2, DecoratedIcon.Location.LOWER_RIGHT);

        JLabel label1 = new JLabel();
        label1.setIcon(baseIcon);

        JLabel label2 = new JLabel();
        label2.setIcon(leftDecorIcon);

        JLabel label3 = new JLabel();
        label3.setIcon(rightDecorIcon);

        JLabel label4 = new JLabel();
        label4.setIcon(bothIcon);


        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
