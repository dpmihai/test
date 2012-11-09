package wait;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 12, 2007
 * Time: 11:57:57 AM
 */
class TestRunnable implements Runnable {

    private JWaitDialog d;
    private UIActivator activator;

    public TestRunnable(JWaitDialog d) {
        this.d = d;
    }

    public TestRunnable(UIActivator activator) {
        this.activator = activator;
    }

    public void run() {
        if (d != null) {
            int i = 0;
            while (!d.isStop()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String message = i++ + " files ...";
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        d.updateProgress(message);
                    }
                });
            }
            d.dispose();
        } else {
            for (int i = 0; i < activator.getTasks(); i++) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    activator.stop();
                    return;
                }
                final String message = (i + 1) + " files ...";
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        activator.updateProgress(message);
                    }
                });
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activator.stop();
                }
            });

        }
    }


    public static void main(String[] args) {
        
        final JFrame frame = new JFrame();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        JTextField txt = new JTextField();
        JButton btn = new JButton("Do");
        txt.setColumns(20);
        leftPanel.add(txt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        leftPanel.add(btn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));


        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        ButtonGroup bg = new ButtonGroup();
        JRadioButton one = new JRadioButton("one");
        JRadioButton two = new JRadioButton("two");
        bg.add(one);
        bg.add(two);
        btnPanel.add(one);
        btnPanel.add(Box.createRigidArea(new Dimension(5,5)));
        btnPanel.add(two);
        btnPanel.add(Box.createVerticalGlue());
        btnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JRootPane rootPane = new JRootPane();
        rootPane.setContentPane(leftPanel);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIActivator activator = new UIActivator(rootPane, "Generate documents ...", 20);
                Thread t = new Thread(new TestRunnable(activator));
                activator.start(new StopAction(t));
//                JWaitDialog d = new JWaitDialog(frame, "Generate documents ...", "computing ...", 10);
//                d.setVisible(true);

                t.start();
            }
        });




        frame.setLayout(new GridBagLayout());
        frame.add(rootPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(btnPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocation(400, 400);
        frame.setTitle("Test");        
        frame.setVisible(true);
    }
}