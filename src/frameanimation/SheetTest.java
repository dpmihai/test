package frameanimation;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 3:25:16 PM
 */

import util.ImageUtil;

import javax.swing.*;
import java.beans.*;

public class SheetTest implements PropertyChangeListener {

    JOptionPane optionPane;
    AnimatedSheetableJFrame frame;

    public static void main(String[] args) {
        new SheetTest();
    }

    public SheetTest() {
        frame = new AnimatedSheetableJFrame("Sheet test");
        // put an image in the frameanimation's content pane
        ImageIcon icon = ImageUtil.getImageIcon("splash.png");
        JLabel label = new JLabel(icon);
        frame.getContentPane().add(label);
        // build JOptionPane dialog and hold onto it
        optionPane = new JOptionPane("Do you want to save?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);
        frame.setSize(600, 500);
        frame.setLocation(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        optionPane.addPropertyChangeListener(this);
        // pause for effect, then show the sheet
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException ie) {
        }
        JDialog dialog = optionPane.createDialog(frame, "irrelevant");
        frame.showJDialogAsSheet(dialog);
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
            System.out.println("Selected option " + pce.getNewValue());
            frame.hideSheet();
        }
    }
}
