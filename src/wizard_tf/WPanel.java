package wizard_tf;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 11:39:19 AM
 */
public class WPanel extends JPanel {

    private String title;
    private JComponent content;

    public WPanel(String title, JComponent content) {
        this.title = title;
        this.content = content;        
        setLayout(new BorderLayout(1,1));
        add(content, BorderLayout.CENTER);
    }

    public String getTitle() {
        return title;
    }

    
}
