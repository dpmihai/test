package thread.safe;

import java.util.ArrayList;
import java.util.List;

/** Or how to crash your OS :) **/
public class MaxThreads {

	public static final int BATCH_SIZE = 4000;

	public static void main(String... args) throws InterruptedException {
		List<Thread> threads = new ArrayList<Thread>();
		try {
			for (int i = 0; i <= 100 * 1000; i += BATCH_SIZE) {
				long start = System.currentTimeMillis();
				addThread(threads, BATCH_SIZE);
				long end = System.currentTimeMillis();
				Thread.sleep(1000);
				long delay = end - start;
				System.out.printf("%,d threads: Time to create %,d threads was %.3f seconds %n",
								threads.size(), BATCH_SIZE, delay / 1e3);
			}
		} catch (Throwable e) {
			System.err.printf("After creating %,d threads, ", threads.size());
			e.printStackTrace();
		}

	}

	private static void addThread(List<Thread> threads, int num) {
		for (int i = 0; i < num; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (!Thread.interrupted()) {
							Thread.sleep(1000);
						}
					} catch (InterruptedException ignored) {
						//
					}
				}
			});
			t.setDaemon(true);
			t.setPriority(Thread.MIN_PRIORITY);
			threads.add(t);
			t.start();
		}
	}
}
