package annotation.validation;
import java.util.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;


/**
 * A factory (registry) for validators of Annotation based Constraints. This
 * is a singleton to provide a single access point to the registered validators.
 * Note this class is thread safe.
 *
 * @author Anders Holmgren
 */
public class ValidatorFactory {
    private static ValidatorFactory instance = new ValidatorFactory();
    
    private Map<Class<? extends Annotation>, ConstraintValidator> validators
            = new HashMap<Class<? extends Annotation>, ConstraintValidator>();
    

    private ValidatorFactory() {
        /*
         * TODO: These should ideally be read from some configuration file.
         */
        register(new MaxLengthValidator());
        register(new MinLengthValidator());
        register(new MaxInclusiveValidator());
        register(new MinInclusiveValidator());
        register(new PatternValidator());
        register(new MandatoryValidator());
        register(new MinOccursValidator());
        register(new MaxOccursValidator());
        
        /*
         * Synchronise the map from now on.
         * TODO: Need to determine if this is an unnacceptable performance hit.
         * In reality, this is predominantly a read only map, but we want to
         * allow for easy customisation of constraints & validators.
         */
        validators = Collections.synchronizedMap(validators);
    }
    
    /**
     * Returns the singleton instance
     */
    public static ValidatorFactory getInstance() {
        return instance;
    }

    /**
     * Returns the validator for the given constraintType or null if there
     * is no registered validator.
     * @throws IllegalArgumentException if constraintType is null
     */
    public ConstraintValidator 
            getValidatorFor(Class<? extends Annotation> constraintType) {
        if (constraintType == null)
            throw new IllegalArgumentException("null is not a legal value for constraintType");

        return validators.get(constraintType);
    }

        
    /**
     * Registers the given validator for the given constraint type.
     * <p>
     * This provides a mechanism to extend the default set of constraints
     * and corresponding validators as well as a way to replace default 
     * validators.
     * </p>
     * @throws IllegalArgumentException if validator is null, or if the 
     * constraintType is null, or if the constraintType is not one of the types
     * returned by validator.getSupportedConstraintTypes()
     */
    public void register(Class<? extends Annotation> constraintType, 
            ConstraintValidator validator) {
        if (constraintType == null)
            throw new IllegalArgumentException("null is not a legal value " +
                    "for constraintType");
        if (validator == null)
            throw new IllegalArgumentException("null is not a legal value " +
                    "for validator");
        if (!validator.getSupportedConstraintTypes().contains(constraintType))
            throw new IllegalArgumentException("Attempt to register a " +
                    "validator for a constraint type it does not support");
        validators.put(constraintType, validator);
    }

    /**
     * Registers the given validator for all its supported constraint types.
     * i.e. all the types returned by validator.getSupportedConstraintTypes()
     * <p>
     * This provides a mechanism to extend the default set of constraints
     * and corresponding validators as well as a way to replace default 
     * validators.
     * </p>
     * @throws IllegalArgumentException if validator is null
     */
    public void register(ConstraintValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("null is not a legal value " +
                    "for validator");
        for (Class<? extends Annotation> type : 
                validator.getSupportedConstraintTypes()) {
            // for each supported type register this validator
            register(type, validator);
        }
    }
}
