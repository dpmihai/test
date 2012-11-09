package animation;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AlphaTest extends JPanel implements ActionListener {

    private static final Font FONT = new Font("Serif", Font.PLAIN, 32);
    private static final String STRING = "Mothra alert!";
    private static final float DELTA = -0.1f;
    private static final Timer timer = new Timer(100, null);
    private float alpha = 1f;

    AlphaTest() {
        this.setPreferredSize(new Dimension(256, 96));
        this.setOpaque(true);
        this.setBackground(Color.black);
        timer.setInitialDelay(1000);
        timer.addActionListener(this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(FONT);
        int xx = this.getWidth();
        int yy = this.getHeight();
        int w2 = g.getFontMetrics().stringWidth(STRING) / 2;
        int h2 = g.getFontMetrics().getDescent();
        g2d.fillRect(0, 0, xx, yy);
        g2d.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_IN, alpha));
        g2d.setPaint(Color.red);
        g2d.drawString(STRING, xx / 2 - w2, yy / 2 + h2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        alpha += DELTA;
        if (alpha < 0) {
            alpha = 1;
            timer.restart();
        }
        repaint();
    }

    static public void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame f = new JFrame();
                f.setLayout(new GridLayout(0, 1));
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new AlphaTest());
                f.add(new AlphaTest());
                f.add(new AlphaTest());
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
