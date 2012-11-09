package popup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import animation.AnimatedPanel;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 20, 2005 Time: 12:13:34 PM
 */
public class Test {


	public static void main(String args[]) {
		JFrame frm = new JFrame("IncrementalSearch::Demo");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextArea textArea = new JTextArea(10, 45);

		final PopupTextField searchField = new PopupTextField(new ImageIcon(AnimatedPanel.class.getResource("capone_16x16.png")));
		// build menu
		JPopupMenu menu = searchField.getPopupMenu();
        final JMenuItem m1 = new JMenuItem("Test1");
        m1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText(m1.getText());
            }
        });
		menu.add(m1);
		menu.add(new JMenuItem("Test2"));
		menu.add(new JMenuItem("Test3"));
		menu.add(new JMenuItem("Test4"));


		((JPanel)frm.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		frm.getContentPane().add(searchField, BorderLayout.PAGE_START);
		frm.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		frm.pack();
		frm.setVisible(true);

	}
}

