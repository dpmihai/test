package db;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.launcher.LauncherClassLoader;
import util.ResizeUtil;
import db.rep.util.Globals;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 25, 2005 Time: 11:23:51 AM
 */
public class ConnectionDialog extends JDialog implements ActionListener,
        ItemListener {

    private Dimension dim = new Dimension(500, 400);

    private final String STATUS = "Status";
    private final String DEFAULT_DRIVER_TYPE = "Oracle";
    private final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final String DEFAULT_URL = "jdbc:oracle:thin:@10.230.169.198:1521:capfour";
    private final String DEFAULT_USER = "collect";
    private final String DEFAULT_PASS = "collex";
    private Color backColor;

    final static String sJDBCTypes[][] = {
        {
            "HSQL Database Engine In-Memory", "org.hsqldb.jdbcDriver",
            "jdbc:hsqldb:."
        }, {
            "HSQL Database Engine Standalone", "org.hsqldb.jdbcDriver",
            "jdbc:hsqldb:test"
        }, {
            "HSQL Database Engine Server", "org.hsqldb.jdbcDriver",
            "jdbc:hsqldb:hsql://localhost"
        }, {
            "HSQL Database Engine WebServer", "org.hsqldb.jdbcDriver",
            "jdbc:hsqldb:http://localhost"
        }, {
            "JDBC-ODBC Brigde from Sun", "sun.jdbc.odbc.JdbcOdbcDriver",
            "jdbc:odbc:test"
        }, {
            "Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@"
        }, {
            "IBM DB2", "COM.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2:test"
        }, {
            "Cloudscape RMI", "RmiJdbc.RJDriver",
            "jdbc:rmi://localhost:1099/jdbc:cloudscape:test;create=true"
        }, {
            "InstantDb", "jdbc.idbDriver", "jdbc:idb:sample.prp"
        }, {
            "PointBase", "com.pointbase.jdbc.jdbcUniversalDriver",
            "jdbc:pointbase://localhost/sample"
        }, // PUBLIC / public
    };
    Connection mConnection;
    JTextField mDriver, mURL, mUser;
    JPasswordField mPassword;
    JTextArea txaStatus;
    JScrollPane scr;
    JComboBox types;

    public static Connection createConnection(String driver, String url,
                                              String user, String password) throws Exception {
        Class.forName(driver).newInstance();
        Connection con = DriverManager.getConnection(url, user, password);
        Globals.setDbConnection(con);
        return con;
    }

    public ConnectionDialog(JFrame owner, String title) {
        super(owner, title, true);
        try {
            DriverPath.loadDrivers();
        } catch (IOException e) {
            e.printStackTrace();  
        }
        create();
        try {
            Connection con = Globals.getDbConnection();
            if (con != null) {
                mConnection = con;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void create() {
        this.getContentPane().setLayout(new GridBagLayout());
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });

        JPanel p = new JPanel(new GridBagLayout());
        backColor = p.getBackground();

        mDriver = new JTextField();
        mDriver.setBackground(backColor);
        mURL = new JTextField();
        mURL.setBackground(backColor);
        mUser = new JTextField();
        mUser.setBackground(backColor);
        mPassword = new JPasswordField();
        mPassword.setEchoChar('*');
        mPassword.setBackground(backColor);
        txaStatus = new JTextArea();
        txaStatus.setBackground(backColor);
        scr = new JScrollPane(txaStatus);
        scr.setSize(200, 40);
        scr.setBorder(new TitledBorder(STATUS));
        types = new JComboBox();
        types.addItemListener(this);
        for (int i = 0; i < sJDBCTypes.length; i++) {
            types.addItem(sJDBCTypes[i][0]);
        }
        types.setSelectedItem(DEFAULT_DRIVER_TYPE);

        mDriver.setText(DEFAULT_DRIVER);
        mURL.setText(DEFAULT_URL);
        mUser.setText(DEFAULT_USER);
        mPassword.setText(DEFAULT_PASS);

        p.add(new JLabel("Type:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        p.add(types, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        p.add(new JLabel("Driver:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        p.add(mDriver, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        p.add(new JLabel("URL:"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        p.add(mURL, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        p.add(new JLabel("User:"), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        p.add(mUser, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        p.add(new JLabel("Password:"), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        p.add(mPassword, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        JButton okBtn, cancelBtn, disBtn, drvBtn;
        okBtn = new JButton("Connect");
        okBtn.setActionCommand("ConnectOk");
        okBtn.addActionListener(this);
        disBtn = new JButton("Disconnect");
        disBtn.setActionCommand("Disconnect");
        disBtn.addActionListener(this);
        drvBtn = new JButton("Add Driver");
        drvBtn.setActionCommand("AddDriver");
        drvBtn.addActionListener(this);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(okBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        btnPanel.add(disBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        btnPanel.add(drvBtn);
        ResizeUtil.equalizeButtonSizes(btnPanel);

        p.add(btnPanel, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        p.add(scr, new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));


        this.getContentPane().add(p, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));
    }

    private void trace(String s) {
        txaStatus.setText(s);
        System.out.println(s);
    }

    public Connection getConnection() {
        return mConnection;
    }

    public void actionPerformed(ActionEvent ev) {
        String s = ev.getActionCommand();
        if (s.equals("ConnectOk")) {
            try {
                mConnection = createConnection(mDriver.getText(),
                        mURL.getText(),
                        mUser.getText(),
                        String.valueOf(mPassword.getPassword()));

                DBViewer v = new DBViewer(mConnection, DBViewer.INFO);
                txaStatus.setText("Connected.\r\n" + v.getInfo());

                String[] columns = { "NAME", "CREATED_BY" };
                TableViewer tv = new TableViewer(mConnection, "ADDRESS_TYPES", columns);
                System.out.println(tv.getColumnNames());
                System.out.println(tv.getColumnTypes());
                for (Iterator it = tv.getData().iterator(); it.hasNext();) {
                    ArrayList row = (ArrayList)it.next();
                    System.out.println(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
                txaStatus.setText(e.toString());
            }
        } else if (s.equals("AddDriver")) {

            final JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.addChoosableFileFilter(new JarFilter());
            String path = PreferencesManager.getInstance().loadParameter(PreferencesManager.JAR_PATH_KEY);
            if (path != null) {
                chooser.setSelectedFile(new File(path));
            }
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                PreferencesManager.getInstance().storeParameter(PreferencesManager.JAR_PATH_KEY,
                        file.getAbsolutePath());
                try {
                    DriverPath.addEntry(file.getAbsolutePath());
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    URL url = file.toURL();
                    LauncherClassLoader jarClassLoader = (LauncherClassLoader)this.getClass().getClassLoader();
                    if (file.isDirectory()) {
                        jarClassLoader.loadJars(file.getAbsolutePath());
                    } else {
                        jarClassLoader.loadJar(file);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }

        } else if (s.equals("Disconnect")) {
            if (mConnection != null) {
                try {
                    mConnection.close();
                    txaStatus.setText("Disconnected.");
                    mConnection = null;
                    Globals.setDbConnection(mConnection);
                } catch (SQLException e) {
                    txaStatus.setText(e.toString());
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        String s = (String) e.getItem();
        for (int i = 0; i < sJDBCTypes.length; i++) {
            if (s.equals(sJDBCTypes[i][0])) {
                mDriver.setText(sJDBCTypes[i][1]);
                mURL.setText(sJDBCTypes[i][2]);
            }
        }
    }

    public void resize() {
        Dimension d = getSize();
        Dimension min = getMinimumSize();
        if (d.width < min.width) {
            d.width = min.width;
        }
        if (d.height < min.height) {
            d.height = min.height;
        }
        setSize(d);
    }

    public Dimension getPreferredSize() {
        return dim;
    }

    public Dimension getMinimumSize() {
        return dim;
    }

}

