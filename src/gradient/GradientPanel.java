package gradient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GradientPanel extends JPanel {

   private Color color1;
   private Color color2;

   public GradientPanel() {
      this(Color.blue, Color.green);
   }

   public GradientPanel(Color c1, Color c2) {
      super();
      this.color1 = c1;
      this.color2 = c2;
   }

   public void setColor1(Color c1) {
      this.color1 = c1;
      repaint();
   }

   public void setColor2(Color c2) {
      this.color2 = c2;
      repaint();
   }

   // Overloaded in order to paint the background
   protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;

      int w = getWidth();
      int h = getHeight();

      GradientPaint gradient = new  GradientPaint(0, 0, color1, w, h, color2, true);
      g2.setPaint(gradient);
      g2.fillRect(0, 0, w, h);
   }

   public static void main(String[] args) {

      final GradientPanel pGradient = new GradientPanel();

      Color[] colors = { createColor("Black",  Color.black),
                         createColor("Blue",   Color.blue),
                         createColor("Green",  Color.green),
                         createColor("yellow", Color.yellow),
                         createColor("orange", Color.orange),
                         createColor("red",    Color.red),
                         createColor("white",  Color.white),
                         createColor("lightgray",  Color.lightGray)};

      JComboBox c1 = new JComboBox(colors);
      c1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox)e.getSource();
            Color c = (Color)combo.getSelectedItem();
            pGradient.setColor1(c);
         }
      });

      JComboBox c2 = new JComboBox(colors);
      c2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox)e.getSource();
            Color c = (Color)combo.getSelectedItem();
            pGradient.setColor2(c);
         }
      });

      JPanel pColors = new JPanel(new GridLayout(0,2));
      pColors.add(c1);
      pColors.add(c2);

      pGradient.add(new JButton("A button"));
      pGradient.add(new JTextField("A text field"));

      c1.setSelectedItem(colors[6]);
      c2.setSelectedItem(colors[7]);

      JFrame f = new JFrame("Gradient test");
      f.setSize(300, 200);
      f.getContentPane().add(pColors, BorderLayout.NORTH);
      f.getContentPane().add(pGradient, BorderLayout.CENTER);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setVisible(true);
   }

   private static Color createColor(String name, Color c) {

      final String colorname = name;
      Color color = new Color( c.getRed(),
                               c.getGreen(),
                               c.getBlue() ) {
         private String name = colorname;
         public String toString() {
            return name;
         }
      };
      return color;
   }
}
