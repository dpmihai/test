package wizard_tf;

import graphics.morphing.DirectionButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 11:22:40 AM
 */
public class WButtonPanel extends JPanel {

    private String BACKWARD = "Backward";
    private String FORWARD = "Forward";
    private String CANCEL = "Cancel";

    private DirectionButton backwardButton;
    private DirectionButton forwardButton;
    private DirectionButton cancelButton;

    public WButtonPanel() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        backwardButton = new DirectionButton(BACKWARD, DirectionButton.Direction.LEFT);
        forwardButton = new DirectionButton(FORWARD, DirectionButton.Direction.RIGHT);
        cancelButton = new DirectionButton(CANCEL, DirectionButton.Direction.NONE);

        backwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backward();
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forward();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        add(Box.createHorizontalGlue());
        add(backwardButton);
        add(Box.createRigidArea(new Dimension(5,5)));
        add(forwardButton);
        add(Box.createRigidArea(new Dimension(5,5)));
        add(cancelButton);
    }

    protected void backward(){

    }

    protected void forward() {

    }

    protected void cancel() {

    }

    public void enableForward(boolean enabled) {
        forwardButton.setEnabled(enabled);
    }

    public void enableBackward(boolean enabled) {
        backwardButton.setEnabled(enabled);
    }

    public void enableCancel(boolean enabled) {
        cancelButton.setEnabled(enabled);
    }

    

}
