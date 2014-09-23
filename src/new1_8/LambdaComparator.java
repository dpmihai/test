package new1_8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LambdaComparator {
		
	
	public static void main(String[] args) {
		
		String[] names = {"Dragon", "Dad", "Ant", "Bloom"};
		List<String> list = Arrays.asList(names);
		System.out.println("Unsorted list : " + list);

		Comparator<String> c = (a, b) -> Integer.compare(a.length(), b.length() );
//		Comparator<String> c = new Comparator<String>() {
//			public int compare(String o1, String o2) {
//				int i = Integer.compare(o1.length(), o2.length());
//				if (i == 0) {
//					return o1.compareTo(o2);
//				} else {
//					return i;
//				}
//			}
//		};
		Collections.sort(list, c);
		System.out.println("Sorted list : " + list);
		
	}

}
