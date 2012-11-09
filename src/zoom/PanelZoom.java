package zoom;

/**
 * User: mihai.panaitescu
 * Date: 30-Oct-2009
 * Time: 16:44:05
 */

import com.imagero.swing.renderer.AffinePanel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;


public class PanelZoom {
    public static void main(String[] in) {
        System.setProperty("swing.aatext", "true");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {                
                new PanelZoom();
            }
        });
    }

    private ScalablePanel2 scalablePanel;

    public PanelZoom() {

        final JComboBox cbZoom=new JComboBox(new String[] {"1","2","3"});
        //cbZoom.setSelectedItem("2");
        cbZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                float f=Float.parseFloat(cbZoom.getSelectedItem().toString());
                scalablePanel.setZoom(f);
            }
        });

        JPanel zoomPanel = new JPanel();
        JPanel p = new JPanel();
        final JLabel marker = new javax.swing.JLabel("Testing the mouse position on zoom");
        marker.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        p.setLayout(new BorderLayout());
        p.add(marker, BorderLayout.CENTER);
        p.add(cbZoom, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(100,100));
        JTextArea textArea = new JTextArea();
        sp.setViewportView(textArea);
        p.add(sp, BorderLayout.SOUTH);


//        int rows = 2;
//        int cols = 3;
//        TableModel model = new DefaultTableModel(rows, cols);
//        for (int i=0; i<rows; i++) {
//            for (int j=0; j<cols; j++) {
//                model.setValueAt(i+j,i, j);
//            }
//        }
//        JTable table = new JTable(model);
//        p.add(table, BorderLayout.SOUTH);

        zoomPanel.add(p);

        final JFrame frame = new JFrame();
        scalablePanel = new ScalablePanel2(zoomPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scalablePanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

