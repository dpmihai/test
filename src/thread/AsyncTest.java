package thread;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 28, 2006
 * Time: 3:41:27 PM
 */
import java.util.concurrent.*;

public class AsyncTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();

        Callable<Integer> c = new Callable<Integer>() {
            public Integer call() {

                // after long computation
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                return 1;
            }
        };

        Future<Integer> result = exec.submit(c);

        while (!result.isDone()) {
            System.out.println("Not yet.");
        }

        System.out.println("We’ve got: " + result.get());

        // exec should be shutdown to stop this program
        exec.shutdown();
    }
}