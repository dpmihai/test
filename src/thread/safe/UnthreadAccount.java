package thread.safe;

public class UnthreadAccount {
	public static void main(String... args) {
		int accountCount = 20 * 1000;
		Account[] accounts = new Account[accountCount];
		for (int i = 0; i < accounts.length; i++)
			accounts[i] = new Account(100);
		for (int r = 0; r < 9; r++) {
			long start = System.nanoTime();
			for (int i = 0; i < accountCount; i += 2) {
				transfer(accounts[i], accounts[i + 1], 10);
				transfer(accounts[i + 1], accounts[i], 11);
			}
			long time = System.nanoTime() - start;
			System.out
					.printf("Took an average of %,d ns to transfer money, single threaded%n",
							time / accountCount);
		}
	}

	private static void transfer(Account from, Account to, int amount) {
		if (from.balance() < amount)
			throw new IllegalArgumentException();
		from.transfer(-amount);
		to.transfer(amount);
	}

	static class Account {
		private long balance;

		public Account(long balance) {
			this.balance = balance;
		}

		public long balance() {
			return balance;
		}

		public void transfer(long delta) {
			if (delta + balance < 0)
				throw new IllegalStateException();
			balance += delta;
		}
	}
}
