package annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:15:44 PM
 */
/**
 * A mandatory constraint. Specifies that the property is mandatory, or in
 * other words the property value must not be null.
 * This is only useful on when applied to non primitive properties, although it
 * is harmless otherwise. The following shows an example of how to apply this
 * constraint
 * <pre>
 * <code>@Mandatory
 * public void setStringPropertyOne(String stringPropertyOne) { ... }
 * </code>
 * </pre>
 * 
 */
@Constraint
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mandatory {}
