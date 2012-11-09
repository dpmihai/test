package progressbar;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 6, 2006
 * Time: 10:20:59 AM
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class GradientProgressBarUI extends BasicProgressBarUI {
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        // for antialiasing geometric shapes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // for antialiasing text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // to go for quality over speed
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paint(g, c);
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        if (progressBar.getOrientation() == JProgressBar.VERTICAL) {
            super.paintDeterminate(g, c);
            return;
        }
        Insets b = progressBar.getInsets(); // area for border
        int width = progressBar.getWidth();
        int height = progressBar.getHeight();
        int barRectWidth = width - (b.right + b.left);
        int barRectHeight = height - (b.top + b.bottom);
        int arcSize = height / 2 - 1;
        // amount of progress to draw
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(progressBar.getBackground());
        g2.fillRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);
        //g2.setColor(Color.BLACK);
        //g2.drawRoundRect(0, 0, width - 1, height - 1, arcSize, arcSize);

        // Set the gradient fill
        Color color = progressBar.getForeground();
        GradientPaint gradient = new GradientPaint(width / 2, 0, Color.WHITE, width / 2, height / 4, color, false);
        g2.setPaint(gradient);
        g2.fillRoundRect(b.left, b.top, amountFull - 1, barRectHeight - 1, arcSize, arcSize);

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            if (dim.width < dim.height * 4) {
                dim.width = dim.height * 4;
            }
        }
        return dim;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.getDefaults().put("ProgressBar.selectionForeground",
                    Color.white);
            UIManager.getDefaults().put("ProgressBar.selectionBackground",
                    Color.white);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        final JProgressBar bar = new JProgressBar();
        bar.setValue(0);
        bar.setUI(new GradientProgressBarUI());
        bar.setStringPainted(true);
        bar.setFont(new Font("Serif", Font.PLAIN, 18).deriveFont(Font.BOLD));
        bar.setOpaque(false);
        bar.setBorderPainted(false);
        bar.setBackground(Color.LIGHT_GRAY);
        bar.setForeground(Color.GRAY);

        frame.getContentPane().add(bar, constraints);

        JButton minusButton = new JButton(" - ");
        minusButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                bar.setValue(bar.getValue() - 10);
            }
        });
        frame.getContentPane().add(minusButton, constraints);

        JButton plusButton = new JButton(" + ");
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                bar.setValue(bar.getValue() + 10);
            }
        });
        frame.getContentPane().add(plusButton, constraints);

        frame.pack();
        frame.setVisible(true);
    }

}
