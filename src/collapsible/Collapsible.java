package collapsible;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 11:13:36 AM
 */
public interface Collapsible {

    final boolean collapsible = true;

    /**
   * Tells you whether this component is collapsible.
   * @return a boolean indicating this component is collapsible.
   */

    public boolean isCollapsible();

  /**
   * Tells you whether this component is currently collapsed.
   * Useful for checking the component's status.
   * @return true if this component is collapsed, false if it is not.
   */

    public boolean isCollapsed();

    public void expand();
    public void collapse();
}
