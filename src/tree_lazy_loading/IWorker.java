package tree_lazy_loading;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2008
 * Time: 5:21:18 PM
 */
public interface IWorker<T> {

	public void done(T result);

	public T doInBackground();

}
