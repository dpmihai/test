package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:58:46 PM
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.EventObject;

/**
 * A {@link RubberBand} which is rectangular
 *
 * @author rwickesser
 * $Revision: $
 */
public class RectangularRubberBand extends AbstractRubberBand {

    protected AnimatedStroke animStroke;

    public RectangularRubberBand(RubberBandCanvas canvas) {
        super(canvas);
        animStroke = new AnimatedStroke(canvas);
    }

    /* (non-Javadoc)
     * @see gui.rubberband.RubberBand#update(int, int, int, int)
     */
    public void update(int x, int y, int width, int height) {
        rubberband.setBounds(x, y, width, height);
    }

    /* (non-Javadoc)
     * @see gui.rubberband.RubberBand#draw(java.awt.Graphics)
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(animStroke.getStroke());
        g2.draw(rubberband);
        g2.setStroke(oldStroke);

        // TODO: use this call if you dont want an animated stroke
        //g.drawRect(rubberband.x, rubberband.y, rubberband.width, rubberband.height);
    }

    /* (non-Javadoc)
     * @see gui.rubberband.tmp.RubberBand#startRubberBand(java.util.EventObject)
     */
    public void startRubberBand(EventObject event) {
        animStroke.startAnimation();
    }

    /* (non-Javadoc)
     * @see gui.rubberband.tmp.RubberBand#stopRubberBand(java.util.EventObject)
     */
    public void stopRubberBand(EventObject event) {
        animStroke.stopAnimation();
    }

    /* (non-Javadoc)
     * @see gui.rubberband.tmp.RubberBand#updateRubberBand(java.util.EventObject)
     */
    public void updateRubberBand(EventObject event) {
        // don't need to do anything specific here for this rubber band
    }
}

