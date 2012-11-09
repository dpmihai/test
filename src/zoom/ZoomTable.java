package zoom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 7, 2006
 * Time: 5:20:34 PM
 */
import java.awt.*;
import javax.swing.*;

public class ZoomTable
{
    public static void main(String[] args)
    {
        Object data[][] = {
            {"AMZN", "Amazon", 41.28, "BUY"},
            {"EBAY", "eBay", 41.57, "BUY"},
            {"GOOG", "Google", 388.33, "SELL"},
            {"MSFT", "Microsoft", 26.56, "SELL"},
            {"NOK", "Nokia Corp", 17.13, "BUY"},
            {"ORCL", "Oracle Corp.", 12.52, "BUY"},
            {"SUNW", "Sun Microsystems", 3.86, "BUY"},
            {"TWX",  "Time Warner", 17.66, "SELL"},
            {"VOD",  "Vodafone Group", 26.02, "SELL"},
            {"YHOO", "Yahoo!", 37.69, "BUY"}
        };
        String columns[] = {"Symbol", "Name", "Price", "Guidance"};

        JvUndoableTableModel tableModel = new JvUndoableTableModel(data, columns);
        JvTable table = new JvTable(tableModel);
        table.setCellSelectionEnabled(true); // required for Fill Down
        JScrollPane pane = new JScrollPane(table);

        JvUndoManager undoManager = new JvUndoManager();
        tableModel.addUndoableEditListener(undoManager);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(undoManager.getUndoAction());
        editMenu.add(undoManager.getRedoAction());
        editMenu.addSeparator();
        editMenu.add(new JvFillDownAction(table));

        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JvFontAction(table, undoManager, "Arial"));
        viewMenu.add(new JvFontAction(table, undoManager, "Century Gothic"));
        viewMenu.add(new JvFontAction(table, undoManager, "Courier New"));
        viewMenu.add(new JvFontAction(table, undoManager, "Garamond"));
        viewMenu.add(new JvFontAction(table, undoManager, "Sans Serif"));
        viewMenu.add(new JvFontAction(table, undoManager, "Times New Roman"));
        viewMenu.addSeparator();
        viewMenu.add(new JvZoomAction(table, undoManager, 200));
        viewMenu.add(new JvZoomAction(table, undoManager, 100));
        viewMenu.add(new JvZoomAction(table, undoManager, 75));
        viewMenu.add(new JvZoomAction(table, undoManager, 50));
        viewMenu.add(new JvZoomAction(table, undoManager, 25));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        JFrame frame = new JFrame("Zoomable JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.add(pane, BorderLayout.CENTER);
        frame.setSize(400, 250);
        frame.setLocation(200, 300);
        frame.setVisible(true);
    }
}
