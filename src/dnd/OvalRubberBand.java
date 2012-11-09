package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:59:19 PM
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * An Oval {@link RubberBand} which subclasses {@link RectangularRubberBand}
 * The only difference between the two rubber bands is the {@link RubberBand#draw(Graphics)} method
 *
 * @author rwickesser
 * $Revision: $
 */
public class OvalRubberBand extends RectangularRubberBand {

    public OvalRubberBand(RubberBandCanvas canvas) {
        super(canvas);
    }

    /* (non-Javadoc)
     * @see gui.rubberband.RubberBand#draw(java.awt.Graphics)
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(animStroke.getStroke());
        g2.drawOval(rubberband.x, rubberband.y, rubberband.width, rubberband.height);
        g2.setStroke(oldStroke);
    }
}
