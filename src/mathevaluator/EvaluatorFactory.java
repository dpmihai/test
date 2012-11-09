package mathevaluator;

import java.util.HashMap;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 11:40:19
 */
public class EvaluatorFactory {

    private static HashMap<Integer, Evaluator> map = new HashMap<Integer, Evaluator>();

    public static Evaluator getInstance(int type) {
        Evaluator evaluator = map.get(type);
        if (evaluator == null) {
            if (type == EvaluatorType.NUMERIC_TYPE) {
                evaluator = new NumericEvaluator();
            } else {
                evaluator = new StringEvaluator();
            }
            map.put(type, evaluator);
        }
        return evaluator;
    }
}
