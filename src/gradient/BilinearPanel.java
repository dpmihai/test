package gradient;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class BilinearPanel extends javax.swing.JPanel  {
	
	private int width;
	private int height;		
	
    public BilinearPanel(int width, int height) {
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
          
        final Color UPPER_LEFT = new Color(1.0f, 0.0f, 0.0f, 1.0f);  
        final Color UPPER_RIGHT = new Color(1.0f, 1.0f, 0.0f, 1.0f);  
        final Color LOWER_LEFT = new Color(0.0f, 0.0f, 1.0f, 1.0f);  
        final Color LOWER_RIGHT = new Color(0.0f, 1.0f, 1.0f, 1.0f);  
          
        final Rectangle RECT = new Rectangle(0, 0, width, height);  
          
        BiLinearGradientPaint BILINEAR_GRADIENT = new BiLinearGradientPaint(RECT, UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT);  
          
        G2.setPaint(BILINEAR_GRADIENT);  
        G2.fill(RECT);  
          
        G2.dispose();  
    }  
    
    public static void main(String[] args) {
		showFrame(new BilinearPanel(300,300));
	}
    
    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(400, 400);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }
}  