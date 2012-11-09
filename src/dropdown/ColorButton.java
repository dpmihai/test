package dropdown;

import javax.swing.*;
import java.awt.*;

/**
 * User: mihai.panaitescu
 * Date: 07-Jan-2010
 * Time: 11:18:44
 */
public class ColorButton extends JButton {
    public ColorButton(Color col) {
        super();
        this.setText("");
        Dimension dim = new Dimension(15, 15);
        this.setSize(dim);
        this.setPreferredSize(dim);
        this.setMinimumSize(dim);
        this.setBorderPainted(true);
        this.setBackground(col);

    }
}
