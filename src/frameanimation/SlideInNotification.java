package frameanimation;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 15, 2006
 * Time: 3:46:23 PM
 */

import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class SlideInNotification {

    protected static final int ANIMATION_TIME = 1000;
    protected static final float ANIMATION_TIME_F = (float) ANIMATION_TIME;
    protected static final int ANIMATION_DELAY = 50;

    JWindow window;
    JComponent contents;
    AnimatingSheet animatingSheet;
    Rectangle desktopBounds;
    Dimension tempWindowSize;
    Timer animationTimer;
    int showX, startY;
    long animationStart;

    public SlideInNotification() {
        initDesktopBounds();
    }

    public SlideInNotification(JComponent contents) {
        this();
        setContents(contents);
    }

    protected void initDesktopBounds() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        desktopBounds = env.getMaximumWindowBounds();
        System.out.println("max window bounds = " + desktopBounds);
    }

    public void setContents(JComponent contents) {
        this.contents = contents;
        JWindow tempWindow = new JWindow();
        tempWindow.getContentPane().add(contents);
        tempWindow.pack();
        tempWindowSize = tempWindow.getSize();
        tempWindow.getContentPane().removeAll();
        window = new JWindow();
        window.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                window.dispose();
                System.exit(0);
            }
        });
        animatingSheet = new AnimatingSheet();
        animatingSheet.setSource(contents);
        window.getContentPane().add(animatingSheet);
    }


    public void showAt(int x) {
        // create a window with an animating sheet
        // copy over its contents from the temp window
        // animate it
        // when done, remove animating sheet and add real contents

        showX = x;
        startY = desktopBounds.y + desktopBounds.height;

        ActionListener animationLogic = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final long elapsed = System.currentTimeMillis() - animationStart;
                if (elapsed > ANIMATION_TIME) {
                    // put real contents in window and show
//                    window.getContentPane().removeAll();
//                    window.getContentPane().add(contents);
//                    window.pack();
//                    window.setLocation(showX, startY -animatingSheet.getHeight());
//                    window.setVisible(true);
//                    window.repaint();

                    // wait some time until the note dissapears
                    Runnable r = new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            } finally {
                                animate(false, elapsed);
                                if (animatingSheet.getHeight() <= 1) {
                                    animationTimer.stop();
                                    animationTimer = null;
                                    window.dispose();
                                    System.exit(0);
                                }
                            }
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();

                } else {
                    animate(true, elapsed);
                }
            }
        };
        animationTimer = new Timer(ANIMATION_DELAY, animationLogic);
        animationStart = System.currentTimeMillis();
        animationTimer.start();
    }

    private void animate(boolean up, long elapsed) {
        if (up) {
            float progress = (float) elapsed / ANIMATION_TIME_F;
            // get height to show
            int animatingHeight = (int) (progress * tempWindowSize.getHeight());
            animatingHeight = Math.max(animatingHeight, 1);

            animatingSheet.setAnimatingHeight(animatingHeight);
            window.pack();
            window.setLocation(showX, startY - window.getHeight());
            window.setVisible(true);
            window.repaint();
        }  else {
            // down animation : closing/hiding note
            float progress = ANIMATION_TIME_F / (float) elapsed;
            int max = (int)tempWindowSize.getHeight();
            int animatingHeight = (int) (progress * max);
            animatingHeight = Math.max(animatingHeight, 1);
            // need to test the current size (so that the down animation does not take to much)
            int old = (int)animatingSheet.getSize().getHeight();
            if (animatingHeight >= old) {
                animatingHeight = old-1;
            }
            //System.out.println("height="+animatingHeight);
            animatingSheet.setAnimatingHeight(animatingHeight);
            window.pack();
            window.setLocation(showX, startY - animatingHeight);
            window.setVisible(true);
            window.repaint();
        }
    }

    // AnimatingSheet inner class listed below
    class AnimatingSheet extends JPanel {
        Dimension animatingSize = new Dimension(0, 1);
        JComponent source;
        BufferedImage offscreenImage;

        public AnimatingSheet() {
            super();
            setOpaque(true);
        }

        public void setSource(JComponent source) {
            this.source = source;
            animatingSize.width = source.getWidth();
            makeOffscreenImage(source);
        }

        public void setAnimatingHeight(int height) {
            animatingSize.height = height;
            setSize(animatingSize);
        }

        private void makeOffscreenImage(JComponent source) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsConfiguration gfxConfig = ge.getDefaultScreenDevice().getDefaultConfiguration();
            offscreenImage = gfxConfig.createCompatibleImage(source.getWidth(), source.getHeight());
            Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
            // windows workaround
            offscreenGraphics.setColor(source.getBackground());
            offscreenGraphics.fillRect(0, 0, source.getWidth(), source.getHeight());
            // paint from source to offscreen buffer
            source.paint(offscreenGraphics);
        }

        public Dimension getPreferredSize() {
            return animatingSize;
        }

        public Dimension getMinimumSize() {
            return animatingSize;
        }

        public Dimension getMaximumSize() {
            return animatingSize;
        }

        public void update(Graphics g) {
            // override to eliminate flicker from
            // unnecessary clear
            paint(g);
        }

        public void paint(Graphics g) {
            // get the top-most n pixels of source and
            // paint them into g, where n is height
            // (different from sheet example, which used bottom-most)
            BufferedImage fragment = offscreenImage.getSubimage(0, 0, source.getWidth(), animatingSize.height);
            g.drawImage(fragment, 0, 0, this);

            // draw a border
            g.drawLine(0, 0, animatingSize.width, 0); //top
            g.drawLine(0, 0, 0, animatingSize.height); //left
            g.drawLine(animatingSize.width-1, 0, animatingSize.width-1, animatingSize.height); //right
            g.drawLine(0, animatingSize.height-1, animatingSize.width, animatingSize.height-1); //bottom
        }
    }

    public static void main(String[] args) {
        //Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        Icon icon = ImageUtil.getImageIcon("img.png");
        JLabel label = new JLabel("You've got mail", icon, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension((int) (label.getPreferredSize().getWidth()+10),
                (int) (label.getPreferredSize().getHeight()+10)));
        SlideInNotification slider = new SlideInNotification(label);
        int where = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - label.getWidth());
        //System.out.println("where=" + where);
        slider.showAt(where);
    }


}
