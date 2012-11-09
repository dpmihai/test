package db.rep.std;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/** 
 * @author Decebal Suiu
 */
public class BaseOKCancelDialog extends BaseDialog {

    public static final int OK_BUTTON = 1;
    public static final int CANCEL_BUTTON = 2;
    public static final int CLOSE_BUTTON = 4;
    public static final int HELP_BUTTON = 8;
    public static final int APPLY_BUTTON = 16;

    private boolean okPressed;
    private int buttons;
    private BaseOKCancelPanel pneOKCancel;

    // todo mnemonics
    private String okButtonText = "OK";
    private String cancelButtonText = "Cancel";
    private String closeButtonText = "Close";
    private String helpButtonText = "Help";
    private String applyButtonText = "Apply";

    private JPanel pneDialog = new JPanel();
    private JPanel pneButtons = new JPanel();
    private BorderLayout borderLayout = new BorderLayout();

    private JButton btnOK = new JButton();
    private JButton btnClose = new JButton();
    private JButton btnCancel = new JButton();
    private JButton btnHelp = new JButton();
    private JButton btnApply = new JButton();

    private Insets okCancelPanelInsets = new Insets(0, 0, 0, 0);
    private Insets butonsPanelInsets = new Insets(0, 0, 0, 0);

    public BaseOKCancelDialog(Frame frame, String title) {
        this(frame, title, true);
    }

    public BaseOKCancelDialog(Frame frame, String title, boolean modal) {
        this(frame, title, null, OK_BUTTON | CANCEL_BUTTON, modal);
    }

    public BaseOKCancelDialog(Frame frame, String title, BaseOKCancelPanel PneOKCancel) {
        this(frame, title, PneOKCancel, OK_BUTTON | CANCEL_BUTTON, true);
    }

    public BaseOKCancelDialog(Frame frame, String title, BaseOKCancelPanel PneOKCancel, boolean modal) {
        this(frame, title, PneOKCancel, OK_BUTTON | CANCEL_BUTTON, modal);
    }

    public BaseOKCancelDialog(Frame frame, String title, int buttons) {
        this(frame, title, null, buttons, true);
    }

    public BaseOKCancelDialog(Frame frame, String title, int buttons, boolean modal) {
        this(frame, title, null, buttons, modal);
    }

    public BaseOKCancelDialog(Frame frame, String title, BaseOKCancelPanel pneOKCancel, int buttons) {
        this(frame, title, pneOKCancel, buttons, true);
    }

    public BaseOKCancelDialog(Frame frame, String title, BaseOKCancelPanel pneOKCancel, int buttons, boolean modal) {
        super(frame, title, modal);
        doInit(pneOKCancel, buttons);
    }

    public BaseOKCancelDialog(Dialog dialog, String title) {
        this(dialog, title, null, OK_BUTTON | CANCEL_BUTTON);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, boolean modal) {
        this(dialog, title, null, OK_BUTTON | CANCEL_BUTTON, modal);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, BaseOKCancelPanel pneOKCancel) {
        this(dialog, title, pneOKCancel, OK_BUTTON | CANCEL_BUTTON, true);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, BaseOKCancelPanel pneOKCancel, boolean modal) {
        this(dialog, title, pneOKCancel, OK_BUTTON | CANCEL_BUTTON, modal);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, int buttons) {
        this(dialog, title, null, buttons, true);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, int buttons, boolean modal) {
        this(dialog, title, null, buttons, modal);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, BaseOKCancelPanel pneOKCancel, int buttons) {
        this(dialog, title, pneOKCancel, buttons, true);
    }

    public BaseOKCancelDialog(Dialog dialog, String title, BaseOKCancelPanel pneOKCancel, int buttons, boolean modal) {
        super(dialog, title, modal);
        doInit(pneOKCancel, buttons);
    }

    private void doInit(BaseOKCancelPanel pneOKCancel, int buttons) {
        this.pneOKCancel = pneOKCancel;
        this.okPressed = false;
        this.buttons = buttons;
    }

    public BaseOKCancelPanel getOKCancelPanel() {
        return pneOKCancel;
    }

    public void setOKCancelPanel(BaseOKCancelPanel pneOKCancel) {
        this.pneOKCancel = pneOKCancel;
        Border border = BorderFactory.createEmptyBorder(okCancelPanelInsets.top, okCancelPanelInsets.left,
                        okCancelPanelInsets.bottom, okCancelPanelInsets.right);
        pneOKCancel.setBorder(border);
    }

    public void setOkButtonText(String approveButtonText) {
        if (this.okButtonText == approveButtonText) {
            return;
        }
        this.okButtonText = approveButtonText;
        btnOK.setText(approveButtonText);
    }

    public void setOkButtonText(String approveButtonText, int mnemonic) {
        setOkButtonText(approveButtonText);
        btnOK.setMnemonic(mnemonic);
    }

    public String getOkButtonText() {
        return okButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        if (this.cancelButtonText == cancelButtonText) {
            return;
        }
        this.cancelButtonText = cancelButtonText;
        btnCancel.setText(cancelButtonText);
    }

    public void setCancelButtonText(String cancelButtonText, int mnemonic) {
        setCancelButtonText(cancelButtonText);
        btnCancel.setMnemonic(mnemonic);
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public void setApplyButtonText(String applyButtonText) {
        if (this.applyButtonText == applyButtonText) {
            return;
        }
        this.applyButtonText = applyButtonText;
        btnApply.setText(applyButtonText);
    }

    public void setApplyButtonText(String applyButtonText, int mnemonic) {
        setApplyButtonText(applyButtonText);
        btnApply.setMnemonic(mnemonic);
    }

    public String getApplyButtonText() {
        return applyButtonText;
    }

    public Insets getOkCancelPanelInsets() {
        return okCancelPanelInsets;
    }

    public void setOkCancelPanelInsets(Insets okPanelInsets) {
        this.okCancelPanelInsets = okPanelInsets;
    }

    public Insets getButonsPanelInsets() {
        return butonsPanelInsets;
    }

    public void setButonsPanelInsets(Insets butonsPanelInsets) {
        this.butonsPanelInsets = butonsPanelInsets;
    }

    public void show() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        pack();
        setLocationRelativeTo(this.getParent());
        
        super.show();
    }

    void jbInit() throws Exception {
        pneDialog.setLayout(borderLayout);

        pneButtons.setLayout(new BoxLayout(pneButtons, BoxLayout.X_AXIS));
        pneButtons.add(Box.createGlue());

        CompoundBorder innerBorder = new CompoundBorder(new EdgeBorder(SwingConstants.NORTH), new EmptyBorder(10, 10,
                        10, 0));
        pneButtons.setBorder(new CompoundBorder(new EmptyBorder(butonsPanelInsets.top, butonsPanelInsets.left,
                        butonsPanelInsets.bottom, butonsPanelInsets.right), innerBorder));

        boolean addSeparator = false;

        if ((buttons & OK_BUTTON) != 0) {
            btnOK.setText(okButtonText);
            btnOK.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ok();
                }
            });

            pneButtons.add(btnOK, null);
//            getRootPane().setDefaultButton(btnOK);
            if (btnOK.getMnemonic() <= 0) {
                btnOK.setMnemonic(KeyEvent.VK_O);
            }

            addSeparator = true;
        }

        if ((buttons & CANCEL_BUTTON) != 0) {
            btnCancel.setText(cancelButtonText);
            btnCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });

            if (addSeparator) {
                pneButtons.add(Box.createHorizontalStrut(5));
            }
            addSeparator = true;
            pneButtons.add(btnCancel, null);
            if (btnCancel.getMnemonic() <= 0) {
                btnCancel.setMnemonic(KeyEvent.VK_R);
            }
        }

        if ((buttons & APPLY_BUTTON) != 0) {
            btnApply.setText(this.applyButtonText);
            btnApply.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    apply();
                }
            });

            if (addSeparator) {
                pneButtons.add(Box.createHorizontalStrut(5));
            }
            addSeparator = true;
            pneButtons.add(btnApply, null);
//            btnApply.setMnemonic(KeyEvent.VK_A);
        }

        if ((buttons & CLOSE_BUTTON) != 0) {
            btnClose.setText(closeButtonText);
            btnClose.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    close();
                }
            });

            if (addSeparator) {
                pneButtons.add(Box.createHorizontalStrut(5));
            }
            addSeparator = true;
            pneButtons.add(btnClose, null);
//            getRootPane().setDefaultButton(btnClose);
            if (btnClose.getMnemonic() <= 0) {
                btnClose.setMnemonic(KeyEvent.VK_I);
            }
        }

        if ((buttons & HELP_BUTTON) != 0) {
            btnHelp.setText(this.helpButtonText);
            btnHelp.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    help();
                }
            });

            if (addSeparator) {
                pneButtons.add(Box.createHorizontalStrut(5));
            }
            addSeparator = true;
            pneButtons.add(btnHelp, null);
//            btnHelp.setMnemonic(KeyEvent.VK_H);
        }

        if (pneOKCancel != null) {
            Border border = BorderFactory.createEmptyBorder(okCancelPanelInsets.top, okCancelPanelInsets.left,
                            okCancelPanelInsets.bottom, okCancelPanelInsets.right);
            pneOKCancel.setBorder(border);
        }
        pneDialog.add(pneOKCancel, BorderLayout.CENTER);
        pneDialog.add(pneButtons, BorderLayout.SOUTH);

        getContentPane().add(pneDialog);
        equalizeButtonSizes();
    }

    /**
     * Sets the buttons along the bottom of the dialog to be the same size. This is done dynamically by setting each
     * button's preferred and maximum sizes after the buttons are created. This way, the layout automatically adjusts to
     * the locale- specific strings.
     */
    private void equalizeButtonSizes() {
        List lbuttons = new ArrayList();
        for (int i = 0; i < pneButtons.getComponentCount(); i++) {
            Component c = pneButtons.getComponent(i);
            if (c instanceof JButton) {
                lbuttons.add(c);
            }
        }

        // Get the largest width and height
        Dimension maxSize = new Dimension(0, 0);
        for (int i = 0; i < lbuttons.size(); i++) {
            Dimension d = ((JButton) lbuttons.get(i)).getPreferredSize();
            maxSize.width = Math.max(maxSize.width, d.width);
            maxSize.height = Math.max(maxSize.height, d.height);
        }

        // reset preferred and maximum size since BoxLayout takes both
        // into account
        for (int i = 0; i < lbuttons.size(); ++i) {
            ((JButton) lbuttons.get(i)).setPreferredSize((Dimension) maxSize.clone());
            ((JButton) lbuttons.get(i)).setMaximumSize((Dimension) maxSize.clone());
        }
    }

    private void ok() {
        if (pneOKCancel.onOK()) {
            okPressed = true;
            dispose();
        }
    }

    private void cancel() {
        if (pneOKCancel.onCancel()) {
            dispose();
        }
    }

    private void close() {
        if (pneOKCancel.onClose()) {
            dispose();
        }
    }

    private void help() {
        pneOKCancel.onHelp();
    }

    private void apply() {
        pneOKCancel.onApply();
    }

    public boolean okPressed() {
        return okPressed;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("test dlg");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar mb = new JMenuBar();
        frame.setJMenuBar(mb);
        JMenu menu = new JMenu("TestI18n");
        mb.add(menu);
        JMenuItem mi = new JMenuItem("Dialog TestI18n");
        mi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int buttons = OK_BUTTON | APPLY_BUTTON | CANCEL_BUTTON | HELP_BUTTON | CLOSE_BUTTON;
                BaseOKCancelDialog dlg = new BaseOKCancelDialog(frame, "test", null, buttons, true);
                BaseOKCancelPanel panel = new BaseOKCancelPanel();
                panel.setPreferredSize(new Dimension(600, 300));
                panel.add(new JButton("test"));
                dlg.setOKCancelPanel(panel);
                dlg.show();
            }
        });
        menu.add(mi);
        frame.setSize(300, 300);
        frame.validate();
        frame.setVisible(true);
    }

}
