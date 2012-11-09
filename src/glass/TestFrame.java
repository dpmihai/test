package glass;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 23, 2005 Time: 11:28:10 AM To change this template use
 * File | Settings | File Templates.
 */
public class TestFrame extends JFrame {

    private GlassPane glassPane;
    private InfiniteProgressPanel inf;    

    public static final int GLASS = 0;
    public static final int INF = 1;
    private int type;
    private int defaultClose;
    private boolean resizable;

    public TestFrame(int type) {
        this.type = type;

        if (type == GLASS) {
            this.glassPane = new GlassPane();
            this.setGlassPane(this.glassPane);
            this.glassPane.setVisible(false);
        }
        else {
            this.inf = new InfiniteProgressPanel("Please wait ...", 9, 0.8f, 3);
            this.setGlassPane(inf);
        }

        jbInit();
    }

    public void start() {

        resizable = isResizable();
        defaultClose = getDefaultCloseOperation();
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        if (type == GLASS) {
            this.glassPane.setVisible(true);
        } else {
            inf.start();
        }

    }

    public void stop() {
        if (type == GLASS) {
            this.glassPane.setVisible(false);
        } else {
            inf.stop();
        }

        setResizable(resizable);
        setDefaultCloseOperation(defaultClose);
    }

    public void jbInit() {

        JTextField comp = new JTextField();
        JButton btn = new JButton("Freeze");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                start();

                // computing ...
                Thread t = new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } finally {
                            stop();
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocation(400, 400);
        setTitle("Test");
    }

}
