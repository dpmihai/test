package gradient;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ConicalPanel extends javax.swing.JPanel  {
	
	private int width;
	private int height;		
	
    public ConicalPanel(int width, int height) {
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
          
        final Rectangle RECT = new Rectangle(0, 0, width, height);  
          
        ConicalGradientPaint CONICAL_GRADIENT = new ConicalGradientPaint(new Point2D.Float(width/2, height/2), 
        		new float[] {0.5f}, new Color[] {Color.RED});  
          
        G2.setPaint(CONICAL_GRADIENT);  
        G2.fill(RECT);  
          
        G2.dispose();  
    }  
    
    public static void main(String[] args) {
		showFrame(new ConicalPanel(300,300));
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