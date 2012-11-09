package puzzles;

public class ExitTest {
	
	private static final Object lock = new Object();

	public static void main(String... args) {
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        @Override
	        public void run() {
	            System.out.println("Locking");
	            synchronized (lock) {
	                System.out.println("Locked");
	            }
	        }
	    }));
	    synchronized (lock) {
	        //System.exit(0);               // application stays locked
	    	Thread.currentThread().stop();  // application ends because stop unlocks all of the monitors that it has locked
	    }
	}

}
