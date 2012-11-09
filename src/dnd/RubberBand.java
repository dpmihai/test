package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:55:46 PM
 */
import java.awt.Graphics;
import java.util.EventObject;


/**
 * Defines the required functionality for creating a rubber band
 *
 * @author rwickesser
 * $Revision: $
 */
public interface RubberBand {

    /**
     * Updates the parameters of the rubber band
     *
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param width     the width of the rubber band
     * @param height    the height of the rubber band
     */
    public void update(int x, int y, int width, int height);

    /**
     * Draws the rubber band on the given <code>Graphics</code> object
     *
     * @param g the <code>Graphics</code> object to draw the rubber band on
     */
    public void draw(Graphics g);

    /**
     * Sets the canvas which the rubber band will be drawn onto
     *
     * @param canvas    the canvas which the rubber band will be drawn onto
     */
    public void setCanvas(RubberBandCanvas canvas);

    /**
     * Enforces that the mouse listeners are added to the canvas
     */
    public void addMouseListeners();

    /**
     * Called when the rubber band is first created, typically on a mouse pressed
     * event
     *
     * @param event the event that started the rubber band
     */
    public void startRubberBand(EventObject event);

    /**
     * Called when the rubber band is done being created, typically on a mouse released
     * event
     *
     * @param event the event that stopped the rubber band
     */
    public void stopRubberBand(EventObject event);

    /**
     * Called when the rubber band is being updated, typically on a mouse dragged
     * event
     *
     * @param event the event that started the rubber band
     */
    public void updateRubberBand(EventObject event);
}
