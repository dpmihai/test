package annotation.validation;

import java.util.*;
import java.lang.annotation.Annotation;
import annotation.MaxOccurs;

/**
 * A validator for the {@link MaxOccurs MaxOccurs} constraint.
 * @see MaxOccurs
 *
 * @author Anders Holmgren
 */
public class MaxOccursValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MaxOccursValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MaxOccursValidator() {
        super(MaxOccurs.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        /*
        * Figure out the number of items in the collection or map.
        * Null collections are treated the same as empty collections. i.e.
        * they both have 0 items.
        */
        int numItems = 0;
        if (objValue != null) {
            if (!(objValue instanceof Collection) && !(objValue instanceof Map))
                throw new IllegalArgumentException("MaxOccurs constraint can only be applied to subclasses of Collection or Map.");

            Collection col = (objValue instanceof Collection) ?
                (Collection)objValue : ((Map)objValue).keySet();
            numItems = col.size();
        }

        MaxOccurs mo = (MaxOccurs)constraint;
        ConstraintViolation result = null;

        if (numItems > mo.value()) {
            result = new ConstraintViolation(mo,
                "MaxOccurs constraint violated. Number of items " + numItems +
                " is more than max " + mo.value());
        }

        return result;
    }
}
