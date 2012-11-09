package mathevaluator;

/**
 * Abstract Syntax Tree Node
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:18:55
 */
public class  ASTNode {

    public String nString = null;
    private int type;
    public Operator nOperator = null;
    public ASTNode nLeft = null;
    public ASTNode nRight = null;
    public ASTNode nParent = null;
    public int nLevel = 0;
    public Object nValue = null;

    public ASTNode(String s, int type) throws Exception {
        init(null, s, 0, type);
    }

    public ASTNode(ASTNode parent, String s, int level, int type) throws Exception {
        init(parent, s, level, type);
    }

    private void init(ASTNode parent, String s, int level, int type) throws Exception {
        s = removeIllegalCharacters(s);
        s = removeBrackets(s);
        s = addZero(s);
        if (checkBrackets(s) != 0) throw new Exception("Wrong number of brackets in [" + s + "]");

        nParent = parent;
        nString = s;
        this.type = type;
        nValue = getValue(s);
        nLevel = level;
        int sLength = s.length();
        int inBrackets = 0;
        int startOperator = 0;

        for (int i = 0; i < sLength; i++) {
            if (s.charAt(i) == '(')
                inBrackets++;
            else if (s.charAt(i) == ')')
                inBrackets--;
            else {
                // the expression must be at "root" level
                if (inBrackets == 0) {
                    Operator o = getOperator(nString, i);
                    if (o != null) {
                        // if first operator or lower priority operator
                        if (nOperator == null || nOperator.getPriority() >= o.getPriority()) {
                            nOperator = o;
                            startOperator = i;
                        }
                    }
                }
            }
        }

        if (nOperator != null) {
            // one operand, should always be at the beginning
            if (startOperator == 0 && nOperator.getType() == 1) {
                // the brackets must be ok
                if (checkBrackets(s.substring(nOperator.getOperator().length())) == 0) {
                    nLeft = new ASTNode(this, s.substring(nOperator.getOperator().length()), nLevel + 1, type);
                    nRight = null;
                    return;
                } else
                    throw new Exception("Error during parsing... missing brackets in [" + s + "]");
            }
            // two operands
            else if (startOperator > 0 && nOperator.getType() == 2) {
                nOperator = nOperator;
                nLeft = new ASTNode(this, s.substring(0, startOperator), nLevel + 1, type);
                nRight = new ASTNode(this, s.substring(startOperator + nOperator.getOperator().length()), nLevel + 1, type);
            }
        }
    }

    private Operator getOperator(String s, int start) {
        Operator[] operators = getOperators();
        String temp = s.substring(start);
        temp = getNextWord(temp);
        for (int i = 0; i < operators.length; i++) {
            if (temp.startsWith(operators[i].getOperator()))
                return operators[i];
        }
        return null;
    }

    private String getNextWord(String s) {
        int sLength = s.length();
        for (int i = 1; i < sLength; i++) {
            char c = s.charAt(i);
            if ((c > 'z' || c < 'a') && (c > '9' || c < '0'))
                return s.substring(0, i);
        }
        return s;
    }

    /**
     * checks if there is any missing brackets
     *
     * @return true if s is valid
     */
    protected int checkBrackets(String s) {
        int sLength = s.length();
        int inBracket = 0;

        for (int i = 0; i < sLength; i++) {
            if (s.charAt(i) == '(' && inBracket >= 0)
                inBracket++;
            else if (s.charAt(i) == ')')
                inBracket--;
        }

        return inBracket;
    }

    /**
     * returns a string that doesnt start with a + or a -
     */
    protected String addZero(String s) {
        if (s.startsWith("+") || s.startsWith("-")) {
            int sLength = s.length();
            for (int i = 0; i < sLength; i++) {
                if (getOperator(s, i) != null)
                    return "0" + s;
            }
        }

        return s;
    }

    /**
     * displays the tree of the expression
     */
    public void trace() {
        String op = getOperator() == null ? " " : getOperator().getOperator();
        _D(op + " : " + getString());
        if (this.hasChild()) {
            if (hasLeft())
                getLeft().trace();
            if (hasRight())
                getRight().trace();
        }
    }

    protected boolean hasChild() {
        return (nLeft != null || nRight != null);
    }

    protected boolean hasOperator() {
        return (nOperator != null);
    }

    protected boolean hasLeft() {
        return (nLeft != null);
    }

    protected ASTNode getLeft() {
        return nLeft;
    }

    protected boolean hasRight() {
        return (nRight != null);
    }

    protected ASTNode getRight() {
        return nRight;
    }

    protected Operator getOperator() {
        return nOperator;
    }

    protected int getLevel() {
        return nLevel;
    }

    protected Object getValue() {
        return nValue;
    }

    protected void setValue(Object f) {
        nValue = f;
    }

    protected String getString() {
        return nString;
    }

    /**
     * Removes spaces, tabs and brackets at the begining
     */
    public String removeBrackets(String s) {
        String res = s;
        if (s.length() > 2 && res.startsWith("(") && res.endsWith(")") && checkBrackets(s.substring(1, s.length() - 1)) == 0) {
            res = res.substring(1, res.length() - 1);
        }
        if (res != s)
            return removeBrackets(res);
        else
            return res;
    }

    /**
     * Removes illegal characters
     */
    public String removeIllegalCharacters(String s) {
        char[] illegalCharacters = {' '};
        String res = s;

        for (int j = 0; j < illegalCharacters.length; j++) {
            int i = res.lastIndexOf(illegalCharacters[j], res.length());
            while (i != -1) {
                String temp = res;
                res = temp.substring(0, i);
                res += temp.substring(i + 1);
                i = res.lastIndexOf(illegalCharacters[j], s.length());
            }
        }
        return res;
    }

    protected void _D(String s) {
        String nbSpaces = "";
        for (int i = 0; i < nLevel; i++) nbSpaces += "  ";
        System.out.println(nbSpaces + "|" + s);
    }

    protected Object getValue(String s) {
        return EvaluatorFactory.getInstance(type).getValue(s);
    }

    protected Operator[] getOperators() {        
        return EvaluatorFactory.getInstance(type).getOperators();
    }


}


