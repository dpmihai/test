package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:12:29 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * A constraint on the maximum number of items in a collection type
 * property. To be valid the collection must have no more items than the value
 * specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MaxOccurs(8)
 * public void setStringList({@code List<String>} stringList) { ... }
 * </code>
 * </pre>
 *
 */
@Constraint(Constraint.Type.COLLECTION)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MaxOccurs {
    /**
     * the maximum allowable number of items in the Collection
     */
    int value();
}
