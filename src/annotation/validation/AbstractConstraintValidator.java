package annotation.validation;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * An abstract base class for {@link ConstraintValidator ConstraintValidators}.
 * Provides some common functionality, making it a little easier to implement 
 * validators.
 *
 * @author Anders Holmgren
 */
public abstract class AbstractConstraintValidator implements ConstraintValidator {
    private Set<Class<? extends Annotation>> constraintTypeSet;

    /**
     * Constructs an AbstractConstraintValidator with the constraintTypes that
     * this validator will support.
     * @param constraintTypes the constraintTypes to support
     * @throws IllegalArgumentException if no constraintType is provided
     */
    protected AbstractConstraintValidator(Class<? extends Annotation>... constraintTypes) {
        if (constraintTypes.length == 0)
            throw new IllegalArgumentException("must specify at least one constraint type for the validator");
            
        /*
         * TODO: Figure out why the varargs in the AbstractConstraintValidator 
         * ctr is giving compiler warnings in subclasses
         */
        List<Class<? extends Annotation>> l = Arrays.asList(constraintTypes);
        
        Set<Class<? extends Annotation>> s = new HashSet<Class<? extends Annotation>>();
        s.addAll(l);
        constraintTypeSet = Collections.unmodifiableSet(s);
    }

    public Set<Class<? extends Annotation>> getSupportedConstraintTypes() {
        return constraintTypeSet;
    }

    /**
     * A helper method to ensure constraint is a legal value for this validator.
     * Intended to be called at the beginning of a subclasses 
     * {@link #validate validate} method.
     * @param constraint the constraint value to check
     * @throws IllegalArgumentException if constraint is either null or not one
     * of the constraint types supported by this validator
     */
    protected void ensureConstraintIsSupported(Annotation constraint) {
        if (constraint == null)
            throw new IllegalArgumentException("null is not a legal value for constraint");
        if (!(constraintTypeSet.contains(constraint.annotationType())))
            throw new IllegalArgumentException("the provided constraint is not supported by this validator");
    }
}
