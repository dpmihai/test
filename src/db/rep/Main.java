package db.rep;

import javax.swing.*;


import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

import db.rep.util.Show;

/**
 * @author Decebal Suiu
 */
public class Main {

    public static void main(String[] args) {
        try {
//            System.setProperty("swing.noxp", "true");
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
//            PlasticXPLookAndFeel.setMyCurrentTheme(new com.jgoodies.plaf.plastic.theme.SkyBluer());
            PlasticXPLookAndFeel.setMyCurrentTheme(new ExperienceBlue());
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MainFrame mainFrame = new MainFrame("DBRep");
        mainFrame.setSize(650, 600);
        Show.centrateComponent(mainFrame);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        mainFrame.setIconImage(new ImageIcon(mainFrame.getClass().getResource("incentive.png")).getImage());
        mainFrame.setVisible(true);
    }

}
