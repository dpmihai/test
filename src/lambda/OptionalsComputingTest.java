package lambda;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Create a divide of two Integer optionals
 *
 * Created by Mihai Dinca-Panaitescu on 25.07.2017.
 */
public class OptionalsComputingTest {

    public static Double divide(Integer first, Integer second) {
        if (second == null) {
            return null; // undefined
        } else {
            return first.doubleValue() / second.doubleValue();
        }
    }

    // Wrong way of using Optional because we unwrapped the optionals
    public static Optional<Double> divideWrong(Optional<Integer> first, Optional<Integer> second) {
        if(first.isPresent() && second.isPresent()) {
            double result = divide(first.get(), second.get());
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    // Better way of using Optional in a functional style
    public static Optional<Double> divideBetter(Optional<Integer> first, Optional<Integer> second) {
        return first.flatMap(f -> second.map(s -> divide(f,s)));
    }


    // Best way of using Optional by creating a BiFunction
    public  static <R, T, Z> BiFunction<Optional<T>, Optional<R>, Optional<Z>> compute(BiFunction<? super T, ? super R, ? extends Z> function) {
        return (left, right) -> left.flatMap(leftVal -> right.map(rightVal -> function.apply(leftVal, rightVal)));
    }

    public static Optional<Double> divideBest(Optional<Integer> first, Optional<Integer> second) {
        return compute(OptionalsComputingTest::divide).apply(first, second);
    }


    public static void main(String[] args) {

        NumberProvider np = new RandomNumberProvider();

        Optional<Integer> first = np.getNumber();
        System.out.println("* first generated number: " + first);

        Optional<Integer> second = np.getNumber();
        System.out.println("* second generated number: " + second);

        Optional<Double> result = divideWrong(first, second);
        System.out.println("* (1) result: " + result);

        result = divideBetter(first, second);
        System.out.println("* (2) result: " + result);

        result = divideBest(first, second);
        System.out.println("* (3) result: " + result);
    }
}
