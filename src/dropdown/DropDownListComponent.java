package dropdown;

import javax.swing.*;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mihai.panaitescu
 * Date: 07-Jan-2010
 * Time: 11:36:46
 */
public class DropDownListComponent extends JComponent {

    private JButton status;
    private JPanel panel;

    public DropDownListComponent(List<String> items) {
        status = new JButton();
        panel = new ListSelectionPanel(items);
        final DropDownComponent dropdown = new DropDownComponent(status, panel);
        panel.addPropertyChangeListener("selectedItem",
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        dropdown.hidePopup();
                        status.setText((String) evt.getNewValue());
                    }
                });
        setLayout(new GridBagLayout());
        add(dropdown, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    }

    class ListSelectionPanel extends JPanel {

        Color selBackground = (Color)UIManager.getDefaults().get("ComboBox.selectionBackground");
        Color selForeground = (Color)UIManager.getDefaults().get("ComboBox.selectionForeground");
        Color unselBackground = (Color)UIManager.getDefaults().get("ComboBox.background");
        Color unselForeground = (Color)UIManager.getDefaults().get("ComboBox.foreground");

        public ListSelectionPanel(List<String> items) {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            ActionListener item_listener = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    selectItem(((JButton) evt.getSource()).getText());
                }
            };
            MouseListener mouseListener = new MouseAdapter() {


                public void mouseEntered(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    button.setBackground(selBackground);
                    button.setForeground(selForeground);
                }

                public void mouseExited(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    button.setBackground(unselBackground);
                    button.setForeground(unselForeground);
                }
            };

            int i=0;
            for (String item : items) {
                JButton button = new JButton(item);
                button.setHorizontalAlignment(SwingUtilities.LEFT);
                button.setBackground(unselBackground);
                button.setForeground(unselForeground);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.addActionListener(item_listener);
                button.addMouseListener(mouseListener);
                add(button, new GridBagConstraints(0, i, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                i++;
            }

        }

        // fire off a selectedItem property event
        protected String selectedText = "";

        public void selectItem(String item) {
            String oldText = selectedText;
            selectedText = item;
            firePropertyChange("selectedItem", oldText, item);
        }

    }

}
