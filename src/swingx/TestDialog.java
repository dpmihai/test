package swingx;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 11, 2008
 * Time: 1:34:16 PM
 */
public class TestDialog {

    public static void main(String[] args) {

        ServerDialog sd = new ServerDialog();        
        sd.pack();
        sd.setVisible(true);
        
        if (sd.isOkPressed()) {
            System.out.println(sd.getServer());
        }
        
    }
}
