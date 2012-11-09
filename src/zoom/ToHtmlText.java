package zoom;

/**
 * User: mihai.panaitescu
 * Date: 24-Aug-2010
 * Time: 11:23:42
 */
public class ToHtmlText {

    private static String text = "import org.jdesktop.jxlayer.JXLayer;\n" +
            "import org.pbjar.jxlayer.demo.TransformUtils;\n" +
            "import org.pbjar.jxlayer.demo.QualityHints;\n" +
            "import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;\n" +
            "import org.pbjar.jxlayer.plaf.ext.TransformUI;\n" +
            "\n" +
            "import javax.swing.*;\n" +
            "import javax.swing.plaf.metal.MetalComboBoxUI;\n" +
            "import javax.swing.plaf.basic.ComboPopup;\n" +
            "import javax.swing.plaf.basic.BasicComboPopup;\n" +
            "import javax.swing.table.TableModel;\n" +
            "import javax.swing.table.DefaultTableModel;\n" +
            "import java.awt.*;\n" +
            "import java.awt.event.ActionListener;\n" +
            "import java.awt.event.ActionEvent;\n" +
            "\n" +
            "/**\n" +
            " * User: mihai.panaitescu\n" +
            " * Date: 17-Aug-2010\n" +
            " * Time: 11:22:56\n" +
            " */\n" +
            "public class TransformPanelTest {\n" +
            "\n" +
            "    private JXLayer layer;\n" +
            "    private SteppedComboBoxUI firstComboUI;\n" +
            "    private SteppedComboBoxUI secondComboUI;\n" +
            "\n" +
            "    public static void main(String[] in) {\n" +
            "              javax.swing.SwingUtilities.invokeLater(new Runnable() {\n" +
            "                  public void run() {\n" +
            "                      new TransformPanelTest();\n" +
            "                  }\n" +
            "              });\n" +
            "          }\n" +
            "\n" +
            "    public TransformPanelTest() {\n" +
            "\n" +
            "        final JComboBox cbZoom=new JComboBox(new String[] {\"1\",\"2\",\"3\"});\n" +
            "        cbZoom.addActionListener(new ActionListener() {\n" +
            "            public void actionPerformed(ActionEvent e) {\n" +
            "\n" +
            "                double f = Double.parseDouble(cbZoom.getSelectedItem().toString());\n" +
            "\n" +
            "                DefaultTransformModel model = (DefaultTransformModel) ((TransformUI) layer.getUI()).getModel();\n" +
            "                model.setScale(f);\n" +
            "                layer.repaint();\n" +
            "\n" +
            "                firstComboUI.zoom(f);\n" +
            "                secondComboUI.zoom(f);\n" +
            "            }\n" +
            "        });\n" +
            "\n" +
            "        JFrame frame = new JFrame();\n" +
            "        JPanel testPanel = createPanel();\n" +
            "        testPanel.setPreferredSize(new Dimension(400, 400));\n" +
            "\n" +
            "        layer = TransformUtils.createTransformJXLayer(testPanel, 1.0, new QualityHints());\n" +
            "\n" +
            "        JScrollPane scr = new JScrollPane();\n" +
            "        scr.setViewportView(layer);\n" +
            "\n" +
            "        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" +
            "        frame.setLayout(new BorderLayout());\n" +
            "        frame.add(cbZoom, BorderLayout.NORTH);\n" +
            "        frame.add(scr, BorderLayout.CENTER);\n" +
            "        frame.pack();\n" +
            "        frame.setLocationRelativeTo(null);\n" +
            "        frame.setVisible(true);\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "\n" +
            "    private JPanel createPanel() {\n" +
            "        JPanel zoomPanel = new JPanel();\n" +
            "        zoomPanel.setLayout(new GridBagLayout());\n" +
            "\n" +
            "        final JLabel marker = new javax.swing.JLabel(\"Testing the mouse position on zoom\");\n" +
            "        marker.setHorizontalAlignment(javax.swing.JLabel.CENTER);\n" +
            "        zoomPanel.add(marker, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,\n" +
            "                GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));\n" +
            "\n" +
            "        JComboBox firstComboBox = new JComboBox(new String[] {\"First Item\", \"Second Item\", \"Third Item\"});\n" +
            "        firstComboUI = new SteppedComboBoxUI();\n" +
            "        firstComboBox.setUI(firstComboUI);\n" +
            "        zoomPanel.add(firstComboBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,\n" +
            "                GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));\n" +
            "\n" +
            "        JComboBox secondComboBox = new JComboBox(new String[] {\"Text one\", \"Text two\", \"Text three\"});\n" +
            "        secondComboUI = new SteppedComboBoxUI();\n" +
            "        secondComboBox.setUI(secondComboUI);\n" +
            "        zoomPanel.add(secondComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,\n" +
            "                GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));\n" +
            "\n" +
            "        int rows = 2;\n" +
            "        int cols = 3;\n" +
            "        TableModel model = new DefaultTableModel(rows, cols);\n" +
            "        for (int i=0; i<rows; i++) {\n" +
            "            for (int j=0; j<cols; j++) {\n" +
            "                model.setValueAt(i+j,i, j);\n" +
            "            }\n" +
            "        }\n" +
            "        JTable table = new JTable(model);        \n" +
            "        zoomPanel.add(table, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,\n" +
            "                GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));\n" +
            "        \n" +
            "        JScrollPane sp = new JScrollPane();\n" +
            "        sp.setPreferredSize(new Dimension(100,100));\n" +
            "        JTextArea textArea = new JTextArea();\n" +
            "        sp.setViewportView(textArea);\n" +
            "        zoomPanel.add(sp, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,\n" +
            "                GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0));        \n" +
            "\n" +
            "        return zoomPanel;\n" +
            "    }\n" +
            "\n" +
            "    class SteppedComboBoxUI extends MetalComboBoxUI {\n" +
            "\n" +
            "        private double zoom = 1.0;\n" +
            "        private JXLayer layer;\n" +
            "\n" +
            "        protected ComboPopup createPopup() {\n" +
            "            BasicComboPopup popup = new BasicComboPopup(comboBox) {\n" +
            "\n" +
            "                public void show() {\n" +
            "\n" +
            "                    //compute width of text\n" +
            "                    int widest = getWidestItemWidth();\n" +
            "\n" +
            "                    //Get the box's size\n" +
            "                    Dimension popupSize = comboBox.getSize();\n" +
            "\n" +
            "                    //Set the size of the popup\n" +
            "                    popupSize.setSize(widest , getPopupHeightForRowCount(comboBox.getMaximumRowCount()));\n" +
            "\n" +
            "                    //Compute the complete bounds\n" +
            "                    Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, popupSize.height);\n" +
            "\n" +
            "                    //Set the size of the scroll pane\n" +
            "                    Dimension dim = new Dimension((int)(popupBounds.getSize().getWidth() * zoom),\n" +
            "                            (int)(popupBounds.getSize().getHeight()*zoom));\n" +
            "                    scroller.setMaximumSize(dim);\n" +
            "                    scroller.setPreferredSize(dim);\n" +
            "                    scroller.setMinimumSize(dim);\n" +
            "\n" +
            "                    //Cause it to re-layout\n" +
            "                    list.invalidate();\n" +
            "\n" +
            "                    //Handle selection of proper item\n" +
            "                    int selectedIndex = comboBox.getSelectedIndex();\n" +
            "                    if (selectedIndex == -1) {\n" +
            "                        list.clearSelection();\n" +
            "                    } else {\n" +
            "                        list.setSelectedIndex(selectedIndex);\n" +
            "                    }\n" +
            "\n" +
            "                    //Make sure the selected item is visible\n" +
            "                    list.ensureIndexIsVisible(list.getSelectedIndex());\n" +
            "\n" +
            "                    //Use lightweight if asked for\n" +
            "                    setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());\n" +
            "\n" +
            "                    layer = TransformUtils.createTransformJXLayer(list, zoom, new QualityHints());\n" +
            "                    scroller.setViewportView(layer);\n" +
            "                    DefaultTransformModel model = (DefaultTransformModel) ((TransformUI) layer.getUI()).getModel();\n" +
            "                    model.setScale(zoom);\n" +
            "\n" +
            "                    Point point = new Point(popupBounds.x, popupBounds.y);\n" +
            "\n" +
            "                    //Show the popup\n" +
            "                    show(comboBox, point.x, point.y);\n" +
            "\n" +
            "                }\n" +
            "            };\n" +
            "            popup.getAccessibleContext().setAccessibleParent(comboBox);\n" +
            "            return popup;\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "\n" +
            "        public int getWidestItemWidth() {\n" +
            "            int numItems = comboBox.getItemCount();\n" +
            "            Font font = comboBox.getFont();\n" +
            "            FontMetrics metrics = comboBox.getFontMetrics(font);\n" +
            "\n" +
            "            //The widest width\n" +
            "            int widest = 0;\n" +
            "            for (int i = 0; i < numItems; i++) {\n" +
            "                Object item = comboBox.getItemAt(i);\n" +
            "                int lineWidth = metrics.stringWidth(item.toString());\n" +
            "                widest = Math.max(widest, lineWidth);\n" +
            "            }\n" +
            "\n" +
            "            return widest;\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        public void zoom(double f) {\n" +
            "            zoom = f;\n" +
            "            if (zoom < 1.0) {\n" +
            "                // popup has a minimum size;\n" +
            "                zoom = 1.0;\n" +
            "            }\n" +
            "            popup = createPopup();\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "}";

    public static void main(String[] args) {
        text =  text.replaceAll(" ", "&nbsp;");
        System.out.println("text="+text);
    }
}
