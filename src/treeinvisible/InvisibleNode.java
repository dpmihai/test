package treeinvisible;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2008
 * Time: 11:27:01 AM
 */
import javax.swing.tree.*;
import java.util.Enumeration;

public class InvisibleNode extends DefaultMutableTreeNode {

    protected boolean isVisible;

    public InvisibleNode() {
        this(null);
    }

    public InvisibleNode(Object userObject) {
        this(userObject, true, true);
    }

    public InvisibleNode(Object userObject, boolean allowsChildren
            , boolean isVisible) {
        super(userObject, allowsChildren);
        this.isVisible = isVisible;
    }

    public TreeNode getChildAt(int index, boolean filterIsActive) {
        if (!filterIsActive) {
            return super.getChildAt(index);
        }
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        int realIndex = -1;
        int visibleIndex = -1;
        Enumeration menum = children.elements();
        while (menum.hasMoreElements()) {
            InvisibleNode node = (InvisibleNode) menum.nextElement();
            if (node.isVisible()) {
                visibleIndex++;
            }
            realIndex++;
            if (visibleIndex == index) {
                return (TreeNode) children.elementAt(realIndex);
            }
        }

        throw new ArrayIndexOutOfBoundsException("index unmatched");

    }

    public int getChildCount(boolean filterIsActive) {
        if (!filterIsActive) {
            return super.getChildCount();
        }
        if (children == null) {
            return 0;
        }

        int count = 0;
        Enumeration menum = children.elements();
        while (menum.hasMoreElements()) {
            InvisibleNode node = (InvisibleNode) menum.nextElement();
            if (node.isVisible()) {
                count++;
            }
        }

        return count;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

}