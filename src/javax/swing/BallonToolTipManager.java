package javax.swing;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.border.Border;

import balloon.JBalloonToolTip;

/*
 * @(#)BallonToolTipManager.java         1.00
 *
 * User: sv
 * Date: Aug 2, 2004
 * Time: 10:31:58 PM
 *
 * Copyright 2004 jballoon.net.  All rights reserved.
 */


/**
 * The  <code>BallonToolTipManager</code>
 *
 *
 *
 *
 * @author  Serge S. Vasiljev
 * @version 1.00, Aug 2, 2004
 */
public class BallonToolTipManager extends ToolTipManager2 {

    // ballon pointer fields
    private final int POINTER_HEIGHT = 10;
    private final int POINTER_WIDTH = 10;
    private Popup[] pWindows;
    private JToolTip[] pTips;

    // ballon sides fields
    private final int SIDE_WIDTH = 5;
    private Popup[] sWindows;
    private JToolTip[] sTips;


    BallonToolTipManager() {
        super();
    }

    public void mouseMoved(MouseEvent event) {
        if (tipShowing) {
            checkForTipChange(event);
        }

        // this fork disabled to avoid effect of "losted ballons" in case of mouse crossed the borders of components
        // and the tooltip at this time is still active

//        else if (showImmediately) {
//            JComponent component = (JComponent) event.getSource();
//            toolTipText = component.getToolTipText(event);
//            System.out.println("immediately");
//            if (toolTipText != null) {
//                System.out.println("tooltups!+null");
//                preferredLocation = component.getToolTipLocation(event);
//                mouseEvent = event;
//                insideComponent = component;
//                exitTimer.stop();
//                showTipWindow();
//            }
//        }
        else {
            // Lazily lookup the values from within insideTimerAction
            insideComponent = (JComponent) event.getSource();
            mouseEvent = event;
            toolTipText = null;
            enterTimer.restart();
        }
    }

    void showTipWindow() {

        if (insideComponent == null || !insideComponent.isShowing())
            return;

        if (enabled) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Point screenLocation = insideComponent.getLocationOnScreen();

            Dimension size;
            Point location;

            // Just to be paranoid (original SUN's paranoja :)
            hideTipWindow();

            // create tip
            tip = (JBalloonToolTip.isBalloonDefault()) ? JBalloonToolTip.createToolTip(insideComponent) : insideComponent.createToolTip();
            tip.setTipText(toolTipText);


            if (!(tip instanceof JBalloonToolTip)) {
                // execute original code
                super.showTipWindow();
                return;
            }


            size = tip.getPreferredSize();

            // get preffered location or mouse event as event source
            Point eventSource = mouseEvent.getPoint();

            // precalculate location popUp box location
            location = new Point();
            location.x = screenLocation.x + eventSource.x - SIDE_WIDTH * 2;
            location.y = screenLocation.y + eventSource.y - (size.height + POINTER_HEIGHT);

            // check if this location\size still fit in screen
            int tmp;

            // check most top point -> flip if it necessary
            location.y = (location.y <= 0) ? screenLocation.y + eventSource.y + (POINTER_HEIGHT + 20) : location.y;

            // check most right point
            tmp = location.x + size.width + SIDE_WIDTH * 2;
            if (tmp >= screenSize.width) {
                // flip horisontal from right to the left
                location.x = location.x - (tmp - screenSize.width);
            }

            // check most left point
            tmp = location.x - SIDE_WIDTH;
            if (tmp < 0) {
                location.x = SIDE_WIDTH + 1; // +1 -> to have a little gap from the right side
            }

            PopupFactory popupFactory = PopupFactory.getSharedInstance();

            if (lightWeightPopupEnabled) {
                popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);
            } else {
                popupFactory.setPopupType(PopupFactory.MEDIUM_WEIGHT_POPUP);
            }
            tipWindow = popupFactory.getPopup(insideComponent, tip,
                    location.x,
                    location.y);
            popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);

            tipWindow.show();
            Rectangle r = new Rectangle(tipWindow.getComponent().getLocationOnScreen(), tipWindow.getComponent().getSize());
            Point eventPoint = new Point(eventSource.x + screenLocation.x, eventSource.y + screenLocation.y);

            // show balloon now :)
            showBallon(r, eventPoint);

            Window componentWindow = SwingUtilities.windowForComponent(
                    insideComponent);

            window = SwingUtilities.windowForComponent(tip);
            if (window != null && window != componentWindow) {
                window.addMouseListener(this);
            } else {
                window = null;
            }

            insideTimer.start();
            tipShowing = true;
        }
    }

    void hideTipWindow() {
        if (tip instanceof JBalloonToolTip)
            hideBallon();

        if (tipWindow != null) {
            if (window != null) {
                window.removeMouseListener(this);
                window = null;
            }
            tipWindow.hide();
            tipWindow = null;
            tipShowing = false;
            (tip.getUI()).uninstallUI(tip);
            tip = null;
            insideTimer.stop();
        }
    }

    private void showBallon(Rectangle tipRectangle, Point eventPoint) {
        // show pointer and balloon's sides
        showBallonPointer(tipRectangle, eventPoint);
        showBallonSides(tipRectangle);
    }

    private void hideBallon() {
        // hide in the order opposite to the show method
        hideBallonSides();
        hideBallonPointer();
    }

    private void showBallonPointer(Rectangle r, Point p) {
        int x = 0,y = 0;

        boolean isPointerOnLeftSide = true;
        boolean isBallonUp = true;

        if (p.x < (r.x + r.width / 2)) {
            // pointer on left side

            x = (p.x < r.x) ? r.x : p.x;
            if (p.y < r.y) {
                // ballon -> down
                isBallonUp = false;
                y = r.y - POINTER_HEIGHT;
            } else {
                // ballon -> up
                y = r.y + r.height - 1;
            }

        } else {
            // pointer on the right side
            isPointerOnLeftSide = false;

            x = (p.x > (r.x + r.width)) ? r.x + r.width : p.x - POINTER_WIDTH;

            if (p.y < r.y) {
                // ballon -> down
                isBallonUp = false;
                y = r.y - POINTER_HEIGHT;
            } else {
                // ballon -> up
                y = r.y + r.height - 1;
                x = ((x + POINTER_WIDTH) > r.x + r.width) ? x - (x + POINTER_WIDTH - r.x - r.width) : x;
            }
        }


        PopupFactory popupFactory = PopupFactory.getSharedInstance();
        pTips = new JToolTip[POINTER_HEIGHT];
        pWindows = new Popup[POINTER_HEIGHT];

        // create pointer
        JComponent owner = tip;
        Color bgColor = tip.getBackground();
        Border border = tip.getBorder();
        for (int i = 0; i < pTips.length; i++) {
            int pW = (isBallonUp) ? POINTER_WIDTH - i : i;
            int pH = 2;
            Rectangle rec = new Rectangle(1, 0, pW - 2, pH);
            JToolTip t = new PointerToolTip(new Rectangle[]{rec}, bgColor);
            t.setComponent(owner);
            t.setPreferredSize(new Dimension(pW, pH));
            t.setBorder(border);

            if (lightWeightPopupEnabled) {
                popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);
            } else {
                popupFactory.setPopupType(PopupFactory.MEDIUM_WEIGHT_POPUP);
            }

            int pX = (isPointerOnLeftSide) ? x : (isBallonUp) ? x + i : x - i;
            pWindows[i] = popupFactory.getPopup(owner, t,
                    pX,
                    y + i);
            popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);

            owner = t;
            pTips[i] = t;
            pWindows[i].show();
        }
    }

    private void hideBallonPointer() {
        if (pWindows != null) {
            for (int i = pWindows.length - 1; i >= 0; i--) {
                Popup pWindow = pWindows[i];
                pWindow.hide();
                pWindows[i] = null;

                pTips[i].getUI().uninstallUI(pTips[i]);
                pTips[i] = null;
            }

            pTips = null;
            pWindows = null;
        }
    }

    private void showBallonSides(Rectangle r) {


        PopupFactory popupFactory = PopupFactory.getSharedInstance();
        int n = 8;
        sTips = new JToolTip[n];
        sWindows = new Popup[n];


        Color bgColor = tip.getBackground();
        Border border = tip.getBorder();

        // keep to debug
//        Color[] cArray = new Color[]{Color.red, Color.green, Color.blue, Color.black,
//                                     Color.red, Color.green, Color.blue, Color.black};
        int rXW = r.x + r.width;
        int[] wArray = new int[]{3, 2, 2, 2, // left
                                 3, 2, 2, 2}; // right
        int[] hArray = new int[]{r.height - 2, r.height - 4, r.height - 6, r.height - 10, // left
                                 r.height - 2, r.height - 4, r.height - 6, r.height - 10}; // right
        int[] xArray = new int[]{r.x - 2, r.x - 3, r.x - 4, r.x - 5, // left
                                 rXW - 1, rXW + 1, rXW + 2, rXW + 3};  // right
        int[] yArray = new int[]{r.y + 1, r.y + 2, r.y + 3, r.y + 5, // left
                                 r.y + 1, r.y + 2, r.y + 3, r.y + 5}; // right
        java.util.List rList = new ArrayList();
        rList.add(new Rectangle[]{new Rectangle(0, 1, 3, r.height - 4)}); // 0
        rList.add(new Rectangle[]{new Rectangle(0, 1, 2, r.height - 6)}); // 1
        rList.add(new Rectangle[]{new Rectangle(1, 1, 2, r.height - 8)}); // 2
        rList.add(new Rectangle[]{new Rectangle(1, 1, 2, r.height - 12)}); // 3

        rList.add(new Rectangle[]{new Rectangle(0, 1, 2, r.height - 4)}); // 4
        rList.add(new Rectangle[]{new Rectangle(0, 1, 1, r.height - 6)}); // 5
        rList.add(new Rectangle[]{new Rectangle(0, 1, 1, r.height - 8)}); // 6
        rList.add(new Rectangle[]{new Rectangle(0, 1, 1, r.height - 12)}); // 7

        JComponent owner = pTips[pTips.length - 1];
        for (int i = 0; i < n; i++) {
            int pW = wArray[i];
            int pH = hArray[i];
            Rectangle[] recs = (Rectangle[]) rList.get(i);
            JToolTip t = new PointerToolTip(recs, bgColor);
            t.setComponent(owner);
            t.setPreferredSize(new Dimension(pW, pH));
            t.setBorder(border);
            if (lightWeightPopupEnabled) {
                popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);
            } else {
                popupFactory.setPopupType(PopupFactory.MEDIUM_WEIGHT_POPUP);
            }
            sWindows[i] = popupFactory.getPopup(owner, t,
                    xArray[i],
                    yArray[i]);
            popupFactory.setPopupType(PopupFactory.LIGHT_WEIGHT_POPUP);

            owner = t;
            sTips[i] = t;
            sWindows[i].show();
        }
    }

    private void hideBallonSides() {
        // hide in the order opposite to the show method

        if (sWindows != null) {
            for (int i = sWindows.length - 1; i >= 0; i--) {
                Popup sWindow = sWindows[i];
                sWindow.hide();
                sWindows[i] = null;

                sTips[i].getUI().uninstallUI(sTips[i]);
                sTips[i] = null;
            }

            sTips = null;
            sWindows = null;
        }
    }

    private final class PointerToolTip extends JToolTip {
        private final Color bgColor;
        private final Rectangle[] recs;

        public PointerToolTip(Rectangle[] recs, Color bgColor) {
            this.bgColor = bgColor;
            this.recs = recs;
        }

        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(bgColor);
            for (int i = 0; i < recs.length; i++) {
                Rectangle rec = recs[i];
                g.fillRect(rec.x, rec.y, rec.width, rec.height);
            }
        }
    }
}
