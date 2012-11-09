package table;

import javax.swing.table.AbstractTableModel;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 8, 2005 Time: 5:55:15 PM To change this template use File
 * | Settings | File Templates.
 */
public class HideColumnTableModel extends AbstractTableModel
{
  private String[] _columnNames =
    { "Name", "Size", "Type", "Date Modified", "Permissions" };

  private String[][] _data =
    {
      { "bin",        "2",  "dir", "Jun 9",  "drwxr-xr-x" },
      { "boot",       "3",  "dir", "Jun 9",  "drwxr-xr-x" },
      { "dev",        "6",  "dir", "Jul 12", "drwxr-xr-x" },
      { "etc",        "34", "dir", "Jul 12", "drwxr-xr-x" },
      { "home",       "5",  "dir", "Jun 9",  "drwxr-xr-x" },
      { "lib",        "4",  "dir", "Jun 9",  "drwxr-xr-x" },
      { "lost+found", "2",  "dir", "Jun 9",  "drwxr-xr-x" },
      { "usr",        "19", "dir", "Jun 21", "drwxr-xr-x" },
      { "var",        "17", "dir", "Jun 1",  "drwxr-xr-x" },
    };

  private boolean[] _columnsVisible =
    { true, true, true, true, true };

  public HideColumnTableModel() {}

  public int getRowCount()
  {
    return this._data.length;
  }

  public int getColumnCount()
  {
    int count = 0;

    for(int index = 0; index < this._columnsVisible.length; index++)
    {
      if(this._columnsVisible[index])
      {
        count++;
      }
    }

    return count;
  }

  public String getColumnName(int column)
  {
    int actualColumn = this.getVisibleColumnIndex(column);
    return this._columnNames[actualColumn];
  }

  private int getVisibleColumnIndex(int column)
  {
    int count = 0;

    for(int index = 0; index < this._columnsVisible.length; index++)
    {
      if(this._columnsVisible[index])
      {
        count++;
      }

      if(count - 1 == column)
      {
        return index;
      }
    }

    return 0;
  }

  public void setColumnVisible(int index, boolean visible)
  {
    this._columnsVisible[index] = visible;
    this.fireTableStructureChanged();
  }

  public Object getValueAt(int row, int column)
  {
    int actualColumn = this.getVisibleColumnIndex(column);
    return this._data[row][actualColumn];
  }
}
