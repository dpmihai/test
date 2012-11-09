package credits;

import credits.Person;
import credits.Picture;
import credits.TypeWriter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 1, 2005 Time: 4:08:10 PM
 */
public class Credits extends JPanel {

    private Picture imgPanel;
    private TypeWriter infoPanel;
    private Person[] persons;
    private boolean stop = false;

    private static int cardWidth = 180;
    private static int height = 75;

    private int speedMillis = 80; // less means faster writing
    private int waitMillis = 8000; // wait millis between two persons cards : must be big enough so every card can be fully written

    public Credits(Person[] persons) {
        this (persons, cardWidth, height);
    }

    public Credits(Person[] persons, int cardWidth, int height) {

        this.cardWidth = cardWidth;
        this.height = height;
        this.persons = persons;

        imgPanel = new Picture(persons[0].getImage());
        infoPanel = new TypeWriter(persons[0].getDescription(), speedMillis, false, 6, false);

        int h = getPreferredHeight();
        Dimension cardDim = new Dimension(cardWidth, h);
        infoPanel.setPreferredSize(cardDim);
        Dimension imgDimension = new Dimension(h, h);
        imgPanel.setPreferredSize(imgDimension);

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createBevelBorder(BevelBorder.RAISED) ,
                                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)));
        add(imgPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(1, 1, 1, 0), 0, 0));
        add(infoPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(1, 1, 1, 1), 0, 0));

        Thread t = new Thread(new Animator());
        t.start();
    }

    public void stop() {
        stop = true;
        infoPanel.stop();
    }

    class Animator implements Runnable {

        public void run() {
            try {
                int no = 1;
                while (!stop) {
                    for (int i = no, size = persons.length; i < size; i++) {
                        if (stop) {
                            infoPanel.stop();
                            return;
                        }
                        Thread.sleep(waitMillis);
                        imgPanel.setImage(persons[i].getImage());
                        infoPanel.setLines(persons[i].getDescription());
                    }
                    no =0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPreferredHeight() {
        int maxLines = 3;  // for height=75
        for (int i=0, size = persons.length; i<size; i++) {
            int lines = persons[i].getDescription().length;
            if (lines > maxLines) {
                maxLines = lines;
            }
        }
        return height*maxLines/3;
    }

}
