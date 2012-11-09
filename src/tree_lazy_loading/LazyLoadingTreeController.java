package tree_lazy_loading;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 10, 2008
 * Time: 5:24:03 PM
 */
import java.util.concurrent.ExecutionException;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingworker.SwingWorker;

public class LazyLoadingTreeController implements TreeWillExpandListener {

	public static class DefaultWorkerFactory implements SwingWorkerFactory<MutableTreeNode[], Object> {

		public SwingWorker<MutableTreeNode[], Object> getInstance(final IWorker<MutableTreeNode[]> worker) {
			final SwingWorker<MutableTreeNode[], Object> myWorker =
				new SwingWorker<MutableTreeNode[], Object>() {

				@Override
				protected void done() {
					try {
						worker.done(get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				@Override
				protected MutableTreeNode[] doInBackground() throws Exception {
					return worker.doInBackground();
				}
			};
			return myWorker;
		}

	}

	private SwingWorkerFactory<MutableTreeNode[], ?> workerFactory = new DefaultWorkerFactory();
	/** Tree Model */
	private DefaultTreeModel model;
	/**
	 * Default constructor
	 * @param model Tree model
	 */
	public LazyLoadingTreeController(DefaultTreeModel model) {
		this.model = model;
	}

	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		//Do nothing on collapse.
	}
	/**
     * Invoked whenever a node in the tree is about to be expanded.
     * If the Node is a LazyLoadingTreeNode load it's children in a SwingWorker
     */
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath path = event.getPath();
		Object lastPathComponent = path.getLastPathComponent();
		if (lastPathComponent instanceof LazyLoadingTreeNode) {
			LazyLoadingTreeNode lazyNode = (LazyLoadingTreeNode) lastPathComponent;
			expandNode(lazyNode, model);
		}
	}

	/**
	 * If the Node is not already loaded
	 * @param node node
	 * @param model model
	 */
	public void expandNode(final LazyLoadingTreeNode node, final DefaultTreeModel model) {
		if (node.areChildrenLoaded()) {
			return;
		}
		node.setChildren(createLoadingNode());
		SwingWorker<MutableTreeNode[], ?> worker = createSwingWorker(node);
		worker.execute();
	}

	/**
	 *
	 * @return a new Loading please wait node
	 */
	protected MutableTreeNode createLoadingNode() {
		return new DefaultMutableTreeNode("Loading ...", false);
	}

	protected IWorker<MutableTreeNode[]> getWorkerInterface(final LazyLoadingTreeNode node) {
		return new IWorker<MutableTreeNode[]>() {

			public void done(MutableTreeNode[] nodes) {
				node.setAllowsChildren(nodes != null && nodes.length > 0);
				node.setChildren(nodes);
			}

			public MutableTreeNode[] doInBackground() {
				return node.loadChildren(model);
			}
		};
	}

	/**
	 * Create worker that will load the nodes
	 * @param node the tree node
	 * @return the newly created SwingWorker
	 */
	protected SwingWorker<MutableTreeNode[], ?> createSwingWorker(
			final LazyLoadingTreeNode node) {
		return getWorkerFactory().getInstance(getWorkerInterface(node));
	}
	
	public SwingWorkerFactory<MutableTreeNode[], ?> getWorkerFactory() {
		if (workerFactory == null) {
			workerFactory = new DefaultWorkerFactory();
		}
		return workerFactory;
	}

	public void setWorkerFactory(SwingWorkerFactory<MutableTreeNode[], ?> workerFactory) {
		this.workerFactory = workerFactory;
	}
}
