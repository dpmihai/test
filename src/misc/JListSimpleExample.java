package misc;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 9, 2006
 * Time: 12:06:50 PM
 * To change this template use File | Settings | File Templates.
 */

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

// Selecting in a JList below the last row makes the last row selected
// This class prohibits this kind of selection (for mousePressed and mouseDragged)
// Obs. : clearSelection() for a JList does not remove the component selected border ...

public class JListSimpleExample extends JFrame {
    public static void main(String[] args) {
        new JListSimpleExample();
    }

    private JList sampleJList;
    private JTextField valueField;
    private Point p;

    public JListSimpleExample() {
        super("Creating a Simple JList");
        //WindowUtilities.setNativeLookAndFeel();
        //addWindowListener(new ExitListener());
        Container content = getContentPane();

        // Create the JList, set the number of visible rows, add a
        // listener, and put it in a JScrollPane.
        String[] entries = {"Entry 1", "Entry 2", "Entry 3",
                "Entry 4", "Entry 5", "Entry 6"};
        sampleJList = new JList(entries);
        final int rows = 10;
        sampleJList.setVisibleRowCount(rows);
        Font displayFont = new Font("Serif", Font.BOLD, 18);
        sampleJList.setFont(displayFont);
        sampleJList.addListSelectionListener(new ValueReporter());
        sampleJList.addMouseListener(new MouseReporter());
        sampleJList.addMouseMotionListener(new MouseReporter());
        JScrollPane listPane = new JScrollPane(sampleJList);

        JPanel listPanel = new JPanel();
        listPanel.setBackground(Color.white);
        Border listPanelBorder =
                BorderFactory.createTitledBorder("Sample JList");
        listPanel.setBorder(listPanelBorder);
        listPanel.add(listPane);
        content.add(listPanel, BorderLayout.CENTER);
        JLabel valueLabel = new JLabel("Last Selection:");
        valueLabel.setFont(displayFont);
        valueField = new JTextField("None", 7);
        valueField.setFont(displayFont);
        JPanel valuePanel = new JPanel();
        valuePanel.setBackground(Color.white);
        Border valuePanelBorder =
                BorderFactory.createTitledBorder("JList Selection");
        valuePanel.setBorder(valuePanelBorder);
        valuePanel.add(valueLabel);
        valuePanel.add(valueField);
        content.add(valuePanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private class ValueReporter implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                if (sampleJList.getSelectedValue() != null) {
                    valueField.setText(sampleJList.getSelectedValue().toString());
                }
            }
        }
    }

    private class MouseReporter implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            done(e);
        }

        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        private void done(MouseEvent e) {
            int lastRow = sampleJList.getModel().getSize() - 1;
            Rectangle rect = sampleJList.getCellBounds(lastRow, lastRow - 1);
            if (rect != null) {
                if (rect.getMaxY() < e.getPoint().y){
                    sampleJList.clearSelection();
                } else {
                    valueField.setText(sampleJList.getSelectedValue().toString());
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            done(e);
        }

        public void mouseMoved(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}
