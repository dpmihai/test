package shadow;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2006
 * Time: 3:12:29 PM
 */
public class ShadowTest {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Test");

//        DropShadowPanel ds = new DropShadowPanel("./images/img.png");
//        ds.setLayout(new BoxLayout(ds, BoxLayout.X_AXIS));
//        ds.add(Box.createRigidArea(new Dimension(5,5)));


        ShadowImageButton btn = new ShadowImageButton("img.png");
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setMaximumSize(new Dimension(35, 35));
        ShadowImageButton btn2 = new ShadowImageButton("img.png");
        btn2.setPreferredSize(new Dimension(35, 35));
        btn2.setMaximumSize(new Dimension(35, 35));
        JToolBar tb = new JToolBar();
        tb.setRollover(true);
        tb.addSeparator();
        tb.add(btn);
        tb.add(btn2);
                
        frame.setLayout(new BorderLayout());
        frame.add(tb, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocation(400, 400);
        frame.setVisible(true);
    }

}
