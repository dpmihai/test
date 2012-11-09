package table;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 24, 2006
 * Time: 5:40:06 PM
 */
public class PackedTable extends JTable {

    public PackedTable(Object[][] rows, Object[] cols) {
        super(rows, cols);
    }

    private TablePacker packer = null;

    public void pack(int rowsIncluded, boolean distributeExtraArea) {
        packer = new TablePacker(rowsIncluded, true);
        if (isShowing()) {
            packer.pack(this);
            packer = null;
        }
    }

    public void addNotify() {
        super.addNotify();
        if (packer != null) {
            packer.pack(this);
            packer = null;
        }
    }
}
