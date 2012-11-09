package tree;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2006
 * Time: 11:34:38 AM
 */
public interface HierarchicalItem {
    public abstract void setData(Object data);

    public abstract Object getData();

    public abstract void setId(Object id);

    public abstract Object getId();

    public abstract Object getParentId();

    public abstract void setParentId(Object parentId);

    public abstract boolean isRoot();
}