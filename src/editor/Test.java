package editor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 14, 2008
 * Time: 10:33:51 AM
 */

// http://java.dzone.com/news/how-create-editor-java-applica
public class Test {

    public static void main(String[] args) {

        JEditorPane editor = new JEditorPane();

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(editor, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLocation(400, 400);
        frame.setTitle("Test");

        jsyntaxpane.DefaultSyntaxKit.initKit();
        editor.setContentType("text/java");

        //frameanimation.pack();
        frame.setVisible(true);
        

    }


}
