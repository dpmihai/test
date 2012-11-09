package validation;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 2, 2006
 * Time: 4:37:42 PM
 */
public class LengthValidator extends AbstractValidator {

    private int len;

    public LengthValidator(JDialog parent, JTextField c, String message, int len) {
        super(parent, c, message);
        this.len = len;
    }

    protected boolean validationCriteria(JComponent c) {
        if (((JTextField)c).getText().length() > len)
            return false;
        return true;
    }
}