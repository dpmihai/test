package border;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 10:34:57 AM
 */
public class Test {

    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            e.printStackTrace();
        }

        final JPanel proxyPanel = new JPanel();
        proxyPanel.add(new JLabel("Proxy Host: "));
        proxyPanel.add(new JTextField("proxy.xyz.com"));
        proxyPanel.add(new JLabel("  Proxy Port"));
        proxyPanel.add(new JTextField("8080"));
        final JCheckBox checkBox = new JCheckBox("Use Proxy", true);
        checkBox.setFocusPainted(false);
        ComponentTitledBorder componentBorder =
                new ComponentTitledBorder(checkBox, proxyPanel
                , BorderFactory.createEtchedBorder());
        checkBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                boolean enable = checkBox.isSelected();
                Component comp[] = proxyPanel.getComponents();
                for(int i = 0; i<comp.length; i++){
                    comp[i].setEnabled(enable);
                }
            } 
        });
        proxyPanel.setBorder(componentBorder);
        JFrame frame = new JFrame("ComponentTitledBorder - santhosh@in.fiorano.com");
        Container contents = frame.getContentPane();
        contents.setLayout(new FlowLayout());
        contents.add(proxyPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
