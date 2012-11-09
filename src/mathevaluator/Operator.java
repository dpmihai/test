package mathevaluator;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:19:24
 */
public class Operator {

    private String op;
    private int type;
    private int priority;

    public Operator(String o, int t, int p) {
        op = o;
        type = t;  // 1-> 1 operator ; 2 -> 2 operators
        priority = p;
    }

    public String getOperator() {
        return op;
    }

    public void setOperator(String o) {
        op = o;
    }

    public int getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }
}
