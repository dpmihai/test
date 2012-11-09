package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:56:21 PM
 */
import javax.swing.JComponent;

/**
 * Defines a canvas
 *
 * @author rwickesser
 * $Revision: $
 */
public interface RubberBandCanvas {
    /**
     * Sets the <code>RubberBand</code> for the canvas
     *
     * @param rubberband    the <code>RubberBand</code> for the canvas
     */
    public void setRubberBand(RubberBand rubberband);

    /**
     * Returns the <code>JComponent</code> which is the canvas for the
     * <code>RubberBand</code>
     *
     * @return  the <code>JComponent</code> which is the canvas for the
     *          <code>RubberBand</code>
     */
    public JComponent getCanvas();
}
