package collapsible;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 11:11:04 AM
 */

import java.awt.*;
import javax.swing.*;


/**
 * see also: Collapsible CollapseButton
 */

public class CollapsiblePanel extends JPanel implements Collapsible {
    private boolean _collapsed = false;
    private GridBagConstraints _constraints;

    private CollapseButton _collapseHorizontalButton;
    private CollapseButton _collapseVerticalButton;

    private JPanel _content;
    private JPanel _spring;

    public CollapsiblePanel(JPanel content) {
        super();

        _content = content;

        setLayout(new GridBagLayout());

        _constraints = new GridBagConstraints();

        _collapseHorizontalButton = new CollapseButton(this, SwingConstants.HORIZONTAL);
        _collapseVerticalButton = new CollapseButton(this, SwingConstants.VERTICAL);

        _spring = new JPanel();

        expand();
    }

    /**
     * Collapses the panel.
     */
    public void collapse() {

        setVisible(false);

        removeAll();

        _constraints.gridx = 0;
        _constraints.gridy = 0;
        _constraints.gridheight = 1;
        _constraints.gridwidth = 1;
        _constraints.ipadx = 0;
        _constraints.ipady = 0;
        _constraints.weightx = 0;
        _constraints.weighty = 0;
        _constraints.insets = new Insets(0, 0, 0, 0);
        _constraints.anchor = GridBagConstraints.NORTHWEST;
        _constraints.fill = GridBagConstraints.NONE;

        add(_collapseHorizontalButton, _constraints);

        // fix: does this make sense?
        Dimension dim = _collapseHorizontalButton.getSize();
        _collapseHorizontalButton.reshape(0, 0, dim.width, dim.height);

        _constraints.gridx = GridBagConstraints.RELATIVE;
        _constraints.gridy = 0;
        _constraints.gridheight = GridBagConstraints.REMAINDER;
        _constraints.gridwidth = GridBagConstraints.REMAINDER;
        _constraints.weightx = 1.0;
        _constraints.weighty = 1.0;
        _constraints.anchor = GridBagConstraints.WEST;
        _constraints.fill = GridBagConstraints.BOTH;

        add(_spring, _constraints);

        _collapsed = true;

        revalidate();
        setVisible(true);
    }

    /**
     * Uncollapses the panel.
     */

    public void expand() {

        setVisible(false);
        removeAll();

        _constraints.gridx = 0;
        _constraints.gridy = 0;
        _constraints.gridheight = 1;
        _constraints.gridwidth = 1;
        _constraints.ipadx = 0;
        _constraints.ipady = 0;
        _constraints.weightx = 0;
        _constraints.weighty = 0;
        _constraints.insets = new Insets(0, 0, 0, 0);
        _constraints.anchor = GridBagConstraints.NORTHWEST;
        _constraints.fill = GridBagConstraints.NONE;

        add(_collapseVerticalButton, _constraints);

        // fix: does this make sense?
        Dimension dim = _collapseVerticalButton.getSize();
        _collapseVerticalButton.reshape(0, 0, dim.width, dim.height);

        //    constraints.insets = new Insets(5,5,5,5);
        _constraints.gridx = GridBagConstraints.RELATIVE;
        _constraints.gridy = 0;
        _constraints.gridheight = GridBagConstraints.REMAINDER;
        _constraints.gridwidth = GridBagConstraints.REMAINDER;
        _constraints.weightx = 1.0;
        _constraints.weighty = 1.0;
        _constraints.anchor = GridBagConstraints.WEST;
        _constraints.fill = GridBagConstraints.BOTH;

        add(_content, _constraints);

        _collapsed = false;

        revalidate();
        setVisible(true);
        // does toggling setVisible make a difference?
    }

    /**
     * Tells you whether this component is collapsible.
     *
     * @return a boolean indicating this component is collapsible.
     */

    public boolean isCollapsible() {
        return collapsible;
    }

    /**
     * Tells you whether this component is currently collapsed. Useful for checking the component's status.
     *
     * @return true if this component is collapsed, false if it is not.
     */

    public boolean isCollapsed() {
        return _collapsed;
    }

}