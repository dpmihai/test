package reference.phantom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 1:31:31 PM
 */
import java.util.*;

public class NativeResourceTest {

    public static void main(String[] args) {
        NativeResourceReclaimer reclaimer = new NativeResourceReclaimer();
        Thread reclaimerThread = new Thread(reclaimer, "Reclaimer");
        reclaimerThread.setDaemon(true);
        reclaimerThread.start();

        Timer memoryHog = new Timer(true);
        memoryHog.schedule(new MemoryHog(), 100, 100);

        for (int i = 0; i < 10; i++) {
            final long handle = i;
            Object referent = new Object() {
                    private final long h = handle;

                    public String toString() {
                        return (super.toString() + " [" + this.h + "]");
                    }

                    protected void finalize() throws Throwable {
                        try {
                            System.out.println("finalize " + this);
                        } finally {
                            super.finalize();
                        }
                    }
                };
            reclaimer.register(referent, handle);
        }

        // don't exit main thread
        try {
            Thread.currentThread().join();
        } catch (InterruptedException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
