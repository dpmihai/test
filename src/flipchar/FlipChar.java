package flipchar;


/**
 *
 * @author hansolo
 */
public class FlipChar extends javax.swing.JComponent implements java.awt.event.ActionListener {
    // <editor-fold defaultstate="collapsed" desc="Variable declarations">

    private final java.awt.Rectangle INNER_BOUNDS = new java.awt.Rectangle(0, 0, 45, 62);
    private String character = " ";    
    private static final String PROPERTY_CHARACTER = "character";
    private java.awt.image.BufferedImage backgroundImage = null;
    private java.awt.image.BufferedImage foregroundImage = null;
    private final java.awt.BasicStroke THIN_STROKE = new java.awt.BasicStroke(0.5f);
    private char currentChar = 32;
    private char lastChar = 32;    
    private java.awt.Font font;    
    private java.awt.image.BufferedImage flipSequenceImage = null;
    private volatile int currentFlipSequenceImage = 0;
    private boolean reachedChar = false;
    private boolean flipComplete = false;
    private java.awt.Color fontColor;
    private final java.awt.geom.Rectangle2D CLIP;
    private final java.awt.geom.Rectangle2D TOP_CLIP;
    private final java.awt.geom.Rectangle2D BOTTOM_CLIP;
    private final java.awt.geom.Point2D TOP_FONT_GRADIENT_START;
    private final java.awt.geom.Point2D TOP_FONT_GRADIENT_STOP;
    private final java.awt.geom.Point2D BOTTOM_FONT_GRADIENT_START;
    private final java.awt.geom.Point2D BOTTOM_FONT_GRADIENT_STOP;
    private final float[] TOP_FONT_GRADIENT_FRACTIONS = {
        0.0f,
        1.0f
    };
    private java.awt.Color[] topFontGradientColors = {
        new java.awt.Color(0xFFFEA7),
        new java.awt.Color(0xCCCB74)
    };    
    private final float[] BOTTOM_FONT_GRADIENT_FRACTIONS = {
        0.0f,
        1.0f
    };
    private java.awt.Color[] bottomFontGradientColors = {
        new java.awt.Color(0xCCCB74),
        new java.awt.Color(0xFFFEA7)
    };
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
            
            revalidate();
            repaint(INNER_BOUNDS);
        }
    };
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public FlipChar() {
        addComponentListener(COMPONENT_LISTENER);
        GlobalTimer.INSTANCE.addComponent(this);
        fontColor = new java.awt.Color(0xFFFEA7);
        CLIP = new java.awt.geom.Rectangle2D.Double();
        TOP_CLIP = new java.awt.geom.Rectangle2D.Double();        
        BOTTOM_CLIP = new java.awt.geom.Rectangle2D.Double();
        TOP_FONT_GRADIENT_START = new java.awt.geom.Point2D.Double();
        TOP_FONT_GRADIENT_STOP = new java.awt.geom.Point2D.Double();
        BOTTOM_FONT_GRADIENT_START = new java.awt.geom.Point2D.Double();
        BOTTOM_FONT_GRADIENT_STOP = new java.awt.geom.Point2D.Double();        
        
        init(getWidth(), getHeight());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Initialization">
    private void init(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 1 || HEIGHT <= 1) {
            return;
        }

        if (backgroundImage != null) {
            backgroundImage.flush();
        }
        backgroundImage = createBackgroundImage(WIDTH, HEIGHT);
        
        if (foregroundImage != null) {
            foregroundImage.flush();
        }
        foregroundImage = createForegroundImage(WIDTH, HEIGHT);
                
        font = FlipImages.INSTANCE.getFont().deriveFont((0.6451612903f * HEIGHT)).deriveFont(java.awt.Font.BOLD);
        
        FlipImages.INSTANCE.recreateImages(WIDTH, HEIGHT);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Visualization">
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw instrument background
        G2.drawImage(backgroundImage, 0, 0, this);
        
        // Get current clip
        CLIP.setRect(G2.getClipBounds());
        
        // Draw top character        
        G2.setClip(TOP_CLIP);
        final java.awt.LinearGradientPaint TOP_FONT_GRADIENT = new java.awt.LinearGradientPaint(TOP_FONT_GRADIENT_START, TOP_FONT_GRADIENT_STOP, TOP_FONT_GRADIENT_FRACTIONS, topFontGradientColors);
        G2.setPaint(TOP_FONT_GRADIENT);
        G2.setFont(font);
        java.awt.FontMetrics metrics = G2.getFontMetrics();
        java.awt.geom.Rectangle2D charBounds = metrics.getStringBounds(Character.toString(currentChar), G2);
        G2.translate(getWidth() * 0.1111111111111111, getHeight() * 0.078);
        G2.drawString(Character.toString(currentChar), (float) ((TOP_CLIP.getWidth() - charBounds.getWidth()) / 2f), (float) (TOP_CLIP.getHeight() + (metrics.getHeight() / 2f) - metrics.getDescent()));
        G2.translate(-getWidth() * 0.1111111111111111, -getHeight() * 0.078);
        
        // Draw bottom character             
        G2.setClip(BOTTOM_CLIP);
        final java.awt.LinearGradientPaint BOTTOM_FONT_GRADIENT = new java.awt.LinearGradientPaint(BOTTOM_FONT_GRADIENT_START, BOTTOM_FONT_GRADIENT_STOP, BOTTOM_FONT_GRADIENT_FRACTIONS, bottomFontGradientColors);
        G2.setPaint(BOTTOM_FONT_GRADIENT);
        G2.setFont(font);
        metrics = G2.getFontMetrics();
        if (!flipComplete) {
            charBounds = metrics.getStringBounds(Character.toString(lastChar), G2);
            G2.translate( (getWidth() * 0.1111111111111111), getHeight() * 0.49);
            G2.drawString(Character.toString(lastChar), (float) ((BOTTOM_CLIP.getWidth() - charBounds.getWidth()) / 2f), ((metrics.getHeight() / 2f) - metrics.getDescent()));
            G2.translate( (-getWidth() * 0.1111111111111111), -getHeight() * 0.49);
        } else {            
            charBounds = metrics.getStringBounds(Character.toString(currentChar), G2);
            G2.translate( (getWidth() * 0.1111111111111111), getHeight() * 0.49);
            G2.drawString(Character.toString(currentChar), (float) ((BOTTOM_CLIP.getWidth() - charBounds.getWidth()) / 2f), ((metrics.getHeight() / 2f) - metrics.getDescent()));
            G2.translate( (-getWidth() * 0.1111111111111111), -getHeight() * 0.49);
        }
        
        // Set clip back to original clip        
        G2.setClip(CLIP);
        
        G2.drawImage(foregroundImage, 0, 0, this);
        
        // Draw flip images
        if (flipSequenceImage != null) {
            G2.drawImage(flipSequenceImage, 0, 0, this);
        }
        
        G2.dispose();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getCharacter() {
        return character;
    }

    public void setCharacter(final String CHARACTER) {
        if (CHARACTER.isEmpty()) {
            return;
        }
                
        String oldCharacter = character;        
        character = CHARACTER;
                        
        firePropertyChange(PROPERTY_CHARACTER, oldCharacter, CHARACTER);
        if (currentChar != character.charAt(0)) {
            reachedChar = false;
            flipComplete = false;
        }
    }
    
    public java.awt.Color getFontColor() {
        return fontColor;
    }
    
    public void setFontColor(final java.awt.Color FONT_COLOR) {
        fontColor = FONT_COLOR;
        final float HUE = java.awt.Color.RGBtoHSB(FONT_COLOR.getRed(), FONT_COLOR.getGreen(), FONT_COLOR.getBlue(), null)[0];
        if (FONT_COLOR.getRed() != FONT_COLOR.getGreen() && FONT_COLOR.getRed() != FONT_COLOR.getBlue()) {
            topFontGradientColors = new java.awt.Color[]{
                java.awt.Color.getHSBColor(HUE, 0.35f, 1.0f),
                java.awt.Color.getHSBColor(HUE, 0.43f, 0.8f),
            };
            bottomFontGradientColors = new java.awt.Color[] {
                java.awt.Color.getHSBColor(HUE, 0.43f, 0.8f),
                java.awt.Color.getHSBColor(HUE, 0.35f, 1.0f),
            };
        } else {
            topFontGradientColors = new java.awt.Color[] {
                FONT_COLOR,
                FONT_COLOR
            };
            bottomFontGradientColors = new java.awt.Color[] {
                FONT_COLOR,
                FONT_COLOR,
            };
        }                        
        repaint(INNER_BOUNDS);
    }        
    // </editor-fold>
       
    // <editor-fold defaultstate="collapsed" desc="Image related">
    private java.awt.image.BufferedImage createBackgroundImage(final int WIDTH, final int HEIGHT) {
        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.OPAQUE);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        // Background
        final java.awt.geom.Point2D START_BACKGROUND = new java.awt.geom.Point2D.Float(0, 0);
        final java.awt.geom.Point2D STOP_BACKGROUND = new java.awt.geom.Point2D.Float(0, HEIGHT);

        final float[] FRACTIONS_BACKGROUND = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] COLORS_BACKGROUND = {
            new java.awt.Color(0x53524D),
            new java.awt.Color(0x3B4137)
        };

        final java.awt.LinearGradientPaint GRADIENT_BACKGROUND = new java.awt.LinearGradientPaint(START_BACKGROUND, STOP_BACKGROUND, FRACTIONS_BACKGROUND, COLORS_BACKGROUND);

        G2.setPaint(GRADIENT_BACKGROUND);
        G2.fill(new java.awt.geom.Rectangle2D.Float(0, 0, WIDTH, HEIGHT));

        // Highlight
        final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Double(0, 0.0322580645 * HEIGHT);
        final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Double(0, 0.9677419355 * HEIGHT);

        final float[] FRACTIONS_HIGHLIGHT = {
            0.0f,
            0.03f,
            0.97f,
            1.0f
        };

        final java.awt.Color[] COLORS_HIGHLIGHT = {
            new java.awt.Color(0x1C1910),
            new java.awt.Color(0x3E3B32),
            new java.awt.Color(0x3E3B32),
            new java.awt.Color(0x938B80),};

        final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT = new java.awt.LinearGradientPaint(START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);

        G2.setPaint(GRADIENT_HIGHLIGHT);
        G2.fill(new java.awt.geom.Rectangle2D.Double(0.0444444444 * WIDTH, 0.0322580645 * HEIGHT, 0.9111111111 * WIDTH, 0.935483871 * HEIGHT));

        // Inner Background
        final java.awt.geom.Point2D START_INNER_BACKGROUND = new java.awt.geom.Point2D.Double(0, 0.0483870968 * HEIGHT);
        final java.awt.geom.Point2D STOP_INNER_BACKGROUND = new java.awt.geom.Point2D.Double(0, 0.9516129032 * HEIGHT);

        final float[] FRACTIONS_INNER_BACKGROUND = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] COLORS_INNER_BACKGROUND = {
            new java.awt.Color(0x3E3B32),
            new java.awt.Color(0x232520)
        };

        final java.awt.LinearGradientPaint GRADIENT_INNER_BACKGROUND = new java.awt.LinearGradientPaint(START_INNER_BACKGROUND, STOP_INNER_BACKGROUND, FRACTIONS_INNER_BACKGROUND, COLORS_INNER_BACKGROUND);

        G2.setPaint(GRADIENT_INNER_BACKGROUND);
        G2.fill(new java.awt.geom.Rectangle2D.Double(0.0666666667 * WIDTH, 0.0483870968 * HEIGHT, 0.8666666667 * WIDTH, 0.9032258065 * HEIGHT));
        
        // Top
        G2.translate(getWidth() * 0.1111111111111111, getHeight() * 0.08064516129032258);        
        final java.awt.geom.GeneralPath TOP = new java.awt.geom.GeneralPath();
        TOP.moveTo(0, HEIGHT * 0.4032258065 * 0.12);
        TOP.quadTo(0, 0, HEIGHT * 0.4032258065 * 0.12, 0);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9142857142857143, 0);
        TOP.quadTo(WIDTH * 0.77777777777, 0, WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.12);
        TOP.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(0, HEIGHT * 0.4032258065 * 0.76);
        TOP.closePath();

        final java.awt.geom.Point2D TOP_START = new java.awt.geom.Point2D.Double(0, TOP.getBounds2D().getMinY());
        final java.awt.geom.Point2D TOP_STOP = new java.awt.geom.Point2D.Double(0, TOP.getBounds2D().getMaxY());

        final float[] TOP_FRACTIONS = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] TOP_COLORS = {
            new java.awt.Color(0x4F5054),
            new java.awt.Color(0x3E4043)
        };

        final java.awt.LinearGradientPaint TOP_GRADIENT = new java.awt.LinearGradientPaint(TOP_START, TOP_STOP, TOP_FRACTIONS, TOP_COLORS);
        
        G2.setPaint(TOP_GRADIENT);
        G2.fill(TOP);
        G2.translate(-getWidth() * 0.1111111111111111, -getHeight() * 0.08064516129032258);
        
        // Bottom
        G2.translate( (getWidth() * 0.1111111111111111), getHeight() * 0.5161290322580645);
        final java.awt.geom.GeneralPath BOTTOM = new java.awt.geom.GeneralPath();
        BOTTOM.moveTo(WIDTH * 0.77777777777 * 0.02857142857142857, 0);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, 0);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.88);
        BOTTOM.quadTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065, WIDTH * 0.77777777777 * 0.9142857142857143, HEIGHT * 0.4032258065);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.08571428571428572, HEIGHT * 0.4032258065);
        BOTTOM.quadTo(0, HEIGHT * 0.4032258065, 0, HEIGHT * 0.4032258065 * 0.88);
        BOTTOM.lineTo(0, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.closePath();

        final java.awt.geom.Point2D BOTTOM_START = new java.awt.geom.Point2D.Double(0, BOTTOM.getBounds2D().getMinY());
        final java.awt.geom.Point2D BOTTOM_STOP = new java.awt.geom.Point2D.Double(0, BOTTOM.getBounds2D().getMaxY());

        final float[] BOTTOM_FRACTIONS = {
            0.0f,
            1.0f
        };

        final java.awt.Color[] BOTTOM_COLORS = {
            new java.awt.Color(0x3B3C40),
            new java.awt.Color(0x4C4D51)
        };

        final java.awt.LinearGradientPaint BOTTOM_GRADIENT = new java.awt.LinearGradientPaint(BOTTOM_START, BOTTOM_STOP, BOTTOM_FRACTIONS, BOTTOM_COLORS);

        G2.setPaint(BOTTOM_GRADIENT);
        G2.fill(BOTTOM);
        G2.translate( (-getWidth() * 0.1111111111111111), -getHeight() * 0.5161290322580645);
        
        TOP_CLIP.setRect(WIDTH * 0.1111111111111111, HEIGHT * 0.08064516129032258, TOP.getBounds2D().getWidth(), TOP.getBounds2D().getHeight());
        BOTTOM_CLIP.setRect(WIDTH * 0.1111111111111111, HEIGHT * 0.5161290322580645, BOTTOM.getBounds2D().getWidth(), BOTTOM.getBounds2D().getHeight());
        
        TOP_FONT_GRADIENT_START.setLocation(0, TOP_CLIP.getHeight() * 0.4 + TOP_CLIP.getMinY());
        TOP_FONT_GRADIENT_STOP.setLocation(0, TOP_CLIP.getMaxY());
        
        BOTTOM_FONT_GRADIENT_START.setLocation(0, BOTTOM_CLIP.getMinY());
        BOTTOM_FONT_GRADIENT_STOP.setLocation(0, BOTTOM_CLIP.getHeight() * 0.6 + BOTTOM_CLIP.getMinY());
        
        G2.dispose();

        return IMAGE;
    }
    
    private java.awt.image.BufferedImage createForegroundImage(final int WIDTH, final int HEIGHT) {
        final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = IMAGE.createGraphics();

        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        
        // Draw flip axis
        G2.setStroke(THIN_STROKE);
        G2.setColor(new java.awt.Color(0x999999));
        G2.draw(new java.awt.geom.Line2D.Double(0.1333333333 * WIDTH, 0.4838709677 * HEIGHT, 0.84444444 * WIDTH, 0.4838709677 * HEIGHT));
        G2.setColor(java.awt.Color.BLACK);
        G2.draw(new java.awt.geom.Line2D.Double(0.1333333333 * WIDTH, 0.5 * HEIGHT, 0.84444444 * WIDTH, 0.5 * HEIGHT));

        // Draw side bars
        final java.awt.geom.Point2D SIDE_BAR_START = new java.awt.geom.Point2D.Double(0, 0.4193548387 * HEIGHT);
        final java.awt.geom.Point2D SIDE_BAR_STOP = new java.awt.geom.Point2D.Double(0, 0.6290322581 * HEIGHT);

        final float[] SIDE_BAR_FRACTIONS = {
            0.0f,
            0.05f,
            0.15f,
            0.85f,
            0.95f,
            1.0f
        };

        final java.awt.Color[] SIDE_BAR_COLORS = {
            new java.awt.Color(0x4A4945),
            new java.awt.Color(0x787876),
            new java.awt.Color(0x474C48),
            new java.awt.Color(0x534F44),
            new java.awt.Color(0x7B7168),
            new java.awt.Color(0x564C43)
        };

        final java.awt.LinearGradientPaint SIDE_BAR_GRADIENT = new java.awt.LinearGradientPaint(SIDE_BAR_START, SIDE_BAR_STOP, SIDE_BAR_FRACTIONS, SIDE_BAR_COLORS);

        G2.setPaint(SIDE_BAR_GRADIENT);
        G2.draw(new java.awt.geom.Line2D.Double(0.1111111111 * WIDTH, 0.3870967742 * HEIGHT, 0.1111111111 * WIDTH, 0.5967741935 * HEIGHT));
        G2.draw(new java.awt.geom.Line2D.Double(0.8666666667 * WIDTH, 0.3870967742 * HEIGHT, 0.8666666667 * WIDTH, 0.5967741935 * HEIGHT));
        
        G2.dispose();

        return IMAGE;
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
        return new java.awt.Dimension(45, 62);
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

    // <editor-fold defaultstate="collapsed" desc="ActionListener related">
    @Override
    public void actionPerformed(final java.awt.event.ActionEvent EVENT) {
        if (EVENT.getActionCommand().equals("flip") && !reachedChar) {
            lastChar = currentChar;
            currentChar++;            
            if (currentChar > 96) {
                currentChar = 32;
            }                        
            javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {

                @Override
                public void run() {
                    repaint(INNER_BOUNDS);
                }
            });
            flipComplete = false;

            if (currentChar == this.character.charAt(0)) {
                reachedChar = true;
            }
        }

        if (EVENT.getActionCommand().equals("flipSequence") && !flipComplete) {
            if (currentFlipSequenceImage == 9) {                            
                currentFlipSequenceImage = 0;
                flipSequenceImage = null;
                flipComplete = true;
                repaint(INNER_BOUNDS);
            } else {
                flipSequenceImage = FlipImages.INSTANCE.getFlipImageArray()[currentFlipSequenceImage];
                currentFlipSequenceImage++;
                repaint(INNER_BOUNDS);
            }
        }
    }
    // </editor-fold>
}
