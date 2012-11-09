package proxy;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 5, 2007
 * Time: 10:58:27 AM
 */
import java.util.concurrent.*;
import java.lang.reflect.*;

//http://javathink.blogspot.com/2007/10/reimplementing-rmi.html

// What I really like about this solution, and Terracotta in general, is that it works exactly
// the same in a single JVM as many JVMs. The key difference to many JVMs is that by clustering the Queue,
// the execution is transferred across physical JVMs, and the wait/notify sends the response back -
// no different than cross-thread communication in a single JVM.

// run() method is synchronized against instance. The requirement was that only one instance
// can be "connected" - in other words servicing requests. The synchronized makes sure only one
// instance is servicing the queue - whether it's a single JVM or many JVMs.

public class RemoteInvoker implements InvocationHandler, Runnable
{
    private BlockingQueue<MethodArguments> queue = new LinkedBlockingQueue<MethodArguments>();

    private final Object instance;

    public RemoteInvoker(Object instance)
    {
        this.instance = instance;
        start();
    }

    private static class MethodArguments
    {
        public final Object proxy;
        public final Method method;
        public final Object[] args;
        private MethodResult result;

        public MethodArguments(Object proxy, Method method, Object[] args)
        {
            this.proxy = proxy;
            this.method = method;
            this.args = args;
        }

        public synchronized MethodResult getResult() throws InterruptedException
        {
           while (result == null) { wait(); }
           return result;
        }

        public synchronized void setResult(MethodResult result)
        {
            this.result = result;
            notify();
        }
    }

    private static class MethodResult
    {
        public final Object object;
        public final Exception exception;

        public MethodResult(Object object, Exception exception)
        {
            this.object = object;
            this.exception = exception;
        }
    }

    private void start()
    {
        Thread t = new Thread(this);
//        t.setDaemon(true);
        t.start();
    }

    public void run()
    {
        synchronized (instance) {
            System.out.println("I am servicing requests...");

            MethodArguments arguments;
            while (true) {
                try {
                    arguments = queue.take();
                    try {
                        Object value = arguments.method.invoke(instance, arguments.args);
                        arguments.setResult(new MethodResult(value, null));
                    } catch (Exception e) {
                        arguments.setResult(new MethodResult(null, e));
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        MethodArguments arguments = new MethodArguments(proxy, method, args);
        queue.put(arguments);
        MethodResult result;

        result = arguments.getResult();
        if (result.exception != null) {
            throw result.exception;
        }

        return result.object;
    }

}