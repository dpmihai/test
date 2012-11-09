package lazy;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: May 4, 2005 Time: 10:06:49 AM
 */

import java.awt.*;
import javax.swing.*;

public class BusyPanel extends LazyPanel {
    public BusyPanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    protected void lazyConstructor() {
        setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows * cols; ++i) {
            add(new JButton(Integer.toString(i + startValue)));
            ++startValue;
        }
    }


    static private int startValue = 0;

    private int rows;
    private int cols;
}