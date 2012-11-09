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
 * Date: Jul 25, 2005 Time: 9:49:24 AM
 */
public class EnhancedScrollingDemo extends JFrame{

    public EnhancedScrollingDemo() {
		super("Enhancing JScrollPane");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		try {
            ScrollGestureRecognizer.getInstance();
			JEditorPane editorPane = new JEditorPane("TXT","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
			editorPane.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(editorPane);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		setSize(400, 400);
		setVisible(true);
	}

    public static void main(String[] args) {
        JFrame frame = new EnhancedScrollingDemo();

    }
}
