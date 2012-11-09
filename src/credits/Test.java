package credits;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 8, 2006
 * Time: 3:17:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) {

        String copyright = "\u00a9";

        Person[] p = new Person[4];
        String[] desc1 = {"Milowsky Deater", "Business Analyst", "Milowsky.Deater@west.com"};
        String[] desc2 = {"Jonh Zukowxan", "Senior Developer", "Jonh.Zukowxan@west.com"};
        String[] desc3 = {"Pamela Anderson", "Tester?", "Pamela.Anderson@aaa.org"};
        String[] desc4 = {" ", "WEST " + copyright+"2005", " "};
        String img1 = "img\\img1.jpg";
        String img2 = "img\\img2.jpg";
        String img3 = "img\\img1.jpg";
        String img4 = "img\\img2.jpg";
        p[0] = new Person(desc1, img1);
        p[1] = new Person(desc2, img2);
        p[2] = new Person(desc3, img3);
        p[3] = new Person(desc4, img4);

        final Credits panel = new Credits(p);
//        panel.setPreferredSize(new Dimension(200, 240));
//        panel.setMinimumSize(new Dimension(200, 240));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        JButton stopBtn = new JButton("Close");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.stop();
            }
        });
        frame.getContentPane().add(stopBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(400, 400);
        frame.setTitle("Credits");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}
