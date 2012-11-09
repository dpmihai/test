package tree_lazy_loading;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2008
 * Time: 5:24:45 PM
 */
public interface SwingWorkerFactory<T, V> {

	public org.jdesktop.swingworker.SwingWorker<T, V> getInstance(final IWorker<T> worker);

}
