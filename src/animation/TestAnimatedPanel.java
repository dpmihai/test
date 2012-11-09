package animation;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 24, 2005 Time: 3:34:29 PM To change this template use File
 * | Settings | File Templates.
 */
public class TestAnimatedPanel {

    public static void main(String[] args) {

        AnimatedPanel panel = new AnimatedPanel("Please wait ...",
                new ImageIcon(TestAnimatedPanel.class.getResource("capone_16X16.png")));
        panel.setPreferredSize(new Dimension(200,100));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(panel, new GridBagConstraints(0,0, 1,1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 0,0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(400,400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
        panel.start();
    }
}
