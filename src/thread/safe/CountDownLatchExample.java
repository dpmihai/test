package thread.safe;

import java.util.concurrent.CountDownLatch;


public class CountDownLatchExample {
	  public static void main(String args[]) {
	    CountDownLatch cdl = new CountDownLatch(5);
	    new MyThread(cdl);
	   
	    try {
	      // Causes the current thread to wait until the latch has counted down to
	      // zero, unless the thread is interrupted
	      cdl.await();
	    } catch (InterruptedException exc) {
	      System.out.println(exc);
	    }
	    System.out.println("Done");
	  }
	}
	   
	class MyThread implements Runnable {
	  CountDownLatch latch;
	   
	  MyThread(CountDownLatch c) {
	    latch = c;
	    new Thread(this).start();
	  }
	   
	  public void run() {
	    for(int i = 0; i<5; i++) {
	      System.out.println(i);
	      latch.countDown(); // decrement count
	    }
	  }
	}

