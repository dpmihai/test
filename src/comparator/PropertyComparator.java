package comparator;



import java.util.Comparator;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 29, 2006
 * Time: 11:45:50 AM
 */
public class PropertyComparator implements Comparator {

    private String propertyName;

    /**
     * Constructor
     * @param propertyName the name property from a bean class
     */
    public PropertyComparator(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("Property Name cannot be null!");
        }
        this.propertyName = propertyName;
    }

    public int compare(Object o1, Object o2) {

        if ((o1 == null) || (o2 == null)) {
            return -1;
        }

        if (!o1.getClass().equals(o2.getClass())) {
            return -1;
        }

        Field[] fields = o1.getClass().getDeclaredFields();
        Field f = find(fields);

        if (f == null) {
            return -1;
        }

        try {
            Method m = o1.getClass().getDeclaredMethod("get" + capitalize(propertyName));

            try {
                Object r1 = m.invoke(o1);
                Object r2 = m.invoke(o2);
                //System.out.println("r1="+r1 + "  r2="+r2);

                String className = f.getType().getName();
                //System.out.println("class="+className);

                if (className.equals("java.lang.String")) {
                    return ((String)r1).compareTo((String)r2);
                } else if (className.equals("java.lang.Byte") || className.equals("byte")) {
                    return ((Byte)r1).compareTo((Byte)r2);
                } else if (className.equals("java.lang.Short") || className.equals("short")) {
                    return ((Short)r1).compareTo((Short)r2);
                } else if (className.equals("java.lang.Integer") || className.equals("int")) {
                    return ((Integer)r1).compareTo((Integer)r2);
                } else if (className.equals("java.lang.Float") || className.equals("float")) {
                    return ((Float)r1).compareTo((Float)r2);
                } else if (className.equals("java.lang.Double") || className.equals("double")) {
                    return ((Double)r1).compareTo((Double)r2);
                } else if (className.equals("java.lang.BigInteger")) {
                    return ((BigInteger)r1).compareTo((BigInteger)r2);
                } else if (className.equals("java.lang.Character") || className.equals("char")) {
                    return ((Character)r1).compareTo((Character)r2);
                } else if (className.equals("java.lang.Boolean") || className.equals("boolean")) {
                    return ((Boolean)r1).compareTo((Boolean)r2);
                }

                return -1;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return -1;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return -1;
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Field find(Field[] fields) {
        for (Field field : fields) {
            if (field.getName().equals(propertyName)) {
                return field;
            }
        }
        return null;
    }

    private String capitalize(String str) {
        StringBuilder sb = new StringBuilder();
        char ch;
        char prevCh;
        int i;
        prevCh = '.';  // Prime the loop with any non-letter character.
        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isLetter(ch) && ! Character.isLetter(prevCh)) {
                sb.append(Character.toUpperCase(ch));
            } else {
                sb.append(ch);
            }
            prevCh = ch;
        }
        return sb.toString();
    }
}
