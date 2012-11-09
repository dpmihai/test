package treeinvisible;

import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 6, 2008
 * Time: 11:27:52 AM
 */

public class InvisibleTreeModel extends DefaultTreeModel {

    protected boolean filterIsActive;

    public InvisibleTreeModel(TreeNode root) {
        this(root, false);
    }

    public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren) {
        this(root, false, false);
    }

    public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren
            , boolean filterIsActive) {
        super(root, asksAllowsChildren);
        this.filterIsActive = filterIsActive;
    }

    public void activateFilter(boolean newValue) {
        filterIsActive = newValue;
    }

    public boolean isActivatedFilter() {
        return filterIsActive;
    }

    public Object getChild(Object parent, int index) {
        if (filterIsActive) {
            if (parent instanceof InvisibleNode) {
                return ((InvisibleNode) parent).getChildAt(index, filterIsActive);
            }
        }
        return ((TreeNode) parent).getChildAt(index);
    }

    public int getChildCount(Object parent) {
        if (filterIsActive) {
            if (parent instanceof InvisibleNode) {
                return ((InvisibleNode) parent).getChildCount(filterIsActive);
            }
        }
        return ((TreeNode) parent).getChildCount();
    }

}