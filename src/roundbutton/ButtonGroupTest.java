package roundbutton;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

/** @see http://stackoverflow.com/questions/6035834 */
public class ButtonGroupTest extends JComponent {

    private static final String ON = "On";
    private static final String OFF = "Off";
    private final JToggleButton bOn = new JToggleButton(ON);
    private final JToggleButton bOff = new JToggleButton(OFF);
    private final JLabel label = new JLabel(" \u2301 ");
    private final ButtonHandler handler = new ButtonHandler();

    public ButtonGroupTest() {
        this.setLayout(new FlowLayout());
        label.setOpaque(true);
        label.setBackground(Color.red);
        label.setFont(label.getFont().deriveFont(36f));
        ButtonGroup bg = new ButtonGroup();
        this.add(bOn);
        bg.add(bOn);
        bOn.setSelected(true);
        bOn.addActionListener(handler);
        this.add(label);
        this.add(bOff);
        bg.add(bOff);
        bOff.addActionListener(handler);
    }

    public void addActionListener(ActionListener listener) {
        bOn.addActionListener(listener);
        bOff.addActionListener(listener);
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (ON.equals(cmd)) {
                label.setBackground(Color.red);
            } else {
                label.setBackground(Color.black);
            }
        }
    }

    private void display() {
        JFrame f = new JFrame("ButtonGroupTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ButtonGroupTest().display();
            }
        });
    }
}
