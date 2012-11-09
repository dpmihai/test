package mathevaluator;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:34:03
 */
public class NumericEvaluator extends AbstractEvaluator {

    public NumericEvaluator() {
        init(EvaluatorType.NUMERIC_TYPE);
    }

    public NumericEvaluator(String s) {
        init(EvaluatorType.NUMERIC_TYPE);
        setExpression(s);
    }


    protected void initializeOperators() {
        operators = new Operator[25];
        operators[0] = new Operator("+", 2, 0);
        operators[1] = new Operator("-", 2, 0);
        operators[2] = new Operator("*", 2, 10);
        operators[3] = new Operator("/", 2, 10);
        operators[4] = new Operator("^", 2, 10);
        operators[5] = new Operator("%", 2, 10);
        operators[6] = new Operator("&", 2, 0);
        operators[7] = new Operator("|", 2, 0);
        operators[8] = new Operator("cos", 1, 20);
        operators[9] = new Operator("sin", 1, 20);
        operators[10] = new Operator("tan", 1, 20);
        operators[11] = new Operator("acos", 1, 20);
        operators[12] = new Operator("asin", 1, 20);
        operators[13] = new Operator("atan", 1, 20);
        operators[14] = new Operator("sqrt", 1, 20);
        operators[15] = new Operator("sqr", 1, 20);
        operators[16] = new Operator("log", 1, 20);
        operators[17] = new Operator("min", 2, 0);
        operators[18] = new Operator("max", 2, 0);
        operators[19] = new Operator("exp", 1, 20);
        operators[20] = new Operator("floor", 1, 20);
        operators[21] = new Operator("ceil", 1, 20);
        operators[22] = new Operator("abs", 1, 20);
        operators[23] = new Operator("neg", 1, 20);
        operators[24] = new Operator("rnd", 1, 20);
    }

    public Object evaluateExpression(Operator o, Object obj1, Object obj2) {
        Double f1 = (Double) obj1;
        Double f2 = (Double) obj2;
        String op = o.getOperator();
        Double res = null;

        if ("+".equals(op)) res = new Double(f1.doubleValue() + f2.doubleValue());
        else if ("-".equals(op)) res = new Double(f1.doubleValue() - f2.doubleValue());
        else if ("*".equals(op)) res = new Double(f1.doubleValue() * f2.doubleValue());
        else if ("/".equals(op)) res = new Double(f1.doubleValue() / f2.doubleValue());
        else if ("^".equals(op)) res = new Double(Math.pow(f1.doubleValue(), f2.doubleValue()));
        else if ("%".equals(op)) res = new Double(f1.doubleValue() % f2.doubleValue());
        else if ("&".equals(op)) res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
        else if ("|".equals(op)) res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
        else if ("cos".equals(op)) res = new Double(Math.cos(f1.doubleValue()));
        else if ("sin".equals(op)) res = new Double(Math.sin(f1.doubleValue()));
        else if ("tan".equals(op)) res = new Double(Math.tan(f1.doubleValue()));
        else if ("acos".equals(op)) res = new Double(Math.acos(f1.doubleValue()));
        else if ("asin".equals(op)) res = new Double(Math.asin(f1.doubleValue()));
        else if ("atan".equals(op)) res = new Double(Math.atan(f1.doubleValue()));
        else if ("sqr".equals(op)) res = new Double(f1.doubleValue() * f1.doubleValue());
        else if ("sqrt".equals(op)) res = new Double(Math.sqrt(f1.doubleValue()));
        else if ("log".equals(op)) res = new Double(Math.log(f1.doubleValue()));
        else if ("min".equals(op)) res = new Double(Math.min(f1.doubleValue(), f2.doubleValue()));
        else if ("max".equals(op)) res = new Double(Math.max(f1.doubleValue(), f2.doubleValue()));
        else if ("exp".equals(op)) res = new Double(Math.exp(f1.doubleValue()));
        else if ("floor".equals(op)) res = new Double(Math.floor(f1.doubleValue()));
        else if ("ceil".equals(op)) res = new Double(Math.ceil(f1.doubleValue()));
        else if ("abs".equals(op)) res = new Double(Math.abs(f1.doubleValue()));
        else if ("neg".equals(op)) res = new Double(-f1.doubleValue());
        else if ("rnd".equals(op)) res = new Double(Math.random() * f1.doubleValue());

        return res;
    }

    public Object getValue(String s) {
        if (s == null) return null;

        Double res = null;
        try {
            res = new Double(Double.parseDouble(s));
        } catch (Exception e) {
            return getVariable(s);
        }

        return res;
    }
}
