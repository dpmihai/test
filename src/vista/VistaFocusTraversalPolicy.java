package vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 20, 2007
 * Time: 11:42:31 AM
 */
public class VistaFocusTraversalPolicy extends FocusTraversalPolicy {

    // ordered list of components
    List<Component> comps = new ArrayList<Component>();    

    public VistaFocusTraversalPolicy(List<Component> theComponents) {
        comps = theComponents;
        if (!comps.isEmpty()) {
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comps.get(0).requestFocusInWindow();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Component getDefaultComponent(Container focusCycleRoot) {
        return ((comps.size() > 0) ? comps.get(0) : null);
    }

    public Component getFirstComponent(Container focusCycleRoot) {
        return ((comps.size() > 0) ? comps.get(0) : null);
    }

    public Component getLastComponent(Container focusCycleRoot) {
        return ((comps.size() > 0) ? comps.get(comps.size() - 1) : null);
    }

    public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
        for (int i = 0; i < comps.size(); i++) {
            if (aComponent.equals(comps.get(i)) || aComponent.getParent().equals(comps.get(i))) {
                if (i == comps.size() - 1) {
                    // if the last components
                    Container anc = focusCycleRoot.getFocusCycleRootAncestor();
                    if (anc != null) {
                        Component c = anc.getFocusTraversalPolicy().getComponentAfter(anc, focusCycleRoot);
                        if (c instanceof Container) {
                            if (((Container) c).getFocusTraversalPolicy() != null) {
                                return ((Container) c).getFocusTraversalPolicy().getFirstComponent((Container)c);
                            }
                        }
                        select(c);
                        return c;
                    } else {
                        select(comps.get(0));
                        return comps.get(0);
                    }
                } else {
                    select(comps.get(i + 1));
                    return comps.get(i + 1);
                }
            }
        }
        select(comps.get(0));
        return comps.get(0);
    }

    public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
        for (int i = 0, size = comps.size(); i < size; i++) {
            if (aComponent.equals(comps.get(i)) || aComponent.getParent().equals(comps.get(i)) &&
                            aComponent.isEnabled()) {
                if (i == 0) {
                    //if the first component
                    Container anc = focusCycleRoot.getFocusCycleRootAncestor();
                    if (anc != null) {
                        Component c = anc.getFocusTraversalPolicy().getComponentBefore(anc, focusCycleRoot);
                        if (c instanceof Container) {
                            if (((Container) c).getFocusTraversalPolicy() != null) {
                                return ((Container) c).getFocusTraversalPolicy().getLastComponent((Container)c);
                            }
                        }
                        select(c);
                        return c;
                    } else {
                        select(comps.get(size-1));
                        return comps.get(size-1);
                    }
                } else {
                    select(comps.get(i - 1));
                    return comps.get(i - 1);
                }
            }
        }
        select(comps.get(0));
        return comps.get(0);
    }

    // make our custom selection of components
    private void select(Component component) {
        for (Component comp : comps) {
            if (comp instanceof VistaButton) {
                ((VistaButton) comp).setSelected(false);
            }
        }
        if (component instanceof VistaButton) {               
            ((VistaButton) component).setSelected(true);
        }

    }
}