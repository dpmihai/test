package db.rep.table;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * @author Decebal Suiu
 */
public class ComboBoxEditor extends DefaultCellEditor {
    
    public ComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }

    public ComboBoxEditor(Vector items) {
        super(new JComboBox(items));
    }
    
}
