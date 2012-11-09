package annotation.validation;

import java.util.*;
import java.lang.annotation.Annotation;
import annotation.MinOccurs;

/**
 * A validator for the {@link MinOccurs MinOccurs} constraint.
 * @see MinOccurs
 *
 * @author Anders Holmgren
 */
public class MinOccursValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MinOccursValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MinOccursValidator() {
        super(MinOccurs.class);
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
                throw new IllegalArgumentException("MinOccurs constraint can only be applied to subclasses of Collection or Map.");

            Collection col = (objValue instanceof Collection) ?
                (Collection)objValue : ((Map)objValue).keySet();
            numItems = col.size();
        }

        MinOccurs mo = (MinOccurs)constraint;
        ConstraintViolation result = null;

        if (numItems < mo.value()) {
            result = new ConstraintViolation(mo,
                "MinOccurs constraint violated. Number of items " + numItems +
                " is less than min " + mo.value());
        }

        return result;
    }
}
