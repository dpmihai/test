package flipchar;

/**
 *
 * @author hansolo
 */
public class BackgroundPanel extends javax.swing.JPanel {
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">
    private final java.awt.Rectangle INNER_BOUNDS = new java.awt.Rectangle(0, 0, 148, 100);
    private java.awt.image.BufferedImage backgroundImage;
    private transient final java.awt.event.ComponentListener COMPONENT_LISTENER = new java.awt.event.ComponentAdapter() {

        @Override
        public void componentResized(java.awt.event.ComponentEvent event) {
            final boolean SQUARE = getWidth() == getHeight() ? true : false;
            final int SIZE = getWidth() <= getHeight() ? getWidth() : getHeight();
            java.awt.Container parent = getParent();
            if ((parent != null) && (parent.getLayout() == null)) {
                if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height) {
                    setSize(getMinimumSize().width, getMinimumSize().height);
                } else {
                    if (SQUARE) {
                        setSize(SIZE, SIZE);
                    } else {
                        setSize(getWidth(), getHeight());
                    }
                }
            } else {
                if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height) {
                    setPreferredSize(getMinimumSize());
                } else {
                    if (SQUARE) {
                        setPreferredSize(new java.awt.Dimension(SIZE, SIZE));
                    } else {
                        setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
                    }
                }
            }
            calcInnerBounds();
            if (SQUARE) {
                init(INNER_BOUNDS.width, INNER_BOUNDS.width);
            } else {
                init(INNER_BOUNDS.width, INNER_BOUNDS.height);
            }

            FlipImages.INSTANCE.recreateImages(getWidth(), getHeight());

            revalidate();
            repaint(INNER_BOUNDS);
        }
    };
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public BackgroundPanel() {
        addComponentListener(COMPONENT_LISTENER);
        setOpaque(false);
        init(getMinimumSize().width, getMinimumSize().height);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return;
        }
        
        if (backgroundImage != null) {
            backgroundImage.flush();
        }
        backgroundImage = createBackgroundImage(WIDTH, HEIGHT);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Visualization">
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);        
        g.drawImage(backgroundImage, 0, 0, null);        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Size related">
    /**
     * Calculates the area that is available for painting the display
     */
    private void calcInnerBounds() {
        final java.awt.Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);
    }

    /**
     * Returns a rectangle2d representing the available space for drawing the
     * component taking the insets into account (e.g. given through borders etc.)
     * @return rectangle2d that represents the area available for rendering the component
     */
    private java.awt.Rectangle getInnerBounds() {
        return INNER_BOUNDS;
    }

    @Override
    public java.awt.Dimension getMinimumSize() {
        /* Return the default size of the component
         * which will be used by ui-editors for initialization
         */
        return new java.awt.Dimension(148, 100);
    }

    @Override
    public void setPreferredSize(final java.awt.Dimension DIM) {
        super.setPreferredSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);
        revalidate();
        repaint();
    }

    @Override
    public void setSize(final int WIDTH, final int HEIGHT) {
        super.setSize(WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);   // Rectangular component
        revalidate();
        repaint();
    }

    @Override
    public void setSize(final java.awt.Dimension DIM) {
        super.setSize(DIM);
        calcInnerBounds();
        init(DIM.width, DIM.height);
        revalidate();
        repaint();
    }

    @Override
    public void setBounds(final java.awt.Rectangle BOUNDS) {
        super.setBounds(BOUNDS);
        calcInnerBounds();
        init(BOUNDS.width, BOUNDS.height);
        revalidate();
        repaint();
    }

    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT) {
        super.setBounds(X, Y, WIDTH, HEIGHT);
        calcInnerBounds();
        init(WIDTH, HEIGHT);
        revalidate();
        repaint();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Image related">
    private java.awt.image.BufferedImage createBackgroundImage(final int WIDTH, final int HEIGHT) {
        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);

        final java.awt.Graphics2D G2 = IMAGE.createGraphics();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);


        java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Double(0, 0);
        java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, HEIGHT);

        final float[] BACKGROUND_FRACTIONS = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] BACKGROUND_COLORS = {
            new java.awt.Color(0x505652),
            new java.awt.Color(0x393E3A)
        };

        final java.awt.Shape BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(0, 0, WIDTH, HEIGHT, 5, 5);

        final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);

        G2.setPaint(BACKGROUND_GRADIENT);
        G2.fill(BACKGROUND);

        java.awt.geom.Point2D INNER_BACKGROUND_START = new java.awt.geom.Point2D.Double(0, 10);
        java.awt.geom.Point2D INNER_BACKGROUND_STOP = new java.awt.geom.Point2D.Double(0, HEIGHT - 10);

        final float[] INNER_BACKGROUND_FRACTIONS = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] INNER_BACKGROUND_COLORS = {
            new java.awt.Color(0x4D5651),
            new java.awt.Color(0x393E3A)
        };

        final java.awt.Shape INNER_BACKGROUND = new java.awt.geom.RoundRectangle2D.Double(10, 10, WIDTH - 20, HEIGHT - 20, 5, 5);

        final java.awt.LinearGradientPaint INNER_BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(INNER_BACKGROUND_START, INNER_BACKGROUND_STOP, INNER_BACKGROUND_FRACTIONS, INNER_BACKGROUND_COLORS);

        G2.setPaint(INNER_BACKGROUND_GRADIENT);
        G2.fill(INNER_BACKGROUND);

        // Draw vertical inset effect
        G2.setColor(new java.awt.Color(0x2F362E));
        G2.drawLine(10, 10, 10, HEIGHT - 10);
        G2.drawLine(WIDTH - 10, 10, WIDTH - 10, HEIGHT - 10);

        // Draw top inset effect
        G2.drawLine(10, 10, WIDTH - 10, 10);

        // Draw bottom inset effect
        G2.setColor(new java.awt.Color(0x6B7167));
        G2.drawLine(10, HEIGHT - 10, WIDTH - 10, HEIGHT - 10);

        G2.dispose();

        return IMAGE;
    }
    // </editor-fold>
}
