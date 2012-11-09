package toolbar;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 12:23:11 PM
 * To change this template use File | Settings | File Templates.
 */
/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
/*
 * PlainAquaToolbarUI.java
 *
 * Created on January 17, 2004, 3:00 AM
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/**
 * A ToolbarUI subclass that gets rid of all borders on buttons and provides a finder-style toolbar look.
 *
 * @author Tim Boudreau
 */
public class AquaToolBarUI extends BasicToolBarUI implements ContainerListener, Border {

    private static final Color UPPER_GRADIENT_TOP = new Color ( 255, 255, 255 );
    private static final Color UPPER_GRADIENT_BOTTOM = new Color ( 228, 220, 222 );

    private static final Color LOWER_GRADIENT_TOP = new Color ( 218, 217, 205 );
    private static final Color LOWER_GRADIENT_BOTTOM = new Color ( 249, 249, 249 );


    /**
     * Creates a new instance of PlainAquaToolbarUI
     */
    private AquaToolBarUI () {
    }

    public static ComponentUI createUI (JComponent c) {
        return new AquaToolBarUI ();
    }

    public void installUI (JComponent c) {
        super.installUI ( c );
        //Editor will try install a custom border - just use ours
//        c.setBorder ( aquaborder );
        c.setBorder (this);
        c.setOpaque ( true );
        c.addContainerListener ( this );

        installButtonUIs ( c );
    }

    public void uninstallUI (JComponent c) {
        super.uninstallUI ( c );
        c.setBorder ( null );
        c.removeContainerListener ( this );
    }

    public boolean isBorderOpaque () {
        return false;
    }

    public Insets getBorderInsets (Component jc) {
        return new Insets (0, 6, 0, 6);
    }

    public void paintBorder (Component jc, Graphics g, int x, int y, int w, int h) {
        //do nothing
    }

    /**
     * Cache for images - normally only ever two - one for editor toolbar height and one for main window toolbar height
     */
    private static Map icache = new HashMap ();

    private BufferedImage getCacheImage (JComponent c) {
        BufferedImage img = (BufferedImage) icache.get ( new Integer ( c.getHeight () ) );
        //Don't make a cache image for very small sizes - we're probably just
        //initializing, and if not, painting will be cheap enough.  Also
        //ensure that the mid area width is at least a reasonable width for
        //multiple blits on wide toolbars
        if ( img == null && c.getWidth () > ( arcsize * 2 ) + 24 && c.getHeight () > 12 ) {
            img = new BufferedImage ( c.getWidth (), c.getHeight (),
                    BufferedImage.TYPE_INT_ARGB_PRE ); //INT_ARGB_PRE is native raster format on mac os
            Graphics g = img.getGraphics();

            paintInto ( img.getGraphics (), c );
            g.dispose();
            icache.put ( new Integer ( c.getHeight () ), img );
        }
        return img;
    }

    public void update (Graphics g, JComponent c) {
        paint ( g, c );
    }

    public void paint (Graphics g, JComponent c) {
        if ( !c.isOpaque () || c.getWidth () < 4 || c.getHeight () < 4 ) {
            return;
        }
//        BufferedImage img = getCacheImage ( c );
//        if ( img == null ) {
            paintInto ( g, c );
//        } else {
            //Use a cached image for painting - the geometry ops and
            //plethora of GradientPaints in aqua toolbars are too expensive
            //for use on every paint - slows down everything and allocates
            //an 6 width * height integer rasters for per toolbar per paint
//            paintImage ( (Graphics2D) g, img, c );
//        }
    }

    /**
     * Paint a cached image of the toolbar.  Paints the left edge and content, if necessary, repeats the interior
     * section until the width less-the-edges is full, then paints the end cap.
     */
    private static void paintImage (Graphics2D g2d, BufferedImage img, JComponent c) {
        int w = c.getWidth ();
        int h = c.getHeight ();
        int imgw = img.getWidth ();
        AffineTransform nullTransform = AffineTransform.getTranslateInstance ( 0, 0 );
        if ( w == imgw ) {
            //Perfect fit, we're done
            g2d.drawRenderedImage ( img, nullTransform );
        } else if ( w > imgw ) {
            //Wider than the image - loop and blit the interior, then draw the
            //end cap
            g2d.drawRenderedImage ( img, nullTransform );
            int uncovered = w - ( imgw - ( arcsize * 2 ) );
            int x = imgw - arcsize;
            //Get the mid section of the cached image, less the end caps
            do {
                BufferedImage mid = img.getSubimage ( arcsize, 0, Math.min ( uncovered, imgw - ( arcsize * 2 ) ), h );
                //blit it until we've filled all the space we need to
                g2d.drawRenderedImage ( mid, AffineTransform.getTranslateInstance ( x, 0 ) );

                x += mid.getWidth ();
                uncovered -= mid.getWidth ();
            } while ( x < w - arcsize );
            BufferedImage rightEdge = img.getSubimage ( imgw - arcsize, 0, arcsize, h );
            g2d.drawRenderedImage ( rightEdge, AffineTransform.getTranslateInstance ( w - arcsize, 0 ) );
        } else if ( w < imgw ) {
            //Narrower than the image - draw the width we need, then the end cap
            BufferedImage left = img.getSubimage ( 0, 0, imgw - arcsize, h );
            //Don't blit the full image, or we will get white "ears" on the right side
            g2d.drawRenderedImage ( left, nullTransform );
            BufferedImage right = img.getSubimage ( imgw - arcsize, 0, arcsize, h );
            g2d.drawRenderedImage ( right, AffineTransform.getTranslateInstance ( w - arcsize, 0 ) );
        }
    }

    private static void paintInto (Graphics g, JComponent c) {
        configureRenderingHints ( g );
        Color temp = g.getColor ();
        Dimension size = c.getSize ();
        Shape clip = g.getClip ();
        Graphics2D g2d = (Graphics2D) g;
        RoundRectangle2D.Double s = getInteriorShape (size.width, size.height);
        //XXX needed for some reason only when painting into an image.  Apple bug?
//        s.y += 1;
//        s.height -= 1;

        if ( clip != null ) {
            Area a = new Area ( clip );
            a.intersect ( new Area ( s ) );
            g.setClip ( a );
        } else {
            g.setClip ( s );
        }

        g2d.setPaint (getUpperPaint (size.width, size.height));
        g2d.fill (getUpperBevelShape ( size.width, size.height));
        g2d.setPaint ( getLowerPaint ( size.width, size.height ) );
        g2d.fill ( getLowerBevelShape ( size.width, size.height ) );

        if (isFinderLook()) {
            g2d.setClip (clip);
            g2d.setColor (new Color (170, 170, 170));
            g2d.draw (s);

            Rectangle r = new Rectangle (0, 0, size.width, size.height / 2);
            g.setClip (r);

            g2d.translate (0, 1);
            g2d.setColor (new Color (140, 140, 140));
            g2d.draw (s);
        } else {
        s.y += 1;
            g2d.setClip (clip);

            Rectangle r = new Rectangle (0, size.height / 2, size.width, size.height / 2);
            if (clip != null) {
                Area a = new Area (clip);
                a.intersect (new Area (r));
                g.setClip (a);
            } else {
                g.setClip (r);
            }

            g2d.setColor (new Color (220, 220, 220));
            g2d.draw (s);

            g2d.translate (0, -1);
            g2d.setColor (new Color (190, 190, 190));
            g2d.draw (s);

            g2d.setClip (clip);

            g2d.translate (0, -1);
            g2d.setColor (new Color (120, 120, 120));
            g2d.draw (s);
        }

        g.setClip ( clip );
        g.setColor ( temp );
    }


    protected Border createRolloverBorder () {
        return BorderFactory.createEmptyBorder ( 2, 2, 2, 2 );
    }

    protected Border createNonRolloverBorder () {
        return createRolloverBorder ();
    }

    protected void setBorderToRollover (Component c) {
        if ( c instanceof AbstractButton ) {
            ( (AbstractButton) c ).setBorderPainted ( false );
            ( (AbstractButton) c ).setBorder ( BorderFactory.createEmptyBorder () );
//            ((AbstractButton) c).setContentAreaFilled(false);
            ( (AbstractButton) c ).setOpaque ( false );
        }
        if ( c instanceof JComponent ) {
            ( (JComponent) c ).setOpaque ( false );
        }
    }

    protected void setBorderToNormal (Component c) {
        if ( c instanceof AbstractButton ) {
            ( (AbstractButton) c ).setBorderPainted ( false );
//            ((AbstractButton) c).setContentAreaFilled(false);
            ( (AbstractButton) c ).setOpaque ( false );
        }
        if ( c instanceof JComponent ) {
            ( (JComponent) c ).setOpaque ( false );
        }
    }

    public void setFloating (boolean b, Point p) {
        //nobody wants this
    }

    private void installButtonUI (Component c) {
        if ( c instanceof AbstractButton ) {
            ( (AbstractButton) c ).setUI ( buttonui );
        }
        if ( c instanceof JComponent ) {
            ( (JComponent) c ).setOpaque ( false );
        }
    }

    private void installButtonUIs (Container parent) {
        Component[] c = parent.getComponents ();
        for ( int i = 0; i < c.length; i++ ) {
            installButtonUI ( c[ i ] );
        }
    }

    private static final ButtonUI buttonui = new AquaToolBarButtonUI ();

    public void componentAdded (ContainerEvent e) {
        installButtonUI ( e.getChild () );
        Container c = (Container) e.getSource ();
        if ( "editorToolbar".equals ( c.getName () ) ) {
            //It's an editor toolbar.  Aqua's combo box ui paints outside
            //of its literal component bounds, and doesn't honor opacity.
            //Need to ensure the toolbar is tall enough that its border is
            //not hidden.
            Dimension min = new Dimension ( 32, 34 );
            ( (JComponent) e.getContainer () ).setPreferredSize ( min );
        }
    }

    public void componentRemoved (ContainerEvent e) {
        //do nothing
    }

    private static final boolean isFinderLook () {
        return Boolean.getBoolean ( "apple.awt.brushMetalLook" );
//        return false;
    }

    private static int arcsize = 13;

        static Paint getUpperPaint (Color top, Color bottom, int w, int h) {
            GradientPaint result =
                    getGradientPaint ( 0, h / 4, top, 0, ( h / 2 ) + ( h / 4 ),
                            bottom, false );
//                    getGradientPaint ( 0, h / 4, Color.BLUE, 0, ( h / 2 ) + ( h / 4 ),
//                            Color.YELLOW, false );
            return result;
        }

        static Paint getLowerPaint (Color top, Color bottom, int w, int h) {
            GradientPaint result =
                    getGradientPaint ( 0, h / 2, top, 0, ( h / 2 ) + ( h / 4 ),
                            bottom, false );

            return result;
        }

        static Paint getUpperPaint (int w, int h) {
            return getUpperPaint ( UPPER_GRADIENT_TOP, UPPER_GRADIENT_BOTTOM, w, h );
        }

        static Paint getLowerPaint (int w, int h) {
            return getLowerPaint ( LOWER_GRADIENT_TOP,
                    LOWER_GRADIENT_BOTTOM, w, h );
        }

        static RoundRectangle2D.Double getInteriorShape (int w, int h) {
//            RoundRectangle2D r2d = new RoundRectangle2D.Double ( 0, isFinderLook() ? 1 : 0, w - 2, isFinderLook() ? h - 1 : h, arcsize, arcsize );
            RoundRectangle2D.Double r2d = new RoundRectangle2D.Double ( 0, 0, w - 2, h - 1, arcsize, arcsize );
            return r2d;
        }

        static Shape getUpperBevelShape (int w, int h) {
            int[] xpoints = new int[]{
                0,
                0,
                h / 2,
                w - ( h / 4 ),
                w,
                w,
                0
            };

            int[] ypoints = new int[]{
                0,
                h - ( h / 4 ),
                h / 2,
                h / 2,
                h / 4,
                0,
                0
            };
            Polygon p = new Polygon ( xpoints, ypoints, ypoints.length );
            return p;
        }

        static Shape getLowerBevelShape (int w, int h) {
            int[] xpoints = new int[]{
                0,
                0,
                h / 4,
                w - ( h / 4 ),
                w,
                w,
                0
            };

            int[] ypoints = new int[]{
                h,
                h - ( h / 4 ),
                h / 2,
                h / 2,
                h / 4,
                h,
                h

            };
            Polygon p = new Polygon ( xpoints, ypoints, ypoints.length );
            return p;
        }

    private static Color mezi (Color c1, Color c2) {
        return new Color ( ( c1.getRed () + c2.getRed () ) / 2,
                ( c1.getGreen () + c2.getGreen () ) / 2,
                ( c1.getBlue () + c2.getBlue () ) / 2 );
    }

    private static HashMap hintsMap = null;

    private static Map getHints () {
        //XXX should do this in update() in the UI instead
        if ( hintsMap == null ) {
            hintsMap = new HashMap ();
            hintsMap.put ( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            hintsMap.put ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        }
        return hintsMap;
    }

    private static void configureRenderingHints (Graphics g) {
        ( (Graphics2D) g ).addRenderingHints ( getHints () );
    }

    //XXX move/duplicate org.netbeans.swing.tabcontrol.plaf.ColorUtil gradient paint caching?
    private static GradientPaint getGradientPaint (float x1, float y1, Color upper, float x2, float y2, Color lower,
                                                  boolean repeats) {
        return new GradientPaint ( x1, y1, upper, x2, y2, lower, repeats );
    }
}
