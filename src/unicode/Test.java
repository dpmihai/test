package unicode;

import db.rep.util.Globals;

import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 2, 2007
 * Time: 12:09:35 PM
 */
public class Test {

    public static void main(String[] args) {
        //String s = "?????????? ??? ??";
        String s = "\u03A9\u03B6";

//        try {
//            s = new String(s.getBytes(), "ISO-8859-7");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }



        List<JLabel> labels = new ArrayList<JLabel>();
        try {
            Connection con = createConnection("oracle.jdbc.driver.OracleDriver",
                    "jdbc:oracle:thin:@hornet1003.intranet.asf.ro:1521:EOSGR",
                    "kollecto",
                    "asf");
            String sql = "select s.first_name, s.last_name from subscribers s where rownum < 11";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                String s1 = rs.getString(1);
                System.out.println("s1b=" + s1.getBytes());
                String s2 = rs.getString(2);
                System.out.println("s2b=" + s2.getBytes());
                JLabel l1 = new JLabel(s1);
                JLabel l2 = new JLabel(s2);
                labels.add(l1);
                labels.add(l2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel =new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (JLabel label : labels) {
             panel.add(label);
             panel.add(Box.createRigidArea(new Dimension(5,5)));
        }

        showFrame(panel);



    }

    public static void showFrame(JComponent comp) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setLocation(400, 400);
        frame.setTitle("Test");
        //frameanimation.pack();
        frame.setVisible(true);
    }

    public static Connection createConnection(String driver, String url,
                                              String user, String password) throws Exception {
        Class.forName(driver).newInstance();
        Connection con = DriverManager.getConnection(url, user, password);
        Globals.setDbConnection(con);
        return con;
    }
}
