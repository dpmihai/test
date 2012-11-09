package shadow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2006
 * Time: 5:36:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextShadow {


    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        JPanel comp = new TextShadow().new TextPanel("LONG TEXT", true);
        comp.setSize(500, 500);
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }

    class TextPanel extends JPanel {

        private String text;
        private boolean shadow;

        public TextPanel(String text, boolean shadow) {
            super();
            this.text = text;
            this.shadow = shadow;
        }

        public void setText(String text) {
            this.text = text;
            repaint();
        }

        public void paint(Graphics g) {

            Graphics2D g2 = (Graphics2D)g;
            GeneralPath arrow = new GeneralPath();
            arrow.moveTo(0f, 10f);
            arrow.lineTo(10f, 10f);
            arrow.lineTo(10f, 0f);
            arrow.lineTo(20f, 15f);
            arrow.lineTo(10f, 30f);
            arrow.lineTo(10f, 20f);
            arrow.lineTo(0f, 20f);
            arrow.lineTo(0f, 10f);
            //g2.draw(arrow);

            AffineTransform defaultTransform = g2.getTransform();

            // We then use an AffineTransform in a new way: we call its createTransformedShape method to modify the
            // original arrow Shape and create new ones:

            // First create a version of the arrow that
            // points North, i.e. that is rotated by -PI/2 around its center.
            AffineTransform shadowRotate = AffineTransform.getRotateInstance(-Math.PI / 2, 20, -20);
            Shape rotatedArrow = shadowRotate.createTransformedShape(arrow);

            // Now, create a new arrow that is a scaled
            // down version of the one pointing north
            AffineTransform shadowScale = AffineTransform.getScaleInstance(1.0, .4);
            Shape scaledArrow = shadowScale.createTransformedShape(rotatedArrow);

            // Finally, the last shape is a skewed version
            // of the shrunk arrow pointing North.
            AffineTransform shadowShear = AffineTransform.getShearInstance(-1.25, 0.);
            Shape skewedArrow = shadowShear.createTransformedShape(scaledArrow);

            Rectangle shadowBounds = rotatedArrow.getBounds();

            // Reset to default transform.
            g2.setTransform(defaultTransform);

            // Combine initial translation so that
            // the arrow appears at the right place.
            g2.translate(20, 20 - shadowBounds.y);

            // Render first arrow.
            g2.setPaint(Color.BLACK);
            g2.fill(rotatedArrow);

            // Combine additional translation so that the
            // second arrow appears to the right of
            // the first one.
            g2.translate(shadowBounds.width + 40, 0);

            // Render second arrow
            g2.fill(scaledArrow);

            // Combine additional translation so that the
            // third shadow appears to the right of the
            // second one.
            shadowBounds = scaledArrow.getBounds();
            g2.translate(shadowBounds.width + 40, 0);

            // Render third arrow
            g2.fill(skewedArrow);

            // Combine additional translation so that the
            // last arrow appears to the right of the
            // previous one.
            shadowBounds = skewedArrow.getBounds();
            g2.translate(shadowBounds.width + 40, 0);

            // Render last arrow and its shadow.
            g2.setPaint(Color.LIGHT_GRAY);
            g2.fill(skewedArrow);
            g2.setPaint(Color.BLACK);
            g2.fill(rotatedArrow);

            // Revert to the original transform.
            g2.setTransform(defaultTransform);


            ////////////// FONT /////////////////
            String fontName = "Arial";
            int fontSize= 24;
            // A base font is used to create another one for the drop shadow
            Font textFont = new Font(fontName, Font.BOLD, fontSize);

            // The reflection creates the vertical flip effect.
            AffineTransform fontTransform = AffineTransform.getScaleInstance(1., -1);
            // The shear skews the font.
            //fontTransform.shear(-1, 0.);

            // deriveFont creates a new Font object.
            Font reflectionFont = textFont.deriveFont(fontTransform);

            // Set Font and Paint to use in the following drawString
            g2.setFont(textFont);
            g2.setPaint(Color.RED);
            g2.drawString(text, 20, 20 + shadowBounds.height + 20 + textFont.getSize());

            // Set a different Font and Paint and use drawString to create the text's drop shadow
            g2.setFont(reflectionFont);
            g2.setPaint(new GradientPaint(100,0, Color.GRAY, 100, 90, this.getBackground()));
            g2.drawString(text, 20, 20 + shadowBounds.height + 20 + reflectionFont.getSize());


        }

    }
}
