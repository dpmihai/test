package proxy;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 5, 2007
 * Time: 10:59:46 AM
 */
import java.lang.reflect.*;

public class Test implements TestInterface
{
    int counter = 0;

    public int count(int count)
    {
        System.out.println("Incrementing counter by: " + count);

        counter += count;
        return counter;
    }

    public static void main(String[] args)
    {
        Test t = new Test();
        RemoteInvoker invoker = new RemoteInvoker(t);
        final TestInterface proxy = (TestInterface) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                new Class[] { TestInterface.class } , invoker);

        int no = 5;
        Thread[] threads = new Thread[no];
        for (int i=0; i<no; i++) {
            final int id = i;
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    System.out.println("Thread  " +  id + " : Proxy returns: " + proxy.count(3));
                }
            });
        }
        for (int i=0; i<no; i++) {            
            threads[i].start();
        }

    }
}