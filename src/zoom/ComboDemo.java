//package zoom;
//
///**
// * User: mihai.panaitescu
// * Date: 23-Aug-2010
// * Time: 15:55:51
// */
//
//import java.awt.BorderLayout;
//import java.awt.Component;
//
//import javax.swing.JComboBox;
//import javax.swing.JComponent;
//import javax.swing.JFrame;
//import javax.swing.JList;
//import javax.swing.JPanel;
//import javax.swing.ListCellRenderer;
//import javax.swing.SwingUtilities;
//
//import org.jdesktop.jxlayer.JXLayer;
//import org.pbjar.jxlayer.demo.TransformUtils;
//import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
//
//public class ComboDemo {
//
//    public static class TransformingRenderer implements ListCellRenderer {
//
//        private static final long serialVersionUID = 1L;
//        private final JXLayer<JComponent> layer;
//        private final DefaultTransformModel model;
//        private final JPanel panel;
//        private final ListCellRenderer wrappedRenderer;
//
//        public TransformingRenderer(JComboBox combo) {
//            this.wrappedRenderer = combo.getRenderer();
//            model = new DefaultTransformModel();
//            layer = TransformUtils.createTransformJXLayer(null, model);
//            panel = new JPanel(new BorderLayout());
//            panel.add(layer);
//        }
//
//        @Override
//        public Component getListCellRendererComponent(JList list, Object value,
//                                                      int index, boolean isSelected, boolean cellHasFocus) {
//            layer.setView((JComponent) wrappedRenderer
//                    .getListCellRendererComponent(list, value, index,
//                            isSelected, cellHasFocus));
//            return panel;
//        }
//
//        public DefaultTransformModel getModel() {
//            return model;
//        }
//
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                new ComboDemo().createGUI();
//            }
//        });
//    }
//
//    private void createGUI() {
//        String[] selections = new String[12];
//        for (int index = 0; index < selections.length; index++) {
//            selections[index] = "Selection " + (index + 1);
//        }
//        JComboBox combo = new JComboBox(selections);
//
//        TransformingRenderer renderer = new TransformingRenderer(combo);
//        combo.setRenderer(renderer);
//
//        renderer.getModel().setScale(3);
//        renderer.getModel().setScaleToPreferredSize(true);
//        renderer.getModel().setPreserveAspectRatio(true);
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(combo);
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//}
