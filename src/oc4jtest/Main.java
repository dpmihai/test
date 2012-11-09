package oc4jtest;

import javax.swing.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.awt.*;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 10, 2007
 * Time: 9:58:41 AM
 */
public class Main {

    public static void main(String[] args) {

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("dedicated.connection", "true");
        env.put("java.naming.factory.initial", "oracle.j2ee.naming.ApplicationClientInitialContextFactory");
        env.put("java.naming.provider.url", "localhost");
        try {
            InitialContext ic = new InitialContext(env);
            System.out.println("ic="+ic);
        } catch (NamingException e) {
            e.printStackTrace();
        }


        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(new JLabel("TEST"), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}
