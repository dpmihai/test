package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by: Mihai Dinca-Panaitescu
 * Date: 08.02.2017
 */
public class LambdaPredicateTest {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1,3,4,6,7,13,16);
        System.out.println(filterNumbers(numbers, isOdd()));

        Optional<String> receivedMessage = Optional.ofNullable(null);
        String s = getNotificationMessage(receivedMessage);
        System.out.println("Got notification: " + s);
    }

    public static Predicate<Integer> isOdd() {
        return n -> n%2 !=0;
    }

    public static List<Integer> filterNumbers (List<Integer> numbers, Predicate<Integer> predicate) {
        return numbers.stream().filter( predicate ).collect(Collectors.<Integer>toList());
    }

    public static String getNotificationMessage(Optional<String> received) {
        return received.map(value -> value).orElse("...");
    }
}
