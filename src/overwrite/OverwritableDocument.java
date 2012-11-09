package overwrite;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 1, 2005 Time: 10:52:33 AM
 */
public class OverwritableDocument extends DefaultStyledDocument {

    private OverwritePane pane;

    public OverwritableDocument(OverwritePane pane) {
        this.pane = pane;
    }

    public OverwritableDocument(OverwritePane pane, Document wrapped) {
        this(pane);
        try {
            if (wrapped != null) {
                // the following line will
                // unfortunately drop attributes if
                // wrapped is a StyledDocument
                insertString(0, wrapped.getText(0,
                        wrapped.getLength()), null);
            }
        } catch (BadLocationException e) {
            // will never occur
        }
    }

    public void insertString(int offset, String str,
                             AttributeSet as) throws BadLocationException {
        if (pane.getTypingMode() == OverwritePane.OVERWRITE && str.length() == 1 && offset != getLength()) {
            remove(offset, 1);
        }
        super.insertString(offset, str, as);
    }

}

