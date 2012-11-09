package annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 1:36:05 PM
 */
/**
 * A marker for constraint annotations. All annotations that are to be used
 * as constraints must have this annotation associated with them.
 * <p>
 * The following code shows an example of how to apply this annotation to a
 * constraint definition
 * </p>
 * <p>
 * <code>@Constraint public @interface MyConstraint {}</code>
 * </p>
 * <p>
 * By default a constraint can be applied to a property of any type. Optionally,
 * the type may be restricted. For example to restrict a constraint to numerics
 * only use <code>@Constraint(Constraint.Type.NUMERIC)</code>.
 * </p>
 * <p>
 * Note it is currently left to the constraint validators to
 * determine if constraints have been applied to a property of a valid type for
 * that constraint. The validators are not required to make use of the type
 * value on the Constraint annotation, but should be consistent with its intent.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Documented()
public @interface Constraint {
    /**
     * An enumeration of the different types that a constraint may be applied to.
     */
    enum Type {
        /**
         * The constraint may be applied to any type of object
         */
        ANY,

        /**
         * The constraint may be applied to any object for which
         * Object.toString() yields a meaningful result. In practice, it is up
         * to the application to determine what is meaningful.
         */
        STRING,

        /**
         * The constraint may be applied to numeric objects only. This means
         * any subclass of Number or corresponding primitive type.
         */
        NUMERIC,

        /**
         * The constraint may be applied to collections only. Note the
         * constraints apply to some attribute of the collection itself (e.g. size)
         * and not to the objects in the collection.
         */
        COLLECTION
    }

    /**
     * The type of object the constraint can be applied to. The default is ANY
     * type.
     */
    Type value() default Type.ANY;
}
