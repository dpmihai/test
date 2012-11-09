package mouse;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 22, 2005 Time: 9:48:22 AM
 */
public class EnhancedScrollPaneDemo extends JFrame {
	public EnhancedScrollPaneDemo() {
		super("Enhancing JScrollPane");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		try {
			JEditorPane editorPane = new JEditorPane("TXT","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
//                    new URL(
//					"http://java.sun.com/j2se/1.4.2/docs/api/javax/swing/JViewport.html"));
			editorPane.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(editorPane);
			MouseScrollHandler.enableMouseScrolling(scrollPane);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		setSize(400, 400);
		setVisible(true);
	}

    public static void main(String[] args) {
        JFrame frame = new EnhancedScrollPaneDemo();

    }
}
