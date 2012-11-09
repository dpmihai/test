package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 5, 2007
 * Time: 11:23:12 AM
 */

// http://unserializableone.blogspot.com/2007/08/make-use-of-java-dynamic-proxy.html

// Want to test a method with and without the synchronize block.
// By using a invocation handler, I can have a variable to control whether or not I want to
// use synchronize for that particular operation.
public class Handler implements InvocationHandler {

    private final Map map;
    private boolean useSynch = false;

    public Handler(Map map) {
        this.map = map;
    }

    public void setUseSynch(boolean flag) {
        this.useSynch = flag;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (useSynch) {
                return invokeWithSynch(method, args);
            } else {
                return method.invoke(map, args);
            }
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private Object invokeWithSynch(Method method, Object[] args) throws Throwable {
        synchronized (map) {
            return method.invoke(map, args);
        }
    }
}


