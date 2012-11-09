package credits;

import credits.PlayAudio;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 30, 2005 Time: 10:30:05 AM
 */
class TypeWriter extends JPanel {

    private String[] lines;
    protected Thread animator;
    protected Thread player;
    protected static WrittenChar[][] wc;
    private int x = 0;
    private int millis;
    private boolean stop = false;
    private boolean withSound = false;
    private int linesPerPage;
    private BufferedImage img;
    // roll = true means all list was written and only scroll will be done
    // without sound
    private boolean roll = false;
    private boolean wantToRoll;

    private int ROLLOVER_ROW = 1000;

    public TypeWriter(String[] lines, int millis, boolean withSound, int linesPerPage, boolean wantToRoll) {
        this.lines = lines;
        this.millis = millis;
        this.withSound = withSound;
        this.linesPerPage = linesPerPage;
        this.wantToRoll = wantToRoll;
        wc = new WrittenChar[lines.length][];
        for (int i = 0, size = lines.length; i < size; i++) {
            char[] chars = lines[i].toCharArray();
            wc[i] = new WrittenChar[chars.length];
            for (int j = 0, no = chars.length; j < no; j++) {
                wc[i][j] = new WrittenChar(chars[j], false);
            }
        }
        try {
            img = ImageIO.read(TypeWriter.class.getResource("img\\old_paper.jpg"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        start();
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

//        Font f = g2d.getFont();
//        f = new Font(f.getName(), Font.BOLD, 12);
//        g2d.setFont(f);
        g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.BLACK);

        if (img != null) {
            g2d.drawImage(img, null, 0, 0);
        }


        if (roll && wantToRoll) {
            int y = 0;
            for (int i = 0, size = lines.length; i < size; i++) {
                y += 20;
                int x = 20;
                g2d.drawString(lines[i], x, y);
            }
        } else {

            int row = 0;
            boolean exit = false;
            for (int i = 0, size = lines.length; i < size; i++) {
                for (int j = 0; j < wc[i].length; j++) {
                    if (wc[i][j].wrote == false) {
                        row = i;
                        exit = true;
                        break;
                    }
                }
                if (exit) {
                    break;
                }
            }

            int index = 0;

            if (exit == false) {
                int dif = lines.length - linesPerPage;
                if (dif > 0) {
                    index = dif;
                }
            } else if (row > linesPerPage) {
                index = row - linesPerPage + 1;
            }

            int y = 0;
            for (int i = index, size = lines.length; i < size; i++) {
                y += 20;
                int x = 20;
                for (int j = 0; j < wc[i].length; j++) {
                    if (wc[i][j].wrote == true) {
                        String s = lines[i].substring(0, j + 1);
                        g2d.drawString(s, x, y);
                    } else {
                        break;
                    }
                }
            }
        }
    }


    public void start() {
        this.animator = new Thread(new Animator(), "Animator");
        if (withSound) {
            this.player = new Thread(new Player("sound\\office002.wav", true), "Player");
            this.player.start();
        }
        this.animator.start();
        stop = false;
    }

    /**
     * Safely stops the animation.
     */
    public void stop() {
        if (this.animator != null)
            this.animator.interrupt();
        this.animator = null;
        stop = true;
        //this.player.stop();
    }

    class Animator implements Runnable {

        public void run() {
            int i = 0, j = 0;
            while (true) {
                int m = millis;
                if (roll) {
                    // time sleep for rolling over a row
                    m = ROLLOVER_ROW;
                }
                try {
                    Thread.sleep(m);
                } catch (InterruptedException e) {
                    stop = true;
                    return;
                }

                if ((i <= x) || roll) {
                    if ((x == lines.length) || roll) {
                        roll = true;
                        int size = lines.length;
                        int from = 1;
                        if (lines.length > linesPerPage) {
                            size = linesPerPage;
                            from = lines.length - linesPerPage + 1;
                        }
                        size = size - 1;
                        if (size < 0) {
                            return;
                        }
                        String[] array = new String[size];
                        if (wantToRoll) {
                            System.arraycopy(lines, from, array, 0, size);
                            lines = array;
                        }
                        //System.out.println("lines.length=" + lines.length);
                        //printLines(lines);
                        repaint();
                        if(!wantToRoll) {
                            stop = true;
                            return;
                        }
                        if (array.length == 0) {
                            stop = true;
                            return;
                        }
                        continue;
                    }
                    if (j < wc[x].length) {
                        wc[i][j].wrote = true;
                        j++;
                    } else {
                        j = 0;
                        i++;
                        x++;
                    }
                }
                repaint();
            }
        }
    }

    class Player implements Runnable {

        private String file;
        private boolean loop;

        public Player(String file) {
            this.file = file;
            this.loop = false;
        }

        public Player(String file, boolean loop) {
            this.file = file;
            this.loop = true;
        }

        public void run() {
            if (loop) {
                while (true) {
                    if (stop || roll) {
                        return;
                    }
                    PlayAudio.playAudioFile(file);
                }
            } else {
                PlayAudio.playAudioFile(file);
            }
        }
    }

    class WrittenChar {
        private char ch;
        private boolean wrote;

        public WrittenChar(char ch, boolean wrote) {
            this.ch = ch;
            this.wrote = wrote;
        }

        public char getCh() {
            return ch;
        }

        public void setCh(char ch) {
            this.ch = ch;
        }

        public boolean isWrote() {
            return wrote;
        }

        public void setWrote(boolean wrote) {
            this.wrote = wrote;
        }

        public String toString() {
            return String.valueOf(ch);
        }
    }

    public static void main(String[] args) {
        String[] lines = {
            "John Surtew", "Senior Developer",
            "", "Mitre Wert", "Senior Developer",
            "", "Buster Dean", "Designer",
            "", "Nyer Wastrend", "Business Analyst"
            , "", "Bugniawsky Fiodor", "One man show"
            ,"","", "WNM Company @ 2005"

        };
        TypeWriter panel = new TypeWriter(lines, 120, true, 11, false);
        panel.setPreferredSize(new Dimension(200, 240));
        panel.setMinimumSize(new Dimension(200, 240));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(400, 400);
        frame.setTitle("Credits");
        //frameanimation.pack();
        frame.setVisible(true);
    }

    private void printLines(String[] lines) {
        for (int i = 0, size = lines.length; i < size; i++) {
            System.out.println("*** " + lines[i]);
        }
    }

    public void setLines(String[] lines) {
        this.lines = lines;
        roll = false;
        x = 0;
        wc = new WrittenChar[lines.length][];
        for (int i = 0, size = lines.length; i < size; i++) {
            char[] chars = lines[i].toCharArray();
            wc[i] = new WrittenChar[chars.length];
            for (int j = 0, no = chars.length; j < no; j++) {
                wc[i][j] = new WrittenChar(chars[j], false);
            }
        }
        start();
    }
}
