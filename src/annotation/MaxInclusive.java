package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:13:16 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * A maximum value constraint for numerics. Constrains numerical properties
 * to being no larger in value than that specified for this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MaxInclusive(23.45692)
 * public void setADouble(double aDouble) { ... }
 * </code>
 * </pre>
 * <p>
 * Note that the maximum value is stored as a double regardless of what type
 * of numeric this constraint is applied to. Values of type long, that exceed
 * the precision of double will lose information during the conversion to double.
 * </p>
 *
 */
@Constraint(Constraint.Type.NUMERIC)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MaxInclusive {
    /**
     * the maximum allowable value for the numerical value of the
     * property that the constraint is associated with.
     */
    double value();
}
