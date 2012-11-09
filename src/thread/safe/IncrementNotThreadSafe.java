package thread.safe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IncrementNotThreadSafe {
	public static void main(String... args) throws InterruptedException {
		for (int nThreads = 1; nThreads <= 64; nThreads *= 2)
			doThreadSafeTest(nThreads);
	}

	static class VolatileInt {
		volatile int num = 0;
	}

	private static void doThreadSafeTest(final int nThreads) throws InterruptedException {
		
		final int count = 100 * 1000 * 1000;

		ExecutorService es = Executors.newFixedThreadPool(nThreads);
		final VolatileInt vi = new VolatileInt();
		// final AtomicInteger num = new AtomicInteger();
		for (int i = 0; i < nThreads; i++)
			es.submit(new Runnable() {
				public void run() {
					for (int j = 0; j < count; j += nThreads) {
						vi.num++;
					    // num.incrementAndGet();
					}	
				}
			});
		es.shutdown();
		es.awaitTermination(1, TimeUnit.MINUTES);
		assert es.isTerminated();
		System.out.printf("With %,d threads should total %,d but was %,d%n",
				nThreads, count, vi.num /* num.longValue() */);
	}

}
