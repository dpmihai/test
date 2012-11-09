package new1_7;

import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


// SecondaryLoop alternative to SwingWorker
// http://sellmic.com/blog/2012/02/29/hidden-java-7-features-secondaryloop/
//
// Every time we have a task that can take a long time, we can then use SecondaryLoop 
// to do the work in a thread but hold the flow of execution without blocking the UI 
// until that thread is done with its work. 
public class NotFreezingUI {
	
	private boolean working = false;
	
	 /**
	 * Given a root file/directory, return all files underneath it.
	 * This method can be safely invoked from the UI dispatch
	 * thread without blocking the UI
	 */
	 public List<File> findFiles(final File baseDir) {
	        working = true;
	        final List<File> files = new ArrayList<File>();

	        if (baseDir == null || !baseDir.isDirectory()) {
	            System.out.println("Not a directory!");
	            return null;
	        }

	        if (baseDir != null && baseDir.exists() &&  baseDir.isDirectory()) {
	            
	        	Toolkit kit = Toolkit.getDefaultToolkit();

	            // Create secondary loop from awt Toolkit, this is the object
	            // we'll use to block the flow of code without freezing the UI
	            // until another task/thread finishes its work
	            final SecondaryLoop loop = kit.getSystemEventQueue().createSecondaryLoop();

	            Thread work = new Thread() {

	                public void run() {
	                    getFiles(baseDir, files);
	                    loop.exit();
	                }
	            };

	            // We start the thread to do the real work
	            work.start();

	            // Blocks until loop.exit() is called
	            loop.enter();
	        }

	        return files;
	    }
	    /**
	    * Recursively traverse all children of "root"
	    * and store them in "files"
	    */
	    public void getFiles(File root, List<File> files) {
	        if (root.isDirectory() && working) {
	            File[] children = root.listFiles();

	            if (children != null) {
	                for (File file : children) {
	                    if (file.isDirectory()) {
	                        getFiles(file, files);
	                    } else {
	                        files.add(file);
	                        //status(file.getAbsolutePath());
	                    }

	                    if (!working) {
	                        break;
	                    }
	                }
	            }
	        }
	    }

}
