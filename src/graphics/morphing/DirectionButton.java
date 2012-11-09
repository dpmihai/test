package graphics.morphing;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.triggers.MouseTrigger;
import org.jdesktop.animation.timing.triggers.MouseTriggerEvent;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.util.Map;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.GeneralPath;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 11:19:51 AM
 */
public class DirectionButton extends JButton {

    public enum Direction {
        LEFT,
        RIGHT,
        NONE
    }

    private DirectionButton.Direction direction;
    private Map desktopHints;
    private float morphing = 0.0f;

    public DirectionButton(String text, Direction direction) {
        super(text);
        this.direction = direction;

        if (!Direction.NONE.equals(direction)) {
            setupTriggers();
        }
        setFont(getFont().deriveFont(Font.BOLD));
        setOpaque(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    private void setupTriggers() {
        Animator animator = PropertySetter.createAnimator(
                150, this, "morphing", 0.0f, 1.0f);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.3f);
        MouseTrigger.addTrigger(this, animator, MouseTriggerEvent.ENTER, true);
    }

    private Morphing2D createMorph() {
        Shape sourceShape = new RoundRectangle2D.Double(2.0, 2.0,
                getWidth() - 4.0, getHeight() - 4.0, 12.0, 12.0);

        GeneralPath.Double destinationShape = new GeneralPath.Double();
        destinationShape.moveTo(2.0, getHeight() / 2.0);
        destinationShape.lineTo(22.0, 0.0);
        destinationShape.lineTo(22.0, 5.0);
        destinationShape.lineTo(getWidth() - 2.0, 5.0);
        destinationShape.lineTo(getWidth() - 2.0, getHeight() - 5.0);
        destinationShape.lineTo(22.0, getHeight() - 5.0);
        destinationShape.lineTo(22.0, getHeight());
        destinationShape.closePath();

        return new Morphing2D(sourceShape, destinationShape);
    }

    public float getMorphing() {
        return morphing;
    }

    public void setMorphing(float morphing) {
        this.morphing = morphing;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (!isEnabled()) {
            morphing = 0;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (desktopHints == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
        }

        if (desktopHints != null) {
            g2.addRenderingHints(desktopHints);
        }

        LinearGradientPaint p;
        Color[] colors;
        if (!getModel().isArmed()) {
            colors = new Color[]{
                    new Color(0x63a5f7),
                    new Color(0x3799f4),
                    new Color(0x2d7eeb),
                    new Color(0x30a5f9)};
        } else {
            colors = new Color[]{
                    new Color(0x63a5f7).darker(),
                    new Color(0x3799f4).darker(),
                    new Color(0x2d7eeb).darker(),
                    new Color(0x30a5f9).darker()};
        }

        p = new LinearGradientPaint(0.0f, 0.0f, 0.0f, getHeight(),
                new float[]{0.0f, 0.5f, 0.501f, 1.0f},
                colors);

        if (isEnabled()) {
            g2.setPaint(p);
        } else {
            g2.setColor(Color.GRAY);
        }

        Morphing2D morph = createMorph();
        morph.setMorphing(getMorphing());
        if (direction == Direction.RIGHT) {
            g2.translate(getWidth(), 0.0);
            g2.scale(-1.0, 1.0);
        }
        g2.fill(morph);
        if (direction == Direction.RIGHT) {
            g2.scale(-1.0, 1.0);
            g2.translate(-getWidth(), 0.0);
        }


        int width = g2.getFontMetrics().stringWidth(getText());

        int x = (getWidth() - width) / 2;
        int y = getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 1;

        g2.setColor(Color.BLACK);
        g2.drawString(getText(), x, y + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), x, y);
    }
}
