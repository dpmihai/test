package db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

import util.ResizeUtil;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 25, 2005 Time: 11:25:33 AM
 */
public class Main {

    public static void main(String[] args) {

        // repaint is done as we drag the mouse
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final ConnectionDialog cd = new ConnectionDialog(f, "DB Connect");
        cd.setSize(400,300);

        JButton cBtn = new JButton("Open");
        cBtn.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                  ResizeUtil.centerComponent(cd, f);
                  cd.setVisible(true);
             }
        });

        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(cBtn);
        f.setSize(400,300);
        f.setLocation(200,200);
        f.setVisible(true);

    }
}
