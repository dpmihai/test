package wizard;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.synth.SynthLookAndFeel;

import wizard.jwiz.JWizardPane;


public class Boot extends JFrame
{	private JWizardPane wiz;

	private final static String[] LABELS =
	{	"Company details", "Install directory", "Components", "Install"
	};


	public Boot()
	{	super("JWizardPane demo");
		try
		{	Runnable r = new Runnable()
			{	public void run() { _initGUI(); }
			};
			SwingUtilities.invokeAndWait(r);
		}catch(Exception e) { e.printStackTrace(); }
	}

	private void _initGUI()
	{
//        if(JWizardPane.BLACK)
//		{	try
//			{	SynthLookAndFeel synth = new SynthLookAndFeel();
//				synth.load(Boot.class.getResourceAsStream("demo.xml"), Boot.class);
//				UIManager.setLookAndFeel(synth);
//			}catch(Exception e) { e.printStackTrace(); }
//		}

		JPanel[] pans =  new JPanel[4];
		pans[0] = BootGUI._panel1(BootGUI.COMPONENTS_1);
		pans[1] = BootGUI._panel1(BootGUI.COMPONENTS_2);
		pans[2] = BootGUI._panel1(BootGUI.COMPONENTS_3);
		pans[3] = BootGUI._panel1(BootGUI.COMPONENTS_4);
		/*for(int i=0;i<pans.length;i++)
		{	JPanel p1 = new JPanel(new BorderLayout(5,5));  p1.setOpaque(true);
			JPanel p2 = new JPanel(new GridLayout(0,1 , 5,5));
			JPanel p3 = new JPanel(new GridLayout(0,1 , 5,5));
			for(int j=0;j<8;j++)
			{	p2.add(new JLabel("Label "+(i+1)+":"+(j+1)));
				p3.add(new JTextField(40));
			}
			p1.add(p2,BorderLayout.WEST);
			p1.add(p3,BorderLayout.CENTER);
			p1.setBorder(new EmptyBorder(10,10,10,10));
			pans[i]=p1;
		}*/

		// Build the UI
		wiz = new JWizardPane(LABELS,pans);
		JPanel mainP = new JPanel();
		mainP.setOpaque(true);
		mainP.add(wiz);
		mainP.setBorder(new EmptyBorder(5,5,5,5));
		getContentPane().add(mainP);
		// Nice desktop functionality
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Blat it on screen
		pack();  setVisible(true);
	}


	public static void main(String[] args)
	{	/*java.util.Hashtable ht = javax.swing.UIManager.getLookAndFeelDefaults();
		for(java.util.Iterator it=ht.keySet().iterator() ; it.hasNext() ;)
		{	String k = (String)it.next();
			//Object v = ht.get(k);
			System.out.println(k);
		}*/

		Boot b = new Boot();
	}
}
