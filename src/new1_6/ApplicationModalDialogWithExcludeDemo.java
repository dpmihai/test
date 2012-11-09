package new1_6;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: 06-Mar-2009
 * Time: 17:24:33
 */
public class ApplicationModalDialogWithExcludeDemo {
    public static void main(String[] args) {
    final JFrame parent1 = new JFrame("Parent Frame 1");
    parent1.setLayout(new FlowLayout());
    parent1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    parent1.setBounds(100, 100, 200, 150);
    parent1.setVisible(true);

    JFrame parent2 = new JFrame("Parent Frame 2");
    parent2.setBounds(500, 100, 300, 150);
    parent2.setVisible(true);

    JFrame parent3 = new JFrame("Parent Frame 3 - Excluded");
    parent3.setBounds(300, 400, 300, 150);
    parent3.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    parent3.setVisible(true);

    JDialog dialog = new JDialog(parent1, "Application-Modal Dialog",
        Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setBounds(300, 200, 300, 150);
    dialog.setVisible(true);

  }
}
