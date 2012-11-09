package annotation.validation;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * A validator for Annotation based Constraints. Validators may validate
 * one or more constraint types.
 * @see ValidatorFactory
 *
 * @author Anders Holmgren
 */
public interface ConstraintValidator {
    /**
     * Validates the given value against the supplied constraint.
     * @param value the value that will be checked
     * @param constraint the constraint that the value will be checked against
     * @return a ConstraintViolation object if there was a violation of the
     * constraint, or null otherwise.
     * @throws IllegalArgumentException if constraint is either null or not one
     * of the constraint types supported by this validator
     */
    ConstraintViolation validate(Object value, Annotation constraint);

    /**
     * Returns the set of constraint types that this validator is capable
     * of validating. Note that the validator may not be registered for all the
     * constraint types that it supports.
     * @return the set of supported constraint types
     */
    Set<Class<? extends Annotation>> getSupportedConstraintTypes();
}
