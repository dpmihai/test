package lambda;

import java.util.Optional;
import java.util.Random;

/**
 * Generate a random integer as an Optional
 *
 * Created by Mihai Dinca-Panaitescu on 25.07.2017.
 */
public class RandomNumberProvider implements NumberProvider {

    @Override
    public Optional<Integer> getNumber() {
        Integer number = null;
        Random r = new Random();
        int b = r.nextInt(10);
        if (b > 0) {
            number = r.nextInt(20);
        }
        return Optional.ofNullable(number);
    }
}
