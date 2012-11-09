package table;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 8, 2005 Time: 5:56:03 PM To change this template use File
 * | Settings | File Templates.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HideColumnFrame extends JFrame
{
  private GridBagLayout gbl = new GridBagLayout();
  private JPanel mainPanel = new JPanel(gbl);
  private JPanel cbPanel = new JPanel(gbl);
  private HideColumnTableModel _tm = new HideColumnTableModel();
  private JTable table = new JTable(_tm);
  private JScrollPane tableSP = new JScrollPane(table);

  public HideColumnFrame()
  {
    this.setTitle("Column Hiding Table");
    this.setSize(500, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setLayout(gbl);

    this.table.setCellSelectionEnabled(true);
    this.table.setRowSelectionAllowed(false);
    this.table.setColumnSelectionAllowed(false);

    for(int index = 0; index < this._tm.getColumnCount() - 1; index++)
    {
      String columnName = this._tm.getColumnName(index + 1);
      final JCheckBox nextCB = new JCheckBox(columnName);
      nextCB.setSelected(true);

      final int columnIndex = index + 1;
      nextCB.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          _tm.setColumnVisible(columnIndex, nextCB.isSelected());
        }
      });

      this.cbPanel.add(nextCB,
        new GridBagConstraints(1, index, 1, 1, 0.0, 0.0,
                               GridBagConstraints.WEST,
                               GridBagConstraints.NONE,
                               new Insets(0, 0, 0, 0), 0, 0));
    }

    this.mainPanel.add(tableSP,
      new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                             GridBagConstraints.BOTH, new Insets(0, 0, 3, 0),
                             0, 0));
    this.mainPanel.add(cbPanel,
      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                             GridBagConstraints.NONE, new Insets(3, 0, 0, 0),
                             0, 0));
    this.getContentPane().add(mainPanel,
      new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                             GridBagConstraints.BOTH, new Insets(6, 6, 6, 6),
                             0, 0));
  }

  public static void main(String[] args)
  {
    HideColumnFrame frame = new HideColumnFrame();
    frame.setVisible(true);
  }
}