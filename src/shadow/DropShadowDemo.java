package shadow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DropShadowDemo extends JFrame {
    private DropShadowPanel dropShadowPanel;

    public DropShadowDemo() throws HeadlessException {
        super("Drop Shadow");
        buildContentPane();

        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buildContentPane() {
        JPanel viewPane = buildViewPane();
        JPanel debugPane = buildDebugPane();
        
        add(new JScrollPane(viewPane), BorderLayout.CENTER);
        add(debugPane, BorderLayout.EAST);
    }

    private JPanel buildDebugPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        JSlider slider;
        
        panel.add(new JLabel("Angle:"),
                  new GridBagConstraints(0, 0,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        panel.add(slider = new JSlider(0, 360, 30),
                  new GridBagConstraints(0, 1,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setAngle(value);
                        dropShadowPanel.repaint();
                    }
                }).start();
            }
        });
        panel.add(new JLabel("Distance:"),
                  new GridBagConstraints(0, 2,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        panel.add(slider = new JSlider(1, 80, 5),
                  new GridBagConstraints(0, 3,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setDistance(value);
                        dropShadowPanel.revalidate();
                        dropShadowPanel.repaint();
                    }
                }).start();
            }
        });
        panel.add(new JLabel("Size:"),
                  new GridBagConstraints(0, 4,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        panel.add(slider = new JSlider(1, 40, 5),
                  new GridBagConstraints(0, 5,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setShadowSize(value);
                        dropShadowPanel.revalidate();
                        dropShadowPanel.repaint();
                    }
                }).start();
            }
        });
        panel.add(new JLabel("Opacity"),
                  new GridBagConstraints(0, 6,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        panel.add(slider = new JSlider(0, 100, 50),
                  new GridBagConstraints(0, 7,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setShadowOpacity((float) (value / 100.0f));
                        dropShadowPanel.repaint();
                    }
                }).start();
            }
        });
        panel.add(new JLabel("Color:"),
                  new GridBagConstraints(0, 8,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        JButton button;
        panel.add(button = new JButton(" "),
                  new GridBagConstraints(0, 9,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.HORIZONTAL, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        button.setBackground(dropShadowPanel.getShadowColor());
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JButton source = (JButton) e.getSource();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Color color = JColorChooser.showDialog(DropShadowDemo.this,
                                                               "Shadow Color",
                                                               source.getBackground());
                        if (color != null) {
                            source.setBackground(color);
                            dropShadowPanel.setShadowColor(color);
                            dropShadowPanel.repaint();
                        }
                    }
                });
            }
        });
        panel.add(new JLabel("Picture:"),
                  new GridBagConstraints(0, 10,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        panel.add(button = new JButton("[Internal]"),
                  new GridBagConstraints(0, 11,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.HORIZONTAL, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JButton source = (JButton) e.getSource();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFileChooser chooser = new JFileChooser();
                        int result = chooser.showOpenDialog(DropShadowDemo.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            source.setText(file.getName());
                            dropShadowPanel.setSubject(file);
                            dropShadowPanel.repaint();
                        }
                    }
                });
            }
        });
        panel.add(new JLabel("Rendering:"),
                  new GridBagConstraints(0, 12,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        JCheckBox checkbox;
        panel.add(checkbox = new JCheckBox("Fast Rendering", true),
                  new GridBagConstraints(0, 13,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.HORIZONTAL, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JCheckBox source = (JCheckBox) e.getSource();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setRenderingHint(DropShadowPanel.KEY_BLUR_QUALITY,
                                                         source.isSelected() ?
                                                         DropShadowPanel.VALUE_BLUR_QUALITY_FAST :
                                                         DropShadowPanel.VALUE_BLUR_QUALITY_HIGH);
                        dropShadowPanel.refreshShadow();
                        dropShadowPanel.repaint();
                    }
                }).start();
            }
        });
        panel.add(Box.createVerticalGlue(),
                  new GridBagConstraints(0, 14,
                                         1, 1,
                                         1.0, 1.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 0, 0, 0),
                                         0, 0));
        return panel;
    }

    private JPanel buildViewPane() {
        JPanel panel = new JPanel(new StackLayout());
        panel.setOpaque(false);
        
        dropShadowPanel = new DropShadowPanel("images/subject.png");
        
        panel.add(new CheckboardPanel(), StackLayout.BOTTOM);
        panel.add(dropShadowPanel, StackLayout.TOP);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DropShadowDemo demo = new DropShadowDemo();
                demo.setVisible(true);
            }
        });
    }
}
