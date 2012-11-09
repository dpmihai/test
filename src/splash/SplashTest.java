package splash;



import java.util.logging.Logger;
import java.util.logging.Level;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 27, 2007
 * Time: 11:01:31 AM
 */
public class SplashTest {


    // Jdk 1.6 SplashScreen usage
    // Drawing a progress bar on the splash screen
    // -splash:splash.png (VM parameter)
        
    private static Logger logger = Logger.getLogger(SplashTest.class.getSimpleName());
    private final SplashScreen splash = SplashScreen.getSplashScreen();
    private Rectangle splashBounds;
    private Graphics2D splashGraphics;
    private int barHeight = 10;
    private int barWidth;
    private final static Color COLOR = new Color(127, 148, 190);

    private static final byte TOP_LEFT = 1;
    private static final byte TOP_CENTER = 2;
    private static final byte TOP_RIGHT = 3;
    private static final byte MIDDLE_LEFT = 4;
    private static final byte MIDDLE_CENTER = 5;
    private static final byte MIDDLE_RIGHT = 6;
    private static final byte BOTTOM_LEFT = 7;
    private static final byte BOTTOM_CENTER = 8;
    private static final byte BOTTOM_RIGHT = 9;

    private int scrollPosition;


    public SplashTest(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    protected void initSplash() throws Exception {
        if (splash == null) {
            throw new Exception("no splash image specified eg. -splash:mysplash.png");
        }
        splashBounds = splash.getBounds();
        splashGraphics = splash.createGraphics();
        if (splashGraphics == null) throw new Exception("no SplashScreen Graphics2D");
        splashGraphics.setColor(COLOR);
        splashGraphics.drawRect(0, 0, splashBounds.width - 1, splashBounds.height - 1);
    }

    protected void updateSplash(String status, int progress) {
        if (splash == null) return;
        if (splashGraphics == null) return;
        drawSplash(splashGraphics, status, progress);
        splash.update();
    }

    protected void drawSplash(Graphics2D splashGraphics, String status, int progress) {
        barWidth = splashBounds.width*50/100;

        int x = getX(scrollPosition);
        int y = getY(scrollPosition);

        // clear drawed string on top of the scroll bar
        splashGraphics.setComposite(AlphaComposite.Clear);
        splashGraphics.fillRect(x-barHeight+1, y-10, splashBounds.width - 2, 20);

        // draw new string
        splashGraphics.setPaintMode();
        splashGraphics.setColor(Color.BLACK);
        splashGraphics.drawString(status, x, y);

        // draw scroll bar rectangle
        splashGraphics.setColor(Color.BLACK);
        splashGraphics.drawRect(x, y+5, barWidth + 2, barHeight);

        // fill scroll bar rectangle with the 2 colors
        splashGraphics.setColor(COLOR);
        int width = progress*barWidth/100;
        splashGraphics.fillRect(x+1, y+6, width + 1, barHeight-1);
        splashGraphics.setColor(Color.WHITE);
        splashGraphics.fillRect(x+1 + width + 1, y+6, barWidth - width, barHeight-1);
    }

    protected void initialiseApplication() {
        try {
            initSplash();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        final String[] stages = {"stage 1", "stage 2", "stage 3"};
        int stage = 0;
        for (int i = 0; i <= 100; i += 5) {
            String status = "Initialising " + stages[stage] + "...";
            if (splash != null) updateSplash(status, i);
            try {
                Thread.sleep(100);
                if (i == 30) stage = 1;
                else if (i == 60) stage = 2;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if (splash != null) splash.close();
        runApplication();
    }

    public static void main(String args[]) {
        new SplashTest(BOTTOM_CENTER).initialiseApplication();
    }

    private void runApplication() {
        System.out.println("... Application started");
    }

    private int getX(int type) {
        switch (type) {
            case TOP_LEFT :
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                return 10;
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                return (splashBounds.width - barWidth) / 2;
            case TOP_RIGHT :
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:    
                return splashBounds.width - barWidth - 10;
            default:
                return 10;
        }
    }

    private int getY(int type) {
        switch (type) {
            case TOP_LEFT :
            case TOP_CENTER:
            case TOP_RIGHT :
                return 20;
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                return (splashBounds.height - barHeight) / 2;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                return splashBounds.height - barHeight - 20;
            default:
                return 20;
        }
    }
}
