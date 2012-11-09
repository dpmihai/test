package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 5:52:58 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * General annotation for todos
 */

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
public @interface GTodo {

    public enum Severity {
        CRITICAL,
        IMPORTANT,
        TRIVIAL,
        DOCUMENTATION
    }

    Severity severity() default Severity.IMPORTANT;

    String item();

    String assignedTo();

    String dateAssigned();
}
