package balloon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 3:07:41 PM
 * To change this template use File | Settings | File Templates.
 */
class CustomToolTip extends JToolTip {

    public CustomToolTip() {
        super();
        // make the tool tip not fill in its background
        this.setOpaque(false);
    }


    public void paintComponent(Graphics g) {

        // set the parent to not be opaque
        Component parent = this.getParent();
        if (parent != null) {
            if (parent instanceof JComponent) {
                JComponent jparent = (JComponent) parent;
                if (jparent.isOpaque()) {
                    jparent.setOpaque(false);
                }
            }
        }

        // create a round rectangle
        Shape round = new RoundRectangle2D.Float(4, 4,
                this.getWidth() - 1 - 8,
                this.getHeight() - 1 - 8,
                15, 15);

        // draw the white background
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);
        g2.fill(round);

        // draw the gray border
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(1));
        g2.draw(round);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);

        // draw the text
        String text = this.getComponent().getToolTipText();
        if (text != null) {
            FontMetrics fm = g2.getFontMetrics();
            int h = fm.getAscent();
            g2.setColor(Color.black);
            g2.drawString(text, 10, (this.getHeight() + h) / 2);
        }
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        return new Dimension((int) dim.getWidth() + 20,
                (int) dim.getHeight() + 20);
    }

    static class CustomJButton extends JButton {
        JToolTip _tooltip;

        public CustomJButton() {
            _tooltip = new CustomToolTip();
            _tooltip.setComponent(this);
        }

        public JToolTip createToolTip() {
            return _tooltip;
        }
    }

    public static void main(String[] args) {
        JButton button;

        JFrame frame = new JFrame("Tool Tips Hack");
        BoxLayout layout = new BoxLayout(
                frame.getContentPane(),
                BoxLayout.Y_AXIS);
        frame.getContentPane().setLayout(layout);

        button = new CustomJButton();
        button.setText("Open");
        button.setToolTipText("Open an existing file");
        frame.getContentPane().add(button);

        button = new CustomJButton();
        button.setText("Save");
        button.setToolTipText("Save the currently open file");
        frame.getContentPane().add(button);

        frame.getContentPane().add(new JLabel("a label"));
        frame.getContentPane().add(new JLabel("a label"));
        frame.getContentPane().add(new JLabel("a label"));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}


