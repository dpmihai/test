package swingx;

import org.jdesktop.swingx.JXTitledSeparator;
import util.ResizeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 11, 2008
 * Time: 1:18:30 PM
 */
public class ServerDialog extends JDialog {

    private String SERVER_STRING = "Server Configuration";
    private String IP_STRING = "IP Address";
    private String PORT_STRING = "Port";
    private String NAME_STRING = "Name";
    private String OK = "Ok";
    private String CANCEL = "Cancel";

    private JTextField ipText = new JTextField(20);
    private JTextField portText = new JTextField(20);
    private JTextField nameText = new JTextField(20);
    private JButton okButton = new JButton(OK);
    private JButton cancelButton = new JButton(CANCEL);
    private JLabel errorLabel = new JLabel();

    private Server server;
    private boolean okPressed;
    //private Color transparentColor = new Color(132, 168, 232, 0);

    public ServerDialog() {        
        this(null);
    }

    public ServerDialog(Server server) {
        setModal(true);
        this.server = server;
        initComponents();
    }

    private void initComponents() {

        setLayout(new GridBagLayout());
        //getContentPane().setBackground(new Color(132, 168, 232, 126));

        JLabel serverLabel = new JLabel(SERVER_STRING);
        serverLabel.setForeground(Color.BLUE.darker());
        JLabel ipLabel = new JLabel(IP_STRING + " *");
        JLabel portLabel = new JLabel(PORT_STRING + " *");
        JLabel nameLabel = new JLabel(NAME_STRING);

        errorLabel.setForeground(Color.RED.darker());
        //errorLabel.setPreferredSize(new Dimension(ipText.getPreferredSize().width, 22));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(cancelButton);
        ResizeUtil.equalizeButtonSizes(buttonPanel);
        //buttonPanel.setBackground(transparentColor);

//        ipText.setBackground(new Color(210, 223, 246));
//        portText.setBackground(new Color(210, 223, 246));
//        nameText.setBackground(new Color(210, 223, 246));
        if (server != null) {
            ipText.setText(server.getIp());
            portText.setText(server.getPort());
            nameText.setText(server.getName());            
        }

        add(serverLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        add(new JXTitledSeparator(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
        add(ipLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(10, 10, 10, 5), 0, 0));
        add(ipText, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 0, 10, 10), 0, 0));
        add(portLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 10, 10, 5), 0, 0));
        add(portText, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));
        add(nameLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 10, 10, 5), 0, 0));
        add(nameText, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 10), 0, 0));
        add(errorLabel, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 10, 5, 10), 0, 0));
        add(new JXTitledSeparator(), new GridBagConstraints(0, 6, 2, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
        add(buttonPanel, new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 ok();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 cancel();
            }
        });
    }

    public Server getServer() {
        String ip = ipText.getText();
        String port = portText.getText();
        String name = nameText.getText();
        if (name.trim().equals("")) {
            name = null;
        }
        Server server = new Server(name, ip, port);        
        return server;
    }

    private boolean validIp(String ip) {
        String[] tokens = ip.split("\\.");        
        if (tokens.length != 4) {
            return false;
        }
        try {
            for (String token : tokens) {
                int i = Integer.parseInt(token);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private boolean validPort(String port) {
        try {
            int i = Integer.parseInt(port);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private void ok() {
        String ip = ipText.getText();
        if (!validIp(ip)) {
            System.err.println("Invalid ip address : " + ip);
            errorLabel.setText("Invalid IP Address!");
            ipText.requestFocus();
            return;
        }
        String port = portText.getText();
        if (!validPort(port)) {
            System.err.println("Invalid port : " + port);
            errorLabel.setText("Invalid Port!");
            portText.requestFocus();
            return;
        }
        okPressed = true;
        dispose();
    }

    private void cancel() {
        dispose();
    }

    public boolean isOkPressed() {
        return okPressed;
    }
}
