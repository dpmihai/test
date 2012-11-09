package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:09:18 PM 
 */
/**
 * A regular expression constraint for Strings. The value of the
 * property needs to match the regular expression specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@Pattern("(\\d{4}\\-){3}\\d{4}")
 * public void setCreditCard(String creditCard) { ... }
 * </code>
 * </pre>
 * The regular expression support is the same as for the java.util.regex package 
 */
@Constraint(Constraint.Type.STRING)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pattern {
    /**
     * the pattern that the property value must conform to
     */
    String value();
}
