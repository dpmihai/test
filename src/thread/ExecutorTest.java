package thread;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 28, 2006
 * Time: 3:41:51 PM
 */
import java.util.concurrent.*;

public class ExecutorTest {

    public static void main(String[] args) throws InterruptedException {

        // This executor creates a thread if there are no thread available.
        // If there are previously created but idle threads, reuse them.
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(
                new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                Thread.sleep(100000);
                                System.out.println("##");
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        for (int i = 100; i >= 0; i--) {
            System.out.println(i);
            Thread.sleep(1);
        }

        exec.shutdownNow();

    }

}