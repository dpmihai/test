package color;

/**
 * User: mihai.panaitescu
 * Date: 14-May-2010
 * Time: 11:15:44
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

public class SystemColorChooser {

    public static void main(String args[]) {
        JFrame frame = new JFrame("JColorChooser Custom Panel Sample");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        final JButton button = new JButton("Pick to Change Background");

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color initialBackground = button.getBackground();

                final JColorChooser colorChooser = new JColorChooser(
                        initialBackground);
                SystemColorChooserPanel newChooser = new SystemColorChooserPanel();
                //        AbstractColorChooserPanel chooserPanels[] = {newChooser};
                //        colorChooser.setChooserPanels(chooserPanels);
                colorChooser.addChooserPanel(newChooser);

                // For okay button selection, change button background to
                // selected color
                ActionListener okActionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        Color newColor = colorChooser.getColor();
                        if (newColor.equals(button.getForeground())) {
                            System.out.println("Color change rejected");
                        } else {
                            button.setBackground(colorChooser.getColor());
                        }
                    }
                };

                // For cancel button selection, change button background to red
                ActionListener cancelActionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        button.setBackground(Color.red);
                    }
                };

                final JDialog dialog = JColorChooser.createDialog(null,
                        "Change Button Background", true, colorChooser,
                        okActionListener, cancelActionListener);

                // Wait until current event dispatching completes before showing
                // dialog
                Runnable showDialog = new Runnable() {
                    public void run() {
                        dialog.show();
                    }
                };
                SwingUtilities.invokeLater(showDialog);
            }
        };
        button.addActionListener(actionListener);
        contentPane.add(button, BorderLayout.CENTER);

        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}

class SystemColorChooserPanel extends AbstractColorChooserPanel implements
        ItemListener {
    private static int NOT_FOUND = -1;

    JComboBox comboBox;

    String labels[] = {"black", "blue", "cyan", "darkGray", "gray", "green",
            "lightGray", "magenta", "orange", "pink", "red", "white", "yellow",
            "activeCaption", "activeCaptionBorder", "activeCaptionText",
            "control", "controlDkShadow", "controlHighlight",
            "controlLtHighlight", "controlShadow", "controlText", "desktop",
            "inactiveCaption", "inactiveCaptionBorder", "inactiveCaptionText",
            "info", "infoText", "menu", "menuText", "scrollbar", "text",
            "textHighlight", "textHighlightText", "textInactiveText",
            "textText", "window", "windowBorder", "windowText", "<Custom>"};

    Color colors[] = {Color.black, Color.blue, Color.cyan, Color.darkGray,
            Color.gray, Color.green, Color.lightGray, Color.magenta,
            Color.orange, Color.pink, Color.red, Color.white, Color.yellow,
            SystemColor.activeCaption, SystemColor.activeCaptionBorder,
            SystemColor.activeCaptionText, SystemColor.control,
            SystemColor.controlDkShadow, SystemColor.controlHighlight,
            SystemColor.controlLtHighlight, SystemColor.controlShadow,
            SystemColor.controlText, SystemColor.desktop,
            SystemColor.inactiveCaption, SystemColor.inactiveCaptionBorder,
            SystemColor.inactiveCaptionText, SystemColor.info,
            SystemColor.infoText, SystemColor.menu, SystemColor.menuText,
            SystemColor.scrollbar, SystemColor.text, SystemColor.textHighlight,
            SystemColor.textHighlightText, SystemColor.textInactiveText,
            SystemColor.textText, SystemColor.window, SystemColor.windowBorder,
            SystemColor.windowText, null};

    // Change combo box to match color, if possible
    private void setColor(Color newColor) {
        int position = findColorPosition(newColor);
        comboBox.setSelectedIndex(position);
    }

    // Given a label, find the position of the label in the list
    private int findColorLabel(Object label) {
        String stringLabel = label.toString();
        int position = NOT_FOUND;
        for (int i = 0, n = labels.length; i < n; i++) {
            if (stringLabel.equals(labels[i])) {
                position = i;
                break;
            }
        }
        return position;
    }

    // Given a color, find the position whose color matches
    // This could result in a position different from original if two are equal
    // Since color is same, this is considered to be okay
    private int findColorPosition(Color color) {
        int position = colors.length - 1;
        // Cannot use equals() to compare Color and SystemColor
        int colorRGB = color.getRGB();
        for (int i = 0, n = colors.length; i < n; i++) {
            if ((colors[i] != null) && (colorRGB == colors[i].getRGB())) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        if (state == ItemEvent.SELECTED) {
            int position = findColorLabel(itemEvent.getItem());
            // last position is bad (not selectable)
            if ((position != NOT_FOUND) && (position != labels.length - 1)) {
                ColorSelectionModel selectionModel = getColorSelectionModel();
                selectionModel.setSelectedColor(colors[position]);
            }
        }
    }

    public String getDisplayName() {
        return "SystemColor";
    }

    public Icon getSmallDisplayIcon() {
        return new DiamondIcon(Color.blue);
    }

    public Icon getLargeDisplayIcon() {
        return new DiamondIcon(Color.green);
    }

    protected void buildChooser() {
        comboBox = new JComboBox(labels);
        comboBox.addItemListener(this);
        add(comboBox);
    }

    public void updateChooser() {
        Color color = getColorFromModel();
        setColor(color);
    }
}

class DiamondIcon implements Icon {
    private Color color;

    private boolean selected;

    private int width;

    private int height;

    private Polygon poly;

    private static final int DEFAULT_WIDTH = 10;

    private static final int DEFAULT_HEIGHT = 10;

    public DiamondIcon(Color color) {
        this(color, true, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public DiamondIcon(Color color, boolean selected) {
        this(color, selected, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public DiamondIcon(Color color, boolean selected, int width, int height) {
        this.color = color;
        this.selected = selected;
        this.width = width;
        this.height = height;
        initPolygon();
    }

    private void initPolygon() {
        poly = new Polygon();
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        poly.addPoint(0, halfHeight);
        poly.addPoint(halfWidth, 0);
        poly.addPoint(width, halfHeight);
        poly.addPoint(halfWidth, height);
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.translate(x, y);
        if (selected) {
            g.fillPolygon(poly);
        } else {
            g.drawPolygon(poly);
        }
        g.translate(-x, -y);
    }
}
