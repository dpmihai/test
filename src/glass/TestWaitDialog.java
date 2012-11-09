package glass;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 26, 2005 Time: 4:23:00 PM
 */
public class TestWaitDialog extends JWaitDialog {
    public TestWaitDialog() {
        jbInit();
        setModal(true);
    }

    public void jbInit() {

        JTextField comp = new JTextField();
        JButton btn = new JButton("Freeze");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //deactivate("Please wait ...", true);
                deactivate("Please wait ...", false);
                // computing ...
                Thread t = new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } finally {
                            activate();
                        }
                    }
                });
                t.start();


            }
        });
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
        getContentPane().add(btn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(50, 10, 10, 10), 0, 0));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 400);
        setLocation(400, 400);
        setTitle("Test");
    }
}
