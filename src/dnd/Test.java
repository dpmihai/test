package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:59:53 PM
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * A simple class with a main method to try out the rubber band's.  The idea is to make
 * the rubber bands flexible and easy to use.  Currently, in order for it to work you need
 * to do two things.
 *
 * 1) Create a JComponent which implements the RubberBandCanvas interface
 *
 * 2) Override the canvas' paintComponent(Graphics g) method so that it calls back
 *    to RubberBand.draw(Graphics g)
 *
 * @author rwickesser
 * $Revision: $
 */
public class Test {
    private static final Dimension SIZE = new Dimension(300, 300);

    public static void main(String[] args) {
        // Create RubberBandCanvas
        RubberBandCanvas canvas = new MyPanel();

        // Create rubber band
        RubberBand rb = new RectangularRubberBand(canvas);

        // Setup demo frameanimation
        JFrame f = new JFrame("Rubber Band Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(SIZE);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (scrSize.width / 2) - (SIZE.width / 2);
        int y = (scrSize.height / 2) - (SIZE.height / 2);
        f.setLocation(x, y);
        f.getContentPane().add(canvas.getCanvas());
        f.setVisible(true);
    }

    /**
     * A demonstration on how to create a {@link RubberBandCanvas}.  Note
     * that paintComponent(Graphics g) has been overridden.
     *
     * @author rwickesser
     * $Revision: $
     */
    private static class MyPanel extends JPanel implements RubberBandCanvas, ActionListener {
        private static final long serialVersionUID = 3256445806658466864L;

        private RubberBand rubberband;

        private JRadioButton radioRect;
        private JRadioButton radioOval;

        public MyPanel() {
            radioRect = new JRadioButton("Rectangle");
            radioRect.setSelected(true);
            radioRect.addActionListener(this);
            radioOval = new JRadioButton("Oval");
            radioOval.addActionListener(this);
            ButtonGroup bg = new ButtonGroup();
            bg.add(radioRect);
            bg.add(radioOval);

            add(radioRect);
            add(radioOval);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            rubberband.draw(g);
        }

        /* (non-Javadoc)
         * @see gui.rubberband.tmp.RubberBandCanvas#setRubberBand(gui.rubberband.tmp.RubberBand)
         */
        public void setRubberBand(RubberBand rubberband) {
            this.rubberband = rubberband;
        }

        /* (non-Javadoc)
         * @see gui.rubberband.tmp.RubberBandCanvas#getCanvas()
         */
        public JComponent getCanvas() {
            return this;
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            if (radioRect.isSelected()) {
                rubberband = new RectangularRubberBand(this);
            }
            else if (radioOval.isSelected()) {
                rubberband = new OvalRubberBand(this);
            }
        }
    }
}
