package mathevaluator;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 12:53:39
 */
public class StringEvaluator extends AbstractEvaluator {

    public StringEvaluator() {
        init(EvaluatorType.STRING_TYPE);
    }

    public StringEvaluator(String s) {
        init(EvaluatorType.STRING_TYPE);
        setExpression(s);
    }


    protected void initializeOperators() {
        operators = new Operator[4];
        operators[0] = new Operator("+", 2, 0);
        operators[1] = new Operator("lowercase", 1, 10);
        operators[2] = new Operator("uppercase", 1, 10);
        operators[3] = new Operator("capitalize", 1, 10);
    }

    public Object evaluateExpression(Operator o, Object obj1, Object obj2) {
        String f1 = (String) obj1;
        String f2 = (String) obj2;
        String op = o.getOperator();
        String res = "";

        if ("+".equals(op)) res = f1 + f2;
        else if ("lowercase".equals(op)) res = f1.toLowerCase();
        else if ("uppercase".equals(op)) res = f1.toUpperCase();
        else if ("capitalize".equals(op)) res = capitalize(f1);
        return res;

    }

    public Object getValue(String s) {
        if (s == null) return null;
        if (s.startsWith("$")) {            
            return getVariable(s);
        } else
            return s;
    }

    private static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
