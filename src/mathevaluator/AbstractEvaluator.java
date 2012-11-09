package mathevaluator;

import java.util.HashMap;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:14:21
 */
public abstract class AbstractEvaluator implements Evaluator {

    protected int type;
    protected Operator[] operators = null;
    protected HashMap<String, Object> variables = new HashMap<String, Object>();
    protected ASTNode node = null;
    protected String expression = null;

    public Operator[] getOperators() {       
        return operators;
    }

    protected void init(int type) {
        this.type = type;
        if (operators == null) initializeOperators();
    }

    protected abstract void initializeOperators();

    public abstract Object evaluateExpression(Operator o, Object f1, Object f2);

    public abstract Object getValue(String s);      

    public void addVariable(String v, Object val) {
        variables.put(v, val);
    }

    public Object getVariable(String s) {
        return variables.get(s);
    }

    public int getType() {
        return type;
    }

    public void setExpression(String s) {
        expression = s;
    }

    public void reset() {
        node = null;
        expression = null;
        variables = new HashMap<String, Object>();
    }

    public void trace() {
        try {
            ASTNode node = new ASTNode(expression, type);
            node.trace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object evaluate(ASTNode n) {
        if (n.hasOperator() && n.hasChild()) {
            if (n.getOperator().getType() == 1)
                n.setValue(evaluateExpression(n.getOperator(), evaluate(n.getLeft()), null));
            else if (n.getOperator().getType() == 2)
                n.setValue(evaluateExpression(n.getOperator(), evaluate(n.getLeft()), evaluate(n.getRight())));
        }
        return n.getValue();
    }

    public Object evaluate() {
        if (expression == null) return null;

        try {
            node = new ASTNode(expression, type);
            return evaluate(node);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
