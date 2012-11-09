//package zoom;
//
///**
// * User: mihai.panaitescu
// * Date: 25-Aug-2010
// * Time: 09:27:52
// */
//
//import org.jdesktop.jxlayer.JXLayer;
//import org.pbjar.jxlayer.demo.TransformUtils;
//import org.pbjar.jxlayer.demo.QualityHints;
//import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
//import org.pbjar.jxlayer.plaf.ext.TransformUI;
//
//import javax.swing.*;
//import javax.swing.plaf.metal.MetalComboBoxUI;
//import javax.swing.plaf.basic.ComboPopup;
//import javax.swing.plaf.basic.BasicComboPopup;
//import javax.swing.table.TableModel;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.geom.AffineTransform;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//
///**
// * User: mihai.panaitescu Date: 17-Aug-2010 Time: 11:22:56
// */
//public class TransformPanelTest2 {
//
//    private JXLayer layer;
//    private SteppedComboBoxUI2 firstComboUI;
//    private SteppedComboBoxUI2 secondComboUI;
//
//    public static void main(String[] in) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new TransformPanelTest2();
//            }
//        });
//    }
//
//    public TransformPanelTest2() {
//
//        final JComboBox cbZoom = new JComboBox(new String[]{"1", "2", "3"});
//        cbZoom.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                double f = Double.parseDouble(cbZoom.getSelectedItem()
//                        .toString());
//
//                DefaultTransformModel model = (DefaultTransformModel) ((TransformUI) layer
//                        .getUI()).getModel();
//                model.setScale(f);
//                layer.repaint();
//            }
//        });
//
//        JFrame frame = new JFrame();
//        JPanel testPanel = createPanel();
//        testPanel.setPreferredSize(new Dimension(400, 400));
//
//        layer = TransformUtils.createTransformJXLayer(testPanel, 1.0,
//                new QualityHints());
//
//        // Just a test to see what happens when a more complex transform is
//        // applied than just scaling. // PB
//        // ((DefaultTransformModel) ((TransformUI) layer.getUI()).getModel())
//        // .setRotation(Math.PI / 32.0);
//
//        JScrollPane scr = new JScrollPane();
//        scr.setViewportView(layer);
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//        frame.add(cbZoom, BorderLayout.NORTH);
//        frame.add(scr, BorderLayout.CENTER);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//
//    private JPanel createPanel() {
//        JPanel zoomPanel = new JPanel();
//        zoomPanel.setLayout(new GridBagLayout());
//
//        final JLabel marker = new javax.swing.JLabel(
//                "Testing the mouse position on zoom");
//        marker.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        zoomPanel.add(marker, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
//                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//                new Insets(5, 5, 5, 5), 0, 0));
//
//        JComboBox firstComboBox = new JComboBox(new String[]{"First Item",
//                "Second Item", "Third Item"});
//        firstComboUI = new SteppedComboBoxUI2();
//        firstComboBox.setUI(firstComboUI);
//        zoomPanel.add(firstComboBox, new GridBagConstraints(0, 1, 1, 1, 0.0,
//                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
//                new Insets(5, 5, 5, 5), 0, 0));
//
//        JComboBox secondComboBox = new JComboBox(new String[]{"Text one",
//                "Text two", "Text three"});
//        secondComboUI = new SteppedComboBoxUI2();
//        secondComboBox.setUI(secondComboUI);
//        zoomPanel.add(secondComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0,
//                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
//                new Insets(5, 5, 5, 5), 0, 0));
//
//        int rows = 2;
//        int cols = 3;
//        TableModel model = new DefaultTableModel(rows, cols);
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                model.setValueAt(i + j, i, j);
//            }
//        }
//        JTable table = new JTable(model);
//        zoomPanel.add(table, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
//                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5,
//                5, 5, 5), 0, 0));
//
//        JScrollPane sp = new JScrollPane();
//        sp.setPreferredSize(new Dimension(100, 100));
//        JTextArea textArea = new JTextArea();
//        sp.setViewportView(textArea);
//        zoomPanel.add(sp, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0,
//                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//                new Insets(5, 5, 5, 5), 0, 0));
//
//        return zoomPanel;
//    }
//
//    class SteppedComboBoxUI2 extends MetalComboBoxUI {
//
//        protected ComboPopup createPopup() {
//            BasicComboPopup popup = new BasicComboPopup(comboBox) {
//
//                public void show() {
//
//                    // PB got rid of the zoom variable and rewrote part of this
//                    // method's code
//                    // Also removed the setZoom() method
//                    // Removed "JXLayer layer" as a variable and have it inlined
//
//                    Container comboContainer = comboBox.getParent();
//                    while (!(comboContainer instanceof JXLayer)) {
//                        comboContainer = comboContainer.getParent();
//                    }
//                    JXLayer parentLayer = (JXLayer) comboContainer;
//                    DefaultTransformModel parentModel = (DefaultTransformModel) ((TransformUI) parentLayer
//                            .getUI()).getModel();
//                    AffineTransform parentTransform = parentModel
//                            .getTransform(parentLayer);
//
//                    // compute width of text
//                    int widest = getWidestItemWidth();
//
//                    // Get the box's size
//                    Dimension popupSize = comboBox.getSize();
//
//                    // Set the size of the popup
//                    popupSize.setSize(comboBox.getWidth(),
//                            getPopupHeightForRowCount(comboBox
//                                    .getMaximumRowCount()));
//
//                    // Compute the complete bounds
//                    Rectangle popupBounds = computePopupBounds(0, comboBox
//                            .getBounds().height, popupSize.width,
//                            popupSize.height);
//
//                    // Set the size of the scroll pane
//                    Dimension dim = parentTransform.createTransformedShape(
//                            popupBounds).getBounds().getSize();
//
//                    list.setPreferredSize(new Dimension((int)dim.getWidth(), (int)list.getPreferredSize().getHeight()));
//                    scroller.setMaximumSize(dim);
//                    scroller.setPreferredSize(dim);
//                    scroller.setMinimumSize(dim);
//
//                    // Cause it to re-layout
//                    list.invalidate();
//
//                    // Handle selection of proper item
//                    int selectedIndex = comboBox.getSelectedIndex();
//                    if (selectedIndex == -1) {
//                        list.clearSelection();
//                    } else {
//                        list.setSelectedIndex(selectedIndex);
//                    }
//
//                    // Make sure the selected item is visible
//                    list.ensureIndexIsVisible(list.getSelectedIndex());
//
//                    // Use lightweight if asked for
//                    setLightWeightPopupEnabled(comboBox
//                            .isLightWeightPopupEnabled());
//
//                    JXLayer layer = TransformUtils.createTransformJXLayer(list,
//                            parentModel, new QualityHints());
//                    scroller.setViewportView(layer);
//
//                    Point point = new Point(0, comboBox.getHeight());
//                    point = SwingUtilities.convertPoint(comboBox, point,
//                            parentLayer);
//                    parentTransform.transform(point, point);
//
//                    // Show the popup relative to JXLayer
//                    show(parentLayer, point.x, point.y);
//                }
//            };
//            popup.getAccessibleContext().setAccessibleParent(comboBox);
//            return popup;
//        }
//
//        public int getWidestItemWidth() {
//            int numItems = comboBox.getItemCount();
//            Font font = comboBox.getFont();
//            FontMetrics metrics = comboBox.getFontMetrics(font);
//
//            // The widest width
//            int widest = 0;
//            for (int i = 0; i < numItems; i++) {
//                Object item = comboBox.getItemAt(i);
//                int lineWidth = metrics.stringWidth(item.toString());
//                widest = Math.max(widest, lineWidth);
//            }
//
//            return widest;
//        }
//
//    }
//
//}
