package db.rep.std;

import javax.swing.JPanel;

/**
 * @author Decebal Suiu
 */ 
public class BaseOKCancelPanel extends JPanel {

    public BaseOKCancelPanel() {
        super();
    }

    public boolean onOK() {
        return true;
    }

    public boolean onCancel() {
        return true;
    }

    public boolean onClose() {
        return true;
    }

    public boolean onHelp() {
        return true;
    }

    public boolean onApply() {
        return true;
    }

}
