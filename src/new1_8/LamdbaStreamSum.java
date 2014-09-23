package new1_8;

import java.util.stream.Stream;

public class LamdbaStreamSum {
	
	public static void main(String[] args) {
		
		int sum = Stream.of(1, 2, 3)
				.reduce(0, (acc, element) -> acc + element);
		
		System.out.println("sum=" +sum);
	}

}
