package function;

import java.awt.*;
import javax.swing.*;

public class Draw extends JFrame {

    public static final int width = 400;
    public static final int height = 300;

    public Draw() {
        Function func = new TestFunction();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new DrawFunction(width, height, func), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Draw frame = new Draw();
        frame.setSize(width, height);
        frame.setTitle("Function Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    class DrawFunction extends JPanel {

        private int width;
        private int height;
        private int gap = 10;
        private Function func;

        public DrawFunction(int width, int height, Function func) {
            super();
            this.width = width - 2*gap;
            this.height = height - 4*gap;
            this.func = func;
        }

        // for resizing
        public void repaint() {
            super.repaint();
            width = this.getWidth() - 2*gap;
            height = this.getHeight() - 2*gap;
        }

        // Draw the function – will force the screen to repaint
        // if ever called. a call to repaint() will result in system
        // call to paintComponent(..)
        public void drawFunction() {
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawLine(gap, height / 2 , width, height / 2 );    // x axis
            g.drawLine(width / 2 , gap, width / 2 , height);     // y axis

            // x arrow
            g.drawLine(width , height / 2, width - 20, height / 2 - 5);
            g.drawLine(width , height / 2, width - 20, height / 2 + 5);
            // y arrow
            g.drawLine(width / 2, gap, width / 2 - 5, gap + 20);
            g.drawLine(width / 2, gap, width / 2 + 5, gap + 20);


            g.drawString("X", width - 10, height / 2 - 10);
            g.drawString("Y", width / 2 + 10, 20);

            // Draw function
            Polygon p = new Polygon();

            //double scaleFactor = 0.01;

            // add only points which are visible
            for (int x=-width/2+gap; x <= width/2-gap; x++) {
                double y = func.f(x);
                if ( (2*y <= height-2*gap) && (2*y>=-height+2*gap) ) {
                    p.addPoint(x+width / 2, height / 2 - (int)y);
                }
            }

            g.drawPolyline(p.xpoints, p.ypoints, p.npoints);
        }
    }
 }