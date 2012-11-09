package loupe;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 20-Aug-2009
// Time: 13:50:20

//
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 *
 * @author Romain Guy <romain.guy@mac.com>
 */
public class ApplicationFrame extends JFrame {
    private JLayeredPane layeredPane;
    private Loupe loupe;

    public ApplicationFrame() {
        super("Layered Pane Layout");

        layeredPane = new JLayeredPane();

        addLayersControl();
        loadImagesInLayers();
        createLoupe();
        
        setSize(540, 350);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ApplicationFrame().setVisible(true);
            }
        });
    }

    private void loadImagesInLayers() {
        layeredPane.setLayout(new FlowLayout());

        for (int i = 2; i <= 5; i++) {
            String name = "images/photo" + i + ".jpg";
            URL url = getClass().getResource(name);
            Icon icon = new ImageIcon(url);
            JLabel label = new JLabel(icon);

            layeredPane.add(label,
                    (Integer) (JLayeredPane.DEFAULT_LAYER + (i - 1) * 2));
        }

        add(layeredPane);
    }

    private void addLayersControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JComboBox layerSelection = new JComboBox(new String[] {
            "Layer 0", "Layer 1", "Layer 2", "Layer 3", "Layer 4"
        });
        layerSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JComboBox layerSelection = (JComboBox) actionEvent.getSource();
                int layerId = layerSelection.getSelectedIndex();
                layeredPane.setLayer(loupe,
                        (Integer) (JLayeredPane.DEFAULT_LAYER + layerId * 2 + 1));
            }
        });
        panel.add(new JLabel("Loupe Layer: "));
        panel.add(layerSelection);

        JSlider zoomSelection = new JSlider(1, 16, 2);
        zoomSelection.setPaintTicks(true);
        zoomSelection.setSnapToTicks(true);
        zoomSelection.setPaintLabels(true);
        zoomSelection.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider zoomSelection = (JSlider) changeEvent.getSource();
                loupe.setZoomLevel(zoomSelection.getValue());
            }
        });

        panel.add(Box.createHorizontalStrut(24));
        panel.add(new JLabel("Zoom: "));
        panel.add(new JLabel("1"));
        panel.add(zoomSelection);
        panel.add(new JLabel("16"));

        add(panel, BorderLayout.NORTH);
    }

    private void createLoupe() {
        loupe = new Loupe(layeredPane);
        Dimension size = loupe.getPreferredSize();
        layeredPane.add(loupe,
                (Integer) (JLayeredPane.DEFAULT_LAYER + 1));
    }
}
