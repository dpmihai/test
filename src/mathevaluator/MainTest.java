package mathevaluator;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 12:01:24
 */
public class MainTest {

    public static void main(String[] args) {
//        Evaluator evaluator = EvaluatorFactory.getInstance(EvaluatorType.NUMERIC_TYPE);
//        evaluator.setExpression("-5-6/(-2) + sqr(15+x)");
//        evaluator.addVariable("x",1.0);
//        Object result = evaluator.evaluate();
//        System.out.println(result);

        Evaluator evaluator = EvaluatorFactory.getInstance(EvaluatorType.STRING_TYPE);
        evaluator.setExpression("capitalize ( $val ) + _test ");
        evaluator.trace();
        evaluator.addVariable("$val", "MuiCA");
        Object result = evaluator.evaluate();
        System.out.println(result);
    }
}
