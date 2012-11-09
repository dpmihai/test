package menu;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 28, 2006
 * Time: 11:41:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class JMenuExpand extends JMenu  {

    // all menu items
    private Set<JMenuItem> allSet = new HashSet<JMenuItem>();
    // seen menu items
    private Set<JMenuItem> seeSet = new HashSet<JMenuItem>();


    public JMenuExpand(String name) {
        super(name);
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                call();
            }
        });
    }

    public JMenuItem add(JMenuItem menu) {
        JMenuItem r = super.add(menu);
        allSet.add(menu);
        seeSet.add(menu);
        return r;
    }

    public JMenuItem add(JMenuItem menu, boolean visible) {
        JMenuItem r = super.add(menu);
        allSet.add(menu);
        if (visible) {
            seeSet.add(menu);
        }
        return r;
    }

    private void call() {        
        // compute maxWidth : used by expand icon (to draw it centered)
        int maxWidth = 0;

        for (JMenuItem aSeeSet : seeSet) {
            int w = (int)aSeeSet.getPreferredSize().getWidth();
            if (w > maxWidth) {
                maxWidth = w;
            }
        }

        allSet.removeAll(seeSet);

        // make unseen items personalized
        for (JMenuItem aAllSet : allSet) {
            JMenuExpandItem.setPersonalized(aAllSet);
        }

        if (allSet.size() > 0) {
            // add separator and expandItem
            addSeparator();
            add(new JMenuExpandItem(maxWidth));
        }
    }
}
