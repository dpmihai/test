package thread.safe;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author peter.lawrey
 */
public class ThreadAccountOneLock {
	public static void main(String... args) {
		int accountCount = 20 * 1000;
		Account[] accounts = new Account[accountCount];
		for (int i = 0; i < accounts.length; i++)
			accounts[i] = new Account(100);
		for (int r = 0; r < 9; r++) {
			long start = System.nanoTime();
			for (int i = 0; i < accountCount; i += 2) {
				transfer(accounts, accounts[i], accounts[i + 1], 10);
				transfer(accounts, accounts[i + 1], accounts[i], 11);
			}
			long time = System.nanoTime() - start;
			System.out
					.printf("Took an average of %,d ns to transfer money with a one global lock.%n",
							time / accountCount);
		}
	}

	private static void transfer(Object lock, Account from, Account to,
			int amount) {
		synchronized (lock) {
			if (from.balance() < amount)
				throw new IllegalArgumentException();
			from.transfer(-amount);
			to.transfer(amount);
		}
	}

	static class Account implements Comparable<Account> {
		private final AtomicLong IDS = new AtomicLong();
		private final long id = IDS.getAndIncrement();
		private final Lock lock = new ReentrantLock();
		private long balance;

		public Account(long balance) {
			this.balance = balance;
		}

		public Lock lock() {
			return lock;
		}

		public long balance() {
			return balance;
		}

		public void transfer(long delta) {
			if (delta + balance < 0)
				throw new IllegalStateException();
			balance += delta;
		}

		@Override
		public int compareTo(Account o) {
			return id > o.id ? +1 : -1;
		}
	}
}
