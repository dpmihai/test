package zoom;

/**
 * User: mihai.panaitescu
 * Date: 16-Aug-2010
 * Time: 14:17:55
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

/** @author John B. Matthews; distribution per GNU Public License */
public class ScaledPanel extends JPanel {

    private static final int SIZE = 8; // 8x8 board
    private static final int DIAM = SIZE * 10; // checker size
    private static final int maxX = SIZE * DIAM;
    private static final int maxY = SIZE * DIAM;
    private static final String glyph = "\u2748";
    private static final Color white = new Color(0xF0F0C0);
    private static final Color light = new Color(0x40C040);
    private static final Color dark = new Color(0x404040);
    private static final Cursor hand = new Cursor(Cursor.HAND_CURSOR);
    private static final Cursor norm = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Font font = new Font("Serif", Font.BOLD, 48);
    private final Ellipse2D.Double checker = new Ellipse2D.Double();
    private final Rectangle2D.Double boundary = new Rectangle2D.Double();
    private int posX = 3 * DIAM; // column three
    private int posY = 3 * DIAM; // row three
    private int pressedX, pressedY;
    private int frameX, frameY;
    private boolean checkerVisible = true;
    private boolean mouseDown = false;

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ScaledPanel());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public ScaledPanel() {
        super(true);
        this.setPreferredSize(new Dimension(maxX, maxY));
        this.addComponentListener(new ComponentHandler());
        this.addKeyListener(new KeyHandler());
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform at = g2D.getTransform();
        g2D.scale(
            (double) this.getWidth() / maxX,
            (double) this.getHeight() / maxY);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                g2D.setColor((row + col) % 2 == 0 ? light : dark);
                g2D.fillRect(col * DIAM, row * DIAM, DIAM, DIAM);
            }
        }

        if (checkerVisible) {
            checker.setFrame(posX, posY, DIAM, DIAM);
            g2D.setColor(white);
            g2D.fill(checker);
            g2D.setFont(font);
            int a = g2D.getFontMetrics().getAscent();
            int w = g2D.getFontMetrics().stringWidth(glyph);
            int bx = posX + DIAM / 2 - w / 2 + 1;
            int by = posY + DIAM / 2 + a / 2 - 1;
            g2D.setColor(dark);
            g2D.drawString(glyph, bx, by);
        }

        g2D.setPaint(Color.red);
        g2D.setTransform(at);
        boundary.setRect(unScaleX(posX), unScaleY(posY), unScaleX(DIAM), unScaleY(DIAM));
        g2D.draw(boundary);
    }

    private void moveChecker(int dx, int dy) {
        hideChecker();
        posX = posX + DIAM * dx;
        posY = posY + DIAM * dy;
        showChecker();
    }

    private void updateChecker(MouseEvent e) {
        hideChecker();
        int deltaX = (int) (scaleX(e.getX() - pressedX));
        int deltaY = (int) (scaleY(e.getY() - pressedY));
        posX = frameX + deltaX;
        posY = frameY + deltaY;
        showChecker();
    }

    private void hideChecker() {
        checkerVisible = false;
        this.repaint();
    }

    private void showChecker() {
        checkerVisible = true;
        this.repaint();
    }

    private class ComponentHandler extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            repaint();
        }
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_UP:
                    moveChecker(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                    moveChecker(0, 1);
                    break;
                case KeyEvent.VK_LEFT:
                    moveChecker(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveChecker(1, 0);
                    break;
                case KeyEvent.VK_HOME:
                    hideChecker();
                    posX = 3 * DIAM;
                    posY = 3 * DIAM;
                    showChecker();
            }
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (checker.contains(scaleX(e.getX()), scaleY(e.getY()))) {
                setCursor(hand);
                mouseDown = true;
                Rectangle2D r = checker.getFrame();
                frameX = (int) r.getX();
                frameY = (int) r.getY();
                pressedX = e.getX();
                pressedY = e.getY();
            } else {
                mouseDown = false;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            posX = ((int) scaleX(e.getX())) / DIAM * DIAM;
            posY = ((int) scaleY(e.getY())) / DIAM * DIAM;
            repaint();
            setCursor(norm);
            mouseDown = false;
        }
    }

    private class MouseMotionHandler extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (mouseDown) {
                updateChecker(e);
            }
        }
    }

    private double scaleX(int x) {
        return ((double) x / getWidth()) * maxX;
    }

    private double scaleY(int y) {
        return ((double) y / getHeight()) * maxY;
    }

    private double unScaleX(int x) {
        return ((double) x / maxX) * getWidth();
    }

    private double unScaleY(int y) {
        return ((double) y / maxY) * getHeight();
    }
}

