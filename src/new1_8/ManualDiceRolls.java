package new1_8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
/*
 * The kinds of problems that parallel stream libraries excel at are those that involve simple
 * operations processing a lot of data, such as simulations
 * 
 * The kind of simulation we’ll be looking at here is a Monte Carlo simulation. Monte Carlo
 * simulations work by running the same simulation many times over with different random
 * seeds on every run. The results of each run are recorded and aggregated in order
 * to build up a comprehensive simulation.
 * 
 * If we throw a fair dice twice and add up the number of dots on the winning side, we’ll
 * get a number between 2 and 12. This must be at least 2 because the fewest number of
 * dots on each side is 1 and there are two dice. The maximum score is 12, as the highest
 * number you can score on each die is 6. We want to try and figure out what the probability
 * of each number between 2 and 12 is.
 * 
 * One approach to solving this problem is to add up all the different combinations of dice
 * rolls that can get us each value. For example, the only way we can get 2 is by rolling 1
 * and then 1 again. There are 36 different possible combinations, so the probability of the
 * two sides adding up to 2 is 1 in 36, or 1/36.
 * 
 * Another way of working it out is to simulate rolling two dice using random numbers
 * between 1 and 6, adding up the number of times that each result was picked, and dividing
 * by the number of rolls. This is actually a really simple Monte Carlo simulation. The
 * more times we simulate rolling the dice, the more closely we approximate the actual
 * result—so we really want to do it a lot.
 * 
 * The majority of the  code implementation deals with spawning, scheduling, and awaiting the 
 * completion of jobs within a thread pool. None of these issues needs to be directly addressed 
 * when using the parallel streams library. (see DiceRolls)
 * 
 */
public class ManualDiceRolls {
	
	private static final int N = 100000000;
	private final double fraction;
	private final Map<Integer, Double> results;
	private final int numberOfThreads;
	private final ExecutorService executor;
	private final int workPerThread;

	public static void main(String[] args) {
		ManualDiceRolls roles = new ManualDiceRolls();
		roles.simulateDiceRoles();
	}

	public ManualDiceRolls() {
		System.out.println("Simulate dice rolls : number of simulations = " + N);
		fraction = 1.0 / N;
		results = new ConcurrentHashMap<>();
		numberOfThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Number of processors = " + numberOfThreads);
		executor = Executors.newFixedThreadPool(numberOfThreads);
		workPerThread = N / numberOfThreads;
	}

	public void simulateDiceRoles() {
		List<Future<?>> futures = submitJobs();
		awaitCompletion(futures);
		printResults();
	}

	private void printResults() {
		results.entrySet().forEach(System.out::println);
	}

	private List<Future<?>> submitJobs() {
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < numberOfThreads; i++) {
			futures.add(executor.submit(makeJob()));
		}
		return futures;
	}

	private Runnable makeJob() {
		return () -> {
			ThreadLocalRandom random = ThreadLocalRandom.current();
			for (int i = 0; i < workPerThread; i++) {
				int entry = twoDiceThrows(random);
				accumulateResult(entry);
			}
		};
	}

	private void accumulateResult(int entry) {
		results.compute(entry, (key, previous) -> previous == null ? fraction : previous + fraction);
	}

	private int twoDiceThrows(ThreadLocalRandom random) {
		int firstThrow = random.nextInt(1, 7);
		int secondThrow = random.nextInt(1, 7);
		return firstThrow + secondThrow;
	}

	private void awaitCompletion(List<Future<?>> futures) {
		futures.forEach((future) -> {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		executor.shutdown();
	}
}