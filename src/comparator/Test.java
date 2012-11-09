package comparator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 29, 2006
 * Time: 11:52:28 AM
 */
public class Test {

    public static void main(String[] args) {
        ObjectOne o1 = new ObjectOne(1, "acum", "testare");
        ObjectOne o2 = new ObjectOne(2, "deci", "munca");

        List<ObjectOne> objs = new ArrayList<ObjectOne>();
        objs.add(o1);
        objs.add(o2);
        Collections.sort(objs, new PropertyComparator("test"));
        System.out.println("objs="+objs);
    }
}
