package print;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: May 7, 2007
 * Time: 1:59:29 PM
 */
public class PrintJobWatcher {
    // true iff it is safe to close the print job's input stream
    boolean done = false;

    PrintJobWatcher(DocPrintJob job) {
        // Add a listener to the print job
        job.addPrintJobListener(new PrintJobAdapter() {
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }

            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }

            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }

            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    public synchronized void waitForDone() {
        try {
            while (!done) {
                wait();
            }
        } catch (InterruptedException e) {
        }
    }
}