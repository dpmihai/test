package dnd;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 21, 2005 Time: 3:57:17 PM
 */
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * An abstract implementation of {@link RubberBand} which handles
 * the basic drawing/setup of the rubber band.
 *
 * TODO: make more flexible, report rubber band selection to interested party
 *
 * @author rwickesser
 * $Revision: $
 */
public abstract class AbstractRubberBand extends MouseInputAdapter implements RubberBand {
    /** the canvas where the rubber band will be drawn onto */
    protected RubberBandCanvas canvas;

    /** maintains the size and location of the rubber band */
    protected Rectangle rubberband;

    /** stores the x coordinate of the mouse pressed event */
    private int pressX;
    /** stores the y coordinate of the mouse pressed event */
    private int pressY;


    /**
     * Creates a new <code>RubberBand</code> and sets the canvas
     *
     * @param canvas    the <code>RubberBandCanvas</code> on which the rubber band
     *                  will be drawn
     *
     * @see #setCanvas(RubberBandCanvas)
     */
    public AbstractRubberBand(RubberBandCanvas canvas) {
        this.canvas = canvas;
        init();
    }

    /**
     * Initializes the rubber band
     */
    private void init() {
        rubberband = new Rectangle();

        if (canvas != null) {
            canvas.setRubberBand(this);
            addMouseListeners();
        }
    }

    /* (non-Javadoc)
     * @see gui.rubberband.RubberBand#addMouseListeners()
     */
    public void addMouseListeners() {
        canvas.getCanvas().addMouseListener(this);
        canvas.getCanvas().addMouseMotionListener(this);
    }

    /* (non-Javadoc)
     * @see gui.rubberband.RubberBand#setCanvas(gui.rubberband.tmp.RubberBandCanvas)
     */
    public void setCanvas(RubberBandCanvas canvas) {
        this.canvas = canvas;
        this.canvas.setRubberBand(this);
        addMouseListeners();
    }

    /* (non-Javadoc)
     * @see javax.swing.event.MouseInputAdapter#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        updateRubberBand(e);

        int x = e.getX();
        int y = e.getY();
        int w = 0;
        int h = 0;

        // adjust x and width
        if (pressX < x) {
            int tmp = x;
            x = pressX;
            w = tmp - x;
        }
        else {
            w = pressX - x;
        }

        // adjust y and height
        if (pressY < y) {
            int tmp = y;
            y = pressY;
            h = tmp - y;
        }
        else {
            h = pressY - y;
        }

        // update rubber band size and location
        update(x, y, w, h);

        // repaint the canvas so the rubber band is updated visually
        updateCanvas();
    }

    /* (non-Javadoc)
     * @see javax.swing.event.MouseInputAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        startRubberBand(e);
        pressX = e.getX();
        pressY = e.getY();

        // update rubber band size and location
        update(pressX, pressY, 0, 0);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.MouseInputAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        stopRubberBand(e);

        // update rubber band size and location
        update(e.getX(), e.getY(), 0, 0);

        // repaint the canvas so the rubber band disappears
        // TODO: perhaps make this an option
        updateCanvas();
    }

    /**
     * Makes a call to the canvas' repaint method
     */
    private void updateCanvas() {
        canvas.getCanvas().repaint();
    }
}
