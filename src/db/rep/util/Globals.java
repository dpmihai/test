package db.rep.util;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

import db.rep.MainFrame;
import db.rep.Main;
import db.rep.bean.Report;

/**
 * @author Decebal Suiu
 */
public class Globals {

    private static MainFrame frame;
    private static DbConfig dbConfig;
    private static boolean isDriverRegistered;
    private static Icon alarmIcon;
    private static JTabbedPane reportPane;
    private static Report report;
    private static Connection con;

    public static void setMainFrame(MainFrame mainFrame) {
        frame = mainFrame;
    }

    public static MainFrame getMainFrame() {
        return frame;
    }

    public static Image getTitleImage() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static DbConfig getDbConfig() {
        if (dbConfig == null) {
            String dbUrl = "jdbc:hsqldb:file:/home/decebal/work/ejbstresser/dist/stresser";
            String dbUser = "sa";
            String dbPassword = "";
            dbConfig = new DbConfig(dbUrl, dbUser, dbPassword);
            System.out.println(dbConfig.toString());
        }

        return dbConfig;
    }
    
    public static synchronized Connection getDbConnection() throws SQLException {
//        if (!isDriverRegistered) {
//            DriverManager.registerDriver (new org.hsqldb.jdbcDriver());
//            isDriverRegistered = true;
//        }
//        DbConfig dbConfig = getDbConfig();
//        Connection conn = DriverManager.getConnection(dbConfig.getUrl(),
//                dbConfig.getUser(), dbConfig.getPassword());
//        conn.setAutoCommit(false);
//        return conn;
        return con;
    }

    public static synchronized void setDbConnection(Connection connection) {
        con = connection;
    }

    public static Icon getAlarmIcon() {
        if (alarmIcon == null) {
            alarmIcon = new ImageIcon(Main.class.getResource("images/alarm.gif"));
        }
        
        return alarmIcon;       
    }
    
    public static JTabbedPane getReportPane() {
        return reportPane;
    }
    
    public static void setReportPane(JTabbedPane reportPane) {
        Globals.reportPane = reportPane;
    }
    
    public static Report getReport() {
        return report;
    }

    public static void setReport(Report report) {
        Globals.report = report;
    }
    
    public static void showAlarm(JPanel panel) {
        if (reportPane == null) {
            return;
        }
        
        if (reportPane.getSelectedComponent() != panel) {
            int index = reportPane.indexOfComponent(panel);
            if (reportPane.getIconAt(index) == null) {
                reportPane.setIconAt(index, getAlarmIcon());
                //Toolkit.getDefaultToolkit().beep();
            }
        }        
    }

    public static void hideAlarm(JPanel panel) {
        if (reportPane == null) {
            return;
        }
        
        if (reportPane.getSelectedComponent() != panel) {
            int index = reportPane.indexOfComponent(panel);
            if (reportPane.getIconAt(index) != null) {
                reportPane.setIconAt(index, null);
                //Toolkit.getDefaultToolkit().beep();
            }
        }        
    }
    
}
