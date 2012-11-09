package filter;

import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Mar 14, 2005 Time: 1:27:59 PM To change this template use File
 * | Settings | File Templates.
 */
public class MYInputDialog extends InputDialog {

    private static final String lblText = "Date MM/YYYY :";
    private static final String pattern = "0([1-9](/(\\d{0,4})?)?)?|1([0-2](/(\\d{0,4})?)?)?";

    /**
     * Constructor : this dialog accepts inputs for Month/Year like MM/YYYY
     * where MM can be 01 to 12 and YYYY any four digits
     *
     * Input must be full entered (7 characters) so that OK button be activated
     *
     * @param parent frameanimation parent
     * @param title dialog title
     * @param modal true if dialog is modal
     */
    public MYInputDialog(Frame parent, String title, boolean modal) {
           super(parent, title, lblText, pattern, 7, modal);
    }
}
