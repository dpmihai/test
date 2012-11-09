/*
 * @(#)javax.TestFrame.java         1.00
 *
 * User: sv
 * Date: Jun 26, 2004
 * Time: 10:30:49 PM
 *
 * Copyright 2004 jballoon.net.  All rights reserved.
 */
package balloon;

//import org.objectweb.asm.*;

import java.awt.*;
import javax.swing.*;



/**
 * The  <code>javax.TestFrame</code>
 *
 *
 *
 *
 * @author  Serge S. Vasiljev
 * @version 1.00, Jun 26, 2004
 */
public class TestFrame extends JFrame {


    public static void main(String[] args) {
        try {
            TestFrame f = new TestFrame();
            f.setDefaultCloseOperation(EXIT_ON_CLOSE);
            f.init();
            f.setBounds(200, 200, 500, 500);
            f.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void init() throws Exception {

        JBalloonToolTip.setBalloonDefault(false);
        JButton btn = new JButton("test button") {
            public JToolTip createToolTip() {
                return new JBalloonToolTip();
            }
        };
        btn.setToolTipText("it's test tooltip texaaa sdfsdfsdf sdfsdf sdf sdf sdf sadf ");
        final JButton btnCenter = new JButton("center") {
            public JToolTip createToolTip() {
                return new JBalloonToolTip();
            }
        };
        btnCenter.setToolTipText("some tool tips text here aaaaaaaaaaaaa bbbbbbbbbbb cccccccccccc ddddddddddd");

        final JButton btnSouth = new JButton("south") {
            public JToolTip createToolTip() {
                return new JBalloonToolTip();
            }
        };
        btnSouth.setToolTipText("<html>sdfsdfsdf asdf sdaf asdf asdf asdf dsa </html>");
        btnSouth.setEnabled(false);

        final JButton btnWest = new JButton("west") {
            public JToolTip createToolTip() {
                return new JBalloonToolTip();
            }
        };
        btnWest.setToolTipText("<html>west<br> tool sfsdfs <br> tips<br> text<br> :)<br><br><br><br><br><br><br><br><br><br><br><br><br><br> ;)</html>");

        final JButton btnEast = new JButton("east");
        btnEast.setToolTipText("east tool tips text");

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(btn, BorderLayout.NORTH);
        this.getContentPane().add(btnSouth, BorderLayout.SOUTH);
        this.getContentPane().add(btnWest, BorderLayout.WEST);
        this.getContentPane().add(btnEast, BorderLayout.EAST);
        this.getContentPane().add(btnCenter, BorderLayout.CENTER);
    }
}
