package collapsible;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 11:12:33 AM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * fix: get Icons from luxor.jar don't use xul itself
 */

class CollapseButton extends JButton implements ActionListener {
    private ImageIcon collapseButtonIconVertical;
    private ImageIcon collapseButtonVerticalRollover;
    private ImageIcon collapseButtonVerticalPressed;
    private ImageIcon collapseButtonIconHorizontal;
    private ImageIcon collapseButtonHorizontalRollover;
    private ImageIcon collapseButtonHorizontalPressed;

    private final String EXPANDED = "expanded.gif";
    private final String COLLAPSED = "collapsed.gif";
    private Collapsible _collapsible;

    public CollapseButton(Collapsible collapsible, int orientation) {
        super();
        _collapsible = collapsible;

        // get icons
/*
    <icon id="COLLAPSE_BUTTON_VERTICAL_ICON"            ref="images/collapseButton-vertical.gif" />
    <icon id="COLLAPSE_BUTTON_VERTICAL_ROLLOVER_ICON"   ref="images/collapseButton-vertical-rollover.gif" />
    <icon id="COLLAPSE_BUTTON_VERTICAL_PRESSED_ICON"    ref="images/collapseButton-vertical-pressed.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_ICON"          ref="images/collapseButton-horizontal.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_ROLLOVER_ICON" ref="images/collapseButton-horizontal-rollover.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_PRESSED_ICON"  ref="images/collapseButton-horizontal-pressed.gif" />

    <icon id="COLLAPSE_BUTTON_VERTICAL_ICON"            ref="images/expanded.gif" />
    <icon id="COLLAPSE_BUTTON_VERTICAL_ROLLOVER_ICON"   ref="images/expanded.gif" />
    <icon id="COLLAPSE_BUTTON_VERTICAL_PRESSED_ICON"    ref="images/expanded.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_ICON"          ref="images/collapsed.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_ROLLOVER_ICON" ref="images/collapsed.gif" />
    <icon id="COLLAPSE_BUTTON_HORIZONTAL_PRESSED_ICON"  ref="images/collapsed.gif" />
*/

        // fix: use properties to let user change icons
        collapseButtonIconVertical = new ImageIcon(CollapseButton.class.getResource(EXPANDED));
        collapseButtonVerticalRollover = new ImageIcon(CollapseButton.class.getResource(EXPANDED));
        collapseButtonVerticalPressed = new ImageIcon(CollapseButton.class.getResource(EXPANDED));
        collapseButtonIconHorizontal = new ImageIcon(CollapseButton.class.getResource(COLLAPSED));
        collapseButtonHorizontalRollover = new ImageIcon(CollapseButton.class.getResource(COLLAPSED));
        collapseButtonHorizontalPressed = new ImageIcon(CollapseButton.class.getResource(COLLAPSED));

        setRolloverEnabled(true);
        setFocusPainted(false);
        setDefaultCapable(false);
        setBorder(null);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setToolTipText("Collapses/Expands Panel");

        if (orientation == SwingConstants.VERTICAL) {
            setIcon(collapseButtonIconVertical);
            setRolloverIcon(collapseButtonVerticalRollover);
            setPressedIcon(collapseButtonVerticalPressed);
        } else // if it is not VERTICAL, it's HORIZONTAL
        {
            setIcon(collapseButtonIconHorizontal);
            setRolloverIcon(collapseButtonHorizontalRollover);
            setPressedIcon(collapseButtonHorizontalPressed);
        }

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        if (_collapsible.isCollapsed() == true)
            _collapsible.expand();
        else
            _collapsible.collapse();
    }

}