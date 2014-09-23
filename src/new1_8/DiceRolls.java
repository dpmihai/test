package new1_8;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;


public class DiceRolls {
	private static final int N = 100000000;

	public static void main(String[] ignore) throws IOException {
		DiceRolls dr = new DiceRolls();
		
		System.out.println("Start serial dice rolls ...");
		long start = System.currentTimeMillis();
		Map<Integer, Double> results = dr.serialDiceRolls();
		long end = System.currentTimeMillis();
		printResults(results);
		System.out.println("End serial dice rolls in " + (end-start)/1000 + " seconds");
		
		System.out.println("\nStart parallel dice rolls ...");
		start = System.currentTimeMillis();
		results = dr.parallelDiceRolls();
		end = System.currentTimeMillis();
		printResults(results);
		System.out.println("End parallel dice rolls in " + (end-start)/1000 + " seconds");
	}


	
	public Map<Integer, Double> serialDiceRolls() {
		double fraction = 1.0 / N;
		return IntStream.range(0, N).mapToObj(twoDiceThrows()).collect(groupingBy(side -> side, summingDouble(n -> fraction)));
	}

	
	public Map<Integer, Double> parallelDiceRolls() {
		double fraction = 1.0 / N;
		return IntStream.range(0, N) // <1>
				.parallel() // <2>
				.mapToObj(twoDiceThrows()) // <3>
				.collect(groupingBy(side -> side, // <4>
						summingDouble(n -> fraction))); // <5>
	}

	// END parallel
	private static IntFunction<Integer> twoDiceThrows() {
		return i -> {
			ThreadLocalRandom random = ThreadLocalRandom.current();
			int firstThrow = random.nextInt(1, 7);
			int secondThrow = random.nextInt(1, 7);
			return firstThrow + secondThrow;
		};
	}
	
	private static void printResults(Map<Integer, Double> results) {
		results.entrySet().forEach(System.out::println);
	}

}