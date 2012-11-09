package lazy;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: May 4, 2005 Time: 10:08:56 AM
 */
public abstract class LazyPanel extends JPanel
{
    // We want to call the lazyConstructor only once.
    private boolean lazyConstructorCalled = false;

    // Some versions of Swing called paint() before
    // the components were added to their containers.


    // We don't want to call lazyConstructor until
    // the components are actually visible.
    private boolean isConstructorFinished = false;

    /**
     * Make a LazyPanel.
     */
    protected LazyPanel ()
    {
        isConstructorFinished = true;
    }

    public void paint (Graphics g)
    {
        callLazyConstructor();

        super.paint (g);
    }

    public void paintAll(Graphics g)
    {
        callLazyConstructor();

        super.paintAll (g);
    }

    public void paintComponents (Graphics g)
    {
        callLazyConstructor();

        super.paintComponents (g);
    }

    public void repaint ()
    {
        callLazyConstructor();

        super.repaint();
    }

    public void repaint (long l)
    {
        callLazyConstructor();

        super.repaint (l);
    }

    public void repaint (int i1, int i2, int i3, int i4)
    {
        callLazyConstructor();

        super.repaint (i1, i2, i3, i4);
    }

    public void repaint (long l, int i1, int i2, int i3, int i4)
    {
        callLazyConstructor();



        super.repaint (l, i1, i2, i3, i4);
    }

    public void update (Graphics g)
    {
        callLazyConstructor();

        super.update (g);
    }

    /**
     * Force the lazyConstructor() method implemented in the child class
     * to be called. If this method is called more than once on
     * a given object, all calls but the first do nothing.
     */
    public synchronized final void callLazyConstructor()
    {
        // The general idea below is as follows:
        //     1) See if this method has already been successfully called.
        //        If so, return without doing anything.
        //
        //     2) Otherwise ... call the lazy constructor.
        //     3) Call validate so that any components added are visible.
        //     4) Note that we have run.

        if ((lazyConstructorCalled == false) && (getParent() != null))
        {
            lazyConstructor();
            lazyConstructorCalled = true;
            validate();
        }
    }

    /**
     * This method must be implemented by any child class. Most of
     * the component creation code that would have gone in the constructor
     * of the child goes here instead. See the example
     * at the top.
     */
    abstract protected void lazyConstructor ();
}