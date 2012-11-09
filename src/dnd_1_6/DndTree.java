package dnd_1_6;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 25, 2007
 * Time: 10:20:06 AM
 */

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;

// http://java.sun.com/mailers/techtips/corejava/2007/tt0607.html#1

public class DndTree {
    public static void main(String args[]) {

        // Nimbus LAF from 1.6 update 10
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {                    
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        Runnable runner = new Runnable() {
            public void run() {
                JFrame f = new JFrame("D-n-D JTree");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel top = new JPanel(new BorderLayout());
                JLabel dragLabel = new JLabel("Drag me:");
                JTextField text = new JTextField();
                text.setDragEnabled(true);  // enable text drag from textfield
                top.add(dragLabel, BorderLayout.WEST);
                top.add(text, BorderLayout.CENTER);
                f.add(top, BorderLayout.NORTH);

                final JTree tree = new JTree();                
                final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                tree.setTransferHandler(new TransferHandler() {

                    // what kind of data is droppable on it 
                    public boolean canImport(TransferHandler.TransferSupport support) {
                        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
                            !support.isDrop()) {
                            return false;
                        }

                        JTree.DropLocation dropLocation =
                                (JTree.DropLocation) support.getDropLocation();

                        return dropLocation.getPath() != null;

                    }

                    // what to do with data once dropped
                    public boolean importData(TransferHandler.TransferSupport support) {
                        if (!canImport(support)) {
                            return false;
                        }

                        JTree.DropLocation dropLocation =
                                (JTree.DropLocation) support.getDropLocation();

                        TreePath path = dropLocation.getPath();

                        Transferable transferable = support.getTransferable();

                        String transferData;
                        try {
                            transferData = (String) transferable.getTransferData(
                                    DataFlavor.stringFlavor);
                        } catch (IOException e) {
                            return false;
                        } catch (UnsupportedFlavorException e) {
                            return false;
                        }

                        int childIndex = dropLocation.getChildIndex();
                        if (childIndex == -1) {
                            childIndex = model.getChildCount(path.getLastPathComponent());
                        }

                        DefaultMutableTreeNode newNode =
                                new DefaultMutableTreeNode(transferData);
                        DefaultMutableTreeNode parentNode =
                                (DefaultMutableTreeNode) path.getLastPathComponent();
                        model.insertNodeInto(newNode, parentNode, childIndex);

                        // ensure the new path element is visible.
                        TreePath newPath = path.pathByAddingChild(newNode);
                        tree.makeVisible(newPath);
                        tree.scrollRectToVisible(tree.getPathBounds(newPath));

                        return true;
                    }
                });

                JScrollPane pane = new JScrollPane(tree);
                f.add(pane, BorderLayout.CENTER);

                JPanel bottom = new JPanel();
                JLabel comboLabel = new JLabel("DropMode");
                String options[] = {"USE_SELECTION",
                        "ON", "INSERT", "ON_OR_INSERT"
                };
                final DropMode mode[] = {DropMode.USE_SELECTION,
                        DropMode.ON, DropMode.INSERT, DropMode.ON_OR_INSERT};
                final JComboBox combo = new JComboBox(options);
                combo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedIndex = combo.getSelectedIndex();
                        tree.setDropMode(mode[selectedIndex]);
                    }
                });
                bottom.add(comboLabel);
                bottom.add(combo);
                f.add(bottom, BorderLayout.SOUTH);
                f.setSize(300, 400);
                f.setVisible(true);
            }
        };
        EventQueue.invokeLater(runner);
    }
}
