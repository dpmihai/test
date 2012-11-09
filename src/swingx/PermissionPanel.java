package swingx;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 16, 2008
 * Time: 3:32:13 PM
 */
public class PermissionPanel extends DisposablePanel {

    private static String TITLE = "Permissions";
    private static String ALL = "All";
    private static String SET = "Set";

    private static String READ = "Read";
    private static String EXECUTE = "Execute";
    private static String WRITE = "Write";
    private static String DELETE = "Delete";
    private static String SECURITY = "Security";

    public static Color transparentColor = new Color(255, 255, 255, 0);

    private List<Permission> permissions;

    public PermissionPanel(List<Permission> permissions) {
        this(permissions, null);
    }

    public PermissionPanel(List<Permission> permissions, Window disposableWindow) {
        this.permissions = permissions;
        this.disposableWindow = disposableWindow;        
        init();
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    private void init() {
        setLayout(new GridBagLayout());
        setBackgroundPainter(getPainter());
        // any transparent color! to view the color beneath  (alpha=0)
        UIManager.getDefaults().put("CheckBox.background", transparentColor);

        final JCheckBox checkAll = new JCheckBox(ALL);
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setForeground(Color.BLUE.darker());

        add(titleLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        add(checkAll, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(10, 0, 0, 10), 0, 0));
        add(new JXTitledSeparator(), new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));

        final List<JCheckBox> checkBoxes = new LinkedList<JCheckBox>();
        int y = 2;
        Permission readPermission = null;
        JCheckBox readCheck = null;
        for (final Permission per : permissions) {
            String s = "[" + per.getInitials() + "] " + per.getName();
            add(new JLabel(s), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 10, 5, 10), 0, 0));
            final JCheckBox ck = new JCheckBox();
            ck.setSelected(per.isSet());
            checkBoxes.add(ck);
            if (READ.equals(per.getName())) {
                readPermission = per;
                readCheck = ck;
            }
            add(ck, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 10, 5, 10), 0, 0));
            y++;
        }


        // deselect read -> deselect all permissions
        // select any permissions other than read -> select read permission
        final Permission rPermission = readPermission;
        final JCheckBox rCheck = readCheck;
        for (int i =0, size = checkBoxes.size(); i<size; i++) {
            final JCheckBox ck = checkBoxes.get(i);
            final int index = i;
            ck.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Permission permission = permissions.get(index);
                    permission.set(ck.isSelected());
                    if (READ.equals(permission.getName()) && !ck.isSelected()) {
                        for (final Permission per : permissions) {
                            per.set(false);
                        }
                        for (JCheckBox ck : checkBoxes) {
                            ck.setSelected(false);
                        }
                    }
                    if (!READ.equals(permission.getName()) && ck.isSelected()) {
                        rPermission.set(true);
                        rCheck.setSelected(true);
                    }
                }
            });
        }

        // select/deselect all permissions
        checkAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = checkAll.isSelected();
                for (final Permission per : permissions) {
                    per.set(selected);
                }
                for (JCheckBox ck : checkBoxes) {
                    ck.setSelected(selected);
                    ck.setEnabled(!selected);
                }
            }
        });

        add(new JXTitledSeparator(), new GridBagConstraints(0, y, 3, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));

        JButton setButton= new JButton(SET);
        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Permissions");
                System.out.println("-----------");
                for (final Permission per : permissions) {
                    System.out.println(per.getName() + " : " + per.isSet());
                }
                System.out.println("-----------");
            }
        });
        List<JButton> buttons = new ArrayList<JButton>();
        buttons.add(setButton);        
        JXPanel buttonPanel = getButtonPanel(buttons);
        buttonPanel.setBackground(transparentColor);
        add(buttonPanel, new GridBagConstraints(0, y + 1, 3, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
    }

//    private Permission getPermissionByName(String name) {
//        for (Permission per : permissions) {
//            if (per.getName().equals(name)) {
//                return per;
//            }
//        }
//        return null;
//    }
//
//    private JCheckBox getCheckBoxforPermission(String name) {
//        int foundIndex = -1;
//        for (int i=0, size = permissions.size(); i<size; i++) {
//            if (permissions.get(i).getName().equals(name)) {
//                foundIndex = i;
//                break;
//            }
//        }
//        if (foundIndex != -1) {
//            return checkBoxes.get(foundIndex);
//        } else {
//            return null;
//        }
//    }
    

    public static void main(String[] args) {
        
        JXFrame frame = new JXFrame();
        frame.getContentPane().setBackground(new Color(132, 168, 232, 64));
        JXPanel panel = new PermissionPanel(getDefaultPermissions(), frame);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();        
        frame.setLocation(400, 400);
        frame.setTitle("Security");
        frame.setVisible(true);
    }

    public static List<Permission> getDefaultPermissions() {
        List<Permission> permissions = new LinkedList<Permission>();
        Permission p = new Permission(READ, "R");
        p.set(true);
        permissions.add(p);
        permissions.add(new Permission(EXECUTE, "X"));
        permissions.add(new Permission(WRITE, "W"));
        permissions.add(new Permission(DELETE, "D"));
        permissions.add(new Permission(SECURITY, "S"));
        return permissions;

    }

    public static Painter getPainter() {
        //int width = getSize().width / 2;
        int width = 100;
        int height = 200;
        Color color1 = new Color(255, 255, 255, 126);
        Color color2 = new Color(132, 168, 232, 126);
        LinearGradientPaint gradientPaint =
                new LinearGradientPaint(0.0f, 0.0f, width, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{color1, color2});
        MattePainter mattePainter = new MattePainter(gradientPaint);
        return mattePainter;
    }

}
