package annotation.validation;

import java.lang.annotation.Annotation;
import annotation.MinInclusive;

/**
 * A validator for the {@link MinInclusive MinInclusive} constraint.
 * Note that all comparisons are done as doubles. Therefore there will be
 * information loss for values of type long that exceed the precision of double,
 * which may affect the comparison.
 * @see MinInclusive
 *
 * @author Anders Holmgren
 */
public class MinInclusiveValidator extends AbstractConstraintValidator {
    /**
     * Constructs a MinInclusiveValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public MinInclusiveValidator() {
        super(MinInclusive.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        MinInclusive mi = (MinInclusive)constraint;
        ConstraintViolation result = null;

         /*
         * This constraint only applies to non null values. i.e. the property could be optional
         */
        if (objValue != null) {
            if (!(objValue instanceof Number))
                throw new IllegalArgumentException("MinInclusiveValidator was passed a value that was not a subclass of Number");

            /*
            * Convert the number to a double and then compare with the
            * constraints value.
            * Note the conversion to double may result in the loss of information
            * for values of type long.
            */
            Number value = (Number)objValue;
            if (!(value.doubleValue() >= mi.value())) {
                result = new ConstraintViolation(mi,
                    "MinInclusive constraint violated. Value " + value +
                    " is less than min " + mi.value());
            }
        }

        return result;
    }
}
