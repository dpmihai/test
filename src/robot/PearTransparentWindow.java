package robot;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 19, 2007
 * Time: 3:05:38 PM
 */
public class PearTransparentWindow extends TransparentWindow {

    public PearTransparentWindow(int width, int height) {
        super(width, height);
    }

    protected void paintImage(Graphics2D tig) {
        tig.setColor(Color.orange);
        tig.fillOval(10, 20, 70, 80);
        tig.setColor(Color.green);
        tig.fillOval(21, 16, 20, 10);
        tig.fillOval(40, 2, 11, 21);
    }
    
    public static void main(String[] args) {
        new PearTransparentWindow(100, 100);
    }
}
