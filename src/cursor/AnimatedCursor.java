package cursor;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 2, 2006
 * Time: 3:15:15 PM
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnimatedCursor implements Runnable, ActionListener {
    private boolean animate;
    private Cursor[] cursors;
    private JFrame frame;
    public AnimatedCursor(JFrame frame) {
        animate = false;
        cursors = new Cursor[8];
        this.frame = frame;
        cursors[0] = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
        cursors[1] = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
        cursors[2] = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
        cursors[3] = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
        cursors[4] = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        cursors[5] = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
        cursors[6] = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
        cursors[7] = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
    }

    public void run() {
        int count = 0;
        while(animate) {
            try {
                Thread.currentThread().sleep(200);
            } catch (Exception ex) {
            }

            frame.setCursor(cursors[count % cursors.length]);
            count++;
        }
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void actionPerformed(ActionEvent evt) {
        JButton button = (JButton)evt.getSource();
        if(animate) {
            button.setText("Start Animation");
            animate = false;
        } else {
            animate = true;
            button.setText("Stop Animation");
            new Thread(this).start();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animated Cursor Hack");

        JButton button = new JButton("Start Animation");
        button.addActionListener(new AnimatedCursor(frame));

        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);
    }

    public static void p(String str) {
        System.out.println(str);
    }

}
