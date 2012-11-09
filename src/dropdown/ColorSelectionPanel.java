package dropdown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mihai.panaitescu
 * Date: 07-Jan-2010
 * Time: 11:18:11
 */
public class ColorSelectionPanel extends JPanel {
    public ColorSelectionPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

        // reusable listener for each button
        ActionListener color_listener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectColor(((JButton) evt.getSource()).getBackground());
            }
        };

        // set up the standard 12 colors
        Color[] colors = new Color[12];
        colors[0] = Color.white;
        colors[1] = Color.black;
        colors[2] = Color.blue;
        colors[3] = Color.cyan;
        colors[4] = Color.gray;
        colors[5] = Color.green;
        colors[6] = Color.lightGray;
        colors[7] = Color.magenta;
        colors[8] = Color.orange;
        colors[9] = Color.pink;
        colors[10] = Color.red;
        colors[11] = Color.yellow;

        // lay out the grid
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = c.NONE;
        c.weightx = 1.0;
        c.weighty = 1.0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                c.gridx = j;
                c.gridy = i;
                JButton button = new ColorButton(colors[j + i * 4]);
                gbl.setConstraints(button, c);
                add(button);
                button.addActionListener(color_listener);
            }
        }

    }

    // fire off a selectedColor property event
    protected Color selectedColor = Color.black;

    public void selectColor(Color newColor) {

        Color oldColor = selectedColor;
        selectedColor = newColor;
        firePropertyChange("selectedColor", oldColor, newColor);
    }

}
