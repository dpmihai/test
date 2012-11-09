package util;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 9, 2006
 * Time: 3:40:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExplorerUtil {

    private static final String SEP = "/";
    private static int level = 1;


    private static TreeNode parse(Set<String> set) {

        TreeNode node = new TreeNode(TreeNode.ROOT);

        for (String s : set) {

            TreeNode nodeLink = node;
            String[] result = s.split(SEP);

            int n = result.length;
            for (int i = 1; i <= n; i++) {
                TreeNode tn = new TreeNode(result[i - 1]);
                tn = nodeLink.addChild(tn);
                nodeLink = tn;
            }
        }

        //TreeNode.print(node);
        return node;

    }

    public static void print(TreeNode node) {
        System.out.println(node.getNode() + " : " + node.getPath() + " : " + node.getChildrenCount());
        for (TreeNode n : node.getChildren()) {
            System.out.println(" ");
            for (int i = 0; i < level; i++) {
                System.out.print("-");
            }
            level++;
            print(n);
            level--;
        }

    }

    public static JTree createJTree(Set<String> set) {
        TreeNode root = parse(set);
        return createJTree(root);
    }

    private static JTree createJTree(TreeNode rootNode) {
        DefaultMutableTreeNode rootS = new DefaultMutableTreeNode(rootNode.getNode());
        createJTree(rootNode, rootS);
        return new JTree(rootS);
    }


    private static void createJTree(TreeNode node, DefaultMutableTreeNode nodeS) {
        for (TreeNode n : node.getChildren()) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(n.getNode());
            nodeS.add(child);
            createJTree(n, child);
        }
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<String>();
        set.add("balanta/raport/test");
        set.add("balanta/raport/test/test1");
        set.add("balanta/raport/test/test1/test1_1");
        set.add("balanta/raport/testR/test1");
        set.add("balanta/raport/test/test1/test1_2");
        set.add("backup");

//        TreeNode root = parse(set);
//        print(root);
//        JTree tree = createJTree(root);
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

    static class TreeNode {
        private String node;
        private Set<TreeNode> children;
        private String path;
        public static final String ROOT = "ROOT";

        public TreeNode(String node) {
            this.node = node;
            this.path = ROOT;
            this.children = new HashSet<TreeNode>();
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public Set<TreeNode> getChildren() {
            return children;
        }

        public int getChildrenCount() {
            return children.size();
        }

        public void setChildren(Set<TreeNode> children) {
            this.children = children;
        }

        public TreeNode addChild(TreeNode tn) {
            tn.setPath(path + SEP + tn.getNode());
            boolean add = children.add(tn);
            if (!add) {
                for (TreeNode n : children) {                    
                    if (tn.equals(n)) {
                        // node already exists
                        return n;
                    }
                }
            } else {
                return tn;
            }
            return null;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isLeaf() {
            return children.size() == 0;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final TreeNode treeNode = (TreeNode) o;

            if (path != null ? !path.equals(treeNode.path) : treeNode.path != null) return false;

            return true;
        }

        public int hashCode() {
            return (path != null ? path.hashCode() : 0);
        }


    }
}
