package annotation_persistence;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 6, 2007
 * Time: 11:13:04 AM
 */

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Persistent {
    String value() default "";    // Won't accept null;
}