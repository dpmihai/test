package thread.safe;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BufferWithLockConditions {
	ReentrantLock lock = new ReentrantLock();
	private String[] names;
	private int MAXLENGTH = 10;
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();
	private int putPosition, getPosition, count;

	public BufferWithLockConditions() {
		names = new String[MAXLENGTH];
	}

	public void put(String str) {
		lock.lock();
		try {
			System.out.println("Writer : Array Size : " + count);
			while (count == MAXLENGTH) {
				System.out.println("Writer Waiting");
				notFull.await();
			}
			count++;
			names[putPosition++] = str;
			if (putPosition == MAXLENGTH)
				putPosition = 0;
			Thread.sleep(100);
			notEmpty.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void get() {
		lock.lock();
		try {
			System.out.println("Reader : Array Size : " + count);
			while (count == 0) {
				System.out.println("Reader Waiting");
				notEmpty.await();
			}
			count--;
			names[getPosition++] = null;
			if (getPosition == MAXLENGTH)
				getPosition = 0;
			Thread.sleep(100);
			notFull.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final BufferWithLockConditions bc = new BufferWithLockConditions();
		ExecutorService es = Executors.newFixedThreadPool(10);		
		for (int i = 0; i < 10; i++) {			
			es.submit(new Runnable() {
				@Override
				public void run() {
					Random random = new Random();
					int val = random.nextInt(2);
					if (val == 0) {
						bc.get();
					} else {
						bc.put(new BigInteger(130, random).toString(32));
					}
				}
			});
		}
		es.shutdown();
		try {
			es.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
