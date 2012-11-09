package icon;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 19, 2006
 * Time: 1:23:35 PM
 * To change this template use File | Settings | File Templates.
 */

// to display multiple icons on a JLabel/JButton/cell renderer. Basically it is an icon that contains an array of icons.
// If you set the alignment to ALIGNMENT_STACKED, you will achieve DecoratedIcon. 
public class MultiIcon implements Icon {
    public static final int ALIGNMENT_HORIZONTAL = 1;
    public static final int ALIGNMENT_VERTICAL = 2;
    public static final int ALIGNMENT_STACKED = 3;

    private int gap = 3; // will be ignored for stacked alignment
    private Icon[] icons;
    private int alignment;

    private int width = 0;
    private int height = 0;

    public MultiIcon() {
        this(ALIGNMENT_HORIZONTAL);
    }

    public MultiIcon(int alignment) {
        this.alignment = alignment;
    }

    public int getIconCount() {
        if (icons == null) {
            return 0;
        } else {
            return icons.length;
        }
    }

    public Icon getIconAt(int index) {
        return icons[index];
    }

    public void replaceIconAt(int index, Icon newIcon) {
        icons[index] = newIcon;
        width = calculateIconWidth();
        height = calculateIconHeight();
    }

    public void addIcon(Icon icon) {
        if (icon == null) {
            return;
        } else {
            if (icons == null) {
                icons = new Icon[]{icon};
            } else {
                Icon[] newIcons = new Icon[icons.length + 1];
                System.arraycopy(icons, 0, newIcons, 0, icons.length);
                newIcons[newIcons.length - 1] = icon;
                icons = newIcons;
            }
            width = calculateIconWidth();
            height = calculateIconHeight();
        }
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (icons == null) {
            return;
        } else if (alignment == ALIGNMENT_VERTICAL) {
            int yIcon = y;
            for (int i = 0; i < icons.length; i++) {
                Icon icon = icons[i];
                int xIcon = x + (width - icon.getIconWidth()) / 2;
                icon.paintIcon(c, g, xIcon, yIcon);
                yIcon += icon.getIconHeight() + gap;
            }
        } else if (alignment == ALIGNMENT_STACKED) {
            for (int i = 0; i < icons.length; i++) {
                Icon icon = icons[i];
                int xIcon = x + (width - icon.getIconWidth()) / 2;
                int yIcon = y + (width - icon.getIconHeight()) / 2;
                icon.paintIcon(c, g, xIcon, yIcon);
            }
        } else {
            assert alignment == ALIGNMENT_HORIZONTAL;
            int xIcon = x;
            for (int i = 0; i < icons.length; i++) {
                Icon icon = icons[i];
                int yIcon = y + (height - icon.getIconHeight()) / 2;
                icon.paintIcon(c, g, xIcon, yIcon);
                xIcon += icon.getIconWidth() + gap;
            }
        }
    }

    public int calculateIconWidth() {
        if (icons == null) {
            return 0;
        } else if (alignment == ALIGNMENT_HORIZONTAL) {
            int width = 0;
            for (int i = 0; i < icons.length; i++) {
                width += icons[i].getIconWidth();
            }
            width += gap * (icons.length - 1);
            return width;
        } else {
            int width = 0;
            for (int i = 0; i < icons.length; i++) {
                width = Math.max(width, icons[i].getIconHeight());
            }
            return width;
        }
    }

    public int calculateIconHeight() {
        if (icons == null) {
            return 0;
        } else if (alignment == ALIGNMENT_VERTICAL) {
            int height = 0;
            for (int i = 0; i < icons.length; i++) {
                height += icons[i].getIconWidth();
            }
            height += gap * (icons.length - 1);
            return height;
        } else {
            int height = 0;
            for (int i = 0; i < icons.length; i++) {
                height = Math.max(height, icons[i].getIconHeight());
            }
            return height;
        }
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

}
