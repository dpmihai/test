package table;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 5, 2009
 * Time: 12:08:27 PM
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

/**
 * @author Elie Levy Jan 04, 2009
 * GPL License (http://www.gnu.org/copyleft/gpl.html)
 * http://weblogs.java.net/blog/elevy/archive/2009/01/freezable_jtabl.html
 */
public class FreezableTableHeader extends JTableHeader {

	private FreezableActionListener mouseListener;
	private Point point;
	private JMenuItem menuItem;
	private JPopupMenu popupMenu;
	private final String FREEZE_COLUMN = "Freeze Column";
	private final String UNFREEZE_COLUMN = "Unfreeze Column";

	public FreezableTableHeader(JTable table, JScrollPane scrollPane) {
		super(table.getTableHeader().getColumnModel());
		mouseListener = new FreezableActionListener(table, scrollPane);
		table.setTableHeader(this);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		popupMenu = new JPopupMenu();
		menuItem = new JMenuItem(FREEZE_COLUMN);
		menuItem.addActionListener(mouseListener);
		popupMenu.add(menuItem);
		table.addMouseListener(new PopupListener());
		table.setFillsViewportHeight(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int division = mouseListener.getDivision();
		if (division > 0) {
			Rectangle r = getVisibleRect();
			BufferedImage image = new BufferedImage(division, r.height,
					BufferedImage.TYPE_INT_ARGB);
			Graphics g2 = image.getGraphics();
			g2.setClip(0, 0, division, r.height);
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, division, r.height);
			super.paint(g2);
			g.drawImage(image, r.x, r.y, division, r.height, null);
			g2.dispose();
		}
	}
	private class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				point = e.getPoint();
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	private class FreezableActionListener implements ActionListener {
		private int division;

		private JScrollPane scrollPane;

		private JTable table;

		private boolean added;

		private int col = -1;

		public FreezableActionListener(JTable table, JScrollPane scrollPane) {
			this.table = table;
			this.scrollPane = scrollPane;
		}

		private JLabel frozenColumns = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {
				Rectangle r = table.getBounds();
				if (division > 0) {
					table.invalidate();
					table.validate();
					Rectangle visibleRect = table.getVisibleRect();
					BufferedImage image = new BufferedImage(division, r.height,
							BufferedImage.TYPE_INT_ARGB);
					Graphics g2 = image.getGraphics();
					g2.setClip(0, visibleRect.y, division,
							table.getBounds().height);
					g2.setColor(Color.WHITE);
					g2.fillRect(0, 0, division, table.getBounds().height);
					table.paint(g2);
					g.drawImage(image, 0, 0, division,
							table.getBounds().height, 0, visibleRect.y,
							division, visibleRect.y + table.getBounds().height,
							null);
					g.setColor(Color.BLACK);
					for (int i = 0; i < visibleRect.y
							+ table.getBounds().height; i += 8) {
						g.drawLine(division - 1, i, division - 1, i + 4);
						g.drawLine(division - 2, i, division - 2, i + 4);
					}
					g2.dispose();
				}
			}
		};

		public int getDivision() {
			return division;
		}

		public void freeze(Point p) {
			col = table.columnAtPoint(p);
			JLayeredPane pane = table.getRootPane().getLayeredPane();
			if (added) {
				pane.remove(frozenColumns);
			} else {
				scrollPane.addComponentListener(new ComponentListener() {

					public void componentHidden(ComponentEvent arg0) {
						// TODO Auto-generated method stub

					}

					public void componentMoved(ComponentEvent arg0) {
						// TODO Auto-generated method stub

					}

					public void componentResized(ComponentEvent arg0) {
						setBoundsOnFrozenColumns();

					}

					public void componentShown(ComponentEvent arg0) {
						// TODO Auto-generated method stub

					}

				});
			}
			pane.add(frozenColumns, JLayeredPane.POPUP_LAYER);
			setBoundsOnFrozenColumns();
			added = true;
			frozenColumns.setVisible(true);
		}

		public void setBoundsOnFrozenColumns() {
			if (col >= 0) {
				division = table.getCellRect(1, col, true).x
						+ table.getCellRect(1, col, true).width;
				int limit = scrollPane.getBounds().width
						- scrollPane.getVerticalScrollBar().getBounds().width
						- 2;
				division = Math.min(division, limit);
				JLayeredPane pane = table.getRootPane().getLayeredPane();
				Point p = scrollPane.getLocationOnScreen();
				SwingUtilities.convertPointFromScreen(p, pane);
				Rectangle scrollPaneBounds = scrollPane.getBounds();
				int headerHeight = table.getTableHeader().getBounds().height + 1;
				int hScrollHeight = (scrollPane.getHorizontalScrollBar()
						.isVisible()) ? scrollPane.getHorizontalScrollBar()
						.getBounds().height : 0;
				frozenColumns.setBounds(p.x + 1, p.y + headerHeight, division,
						scrollPaneBounds.height - headerHeight - hScrollHeight
								- 2);
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			if ((division<=0)&&(point !=null)) {
				freeze(point);
				menuItem.setText(UNFREEZE_COLUMN);
			} else {
				frozenColumns.setVisible(false);
				division = -1;
				col = -1;
				menuItem.setText(FREEZE_COLUMN);
			}
		}
	}

	public static String columnNames[] = { "Customer Name", "City",
			"Payment Amount", "Date", "Item", "Quantity",
			"Related", "Price", "Method", "Campaign", "Affiliate" };

	public static String customers[] = { "Stores", "Exxon", "Chevron",
			"General", "ConocoPhillips", "General", "Ford", "Citigroup",
			"Bank", "AT&T", "Berkshire", "J.P.", "American", "Hewlett-Packard",
			"International", "Valero", "Verizon", "McKesson", "Cardinal",
			"Goldman", "Morgan", "Home", "Procter", "CVS", "UnitedHealth",
			"Kroger", "Boeing", "AmerisourceBergen", "Costco", "Merrill",
			"Target", "State", "WellPoint", "Dell", "Johnson", "Marathon",
			"Lehman", "Wachovia", "United", "Walgreen", "Wells", "Dow",
			"MetLife", "Microsoft", "Sears", "United", "Pfizer", "Lowe's",
			"Time", "Caterpillar", "Medco", "Archer", "Fannie", "Freddie",
			"Safeway", "Sunoco", "Lockheed", "Sprint", "PepsiCo", "Intel",
			"Altria", "Supervalu", "Kraft", "Allstate", "Motorola", "Best",
			"Walt", "FedEx", "Ingram", "Sysco", "Cisco", "Johnson",
			"Honeywell", "Prudential", "American", "Northrop", "Hess", "GMAC",
			"Comcast", "Alcoa", "DuPont", "New", "Coca-Cola", "News", "Aetna",
			"TIAA-CREF", "General", "Tyson", "HCA", "Enterprise", "Macy's",
			"Delphi", "Travelers", "Liberty", "Hartford", "Abbott",
			"Washington", "Humana", "Massachusetts", "3M" };

	public static String[] cities = { "Alaska", "Arizona ", "Arkansas ",
			"California ", "Colorado ", "Connecticut ", "Delaware ",
			"District of", "Florida ", "Georgia ", "Hawaii ", "Idaho ",
			"Illinois ", "Indiana ", "Iowa ", "Kansas ", "Kentucky ",
			"Louisiana ", "Maine ", "Maryland ", "Massachusetts ", "Michigan ",
			"Minnesota ", "Mississippi ", "Missouri ", "Montana ", "Nebraska ",
			"Nevada ", "New Hampshire", "New Jersey", "New Mexico", "New York",
			"North Carolina", "North Dakota", "Ohio ", "Oklahoma ", "Oregon ",
			"Pennsylvania ", "Rhode Island", "South Carolina", "South Dakota",
			"Tennessee ", "Texas ", "Utah ", "Vermont ", "Virginia ",
			"Washington ", "West Virginia", "Wisconsin ", "Wyoming " };

	public static void main(String arg[]) throws Exception {
		int rows = customers.length;
		int columns = columnNames.length;
		Object data[][] = new Object[rows][columns];
		for (int i = 0; i < rows; ++i) {
			data[i][0] = customers[i];
		}
		for (int i = 0; i < rows; ++i) {
			data[i][1] = cities[i % cities.length];
		}
		for (int i = 0; i < rows; ++i) {
			data[i][2] = new BigDecimal(Math.random() * 10000);
			data[i][2] = ((BigDecimal) data[i][2]).setScale(2,
					BigDecimal.ROUND_CEILING);
		}
		for (int i = 3; i < columns; ++i) {
			for (int x = 0; x < rows; ++x) {
				data[x][i] = "element:" + x + "," + i;
			}
		}
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		FreezableTableHeader tableHeader = new FreezableTableHeader(table,
				scrollPane);
		JFrame frame = new JFrame("Test");
		frame.add("Center", scrollPane);
		frame.setSize(600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
