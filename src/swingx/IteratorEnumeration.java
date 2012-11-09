package swingx;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 15, 2008
 * Time: 3:47:47 PM
 */

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration implements Enumeration {
    private Iterator iterator;

    public IteratorEnumeration(Iterator iterator) {
        this.iterator = iterator;
    }

    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    public Object nextElement() {
        return iterator.next();
    }
}