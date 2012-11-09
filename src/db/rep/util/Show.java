package db.rep.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;



/**
 * @author Decebal Suiu
 */
public class Show {
    
    private static final String DATE_SEPATATOR = ".";
    private static NumberFormat nf = NumberFormat.getInstance(new Locale("ro",
                "RO"));

    public static void error(String message) {
        JOptionPane.showMessageDialog(Globals.getMainFrame(), message,
            "Eroare", JOptionPane.ERROR_MESSAGE);
    }

    public static void error(Exception e) {
        e.printStackTrace();
        error(e.getMessage());
    }

    public static void info(String message) {
        JOptionPane.showMessageDialog(Globals.getMainFrame(), message);
    }

    public static String asDMY(Date d) {
        String s = "";

        if (d == null) {
            return s;
        }

        // todo e nevoie de adus de pe server ?
        //        Calendar c = Cache.getCalendar();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        int day;
        int month;
        int year;
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        s = ((day > 9) ? ("" + day) : ("0" + day)) + DATE_SEPATATOR +
            ((month > 9) ? ("" + month) : ("0" + month)) + DATE_SEPATATOR +
            year;

        return s;
    }

    public static String asDMYHM(Date d) {
        if (d == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd" + DATE_SEPATATOR +
                "MM" + DATE_SEPATATOR + "yyyy HH:mm");

        return sdf.format(d);
    }

    public static String asNr1000(double val, boolean groupingUsed) {
        boolean oldGroupingUsed = nf.isGroupingUsed();
        nf.setGroupingUsed(groupingUsed);

        String ret = nf.format(val);
        nf.setGroupingUsed(oldGroupingUsed);

        return ret;
    }

    public static String asNr1000(Double val) {
        if (val == null) {
            return "";
        }

        return nf.format(val.doubleValue());
    }

    public static void equalizeComponentSizes(java.util.List components) {
        // Get the largest width and height
        Dimension maxSize = new Dimension(0, 0);

        for (int i = 0; i < components.size(); i++) {
            Dimension d = ((JComponent) components.get(i)).getPreferredSize();
            maxSize.width = Math.max(maxSize.width, d.width);
            maxSize.height = Math.max(maxSize.height, d.height);
        }

        // set the minimum, preferred and maximum size
        for (int i = 0; i < components.size(); ++i) {
            JComponent c = (JComponent) components.get(i);
            c.setMinimumSize((Dimension) maxSize.clone());
            c.setPreferredSize((Dimension) maxSize.clone());
            c.setMaximumSize((Dimension) maxSize.clone());
        }
    }

    public static void centrateComponent(Component c) {
        Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dimComp = c.getSize();
        c.setLocation((dimScreen.width - dimComp.width) / 2,
            (dimScreen.height - dimComp.height) / 2);
    }

    public static void centrateComponent(Component parent, Component c) {
        Dimension dimParent = parent.getSize();
        Point p = parent.getLocation();
        Dimension dimComp = c.getSize();
        c.setLocation(p.x + ((dimParent.width - dimComp.width) / 2),
            p.y + ((dimParent.height - dimComp.height) / 2));
    }

    public static void pack(Window window) {
        Dimension dim = window.getPreferredSize();
        int prefw = dim.width;
        int w = window.getWidth();
        if (w < prefw) {
            w = prefw;
        }
        int prefh = dim.height;
        int h = window.getHeight();
        if (h < prefh) {
            h = prefh;
        }
        window.setSize(w, h);
    }
    
    public static FocusTraversalPolicy createFocusTraversalPolicy(
        final Component[] components) {
        return new FocusTraversalPolicy() {
                private List componentsList = Arrays.asList(components);

                public Component getInitialComponent(Window window) {
                    return (Component) componentsList.get(0);
                }

                public Component getDefaultComponent(Container focusCycleRoot) {
                    return (Component) componentsList.get(0);
                }

                public Component getFirstComponent(Container focusCycleRoot) {
                    return (Component) componentsList.get(0);
                }

                public Component getLastComponent(Container focusCycleRoot) {
                    return (Component) componentsList.get(componentsList.size() -
                        1);
                }

                public Component getComponentAfter(Container focusCycleRoot,
                    Component aComponent) {
                    if (componentsList.indexOf(aComponent) == -1) {
                        Component parent = findFocusableParent(aComponent);

                        if (parent == null) {
                            return getFirstComponent(focusCycleRoot);
                        } else {
                            return getComponentAfter(focusCycleRoot, parent);
                        }
                    }

                    return findFocusableComponent(aComponent, true);
                }

                public Component getComponentBefore(Container focusCycleRoot,
                    Component aComponent) {
                    if (componentsList.indexOf(aComponent) == -1) {
                        Component parent = findFocusableParent(aComponent);

                        if (parent == null) {
                            return getLastComponent(focusCycleRoot);
                        } else {
                            return getComponentBefore(focusCycleRoot, parent);
                        }
                    }

                    return findFocusableComponent(aComponent, false);
                }

                /**
                 * Finds the parent of <code>focusedComponent</code> that was added
                 * to this focus traversal policy, if any. If you add e.g. an
                 * editable combo box to a focus traversal policy, the combo box
                 * will never be the real focus owner; instead, it's editor will.
                 * @param focusedComponent the component that currently owns the
                 * focus
                 * @return the parent, or <code>null</code>
                 */
                private Component findFocusableParent(
                    Component focusedComponent) {
                    Component parent = focusedComponent.getParent();

                    while (parent != null) {
                        if (componentsList.indexOf(parent) != -1) {
                            return parent;
                        }

                        parent = parent.getParent();
                    }

                    return null;
                }

                /**
                 * Finds the next component in this cycle, which is displayable,
                 * visible, focusable and enabled. Text components must also be
                 * editable.
                 * @param aComponent the focus owner
                 * @param after the cycling direction
                 * @return the next component
                 */
                private Component findFocusableComponent(Component aComponent,
                    boolean after) {
                    int index = componentsList.indexOf(aComponent);

                    // next component
                    if (after) {
                        if (index == (componentsList.size() - 1)) {
                            // wrap around
                            index = 0;
                        } else {
                            index++;
                        }
                    } else {
                        if (index == 0) {
                            // wrap around
                            index = componentsList.size() - 1;
                        } else {
                            index--;
                        }
                    }

                    aComponent = (Component) componentsList.get(index);

                    if (!aComponent.isDisplayable()) {
                        return findFocusableComponent(aComponent, after);
                    }

                    if (!aComponent.isVisible()) {
                        return findFocusableComponent(aComponent, after);
                    }

                    if (!aComponent.isFocusable()) {
                        return findFocusableComponent(aComponent, after);
                    }

                    if (aComponent instanceof JTextComponent) {
                        JTextComponent txt = (JTextComponent) aComponent;

                        if (!txt.isEditable() || !txt.isEnabled()) {
                            return findFocusableComponent(aComponent, after);
                        }
                    }

                    if (!aComponent.isEnabled()) {
                        return findFocusableComponent(aComponent, after);
                    }

                    return aComponent;
                }
            };
    }
    
}
