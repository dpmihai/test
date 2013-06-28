package bet;

import java.util.Random;

public class Test {
	
	public static void main(String[] args) {
				
		int[] one = randomScore(1, 10);
		System.out.print("1<->10 ");
		print(one);
		
		int[] two = randomScore(3, 4);
		System.out.print("3<->4 ");
		print(two);
		
		int[] three = randomScore(12, 11);
		System.out.print("12<->11 ");
		print(three);
		
	}
	
	private static void print(int[] result) {
		System.out.println(result[0] + " : " + result[1]);
	}
	
	
	private static int[] randomScore(int hPos, int gPos) {
		int[] result = new int[2];
		
		int d = hPos - gPos;
		
		if (d < -2) {
			result[0] = new Random().nextInt(5);
			if (result[0] <= 1) {
				result[1] = 0;
			} else {
				result[1] = new Random().nextInt(result[0]);
			}
		} else if (d > 2) {
			result[1] = new Random().nextInt(5);
			if (result[1] <= 1) {
				result[0] = 0;
			} else {
				result[0] = new Random().nextInt(result[1]);
			}			
		} else {
			result[0] = new Random().nextInt(5);
			result[1] = new Random().nextInt(5);
		}
		
		return result;		
	}

}
