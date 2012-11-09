package zoom;

import com.imagero.swing.renderer.AffinePanel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * User: mihai.panaitescu
 * Date: 16-Aug-2010
 * Time: 15:12:06
 */
public class AffinePanelTest {
    public static void main(String[] in) {
           System.setProperty("swing.aatext", "true");
           javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   new AffinePanelTest();
               }
           });
       }



    AffinePanel scalablePanel;
    private double previousScale = 1f;

    public AffinePanelTest() {

        final JComboBox cbZoom=new JComboBox(new String[] {"1","2","3"});
        //cbZoom.setSelectedItem("2");
        cbZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double f=Double.parseDouble(cbZoom.getSelectedItem().toString());
                double d = f / previousScale;
                System.out.println("d =" +d);
                previousScale = f ;
                scalablePanel.scale(d, d);
            }
        });    

        JFrame frame = new JFrame();
        JPanel testPanel = createPanel();
        scalablePanel = new AffinePanel(testPanel);        
        JScrollPane scr = new JScrollPane(scalablePanel);
        scr.setPreferredSize(new Dimension(400, 400));
        scr.setMinimumSize(new Dimension(400, 400));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(cbZoom, BorderLayout.NORTH);
        frame.add(scr, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createPanel() {
        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new BorderLayout());
        //zoomPanel.setPreferredSize(new Dimension(300, 300));

        final JLabel marker = new javax.swing.JLabel("Testing the mouse position on zoom");
        marker.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        zoomPanel.add(marker, BorderLayout.NORTH);

        int rows = 2;
        int cols = 3;
        TableModel model = new DefaultTableModel(rows, cols);
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                model.setValueAt(i+j,i, j);
            }
        }
        JTable table = new JTable(model);
        zoomPanel.add(table, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(100,100));
        JTextArea textArea = new JTextArea();
        sp.setViewportView(textArea);
        zoomPanel.add(sp, BorderLayout.SOUTH);     

        return zoomPanel;
    }
}
