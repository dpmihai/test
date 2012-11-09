//package swingx.table;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.Collection;
//import java.util.TreeSet;
//
//import javax.swing.JButton;
//import javax.swing.JColorChooser;
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.JMenuItem;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JScrollPane;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//import javax.swing.table.AbstractTableModel;
//
//import org.jdesktop.swingx.JXTable;
//import org.jdesktop.swingx.decorator.ColorHighlighter;
//import org.jdesktop.swingx.decorator.FilterPipeline;
//
//import swingx.table.filters.ComparableRangeFilter;
//import swingx.table.filters.DistinctValuesFilter;
//import swingx.table.highlight.ComparableRangeHighlightPredicate;
//import swingx.table.highlight.ValuesHighlightPredicate;
//
//public class FilteringDemo implements Runnable {
//
//	private static class RandomTableModel extends AbstractTableModel {
//
//		private static String[] nominalValues = {"aaa", "bbb", "ccc", "ddd"};
//
//		private static String getRandomNominal() {
//			int index = (int) (Math.floor(Math.random() * nominalValues.length) % nominalValues.length);
//			return nominalValues[index];
//		}
//
//		private int _rowCount;
//
//		private Object[][] _data;
//
//
//
//		RandomTableModel(int rowCount) {
//			_rowCount = rowCount;
//			_data = new Object[getColumnCount()][_rowCount];
//			for (int i = 0; i < _data.length; i++) {
//				for (int j = 0; j < _data[i].length; j++) {
//					if (getColumnClass(i) == String.class) {
//						_data[i][j] = getRandomNominal();
//					} else {
//						_data[i][j] = new Double(Math.random() * i);
//					}
//				}
//
//			}
//		}
//
//
//		@Override
//		public Class<?> getColumnClass(int columnIndex) {
//			return (columnIndex % 2 == 0) ? String.class : Double.class;
//		}
//
//		public int getColumnCount() {
//			return 10;
//		}
//
//		public int getRowCount() {
//			return _rowCount;
//		}
//
//		public Object getValueAt(int rowIndex, int columnIndex) {
//			return _data[columnIndex][rowIndex];
//		}
//
//	}
//
//	private static Color getRandomColor() {
//		int r = (int) Math.floor(Math.random() * 256);
//		int v = (int) Math.floor(Math.random() * 256);
//		int b = (int) Math.floor(Math.random() * 256);
//		int a = (int) Math.floor(Math.random() * 256);
//		return new Color(r, v, b);
//	}
//
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new FilteringDemo());
//	}
//
//	private void buildHighlightMenu(
//			final JXTable table,
//			JMenu highlightMenu,
//			String columnName,
//			final int modelColumn,
//			final Object value,
//			final Comparable<Object> min,
//			final Comparable<Object> max,
//			final Collection<Object> selectedValues) {
//		final Object[] selectedValuesArray =
//			selectedValues.toArray(new Object[selectedValues.size()]);
//
//
//		JMenuItem highlightCells = new JMenuItem("Highlight cells with " + columnName + " = " + value);
//		highlightCells.addActionListener( new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				table.addHighlighter(
//						new ColorHighlighter(
//								showColorChooser(table),
//								Color.black,
//								new ValuesHighlightPredicate(modelColumn, true, value)));
//			}
//		});
//		JMenuItem highlightLines = new JMenuItem("Highlight line with " + columnName + " = " + value);
//		highlightLines.addActionListener( new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				table.addHighlighter(
//						new ColorHighlighter(
//								showColorChooser(table),
//								Color.black,
//								new ValuesHighlightPredicate(modelColumn, false, value)));
//			}
//		});
//		highlightMenu.add(highlightCells);
//		highlightMenu.add(highlightLines);
//
//		if (selectedValues.size() > 1) {
//
//			JMenuItem highlightSelectedCellValues = new JMenuItem("Highlight Selected Cell Values");
//			highlightSelectedCellValues.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					table.addHighlighter(
//							new ColorHighlighter(
//									showColorChooser(table),
//									Color.black,
//									new ValuesHighlightPredicate(modelColumn, false, selectedValuesArray)));
//				}
//			});
//			highlightMenu.add(highlightSelectedCellValues);
//
//			JMenuItem highlightSelectedLineValues = new JMenuItem("Highlight Selected Line Values");
//			highlightSelectedLineValues.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					table.addHighlighter(
//							new ColorHighlighter(
//									showColorChooser(table),
//									Color.black,
//									new ValuesHighlightPredicate(modelColumn, false, selectedValuesArray)));
//				}
//			});
//			highlightMenu.add(highlightSelectedLineValues);
//
//
//			JMenuItem highlightCellValues = new JMenuItem("Highlight Cells with " + columnName + " between " + min + " and " + max);
//			highlightCellValues.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					table.addHighlighter(
//							new ColorHighlighter(
//									showColorChooser(table),
//									Color.black,
//									new ComparableRangeHighlightPredicate(
//											modelColumn,
//											true,
//											(Comparable<Object>) min,
//											(Comparable<Object>) max)));
//				}
//			});
//			highlightMenu.add(highlightCellValues);
//			JMenuItem highlightLinesValues = new JMenuItem("Highlight Lines with " + columnName + " between " + min + " and " + max);
//			highlightLinesValues.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					table.addHighlighter(
//							new ColorHighlighter(
//									showColorChooser(table),
//									Color.black,
//									new ComparableRangeHighlightPredicate(
//											modelColumn,
//											false,
//											(Comparable<Object>) min,
//											(Comparable<Object>) max)));
//				}
//			});
//
//			highlightMenu.add(highlightLinesValues);
//		}
//
//	}
//
//	private Color showColorChooser(Component comp) {
//		return JColorChooser.showDialog(
//				JOptionPane.getFrameForComponent(comp),
//				"Select Color",
//				getRandomColor());
//	}
//
//	private void buildFocusMenu(final JXTable table,
//			JMenu focusMenu,
//			String columnName,
//			final int modelColumn,
//			final Object value,
//			final Comparable<Object> min,
//			final Comparable<Object> max,
//			final Collection<Object> selectedValues) {
//		JMenuItem focusOnValues = new JMenuItem("Focus on " + columnName + " = " + value);
//		focusOnValues.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				FilterPipeline pipe = new FilterPipeline(new DistinctValuesFilter<Object>(modelColumn, value));
//				table.setFilters(pipe);
//			}
//		});
//		focusMenu.add(focusOnValues);
//		if (selectedValues.size() > 1) {
//			JMenuItem focusOnSelectedValues = new JMenuItem("Focus on Selected Values");
//			focusOnSelectedValues.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					FilterPipeline pipe =
//						new FilterPipeline(
//								new DistinctValuesFilter<Object>(
//										modelColumn,
//										selectedValues.toArray(
//												new Object[selectedValues.size()])));
//					table.setFilters(pipe);
//				}
//			});
//			focusMenu.add(focusOnSelectedValues);
//			JMenuItem focusOnRange = new JMenuItem("Focus on " + columnName + " values between " + min + " and " + max);
//			focusOnRange.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					FilterPipeline pipe = new FilterPipeline(new ComparableRangeFilter(modelColumn,
//							(Comparable<Object>) min,
//							(Comparable<Object>) max));
//					table.setFilters(pipe);
//				}
//			});
//			focusMenu.add(focusOnRange);
//		}
//		if (table.getFilters() != null) {
//			JMenuItem removeFocus = new JMenuItem("Remove Focus");
//			removeFocus.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					table.setFilters(null);
//				}
//			});
//			focusMenu.add(removeFocus);
//		}
//	}
//
//
//	public void run() {
//
//            try {
//				UIManager.setLookAndFeel(
//						UIManager.getSystemLookAndFeelClassName());
//			} catch (ClassNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (InstantiationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalAccessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (UnsupportedLookAndFeelException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		final JXTable table = new JXTable();
//		table.setModel(new  RandomTableModel(2000));
//		final JPopupMenu menu = new JPopupMenu();
//		final JMenu highlightMenu = new JMenu("Highlight");
//		menu.add(highlightMenu);
//		final JMenu focusMenu = new JMenu("Focus");
//		menu.add(focusMenu);
//		table.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (SwingUtilities.isRightMouseButton(e)) {
//					int row = table.rowAtPoint(e.getPoint());
//					int viewColumn = table.columnAtPoint(e.getPoint());
//					String columnName = table.getColumnName(viewColumn);
//					final int modelColumn = table.convertColumnIndexToModel(viewColumn);
//					final Object value = table.getValueAt(row, viewColumn);
//					highlightMenu.removeAll();
//					focusMenu.removeAll();
//					int[] rows = table.getSelectedRows();
//					TreeSet<Object> selectedValues = new TreeSet<Object>();
//					for (int i = 0; i < rows.length; i++) {
//						selectedValues.add(table.getValueAt(rows[i], viewColumn));
//					}
//					final Comparable<Object> min = (Comparable<Object>) selectedValues.first();
//					final Comparable<Object> max = (Comparable<Object>) selectedValues.last();
//					buildHighlightMenu(table, highlightMenu, columnName, modelColumn, value, min, max, selectedValues);
//					buildFocusMenu(table, focusMenu, columnName, modelColumn, value, min, max, selectedValues);
//					menu.show(table, e.getPoint().x, e.getPoint().y);
//				}
//			}
//
//		});
//		table.setRowHeightEnabled(true);
//		//table.setDefaultRenderer(Double.class, new BarChartCellRenderer());
//		table.setColumnControlVisible(true);
//		ZoomWheelListener.installZoom(table);
//
//
//		JFrame frame = new JFrame();
//		frame.setLayout(new BorderLayout());
//		JButton reset = new JButton("Reset");
//		reset.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				org.jdesktop.swingx.decorator.Highlighter[] highlighters = table.getHighlighters();
//				for (int i = 0; highlighters != null && i < highlighters.length; i++) {
//					table.removeHighlighter(highlighters[i]);
//				}
//				table.setFilters(null);
//			}
//		});
//		JPanel panel = new JPanel();
//		panel.add(reset);
//		frame.add(panel, BorderLayout.SOUTH);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(new JScrollPane(table));
//		frame.pack();
//		frame.setLocationRelativeTo(JOptionPane.getRootFrame());
//		frame.setVisible(true);
//	}
//}
