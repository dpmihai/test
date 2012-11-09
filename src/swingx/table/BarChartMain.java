package swingx.table;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;



public class BarChartMain {

	private static class NumberTableModel extends AbstractTableModel {

		private Double[][] number = new Double[4][2000];
		
		public NumberTableModel(boolean allowNegativeValue, int rows) {
			number = new Double[4][rows];
			for (int i = 0; i < number.length; i++) {
				for (int j = 0; j < number[i].length; j++) {
					int sign = 1;
					if (allowNegativeValue) {
						sign = Math.random() > 0.5 ? 1 : -1;
					}
					number[i][j] = new Double(Math.random() * 1000 * (i + 1) * sign);
				}
			}
		}
		
		public int getColumnCount() {
			return number.length;
		}

		public int getRowCount() {
			return number[0].length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return number[columnIndex][rowIndex];
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Double.class;
		}
		
	}
	
	
	
	public static void main(String[] args) {
		final JCheckBox allowNegCheckBox = new JCheckBox("Allow Negative Value");
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(2000, 0, Integer.MAX_VALUE, 100));
		JFrame frame = new JFrame("Bar Chart");
		final JTable table = new JTable();
		final TableSorter sorter = new TableSorter(
				new NumberTableModel(allowNegCheckBox.isSelected(),
						((Number) spinner.getValue()).intValue()), 
				table.getTableHeader());
		table.setModel(sorter);
		allowNegCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sorter.setTableModel(new NumberTableModel(allowNegCheckBox.isSelected(),
						((Number) spinner.getValue()).intValue()));
			}
		});
		table.setDefaultRenderer(Double.class, new BarChartCellRenderer());
		ZoomWheelListener.installZoom(table);
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel northPanel = new JPanel();
		northPanel.add(allowNegCheckBox);
		final JCheckBox showGridCheckBox  = new JCheckBox("Show Grid");
		table.setShowGrid(showGridCheckBox.isSelected());
		showGridCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setShowGrid(showGridCheckBox.isSelected());
			}
		});
		northPanel.add(showGridCheckBox);
		spinner.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sorter.setTableModel(
					new NumberTableModel(
							allowNegCheckBox.isSelected(),
							((Number) spinner.getValue()).intValue()));
		
			}
		});
		northPanel.add(new JLabel("Number of Rows = "));
		northPanel.add(spinner);
		
		table.setToolTipText(
			"<html><body>Press on Control and use the mouse wheel to zoom on the table.<BR>" 
		+ "Use Shift + Control and the mouse wheel to zoom over a selection.<BR>"
		+ "Use Control + double click on the mouse wheel to restore default row height.</body></html>");
		
		
		frame.add(northPanel, BorderLayout.NORTH);
		frame.pack();
		frame.setLocationRelativeTo(JOptionPane.getRootFrame());
		frame.setVisible(true);
		
	}
	
	
}
