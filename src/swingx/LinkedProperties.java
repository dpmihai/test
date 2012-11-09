package swingx;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 15, 2008
 * Time: 3:44:24 PM
 */

import java.util.*;

/**
 * A properties implementation that remembers the order of its entries.
 */
public class LinkedProperties extends Properties {

    private final LinkedHashMap map = new LinkedHashMap();

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public synchronized Object get(Object key) {
        return map.get(key);
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized Object clone() {
        return super.clone();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public synchronized boolean contains(Object value) {
        return containsValue(value);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Enumeration elements() {
        return new IteratorEnumeration(map.values().iterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof LinkedProperties)) {
            return false;
        }

        return super.equals(o);
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Enumeration keys() {
        return new IteratorEnumeration(map.keySet().iterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set keySet() {
        return map.keySet();
    }

    @Override
    public Enumeration propertyNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void putAll(Map t) {
        map.putAll(t);
    }

    @Override
    public synchronized Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProperty(String key) {
        final Object oval = get(key);
        final String sval = (oval instanceof String) ? (String) oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }
}