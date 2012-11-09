package gradient;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ContourPanel extends javax.swing.JPanel  {
	
	private int width;
	private int height;		
	
    public ContourPanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	@Override  
    protected void paintComponent(java.awt.Graphics g)  
    {  
        super.paintComponent(g);  
          
        final Graphics2D G2 = (Graphics2D) g.create();  
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);                      
          
        final Rectangle2D RECT = new Rectangle2D.Float(20, 20, width-40, height-40);  
          
        ContourGradientPaint CONTOUR_GRADIENT = new ContourGradientPaint(RECT, 
        		new float[] {45, 45, 45}, new Color[] {Color.RED, Color.BLUE, Color.GREEN});  
          
        G2.setPaint(CONTOUR_GRADIENT);  
        G2.fill(RECT);  
          
        G2.dispose();  
    }  
    
    public static void main(String[] args) {
		showFrame(new ContourPanel(400,400));
	}
    
    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 350);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}  