package wizard;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


// Quick and dirty UI, for test purposes only.
public class BootGUI
{	final static String[] C1_LIST1 =
	{	"Austria", "Belgium", "Canada", "Denmark", "Finland",
		"France", "Germany", "Greece", "Holland", "Italy", "Japan",
		"Luxembourg", "Norway", "Portugal",	"Republic of Ireland",
		"Spain", "Sweden", "United Kingdon", "United States of America"
	};

	public final static Object[][] COMPONENTS_1 =
	{	{	new JLabel("Your name:"),	0,0,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(15),			1,0,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Company name:"), 2,0,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(15),			3,0,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.BOTH,		0,0 	} ,

		{	new JLabel("Address 1:"),	0,1,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(40),			1,1,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.BOTH,		0,0 	} ,

		{	new JLabel("Address 2:"),	0,2,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(40),			1,2,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.BOTH,		0,0 	} ,

		{	new JLabel("Town/City:"),	0,3,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(15),			1,3,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.BOTH,		0,0 	} ,
		{	new JLabel("Post code:"),	2,3,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JTextField(15),			3,3,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.BOTH,		0,0 	} ,

		{	new JLabel("Country:"),		0,4,2,1,	1d,1d,		GridBagConstraints.CENTER,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Business areas:"), 2,4,-1,1,	1d,1d,		GridBagConstraints.CENTER,GridBagConstraints.NONE,		0,0 	} ,

		{	new JScrollPane(new JList(C1_LIST1)),	0,5,2,4,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,	0,0 	} ,
		{	new JLabel("Education:"),	2,5,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			3,5,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Media:"),		4,5,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			5,5,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Finance:"), 	2,6,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			3,6,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Military;"),	4,6,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			5,6,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Import/export:"), 2,7,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			3,7,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Technology:"),	4,7,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			5,7,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Manufacturing:"), 2,8,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			3,8,1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JLabel("Tourism:"),		4,8,1,1,	1d,1d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			5,8,-1,1,	1d,1d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	}
	};

	public final static Object[][] COMPONENTS_2 =
	{	{	new JLabel("Choose installation directory:"),	0,0,1,1,	1d,1d,		GridBagConstraints.CENTER,GridBagConstraints.BOTH,		0,0 	} ,
		{	new JFileChooser(),			0,1,1,1,	1d,1d,		GridBagConstraints.CENTER,GridBagConstraints.BOTH,		0,0 	}
	};

	public final static Object[][] COMPONENTS_3 =
	{	{	new JLabel("Which components should be installed?"),
										0,0,2,1,	0d,0.5d,	GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,		0,0 	} ,

		{	new JLabel("Application:"),	0,1,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,1,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Examples:"),	0,2,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,2,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Plugins:"),		0,3,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,3,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Documentation:"), 0,4,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,4,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	}
	};

	public final static Object[][] COMPONENTS_4 =
	{	{	new JLabel("Desktop integration:"),
										0,0,2,1,	0d,0.5d,	GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,		0,0 	} ,

		{	new JLabel("Desktop icon:"), 0,1,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,1,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Start menu:"),	0,2,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,2,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Tray icon:"),	0,3,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,3,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Show readme:"), 0,4,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,4,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel("Shark repellent spray:"), 0,5,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JCheckBox(),			1,5,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	} ,

		{	new JLabel(), 				0,6,2,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JButton("Abort"), 		0,7,1,1,	0d,0d,		GridBagConstraints.EAST,GridBagConstraints.NONE,		0,0 	} ,
		{	new JButton("Install"),		1,7,-1,1,	0d,0d,		GridBagConstraints.WEST,GridBagConstraints.NONE,		0,0 	}
	};


	public static JPanel _panel1(Object[][] c)
	{	GridBagLayout gbl = new GridBagLayout();
		JPanel pan = new JPanel(gbl);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4,5,4,5);
		for(int i=0;i<c.length;i++)
		{	gbc.gridx=(Integer)c[i][1];  gbc.gridy=(Integer)c[i][2];
			gbc.gridwidth=(Integer)c[i][3];  if(gbc.gridwidth==-1)  gbc.gridwidth=GridBagConstraints.REMAINDER;
			gbc.gridheight=(Integer)c[i][4];
			gbc.weightx=(Double)c[i][5];  gbc.weighty=(Double)c[i][6];
			gbc.anchor=(Integer)c[i][7];  gbc.fill=(Integer)c[i][8];
			gbc.ipadx=(Integer)c[i][9];  gbc.ipady=(Integer)c[i][10];
			gbl.setConstraints((JComponent)c[i][0],gbc);
			pan.add((JComponent)c[i][0]);
		}

		JPanel pan2 = new JPanel(new BorderLayout());
		pan2.setOpaque(true);
		pan2.add(pan,BorderLayout.NORTH);
		pan2.add(new JLabel(),BorderLayout.CENTER);
		return pan2;
	}
}
