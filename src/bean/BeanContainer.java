/** 
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */
package bean;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;



public class BeanContainer extends JFrame implements FocusListener
{
	protected File m_currentDir;
	protected Component m_activeBean;
	protected String m_className = "bean.Clock";
	protected JFileChooser m_chooser = new JFileChooser();
	protected Hashtable m_editors = new Hashtable();

	public BeanContainer() {
		super("Simple Bean Container");
		getContentPane().setLayout(new FlowLayout());

		setSize(300, 300);

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		try {
			m_currentDir = (new File(".")).getCanonicalFile();
		} catch (IOException ex) {}
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mFile = new JMenu("File");

		JMenuItem mItem = new JMenuItem("New...");
		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread newthread = new Thread() {
					public void run() {
						String result = (String)JOptionPane.showInputDialog(
							BeanContainer.this,
							"Please enter class name to create a new bean",
							"Input", JOptionPane.INFORMATION_MESSAGE, null,
							null, m_className);
						repaint();
						if (result==null)
							return;
						try {
							m_className = result;
							Class cls = Class.forName(result);
							Object obj = cls.newInstance();
							if (obj instanceof Component) {
								m_activeBean = (Component)obj;
								m_activeBean.addFocusListener(
									BeanContainer.this);
								m_activeBean.requestFocus();
								getContentPane().add(m_activeBean);
							}
							validate();
						}
						catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(
								BeanContainer.this, "Error: "+ex.toString(),
								"Warning", JOptionPane.WARNING_MESSAGE);
						}
					}
				};
				newthread.start();
			}
		};
		mItem.addActionListener(lst);
		mFile.add(mItem);

		mItem = new JMenuItem("Load...");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread newthread = new Thread() {
					public void run() {
						m_chooser.setCurrentDirectory(m_currentDir);
						m_chooser.setDialogTitle(
							"Please select file with serialized bean");
						int result = m_chooser.showOpenDialog(
							BeanContainer.this);
						repaint();
						if (result != JFileChooser.APPROVE_OPTION)
							return;
						m_currentDir = m_chooser.getCurrentDirectory();
						File fChoosen = m_chooser.getSelectedFile();
						try {
							FileInputStream fStream =
								new FileInputStream(fChoosen);
							ObjectInput	stream	=
								new ObjectInputStream(fStream);
							Object obj = stream.readObject();
							if (obj instanceof Component) {
								m_activeBean = (Component)obj;
								m_activeBean.addFocusListener(
									BeanContainer.this);
								m_activeBean.requestFocus();
								getContentPane().add(m_activeBean);
							}
							stream.close();
							fStream.close();
							validate();
						}
						catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(
								BeanContainer.this, "Error: "+ex.toString(),
								"Warning", JOptionPane.WARNING_MESSAGE);
						}
						repaint();
					}
				};
				newthread.start();
			}
		};
		mItem.addActionListener(lst);
		mFile.add(mItem);

		mItem = new JMenuItem("Save...");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread newthread = new Thread() {
					public void run() {
						if (m_activeBean == null)
							return;
						m_chooser.setDialogTitle(
							"Please choose file to serialize bean");
						m_chooser.setCurrentDirectory(m_currentDir);
						int result = m_chooser.showSaveDialog(
							BeanContainer.this);
						repaint();
						if (result != JFileChooser.APPROVE_OPTION)
							return;
						m_currentDir = m_chooser.getCurrentDirectory();
						File fChoosen = m_chooser.getSelectedFile();
						try {
							FileOutputStream fStream =
								new FileOutputStream(fChoosen);
							ObjectOutput stream	=
								new ObjectOutputStream(fStream);
							stream.writeObject(m_activeBean);
							stream.close();
							fStream.close();
						}
						catch (Exception ex) {
							ex.printStackTrace();
						JOptionPane.showMessageDialog(
							BeanContainer.this, "Error: "+ex.toString(),
							"Warning", JOptionPane.WARNING_MESSAGE);
						}
					}
				};
				newthread.start();
			}
		};
		mItem.addActionListener(lst);
		mFile.add(mItem);

		mFile.addSeparator();

		mItem = new JMenuItem("Exit");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		mItem.addActionListener(lst);
		mFile.add(mItem);
		menuBar.add(mFile);

		JMenu mEdit = new JMenu("Edit");

		mItem = new JMenuItem("Delete");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_activeBean == null)
					return;
				Object obj = m_editors.get(m_activeBean);
				if (obj != null) {
					BeanEditor editor = (BeanEditor)obj;
					editor.dispose();
					m_editors.remove(m_activeBean);
				}
				getContentPane().remove(m_activeBean);
				m_activeBean = null;
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		mEdit.add(mItem);

		mItem = new JMenuItem("Properties...");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_activeBean == null)
					return;
				Object obj = m_editors.get(m_activeBean);
				if (obj != null) {
					BeanEditor editor = (BeanEditor)obj;
					editor.setVisible(true);
					editor.toFront();
				}
				else {
					BeanEditor editor = new BeanEditor(m_activeBean);
					m_editors.put(m_activeBean, editor);
				}
			}
		};
		mItem.addActionListener(lst);
		mEdit.add(mItem);
		menuBar.add(mEdit);

		JMenu mLayout = new JMenu("Layout");
		ButtonGroup group = new ButtonGroup();

		mItem = new JRadioButtonMenuItem("FlowLayout");
		mItem.setSelected(true);
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				getContentPane().setLayout(new FlowLayout());
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		group.add(mItem);
		mLayout.add(mItem);

		mItem = new JRadioButtonMenuItem("GridLayout");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				int col = 3;
				int row = (int)Math.ceil(getContentPane().
					getComponentCount()/(double)col);
				getContentPane().setLayout(new GridLayout(row, col, 10, 10));
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		group.add(mItem);
		mLayout.add(mItem);

		mItem = new JRadioButtonMenuItem("BoxLayout - X");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getContentPane().setLayout(new BoxLayout(
					getContentPane(), BoxLayout.X_AXIS));
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		group.add(mItem);
		mLayout.add(mItem);

		mItem = new JRadioButtonMenuItem("BoxLayout - Y");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getContentPane().setLayout(new BoxLayout(
					getContentPane(), BoxLayout.Y_AXIS));
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		group.add(mItem);
		mLayout.add(mItem);

		mItem = new JRadioButtonMenuItem("DialogLayout");
		lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getContentPane().setLayout(new DialogLayout());
				validate();
				repaint();
			}
		};
		mItem.addActionListener(lst);
		group.add(mItem);
		mLayout.add(mItem);

		menuBar.add(mLayout);

		return menuBar;
	}

	public void focusGained(FocusEvent e) {
		m_activeBean = e.getComponent();
		repaint();
	}

	public void focusLost(FocusEvent e) {}

	// This is a heavyweight component so we override paint
	// instead of paintComponent. super.paint(g) will
	// paint all child components first, and then we
	// simply draw over top of them.
	public void paint(Graphics g) {
		super.paint(g);

		if (m_activeBean == null)
			return;

		Point pt = getLocationOnScreen();
		Point pt1 = m_activeBean.getLocationOnScreen();
		int x = pt1.x - pt.x - 2;
		int y = pt1.y - pt.y - 2;
		int w = m_activeBean.getWidth() + 2;
		int h = m_activeBean.getHeight() + 2;

		g.setColor(Color.black);
		g.drawRect(x, y, w, h);
	}

	public static void main(String argv[]) {
		BeanContainer frame = new BeanContainer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class BeanEditor extends JFrame implements PropertyChangeListener
{
	protected Component m_bean;
	protected JTable m_table;
	protected PropertyTableData m_data;

	public BeanEditor(Component bean) {
		m_bean = bean;
		m_bean.addPropertyChangeListener(this);

		Point pt = m_bean.getLocationOnScreen();
		setBounds(pt.x+50, pt.y+10, 400, 300);
		getContentPane().setLayout(new BorderLayout());

		m_data = new PropertyTableData(m_bean);
		m_table = new JTable(m_data);

		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(m_table);
		getContentPane().add(ps, BorderLayout.CENTER);

		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		m_data.setProperty(evt.getPropertyName(), evt.getNewValue());
	}

	class PropertyTableData extends AbstractTableModel
	{
		protected String[][] m_properties;
		protected int m_numProps = 0;
		protected Vector m_v;

		public PropertyTableData(Component bean) {
			try {
				BeanInfo info = Introspector.getBeanInfo(
					m_bean.getClass());
				BeanDescriptor descr = info.getBeanDescriptor();
				setTitle("Editing "+descr.getName());
				PropertyDescriptor[] props = info.getPropertyDescriptors();
				m_numProps = props.length;

				m_v = new Vector(m_numProps);
				for (int k=0; k<m_numProps; k++) {
					String name = props[k].getDisplayName();
					boolean added = false;
					for (int i=0; i<m_v.size(); i++) {
						String str = ((PropertyDescriptor)m_v.elementAt(i)).
							getDisplayName();
						if (name.compareToIgnoreCase(str) < 0) {
							m_v.insertElementAt(props[k], i);
							added = true;
							break;
						}
					}
					if (!added)
						m_v.addElement(props[k]);
				}

				m_properties = new String[m_numProps][2];
				for (int k=0; k<m_numProps; k++) {
					PropertyDescriptor prop =
						(PropertyDescriptor)m_v.elementAt(k);
					m_properties[k][0] = prop.getDisplayName();
					Method mRead = prop.getReadMethod();
					if (mRead != null &&
					 mRead.getParameterTypes().length == 0) {
						Object value = mRead.invoke(m_bean, null);
						m_properties[k][1] = objToString(value);
					}
					else
						m_properties[k][1] = "error";
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
					BeanEditor.this, "Error: "+ex.toString(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			}
		}

		public void setProperty(String name, Object value) {
			for (int k=0; k<m_numProps; k++)
				if (name.equals(m_properties[k][0])) {
					m_properties[k][1] = objToString(value);
					m_table.tableChanged(new TableModelEvent(this, k));
					m_table.repaint();
					break;
				}
		}

		public int getRowCount() { return m_numProps; }

		public int getColumnCount() { return 2; }

		public String getColumnName(int nCol) {
			return nCol==0 ? "Property" : "Value";
		}

		public boolean isCellEditable(int nRow, int nCol) {
			 return (nCol==1);
		}

		public Object getValueAt(int nRow, int nCol) {
			if (nRow < 0 || nRow>=getRowCount())
				return "";
			switch (nCol) {
				case 0: return m_properties[nRow][0];
				case 1: return m_properties[nRow][1];
			}
			return "";
		}

		public void setValueAt(Object value, int nRow, int nCol) {
			if (nRow < 0 || nRow>=getRowCount())
				return;
			String str = value.toString();
			PropertyDescriptor prop = (PropertyDescriptor)m_v.
				elementAt(nRow);
			Class cls = prop.getPropertyType();
			Object obj = stringToObj(str, cls);
			if (obj==null)
				return;				// can't process

			Method mWrite = prop.getWriteMethod();
			if (mWrite == null || mWrite.getParameterTypes().length != 1)
				return;
			try {
				mWrite.invoke(m_bean, new Object[]{ obj });
				m_bean.getParent().doLayout();
				m_bean.getParent().repaint();
				m_bean.repaint();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
					BeanEditor.this, "Error: "+ex.toString(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			}
			m_properties[nRow][1] = str;
		}

		public String objToString(Object value) {
			if (value==null)
				return "null";
			if (value instanceof Dimension) {
				Dimension dim = (Dimension)value;
				return ""+dim.width+","+dim.height;
			}
			else if (value instanceof Insets) {
				Insets ins = (Insets)value;
				return ""+ins.left+","+ins.top+","+ins.right+","+ins.bottom;
			}
			else if (value instanceof Rectangle) {
				Rectangle rc = (Rectangle)value;
				return ""+rc.x+","+rc.y+","+rc.width+","+rc.height;
			}
			else if (value instanceof Color) {
				Color col = (Color)value;
				return ""+col.getRed()+","+col.getGreen()+","+col.getBlue();
			}
			return value.toString();
		}

		public Object stringToObj(String str, Class cls) {
			try {
				if (str==null)
					return null;
				String name = cls.getName();
				if (name.equals("java.lang.String"))
					return str;
				else if (name.equals("int"))
					return new Integer(str);
				else if (name.equals("long"))
					return new Long(str);
				else if (name.equals("float"))
					return new Float(str);
				else if (name.equals("double"))
					return new Double(str);
				else if (name.equals("boolean"))
					return new Boolean(str);
				else if (name.equals("java.awt.Dimension")) {
					int[] i = strToInts(str);
					return new Dimension(i[0], i[1]);
				}
				else if (name.equals("java.awt.Insets")) {
					int[] i = strToInts(str);
					return new Insets(i[0], i[1], i[2], i[3]);
				}
				else if (name.equals("java.awt.Rectangle")) {
					int[] i = strToInts(str);
					return new Rectangle(i[0], i[1], i[2], i[3]);
				}
				else if (name.equals("java.awt.Color")) {
					int[] i = strToInts(str);
					return new Color(i[0], i[1], i[2]);
				}
				return null;		// not supported
			}
			catch(Exception ex) { return null; }
		}

		public int[] strToInts(String str) throws Exception {
			int[] i = new int[4];
			StringTokenizer tokenizer = new StringTokenizer(str, ",");
			for (int k=0; k<i.length &&
			 tokenizer.hasMoreTokens(); k++)
				i[k] = Integer.parseInt(tokenizer.nextToken());
			return i;
		}
	}
}

