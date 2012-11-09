package vista;

import wait.UIActivator;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 12:04:13 PM
 */
public class VistaDialog extends JDialog {

    private VistaDialogContent content;
    private boolean autoDispose = true;
    private UIActivator activator;
    private int defaultCloseOperation = -1;
    private Icon baseIcon;
    private JButton currentButton;
    private boolean escapeOption = false;
    private List<Component> focusComponents;

    public VistaDialog(VistaDialogContent content) {
        super();
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Frame owner) {
        super(owner);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Frame owner, boolean modal) {
        super(owner, modal);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Frame owner, String title) {
        super(owner, title);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Dialog owner) {
        super(owner);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Dialog owner, boolean modal) {
        super(owner, modal);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Dialog owner, String title) {
        super(owner, title);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Window owner) {
        super(owner);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Window owner, ModalityType modalityType) {
        super(owner, modalityType);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Window owner, String title) {
        super(owner, title);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
        this.content = content;
        init();
    }

    public VistaDialog(VistaDialogContent content, Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
        this.content = content;
        init();
    }

    private void init() {

        setLayout(new GridBagLayout());
        focusComponents = new ArrayList<Component>();

        System.out.println("policy=" +getFocusTraversalPolicy());

        activator = new UIActivator(rootPane, "") {
            public void start() {
                //super.start();
                unregister();
            }

            public void stop() {
                //super.stop();
                register();                
            }
        };
        activator.disableProgressBar();

        JPanel internalPanel = new JPanel();
        internalPanel.setLayout(new GridBagLayout());
        internalPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        JLabel textLabel = new JLabel(getHtmlText(content.getText()));
        textLabel.setForeground(VistaUtil.TEXT_FOREGROUND);
        JLabel descLabel = new JLabel(getHtmlDescription(content.getDescription()));
        //descLabel.setForeground(VistaUtil.BACKGROUND.darker());        

        internalPanel.add(textLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
        if (content.getDescription() != null) {
            internalPanel.add(descLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        }

        List<VistaButton> list = content.getButtons();
        if (list != null) {
            for (int j = 0, size = list.size(); j < size; j++) {
                int bottom = 0;
                if (j == size - 1) {
                    bottom = 10;
                }
                final VistaButton button = list.get(j);
                focusComponents.add(button);
                internalPanel.add(button, new GridBagConstraints(0, j + 2, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, bottom, 10), 0, 0));
            }

            register();

            add(internalPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        }
    }

    private void registerEscape() {
        getRootPane().registerKeyboardAction(new EscapeAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void unregisterEscape() {
        getRootPane().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
    }

    private void register() {

        setFocusTraversalPolicy(new VistaFocusTraversalPolicy(focusComponents));

        if (defaultCloseOperation != -1) {
            setDefaultCloseOperation(defaultCloseOperation);
            defaultCloseOperation = -1;
        }

        // register escape keyboard action
        if (escapeOption) {
            registerEscape();
        }

        List<VistaButton> list = content.getButtons();
        if (list != null) {

            for (int j = 0, size = list.size(); j < size; j++) {

                final VistaButton button = list.get(j);

                // register keyboard action for button
                if (button.getKeyStroke() != null) {
                    getRootPane().registerKeyboardAction(new VistaButtonAction(button), button.getKeyStroke(),
                            JComponent.WHEN_IN_FOCUSED_WINDOW);
                }
                button.addMouseListener(new VistaMouseAdapter(button));
            }

            // register enter keyboard action (action of the selected button will be called)
            getRootPane().registerKeyboardAction(new EnterAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
            // register up/down keyboard actions (button selection)
            getRootPane().registerKeyboardAction(new UpDownAction(true), KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
            getRootPane().registerKeyboardAction(new UpDownAction(false), KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    // unregister keyboard keys and listeners
    private void unregister() {

        // disable focus traversal policy
        setFocusTraversalPolicy(null);

        defaultCloseOperation = getDefaultCloseOperation();        
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        unregisterEscape();

        List<VistaButton> list = content.getButtons();

        if (list != null) {
            for (int j = 0, size = list.size(); j < size; j++) {
                final VistaButton button = list.get(j);
                // unregister keyboard action for button
                if (button.getKeyStroke() != null) {
                    getRootPane().unregisterKeyboardAction(button.getKeyStroke());
                }
                for (MouseListener listener : button.getMouseListeners()) {
                    button.removeMouseListener(listener);
                }
            }

            getRootPane().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            getRootPane().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
            getRootPane().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        }
    }

    // This method allows the selection of a button at start . Must take care of the focus traversal policy
    //
    // If first button is selected -> no need to change focus traversal policy
    // We must rotate the collection so that the selected button become first in the collection
    //
    // 0 -> 0
    // 1 -> 3
    // 2 -> 2
    // 3 -> 1
    public void selectButton(VistaButton button) {
        int index = getIndex(button);
        if (index > 0) {
            index = focusComponents.size() - index;
            Collections.rotate(focusComponents, index);
            setFocusTraversalPolicy(new VistaFocusTraversalPolicy(focusComponents));
        }
        internalSelectButton(button);
    }

    private int getIndex(VistaButton button) {
        for (int i=0, size = content.getButtons().size(); i<size; i++) {
            VistaButton b = content.getButtons().get(i);
            if (b.equals(button)) {
                return i;
            }
        }
        return 0;
    }

    private void internalSelectButton(VistaButton button) {

        currentButton = button;

        List<VistaButton> list = content.getButtons();
        for (VistaButton vb : list) {
            vb.setSelected(false);
        }

        // this is needed for focus traversal, after UP / DOWN the TAB must continue
        // from the selected button
        button.requestFocus();
                
        button.setSelected(true);
    }

    private String getHtmlText(String text) {
        if (VistaUtil.isHtml(text)) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<p>");
        sb.append("<font size=\"4\">");
        sb.append(text);
        sb.append("</font>");
        sb.append("</p>");
        sb.append("</html>");
        return sb.toString();
    }

    private String getHtmlDescription(String description) {
        if (VistaUtil.isHtml(description)) {
            return description;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<p>");
        sb.append("<font size=\"2\">");
        sb.append(description);
        sb.append("</font>");
        sb.append("</p>");
        sb.append("</html>");
        return sb.toString();
    }

    public void setDispose(boolean dispose) {
        autoDispose = dispose;
    }

    public void setEscapeOption(boolean escapeOption) {
        this.escapeOption = escapeOption;
        if (escapeOption) {
            registerEscape();
        } else {
            unregisterEscape();
        }
    }


    // action takes care also about button selection and dialog dispose
    private class VistaButtonAction extends AbstractAction {

        private VistaButton button;

        private VistaButtonAction(VistaButton button) {
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            if (button == null) {
                return;
            }
            currentButton = button;
            baseIcon = button.getActionIcon();
            // change icon to an animated one
            button.setIcon(new ImageIcon(VistaButton.class.getResource("animatedProgress.gif")));
            Action buttonAction = button.getAction();
            if (buttonAction != null) {
                internalSelectButton(button);
                if (buttonAction instanceof VistaAction) {                    
                    ((VistaAction) buttonAction).setActivator(activator);
                    activator.start();                    
                }
                buttonAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), button.getActionName()));
                // if VistaAction the dispose is done inside the action perform
                if (!(buttonAction instanceof VistaAction)) {
                    dispose();
                }
            }
        }
    }

    public void dispose() {
        // restore icon
        if (currentButton != null) {
            if (baseIcon != null) {
                currentButton.setIcon(baseIcon);
                baseIcon = null;
            }
            currentButton = null;            
        }
        if (autoDispose) {
            super.dispose();
        }
    }


    private class EscapeAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            // must escape in any situation (even if autoDispose was set to false)
            autoDispose = true;
            dispose();
        }
    }

    // represents the VistaButtonAction of the selected button
    private class EnterAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            VistaButton button = null;
            List<VistaButton> list = content.getButtons();
            for (VistaButton vb : list) {
                if (vb.isSelected()) {
                    button = vb;
                    break;
                }
            }
            if (button == null) {
                return;
            }
            new VistaButtonAction(button).actionPerformed(new ActionEvent(e.getSource(), e.getID(), button.getActionName()));
        }
    }

    private class UpDownAction extends AbstractAction {

        boolean up = true;

        private UpDownAction(boolean up) {
            this.up = up;
        }

        public void actionPerformed(ActionEvent e) {
            VistaButton button = null;
            List<VistaButton> list = content.getButtons();
            for (int i = 0, size = list.size(); i < size; i++) {
                VistaButton vb = list.get(i);
                if (vb.isSelected()) {
                    if (up) {
                        if (i == 0) {
                            button = list.get(size - 1);
                        } else {
                            button = list.get(i - 1);
                        }
                    } else {
                        if (i == size - 1) {
                            button = list.get(0);
                        } else {
                            button = list.get(i + 1);
                        }
                    }
                    break;
                }
            }
            if (button == null) {
                button = list.get(0);
            }
            internalSelectButton(button);
        }
    }

    private class VistaMouseAdapter extends MouseAdapter {

        private VistaButton button;

        private VistaMouseAdapter(VistaButton button) {
            this.button = button;
        }

        public void mouseEntered(MouseEvent event) {
            internalSelectButton(button);
        }

        public void mouseExited(MouseEvent e) {
            button.setSelected(false);
        }

        public void mouseClicked(MouseEvent e) {
            new VistaButtonAction(button).actionPerformed(
                    new ActionEvent(e.getSource(), e.getID(), button.getActionName()));
        }
    }


}
