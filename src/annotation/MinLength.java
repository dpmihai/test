package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 1:37:38 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * A minimum length constraint for Strings. Specifies that the String must
 * contain no less than the number of characters specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MinLength(4)
 * public void setStringPropertyThree(String stringPropertyThree) { ... }
 * </code>
 * </pre>  
 */
@Constraint(Constraint.Type.STRING)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MinLength {
    /**
     * the minimum allowable number of characters in the String
     */
    int value();
}
