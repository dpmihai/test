package table;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableColumnHider
{
	private JTable table;
	private TableColumnModel tcm;
	private Map hiddenColumns;

	public TableColumnHider(JTable table)
	{
		this.table = table;
		tcm = table.getColumnModel();
		hiddenColumns = new HashMap();
	}

	public void hide(String columnName)
	{
		int index = tcm.getColumnIndex( columnName );
		TableColumn column = tcm.getColumn( index );
		hiddenColumns.put(columnName, column);
		hiddenColumns.put(":" + columnName, new Integer( index ) );
		tcm.removeColumn(column);
	}

    public void hide(String[] columnName)
     {
        for (int i=0; i<columnName.length; i++) {
            int index = tcm.getColumnIndex( columnName[i] );
		    TableColumn column = tcm.getColumn( index );
		    hiddenColumns.put(columnName[i], column);
		    hiddenColumns.put(":" + columnName[i], new Integer( index ) );
        }
         for (int i=0; i<columnName.length; i++) {
            int index = tcm.getColumnIndex( columnName[i] );
		    TableColumn column = tcm.getColumn( index );
            tcm.removeColumn(column);
         }
     }


	public void show(String columnName)
	{
		Object o = hiddenColumns.remove( columnName );

		if (o == null) return;

		tcm.addColumn( (TableColumn)o );

		o = hiddenColumns.remove( ":" + columnName );

		if (o == null) return;

		int column = ((Integer)o).intValue();
		int lastColumn = tcm.getColumnCount() - 1;

		if (column < lastColumn)
			tcm.moveColumn(lastColumn, column);
	}

    public void show(String[] columnName)
	{
        for (int i=0; i<columnName.length; i++) {
            show(columnName[i]);
        }
    }

	public static void main(String[] args)
	{
		final String[] columnNames = {  "Name", "Size", "Type", "Date Modified", "Permissions" };

		String[][] data =
		{
			{"bin", "2", "dir", "Jun 9", "drwxr-xr-x" },
			{"boot", "3", "dir", "Jun 9", "drwxr-xr-x" },
			{"dev", "6", "dir", "Jul 12", "drwxr-xr-x" },
			{"etc", "34", "dir", "Jul 12", "drwxr-xr-x" },
		};

		JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(table);

		final TableColumnHider hider = new TableColumnHider( table );

		JPanel checkBoxes = new JPanel();

		for (int i = 0; i < columnNames.length; i++)
		{
			JCheckBox checkBox = new JCheckBox(columnNames[i]);
			checkBox.setSelected( true );
			checkBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					JCheckBox cb = (JCheckBox)evt.getSource();
					String columnName = cb.getText();

					if (cb.isSelected())
						hider.show( columnName );
					else
						hider.hide( columnName );
				}
			});
			checkBoxes.add(checkBox);
		}

        JCheckBox checkBox = new JCheckBox("Test");
	    checkBox.setSelected( true );
		checkBox.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent evt)
				{
					JCheckBox cb = (JCheckBox)evt.getSource();
					String[] columnName = { columnNames[0], columnNames[4] };

					if (cb.isSelected())
						hider.show( columnName );
					else
						hider.hide( columnName );
				}
		});
		checkBoxes.add(checkBox);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().add(checkBoxes, BorderLayout.SOUTH);
		frame.pack();
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	}
}

