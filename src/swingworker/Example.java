package swingworker;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 21, 2007
 * Time: 10:49:01 AM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Swing Worker in java 1.6
//    * Override the doInBackground() method to run any code on a secondary thread.
//      Use the get() method to retrieve anything calculated during the run of doInBackground().
//
//    * Override the process() and/or done() methods to run any code on the event-dispatch thread.
//
//    * Call execute() to cause the worker to be scheduled to run.

public class Example {

    public static void main(String args[]) {
        Runnable runner = new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Title");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JButton button = new JButton("Press Here");
                ContainerListener container = new ContainerAdapter() {
                    public void componentAdded(final ContainerEvent e) {
                        SwingWorker worker = new SwingWorker() {

                            // a computation done only once
                            protected String doInBackground() throws InterruptedException {
                                return null;
                            }

                            // executed in event dispatching thread
                            protected void done() {
                                System.out.println("On the event thread? : " + EventQueue.isDispatchThread());
                                JButton button = (JButton) e.getChild();
                                String label = button.getText();
                                button.setText(label + "0");
                            }
                        };
                        worker.execute();
                    }
                };
                frame.getContentPane().addContainerListener(container);
                frame.add(button, BorderLayout.CENTER);
                frame.setSize(200, 200);
                System.out.println("I'm about to be realized: " + EventQueue.isDispatchThread());
                frame.setVisible(true);
            }
        };
        EventQueue.invokeLater(runner);
    }
}
