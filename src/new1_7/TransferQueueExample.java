package new1_7;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * Java 7 introduced the TransferQueue. This is essentially a BlockingQueue with an
 * additional operation—transfer(). This operation will immediately transfer a work
 * item to a receiver thread if one is waiting. Otherwise it will block until there is a thread
 * available to take the item. This can be thought of as the “recorded delivery” option—
 * the thread that was processing the item won’t begin processing another item until it
 * has handed off the current item. This allows the system to regulate the speed at which
 * the upstream thread pool takes on new work.
 * 
 * @author Mihai Dinca-Panaitescu
 * @date 03.12.2012
 */
public class TransferQueueExample {
	
	public TransferQueueExample() {
		
		final TransferQueue<String> lbq = new LinkedTransferQueue<>();
		MicroBlogExampleThread t1 = new MicroBlogExampleThread(lbq, 10) {
			public void doAction() {				
				String u = "Server starting at ...";
				boolean handed = false;
				try {
					handed = updates.tryTransfer(u, 100, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
				}
				if (!handed) {
					System.out.println("Unable to hand off String to Queue due to timeout");
				}
			}
		};
		MicroBlogExampleThread t2 = new MicroBlogExampleThread(lbq, 1000) {
			public void doAction() {
				String u = null;
				try {
					u = updates.take();
					System.out.println(">>>>> Took : " + u);
				} catch (InterruptedException e) {
					return;
				}
			}
		};
		t1.start();
		t2.start();
	}
		
	
	abstract class MicroBlogExampleThread extends Thread {
		protected final TransferQueue<String> updates;
		protected String text = "";
		protected final int pauseTime;
		private boolean shutdown = false;

		public MicroBlogExampleThread(TransferQueue<String> lbq_, int pause_) {
			updates = lbq_;
			pauseTime = pause_;
		}

		public synchronized void shutdown() {
			shutdown = true;
		}

		@Override
		public void run() {
			while (!shutdown) {
				doAction();
				try {
					Thread.sleep(pauseTime);
				} catch (InterruptedException e) {
					shutdown = true;
				}
			}
		}

		public abstract void doAction();
	}
	
	
	public static void main(String[] args) {
		new TransferQueueExample();
	}
	
	
	
}
