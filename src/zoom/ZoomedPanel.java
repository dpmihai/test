package zoom;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 21-Aug-2009
// Time: 16:15:45

//
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.tree.*;

public class ZoomedPanel extends JPanel {
    float scale=2;
    public ZoomedPanel() {
    }
    public void paint(Graphics g) {
        Graphics2D g2d=(Graphics2D)g;
        AffineTransform oldTr=g2d.getTransform();
        g2d.scale(scale,scale);
        super.paint(g);
        g2d.setTransform(oldTr);
    }
    protected void processMouseEvent1(MouseEvent e) {
        int x=(int)(e.getX() * scale);
        int y=(int)(e.getY() * scale);
        MouseEvent ze = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), x, y, e.getClickCount(), e.isPopupTrigger());
        super.processMouseEvent(ze);
    }
    public Graphics getGraphics() {
        Graphics g=super.getGraphics();
        Graphics2D g2d=(Graphics2D)g;
        g2d.scale(scale,scale);
        return g;
    }

    public static class MyEventQueue extends EventQueue {
        public MyEventQueue() {
        }
        protected void dispatchEvent(AWTEvent event) {
            if (event instanceof MouseEvent) {
                MouseEvent event2=(MouseEvent)event;
                if (event.getSource() instanceof Component && event instanceof MouseEvent) {

                    Component c=(Component)event.getSource();
                    Component cursorComponent=SwingUtilities.getDeepestComponentAt(c, event2.getX(), event2.getY());
                    ZoomedPanel zContainer=getZoomedPanel(cursorComponent);
                    if (zContainer!=null) {
                        float scale=zContainer.scale;
                        int x=event2.getX();
                        Point p=SwingUtilities.convertPoint(zContainer,0,0,(Component)event.getSource());
                        int cX=event2.getX()-p.x;
                        x=x-cX+(int)(cX/scale);

                        int y=event2.getY();
                        int cY=event2.getY()-p.y;
                        y=y-cY+(int)(cY/scale);
                        MouseWheelEvent ze = new MouseWheelEvent(event2.getComponent(), event2.getID(), event2.getWhen(),
                                event2.getModifiers(), x, y, event2.getClickCount(), event2.isPopupTrigger(), 0, 0, 0);
                        event2 = ze;
                    }
                }
                super.dispatchEvent(event2);
            } else {
                super.dispatchEvent(event);
            }
        }
    }

    public static ZoomedPanel getZoomedPanel(Component c) {
        if (c ==null) {
            return null;
        }
        else if (c instanceof ZoomedPanel) {
            return (ZoomedPanel)c;
        }
        else {
            return getZoomedPanel(c.getParent());
        }
    }
    public static void main(String[] args) {
        MyEventQueue meq=new MyEventQueue();
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(meq);

        JFrame fr=new JFrame();
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(600,500);
        fr.getContentPane().setLayout(new FlowLayout());

        final ZoomedPanel panel = new ZoomedPanel();
        panel.setLayout(null);
        JButton b=new JButton("Ok");
        b.setBounds(10,10,50,20);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("here");
            }
        });
        panel.add(b);

        DefaultMutableTreeNode root=new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode n=new DefaultMutableTreeNode("1");
        root.add(n);
        n=new DefaultMutableTreeNode("2");
        root.add(n);
        n=new DefaultMutableTreeNode("3");
        root.add(n);
        JTree tree=new JTree(new DefaultTreeModel(root));
        tree.setBounds(10,40,60,100);
        panel.add(tree);

        JTextField tf=new JTextField("test");
        tf.setBounds(60,10,50,20);
        panel.add(tf);

        JScrollPane scroll=new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(400,400));
        fr.getContentPane().add(scroll);

        final JComboBox cbZoom=new JComboBox(new String[] {"0.5","1","2","3"});
        cbZoom.setSelectedItem("2");
        cbZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                float f=Float.parseFloat(cbZoom.getSelectedItem().toString());
                panel.scale=f;
//                panel.invalidate();
//                panel.validate();
                panel.repaint();
            }
        });
        fr.getContentPane().add(cbZoom);

        fr.setVisible(true);
    }
}
