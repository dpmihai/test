package comparator;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Sep 14, 2006
 * Time: 3:00:24 PM
 */

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PQueue {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq =
                new PriorityQueue<Integer>(20,
                        new Comparator<Integer>() {
                            // even numbers before odd ones
                            public int compare(Integer i, Integer j) {
                                int result = i % 2 - j % 2;
                                if (result == 0)
                                    result = i - j;
                                return result;
                            }
                        }
                );

        // Fill up with data, in an odd order
        for (int i = 0; i < 20; i++) {
            pq.offer(20 - i);
        }
        // Print out and check ordering
        for (int i = 0; i < 20; i++) {
            System.out.println(pq.poll());
        }
    }
}