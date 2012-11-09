package table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @version 1.0 05/31/99
 */
public class HideColumnTableExample extends JFrame {

    public HideColumnTableExample() {
        super("HideColumnTable Example");

        JTable table = new JTable(5, 7);
        ColumnButtonScrollPane pane = new ColumnButtonScrollPane(table);
        getContentPane().add(pane);
    }

    public static void main(String[] args) {
        HideColumnTableExample frame = new HideColumnTableExample();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(400, 100);
        frame.setVisible(true);
    }
}

class ColumnButtonScrollPane extends JScrollPane {
    Component columnButton;

    public ColumnButtonScrollPane(JTable table) {
        super(table);
        TableColumnModel cm = table.getColumnModel();
        LimitedTableHeader header = new LimitedTableHeader(cm);
        table.setTableHeader(header);
        columnButton = createUpperCorner(header);
        setCorner(UPPER_RIGHT_CORNER, columnButton);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        ColumnButtonScrollPaneLayout layout = new ColumnButtonScrollPaneLayout();
        setLayout(layout);
        layout.syncWithScrollPane(this);
    }

    protected Component createUpperCorner(JTableHeader header) {
        return new ColumnButton(header);        
    }

    public class LimitedTableHeader extends JTableHeader {
        public LimitedTableHeader(TableColumnModel cm) {
            super(cm);
        }

        // actually, this is a not complete way. but easy one.
        // you can see last column painted wider, short time :)
        // If you don't like this kind cheap fake,
        // you have to overwrite the paint method in UI class.
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            columnButton.repaint();
        }
    }

    public class ColumnButton extends JPanel {

        JTable table;
        TableColumnModel cm;
        JButton revealButton;
        JButton hideButton;
        Stack<TableColumn> stack;

        public ColumnButton(JTableHeader header) {
            setLayout(new GridLayout(1, 2));
            setBorder(new LinesBorder(SystemColor.controlShadow, new Insets(0, 1, 0, 0)));

            stack = new Stack<TableColumn>();
            table = header.getTable();
            cm = table.getColumnModel();

            revealButton = createButton(header, SwingConstants.WEST);
            hideButton = createButton(header, SwingConstants.EAST);
            add(revealButton);
            add(hideButton);

            revealButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TableColumn column = stack.pop();
                    cm.addColumn(column);
                    if (stack.empty()) {
                        revealButton.setEnabled(false);
                    }
                    hideButton.setEnabled(true);
                    table.sizeColumnsToFit(-1);
                }
            });
            hideButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int n = cm.getColumnCount();
                    TableColumn column = cm.getColumn(n - 1);
                    stack.push(column);
                    cm.removeColumn(column);
                    if (n < 3) {
                        hideButton.setEnabled(false);
                    }
                    revealButton.setEnabled(true);
                    table.sizeColumnsToFit(-1);
                }
            });

            if (1 < cm.getColumnCount()) {
                hideButton.setEnabled(true);
            } else {
                hideButton.setEnabled(false);
            }
            revealButton.setEnabled(false);
        }

        protected JButton createButton(JTableHeader header, int direction) {
            //int iconHeight = header.getPreferredSize().height - 6;
            int iconHeight = 8;
            JButton button = new JButton();
            button.setIcon(new ArrowIcon(iconHeight, direction, true));
            button.setDisabledIcon(new ArrowIcon(iconHeight, direction, false));
            button.setRequestFocusEnabled(false);
            button.setForeground(header.getForeground());
            button.setBackground(header.getBackground());
            button.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return button;
        }
    }
}

class ColumnButtonScrollPaneLayout extends ScrollPaneLayout {

    public ColumnButtonScrollPaneLayout() {
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void setVerticalScrollBarPolicy(int x) {
        // VERTICAL_SCROLLBAR_ALWAYS
        super.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void layoutContainer(Container parent) {
        super.layoutContainer(parent);

        if ((colHead == null) || (!colHead.isVisible()) || (upperRight == null) || (vsb == null)) {
            return;
        }

        Rectangle vsbR = new Rectangle(0, 0, 0, 0);
        vsbR = vsb.getBounds(vsbR);

        Rectangle colHeadR = new Rectangle(0, 0, 0, 0);
        colHeadR = colHead.getBounds(colHeadR);
        colHeadR.width -= vsbR.width;
        colHead.getBounds(colHeadR);

        Rectangle upperRightR = upperRight.getBounds();
        upperRightR.x -= vsbR.width;
        upperRightR.width += vsbR.width + 1;
        upperRight.setBounds(upperRightR);
    }
}

class LinesBorder extends AbstractBorder implements SwingConstants {

    protected int northThickness;
    protected int southThickness;
    protected int eastThickness;
    protected int westThickness;

    protected Color northColor;
    protected Color southColor;
    protected Color eastColor;
    protected Color westColor;

    public LinesBorder(Color color) {
        this(color, 1);
    }

    public LinesBorder(Color color, int thickness) {
        setColor(color);
        setThickness(thickness);
    }

    public LinesBorder(Color color, Insets insets) {
        setColor(color);
        setThickness(insets);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Color oldColor = g.getColor();

        g.setColor(northColor);
        for (int i = 0; i < northThickness; i++) {
            g.drawLine(x, y + i, x + width - 1, y + i);
        }
        g.setColor(southColor);
        for (int i = 0; i < southThickness; i++) {
            g.drawLine(x, y + height - i - 1, x + width - 1, y + height - i - 1);
        }
        g.setColor(eastColor);
        for (int i = 0; i < westThickness; i++) {
            g.drawLine(x + i, y, x + i, y + height - 1);
        }
        g.setColor(westColor);
        for (int i = 0; i < eastThickness; i++) {
            g.drawLine(x + width - i - 1, y, x + width - i - 1, y + height - 1);
        }

        g.setColor(oldColor);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(northThickness, westThickness, southThickness, eastThickness);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return new Insets(northThickness, westThickness, southThickness, eastThickness);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void setColor(Color c) {
        northColor = c;
        southColor = c;
        eastColor = c;
        westColor = c;
    }

    public void setColor(Color c, int direction) {
        switch (direction) {
            case NORTH:
                northColor = c;
                break;
            case SOUTH:
                southColor = c;
                break;
            case EAST:
                eastColor = c;
                break;
            case WEST:
                westColor = c;
                break;
            default:
        }
    }

    public void setThickness(int n) {
        northThickness = n;
        southThickness = n;
        eastThickness = n;
        westThickness = n;
    }

    public void setThickness(Insets insets) {
        northThickness = insets.top;
        southThickness = insets.bottom;
        eastThickness = insets.right;
        westThickness = insets.left;
    }

    public void setThickness(int n, int direction) {
        switch (direction) {
            case NORTH:
                northThickness = n;
                break;
            case SOUTH:
                southThickness = n;
                break;
            case EAST:
                eastThickness = n;
                break;
            case WEST:
                westThickness = n;
                break;
            default:
        }
    }

    public void append(LinesBorder b, boolean isReplace) {
        if (isReplace) {
            northThickness = b.northThickness;
            southThickness = b.southThickness;
            eastThickness = b.eastThickness;
            westThickness = b.westThickness;
        } else {
            northThickness = Math.max(northThickness, b.northThickness);
            southThickness = Math.max(southThickness, b.southThickness);
            eastThickness = Math.max(eastThickness, b.eastThickness);
            westThickness = Math.max(westThickness, b.westThickness);
        }
    }

    public void append(Insets insets, boolean isReplace) {
        if (isReplace) {
            northThickness = insets.top;
            southThickness = insets.bottom;
            eastThickness = insets.right;
            westThickness = insets.left;
        } else {
            northThickness = Math.max(northThickness, insets.top);
            southThickness = Math.max(southThickness, insets.bottom);
            eastThickness = Math.max(eastThickness, insets.right);
            westThickness = Math.max(westThickness, insets.left);
        }
    }

}

class ArrowIcon implements Icon, SwingConstants {

    private static final int DEFAULT_SIZE = 11;
    //private static final int DEFAULT_SIZE = 5;

    private int size;
    private int iconSize;
    private int direction;
    private boolean isEnabled;
    private BasicArrowButton iconRenderer;

    public ArrowIcon(int direction, boolean isPressedView) {
        this(DEFAULT_SIZE, direction, isPressedView);
    }

    public ArrowIcon(int iconSize, int direction, boolean isEnabled) {
        this.size = iconSize / 2;
        this.iconSize = iconSize;
        this.direction = direction;
        this.isEnabled = isEnabled;
        iconRenderer = new BasicArrowButton(direction);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        iconRenderer.paintTriangle(g, x, y, size, direction, isEnabled);
    }

    public int getIconWidth() {
        //int retCode;
        switch (direction) {
            case NORTH:
            case SOUTH:
                return iconSize;
            case EAST:
            case WEST:
                return size;
        }
        return iconSize;
    }

    public int getIconHeight() {
        switch (direction) {
            case NORTH:
            case SOUTH:
                return size;
            case EAST:
            case WEST:
                return iconSize;
        }
        return size;
    }
}