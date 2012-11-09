package comboboxrenderer;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2006
 * Time: 5:06:05 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    protected static Border noFocusBorder;

    public ComboBoxRenderer() {
        if (noFocusBorder == null) {
            noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        }
        setOpaque(true);
        setBorder(noFocusBorder);
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {


        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(getRenderedString(value));
        return this;
    }

    public abstract String getRenderedString(Object object);

}
