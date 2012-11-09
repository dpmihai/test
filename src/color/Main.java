package color;

/**
 * User: mihai.panaitescu
 * Date: 14-May-2010
 * Time: 11:29:27
 */

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class Main {
    public static void main(String[] argv) {
        JColorChooser chooser = new JColorChooser();
        chooser.addChooserPanel(new ExcelColorChooserPanel());

        final JDialog dialog = JColorChooser.createDialog(null,
                "Change Button Background", true, chooser,
                null, null);
        dialog.setVisible(true);
    }
}

class ExcelColorChooserPanel extends AbstractColorChooserPanel {

    private Dimension btnDim = new Dimension(20, 20);

    private Color[] standardColors = new Color[]{
            new Color(0, 0, 0), new Color(153, 51, 0), new Color(51, 51, 0), new Color(0, 51, 0),
            new Color(0, 51, 102), new Color(0, 0, 128), new Color(51, 51, 153), new Color(51, 51, 51),
            new Color(128, 0, 0), new Color(255, 102, 0), new Color(128, 128, 0), new Color(0, 128, 0),
            new Color(0, 128, 128), new Color(0, 0, 255), new Color(102, 102, 153), new Color(128, 128, 128),
            new Color(255, 0, 0), new Color(255, 153, 0), new Color(153, 204, 0), new Color(51, 153, 102),
            new Color(51, 204, 204), new Color(51, 102, 255), new Color(128, 0, 128), new Color(150, 150, 150),
            new Color(255, 0, 255), new Color(255, 204, 0), new Color(255, 255, 0), new Color(0, 255, 0),
            new Color(0, 255, 255), new Color(0, 204, 255), new Color(153, 51, 102), new Color(192, 192, 192),
            new Color(255, 153, 204), new Color(255, 204, 153), new Color(255, 255, 153), new Color(204, 255, 204),
            new Color(204, 255, 255), new Color(153, 204, 255), new Color(204, 153, 255), new Color(255, 255, 255)
    };

    private Color[] chartFillColors = new Color[]{
            new Color(153, 153, 255), new Color(153, 51, 102), new Color(255, 255, 204), new Color(204, 255, 255),
            new Color(102, 0, 102), new Color(255, 128, 128), new Color(0, 102, 204), new Color(204, 204, 255)
    };

    private Color[] chartLineColors = new Color[]{
            new Color(0, 0, 128), new Color(255, 0, 255), new Color(255, 255, 0), new Color(0, 255, 255),
            new Color(128, 0, 128), new Color(128, 0, 0), new Color(0, 128, 128), new Color(0, 0, 255)
    };

    public void buildChooser() {
        setLayout(new GridBagLayout());

        add(new JLabel("Standard colors"),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
                        GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        JPanel standardPanel = new JPanel();
        standardPanel.setLayout(new GridLayout(5, 8));
        for (Color color : standardColors) {
            standardPanel.add(makeAddButton("", color));
        }
        add(standardPanel,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        add(new JLabel("Chart fills"),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        JPanel fillPanel = new JPanel();
        fillPanel.setLayout(new GridLayout(1, 8));
        for (Color color : chartFillColors) {
            fillPanel.add(makeAddButton("", color));
        }
        add(fillPanel,
                new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

        add(new JLabel("Chart lines"),
                new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        JPanel linePanel = new JPanel();
        linePanel.setLayout(new GridLayout(1, 8));
        for (Color color : chartLineColors) {
            linePanel.add(makeAddButton("", color));
        }
        add(linePanel,
                new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    }

    public void updateChooser() {
    }

    public String getDisplayName() {
        return "Excel Palette";
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }

    public Icon getLargeDisplayIcon() {
        return null;
    }

    private JButton makeAddButton(String name, Color color) {
        JButton button = new JButton(name);
        button.setPreferredSize(btnDim);
        button.setMinimumSize(btnDim);
        button.setMaximumSize(btnDim);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                BorderFactory.createRaisedBevelBorder()));
        button.setAction(setColorAction);
        button.setToolTipText(color.getRed() + "," + color.getGreen() + "," + color.getBlue());
        return button;
    }

    Action setColorAction = new AbstractAction() {
        public void actionPerformed(ActionEvent evt) {
            JButton button = (JButton) evt.getSource();
            getColorSelectionModel().setSelectedColor(button.getBackground());
        }
    };
}