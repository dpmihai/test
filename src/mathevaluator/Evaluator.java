package mathevaluator;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:40:59
 */
public interface Evaluator {

    public int getType();

    public void setExpression(String s);

    public void addVariable(String v, Object val);

    public Object getVariable(String s);

    public Object getValue(String s);

    public Operator[] getOperators();

    public Object evaluateExpression(Operator o, Object f1, Object f2);

    public Object evaluate();

    public void trace();
}
