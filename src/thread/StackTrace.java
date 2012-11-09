package thread;

import java.util.Map;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 26, 2008
 * Time: 4:34:24 PM
 */
public class StackTrace {

    public void whereami() {
        Map<Thread, StackTraceElement[]> st = Thread.getAllStackTraces();
        System.out.println("#########################################################################");
        System.out.println(">>>> Date=" + new Date());
        for (Map.Entry<Thread, StackTraceElement[]> e : st.entrySet()) {
            StackTraceElement[] el = e.getValue();
            Thread t = e.getKey();
            System.out.println("\"" + t.getName() + "\"" + " " +
                    (t.isDaemon() ? "daemon" : "") + " prio=" + t.getPriority() +
                    " Thread id=" + t.getId() + " " + t.getState());
            for (StackTraceElement line : el) {
                System.out.println("\t" + line);
            }
            System.out.println("");
        }
        System.out.println("#########################################################################");
    }

    public static void main(String args[]) {
        StackTrace t1 = new StackTrace();
        t1.whereami();
    }
}

