package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 1:28:57 PM
 */

/**
 * A maximum length constraint for Strings. Specifies that the String must
 * contain no more than the number of characters specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MaxLength(900)
 * public void setStringPropertyThree(String stringPropertyThree) { ... }
 * </code>
 * </pre>  
 */
@Constraint(Constraint.Type.STRING)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MaxLength {
    /**
     * the maximum allowable number of characters in the String
     */
    int value();
}
