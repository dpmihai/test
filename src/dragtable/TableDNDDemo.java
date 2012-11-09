package dragtable;

import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author Decebal Suiu
 */
public class TableDNDDemo extends JFrame {

    private DataFlavor listFlavor;
    private DataObjectTransferHandler dndHandler;
    private TableDNDRecognizer dndRecognizer;

    public TableDNDDemo() {
        super("TableDNDDemo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        listFlavor = new ListDataFlavor();
        dndHandler = new DataObjectTransferHandler(listFlavor);
        dndRecognizer = new TableDNDRecognizer();
        
        initUI();
    }

    private void initUI() {
        getContentPane().setLayout(new GridLayout(1, 2));

        Vector tableData = new Vector();
        Vector dataObject;
        for (int i = 10; i > 0; i--) {
            dataObject = new Vector();
            for (int j = 0; j < 4; j++) {
                dataObject.add("data " + i + "," + j);
            }
            tableData.add(dataObject);
        }

        Vector columns = new Vector();
        columns.add(" ");
        columns.add("Name");
        columns.add("Size");
        columns.add(" Last modified ");
        DefaultTableModel model = new DefaultTableModel(tableData, columns);

        JTable table = new JTable(model) {

            public void changeSelection(int rowIndex, int columnIndex,
                    boolean toggle, boolean extend) {
                if (!dndRecognizer.isDragged()) {
                    super.changeSelection(rowIndex, columnIndex, toggle, extend);
                }
            }
            
        };
//        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setTransferHandler(dndHandler);
        table.setDropTarget(new DropTarget(table, DataObjectTransferHandler.getDropHandler()));
        table.addMouseListener(dndRecognizer);
        table.addMouseMotionListener(dndRecognizer);

        tableData = new Vector();
        for (int i = 10; i > 0; i--) {
            dataObject = new Vector();
            for (int j = 0; j < 4; j++) {
                dataObject.add("data^ " + i + "," + j);
            }
            tableData.add(dataObject);
        }
        model = new DefaultTableModel(tableData, columns);

        JTable table1 = new JTable(model) {

            public void changeSelection(int rowIndex, int columnIndex,
                    boolean toggle, boolean extend) {
                if (!dndRecognizer.isDragged()) {
                    super.changeSelection(rowIndex, columnIndex, toggle, extend);
                }
            }
            
        };
//        table1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setTransferHandler(dndHandler);
        table1.setDropTarget(new DropTarget(table1, DataObjectTransferHandler.getDropHandler()));
        table1.addMouseListener(dndRecognizer);
        table1.addMouseMotionListener(dndRecognizer);

        getContentPane().add(new JScrollPane(table));
        getContentPane().add(new JScrollPane(table1));
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        TableDNDDemo applocation = new TableDNDDemo();
        applocation.show();
    }

}
