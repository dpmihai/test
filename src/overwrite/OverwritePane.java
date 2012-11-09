package overwrite;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 1, 2005 Time: 10:49:24 AM
 */
public class OverwritePane extends JTextPane {


    public static int INSERT = 0;
    public static int OVERWRITE = 1;
    protected int mode;

    public static final String TYPING_MODE_CHANGED_PROPERTY = "TypingModeChanged";

    public OverwritePane() {
        super();
        initializePane();
    }

    public OverwritePane(StyledDocument doc) {
        super(doc);
        initializePane();
    }

    private void initializePane() {
        mode = INSERT;
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                    toggleTypingMode();
                }
            }
        });

        // all editors display another caret when in overwrite mode
        // replace it
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        Caret c = new DefaultCaret() {

            public void paint(Graphics g) {
                if (mode == INSERT) {
                    super.paint(g);
                    return;
                }
                JTextComponent comp = getComponent();

                int dot = getDot();
                Rectangle r = null;
                char c;
                try {
                    r = comp.modelToView(dot);
                    if (r == null)
                        return;
                    c = comp.getText(dot, 1).charAt(0);
                } catch (BadLocationException e) {
                    return;
                }

                // erase provious caret
                if ((x != r.x) || (y != r.y)) {
                    repaint();
                    x = r.x;
                    y = r.y;
                    height = r.height;
                }

                g.setColor(comp.getCaretColor());
                g.setXORMode(comp.getBackground());

                width = g.getFontMetrics().charWidth(c);
                if (c == '\t' || c == '\n')
                    width = g.getFontMetrics().charWidth(' ');
                if (isVisible()) {
                    g.fillRect(r.x, r.y, width, r.height);
                }
            }
        };
        c.setBlinkRate(500); // default rate
        setCaret(c);

    }

    public int getTypingMode() {
        return mode;
    }

    public void setTypingMode(int newMode) {
        int tmp = mode;
        mode = newMode;

        firePropertyChange(TYPING_MODE_CHANGED_PROPERTY,
                tmp, mode);

    }

    public void toggleTypingMode() {
        if (mode == INSERT) {
            setTypingMode(OVERWRITE);
        } else {
            setTypingMode(INSERT);
        }
    }


    public void setDocument(Document doc) {
        super.setDocument(new OverwritableDocument(this, doc));
    }

}
