package util;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Set;
import java.util.HashSet;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 12, 2006
 * Time: 11:29:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExplorerUtil2 {

    private static final String SEP = "/";
    private static final String ROOT = "ROOT";


    private static DefaultMutableTreeNode parse(Set<String> set) {

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(ROOT);

        for (String s : set) {

            DefaultMutableTreeNode nodeLink = node;
            String[] result = s.split(SEP);

            int n = result.length;
            for (int i = 1; i <= n; i++) {
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(result[i - 1]);
                //System.out.println("i="+i + "  nodeLink="+nodeLink + "  treeNode="+treeNode);
                DefaultMutableTreeNode foundNode = findNode(nodeLink, treeNode);
                //System.out.println("foundNode="+foundNode);
                if (foundNode == null) {
                    nodeLink.add(treeNode);
                } else {
                    treeNode = foundNode;
                }
                nodeLink = treeNode;
            }
        }

        return node;

    }

    private static DefaultMutableTreeNode findNode(DefaultMutableTreeNode node, DefaultMutableTreeNode child) {
        for (int j=0, len = node.getChildCount(); j<len; j++) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node.getChildAt(j);
            if (treeNode.getUserObject().equals(child.getUserObject())) {
                return treeNode;
            }
        }
        return null;
    }


    public static JTree createJTree(Set<String> set) {
        DefaultMutableTreeNode root = parse(set);
        return new JTree(root);
    }



    public static void main(String[] args) {
        Set<String> set = new HashSet<String>();
        set.add("balanta/raport/test");
        set.add("balanta/raport/test/test1");
        set.add("balanta/raport/test/test1/test1_1");
        set.add("balanta/raport/testR/test1");
        set.add("balanta/raport/test/test1/test1_2");
        set.add("balanta/raport/testKSI");
        set.add("backup");

        JTree tree = createJTree(set);

        JScrollPane scr = new JScrollPane(tree);
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(scr, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 300);
        frame.setLocation(400, 400);
        frame.setTitle("Tree");
        //frameanimation.pack();
        frame.setVisible(true);
    }


}
