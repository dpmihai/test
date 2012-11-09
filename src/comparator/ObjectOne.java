package comparator;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 29, 2006
 * Time: 11:43:42 AM
 */
public class ObjectOne {

    private int id;
    private String name;
    private String test;

    public ObjectOne(int id, String name, String test) {
        this.id = id;
        this.name = name;
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTest() {
        return test;
    }


    public String toString() {
        return "ObjectOne{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", test='" + test + '\'' +
                '}';
    }
}
