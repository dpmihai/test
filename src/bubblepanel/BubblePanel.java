package bubblepanel;


/**
 *
 * @author hansolo
 */
public class BubblePanel extends javax.swing.JPanel implements java.awt.event.ActionListener
{
    private static final java.awt.GraphicsConfiguration GFX_CONF = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();    
    private static final java.util.Random RANDOM = new java.util.Random();    
    private static final int MAX_BUBBLE_RADIUS = 20;
    private static final int AMOUNT_OF_BUBBLES = 13;    
    private java.util.ArrayList<java.awt.image.BufferedImage> bubbleList = new java.util.ArrayList<java.awt.image.BufferedImage>(AMOUNT_OF_BUBBLES);
    private java.util.ArrayList<Integer> xSpeedList = new java.util.ArrayList<Integer>(AMOUNT_OF_BUBBLES);
    private java.util.ArrayList<Integer> ySpeedList = new java.util.ArrayList<Integer>(AMOUNT_OF_BUBBLES);
    private java.util.ArrayList<java.awt.Point> positionList = new java.util.ArrayList<java.awt.Point>(AMOUNT_OF_BUBBLES);    
    private java.util.ArrayList<Float> alphaList = new java.util.ArrayList<Float>(AMOUNT_OF_BUBBLES);       
    private volatile boolean validated;
    private boolean redrawBubbles = false;
    private int counter;
    private int x;
    private int y;
    private final javax.swing.Timer TIMER = new javax.swing.Timer(50, this);
    
    public BubblePanel()
    {
        validated = false;            
        generateAlphaList();
        generateBubbleList();
        generateXSpeedList();
        generateYSpeedList();
        generatePositionList();                     
                
        TIMER.start();        
    }
    
    public void start()
    {
        TIMER.start();
    }
    
    public void stop()
    {
        TIMER.stop();
    }
            
    @Override
    public void paintComponent(java.awt.Graphics g)
    {
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) g.create();        
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);        
        super.paintComponent(G2);        
        if (redrawBubbles)
        {
            drawBubbles(G2);
            redrawBubbles = false;
        }

        G2.dispose();
    }
    
    public boolean isValidated()
    {
        return this.validated;
    }

    public void setValidated(final boolean VALIDATED)
    {
        this.validated = VALIDATED;
    }    
        
    private void drawBubbles(final java.awt.Graphics2D G2)
    {        
        counter = 0;        
        for (java.awt.image.BufferedImage bubbleImage : bubbleList)
        {
            // Set the alpha value for the current BUBBLE image
            G2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alphaList.get(counter)));
            
            // Recalculate the new position of the BUBBLE with their specific speed
            x = positionList.get(counter).x;
            y = positionList.get(counter).y;
            x -= xSpeedList.get(counter);
            y -= ySpeedList.get(counter);            

            // Draw the BUBBLE without a blur effect
            G2.drawImage(bubbleImage, x, y, null);

            bubbleImage.flush();

            // Check if BUBBLE is outside the panel and create a new BUBBLE if needed
            if (x < -MAX_BUBBLE_RADIUS || x > (getWidth() + MAX_BUBBLE_RADIUS) || y < -MAX_BUBBLE_RADIUS || y > (getHeight() + MAX_BUBBLE_RADIUS))
            {                
                positionList.set(counter, new java.awt.Point(RANDOM.nextInt(getWidth()), getHeight()));
                final java.awt.image.BufferedImage IMAGE;
                // Draw smileys if the form was completed, else draw bubbles
                if (validated)
                {
                    IMAGE = createSmileyImage(5 + RANDOM.nextInt(MAX_BUBBLE_RADIUS));
                    bubbleList.set(counter, IMAGE);
                    IMAGE.flush();                                        
                }
                else
                {
                    IMAGE = createBubbleImage(5 + RANDOM.nextInt(MAX_BUBBLE_RADIUS));
                    bubbleList.set(counter, IMAGE);
                    IMAGE.flush();
                }
            }
            else
            {                
                positionList.set(counter, new java.awt.Point(x, y));
            }
            counter++;
        }
        
        // Reset the alpha value for additional drawings to 1.0f
        G2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));

        G2.dispose();
    }
        
    private void generateBubbleList()
    {                
        java.awt.image.BufferedImage image;
        for (int i = 0 ; i < AMOUNT_OF_BUBBLES ; i++)
        {            
            //final java.awt.image.BufferedImage BUBBLE_IMAGE = createBubbleImage((5 + RANDOM.nextInt(MAX_BUBBLE_RADIUS)));
            image = createBubbleImage(5 + RANDOM.nextInt(MAX_BUBBLE_RADIUS));
            bubbleList.add(image);
            image.flush();
            //BUBBLE_IMAGE.flush();                        
        }        
    }

    private void generateXSpeedList()
    {
        final int LOW = -1;
        final int HIGH = 1;
        for (int i = 0 ; i < AMOUNT_OF_BUBBLES ; i++)
        {            
            xSpeedList.add(RANDOM.nextInt( HIGH - LOW + 1 ) + LOW);
        }
    }

    private void generateYSpeedList()
    {
        for (int i = 0 ; i < AMOUNT_OF_BUBBLES ; i++)
        {
            ySpeedList.add((int) (1 + Math.random() * 2.5f));
        }    
    }

    private void generatePositionList()
    {
        for (int i = 0 ; i < AMOUNT_OF_BUBBLES ; i++)
        {
            positionList.add(new java.awt.Point(RANDOM.nextInt(350), RANDOM.nextInt(70)));
        }
    }

    private void generateAlphaList()
    {
        for (int i = 0 ; i < AMOUNT_OF_BUBBLES ; i++)
        {
            alphaList.add((float) (0.6f * Math.random()) + 0.2f);
        }
    }
  
    private java.awt.image.BufferedImage createBubbleImage(final int BUBBLE_SIZE)
    {        
        final java.awt.image.BufferedImage IMAGE = GFX_CONF.createCompatibleImage(BUBBLE_SIZE + 14, BUBBLE_SIZE + 14, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) IMAGE.getGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a GRADIENT that makes the sphere look like a BUBBLE
        final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double((BUBBLE_SIZE + 14) / 2.0, (BUBBLE_SIZE + 14) / 2.0);

        final float RADIUS = BUBBLE_SIZE / 2f;

        final float[] FRACTIONS =
        {
            0.0f,
            0.8f,
            1.0f
        };

        // Define the COLORS_BUBBLE for the BUBBLE
        // dependent on the variable validated
        final java.awt.Color[] COLORS_BUBBLE;
        final java.awt.Color[] COLORS_BUBBLE_HIGHLIGHT;
        // White BUBBLE
        COLORS_BUBBLE = new java.awt.Color[]
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.1f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.3f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.9f)
        };

        // White HIGHLIGHT
        COLORS_BUBBLE_HIGHLIGHT = new java.awt.Color[]
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.7f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        final java.awt.RadialGradientPaint GRADIENT = new java.awt.RadialGradientPaint(CENTER, RADIUS, FRACTIONS, COLORS_BUBBLE);

        final java.awt.geom.Ellipse2D BUBBLE = new java.awt.geom.Ellipse2D.Double(7, 7, BUBBLE_SIZE, BUBBLE_SIZE);

        G2.setPaint(GRADIENT);
        G2.fill(BUBBLE);

        // Create a HIGHLIGHT effect on top of BUBBLE
        final java.awt.Point START = new java.awt.Point(0, 8);
        final java.awt.Point STOP = new java.awt.Point(0, (int) (8 + RADIUS - 2));

        final java.awt.GradientPaint GRADIENT_HIGHLIGHT = new java.awt.GradientPaint(START, COLORS_BUBBLE_HIGHLIGHT[0], STOP, COLORS_BUBBLE_HIGHLIGHT[1]);
        final java.awt.geom.Ellipse2D HIGHLIGHT = new java.awt.geom.Ellipse2D.Double(9, 8, BUBBLE_SIZE - 4, (BUBBLE_SIZE / 2.0));
        G2.setPaint(GRADIENT_HIGHLIGHT);
        G2.fill(HIGHLIGHT);
        
        G2.dispose();

        return IMAGE;
    }
    
    private java.awt.image.BufferedImage createSmileyImage(final int SMILEY_SIZE)
    {        
        java.awt.image.BufferedImage smileyImage = GFX_CONF.createCompatibleImage(SMILEY_SIZE + 4, SMILEY_SIZE + 4, java.awt.Transparency.TRANSLUCENT);
        final java.awt.Graphics2D G2 = (java.awt.Graphics2D) smileyImage.getGraphics();
        G2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        G2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a GRADIENT that makes the sphere look like a BUBBLE
        final java.awt.geom.Point2D CENTER = new java.awt.geom.Point2D.Double((SMILEY_SIZE + 4) / 2.0, (SMILEY_SIZE + 4) / 2.0);

        final float RADIUS = SMILEY_SIZE / 2f;

        // Background fractions
        final float[] FRACTIONS_BACKGROUND1 =
        {
            0.0f,
            0.75f,
            0.85f,
            1.0f
        };

        final float[] FRACTIONS_BACKGROUND2 =
        {
            0.0f,
            1.0f
        };

        final float[] FRACTIONS_HIGHLIGHT =
        {
            0.0f,
            0.5f,
            0.85f,
            1.0f
        };

        // Define the COLORS_BUBBLE for the BUBBLE
        // dependent on the variable validated
        final java.awt.Color[] COLORS_BACKGROUND1;
        final java.awt.Color[] COLORS_BACKGROUND2;

        final java.awt.Color[] COLORS_HIGHLIGHT;
        // Yellow radial gradient
        COLORS_BACKGROUND1 = new java.awt.Color[]
        {
            new java.awt.Color(253, 227, 46, 255),
            new java.awt.Color(253, 227, 46, 255),
            new java.awt.Color(239, 205, 0, 255),
            new java.awt.Color(195, 158, 26, 255)
        };

        // Yellow linear gradient
        COLORS_BACKGROUND2 = new java.awt.Color[]
        {
            new java.awt.Color(255, 227, 46, 255),
            new java.awt.Color(255, 227, 46, 0)
        };

        // White HIGHLIGHT
        COLORS_HIGHLIGHT = new java.awt.Color[]
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 1.0f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.5f)
        };

        final java.awt.RadialGradientPaint GRADIENT_BACKGROUND1 = new java.awt.RadialGradientPaint(CENTER, RADIUS, FRACTIONS_BACKGROUND1, COLORS_BACKGROUND1);
        final java.awt.LinearGradientPaint GRADIENT_BACKGROUND2 = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(0,2), new java.awt.geom.Point2D.Float(0,SMILEY_SIZE), FRACTIONS_BACKGROUND2, COLORS_BACKGROUND2);

        // Draw Background
        final java.awt.geom.Ellipse2D SMILEY = new java.awt.geom.Ellipse2D.Double(2, 2, SMILEY_SIZE, SMILEY_SIZE);
        G2.setPaint(GRADIENT_BACKGROUND1);
        G2.fill(SMILEY);

        G2.setPaint(GRADIENT_BACKGROUND2);
        G2.fill(SMILEY);

        // Draw eyes
        G2.setColor(java.awt.Color.BLACK);
        // left
        G2.fillOval((int) (SMILEY_SIZE / 3.6184) + 3, (SMILEY_SIZE / 5) + 3, (int) (SMILEY_SIZE / 7.6389), (int) (SMILEY_SIZE / 4.2308));
        // right
        G2.fillOval((int) (SMILEY_SIZE / 1.6871) + 3, (SMILEY_SIZE / 5) + 3, (int) (SMILEY_SIZE / 7.6389), (int) (SMILEY_SIZE / 4.2308));

        // Draw mouth
        G2.setStroke(new java.awt.BasicStroke(SMILEY_SIZE / 22.92f));
        java.awt.geom.GeneralPath mouth = new java.awt.geom.GeneralPath();
        mouth.moveTo((int) (SMILEY_SIZE / 7.0513) + 3, (int) (SMILEY_SIZE / 1.76282) + 2);
        mouth.quadTo((int) (SMILEY_SIZE / 2.037) + 2, (int) (SMILEY_SIZE / 0.95) + 2, (int) (SMILEY_SIZE / 1.1752) + 2, (int) (SMILEY_SIZE / 1.76282) + 2);        
        G2.draw(mouth);

        // Create a HIGHLIGHT effect on top of SMILEY
        final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT = new java.awt.LinearGradientPaint(new java.awt.geom.Point2D.Float(0, 2), new java.awt.geom.Point2D.Float(0, SMILEY_SIZE - 2), FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
        final java.awt.geom.Ellipse2D HIGHLIGHT = new java.awt.geom.Ellipse2D.Double(3, 3, SMILEY_SIZE - 3, SMILEY_SIZE - 3);
        G2.setPaint(GRADIENT_HIGHLIGHT);
        G2.fill(HIGHLIGHT);

        G2.dispose();

        return smileyImage;
    }
    
    @Override
    public void actionPerformed(final java.awt.event.ActionEvent EVENT)
    {
        if (EVENT.getSource().equals(TIMER))
        {
            redrawBubbles = true;
            repaint(getVisibleRect());
        }
    }
    
    @Override
    public String toString()
    {
        return "BubblePanel";
    }
}
