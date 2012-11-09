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

import annotation.ConstrainedClass;

/**
 * This class provides validation services within the constraint framework.
 * There are several ways this class can be used. 
 * <p>
 * Firstly, objects can be tested for whether they would make valid values for
 * properties of a given class. This service is provided via the static method
 * {@link #validateProperty(ConstrainedClass,String,Object) validateProperty}. 
 * The following code illustrates this usage.
 * <pre>
 * <code>
 * String aPossibleValue = "123 the street";
 * ConstrainedClass addressClass = ConstrainedClass.getConstrainedClass(Address.class);
 * PropertyValidationErrors errors = 
 *     ValidatableObject.validateProperty(addressClass, "street", aPossibleValue);
 * </code>
 * </pre>
 * </p>
 * <p>
 * Additionally this class supports validating individual properties of a
 * JavaBean instance 
 * (via the {@link #validateProperty(String) validateProperty} method)
 * and entire JavaBean instances, including all the beans properties
 * (via the {@link #validate() validate} method).
 * This functionality may be accessed in two ways. One way is for the JavaBean 
 * to extend this class. In this way the JavaBean may be directly validated as
 * illustrated in the following code snippet.
 * <pre>
 * <code>
 * Address a = new Address(); // In this case Address does extend ValidatableObject
 * PropertyValidationErrors propertyErrors = 
 *     a.validateProperty("street"); // validate just the street property
 * ObjectValidationErrors objectErrors = 
 *     a.validate(); // validate whole bean
 * </code>
 * </pre>
 * The second way is to wrap the JavaBean in an instance of ValidatableObject
 * for validation purposes. In this case the JavaBeans do not themselves contain
 * validation methods, but can be validated indirectly as shown below.
 * <pre>
 * <code>
 * Address a = new Address(); // In this case Address does not extend ValidatableObject
 * ValidatableObject v = new ValidatableObject(a);
 * PropertyValidationErrors propertyErrors = 
 *     v.validateProperty("street"); // validate just the street property
 * ObjectValidationErrors objectErrors = 
 *     v.validate(); // validate whole bean
 * </code>
 * </pre>
 * </p>
 * <p>
 * This class has simple support for custom validation code. This is for
 * validation that cannot be achieved via the annotation based constraint
 * mechanism. One example usage is cross field validation. Classes wishing to
 * provide custom validation code should do so by overriding the
 * {@link #customValidation customValidation} method.
 * </p>
 *
 * @author Anders Holmgren
 */
public class ValidatableObject {
    private ConstrainedClass constrainedClass;

    // This is to support wrapping approach to validation. 
    // Otherwise the validationTarget is this.
    private Object validationTarget;

    /**
     * Constructs a ValidatableObject instance.
     * This constructor is for classes that extend ValidatableObject. 
     */
    protected ValidatableObject() {
        constrainedClass = ConstrainedClass.getConstrainedClass(getClass());
        validationTarget = this;
    }


    /**
     * Constructs a ValidatableObject as a wrapper of the given validationTarget.
     * This allows JavaBeans to be validated without them needing to subclass
     * ValidatableObject.
     * @throws IllegalArgumentException if validationTarget is null
     */
    public ValidatableObject(Object validationTarget) {
        if (validationTarget == null)
            throw new IllegalArgumentException("null is not a legal value for validationTarget");
        this.validationTarget = validationTarget;
        constrainedClass =
                ConstrainedClass.getConstrainedClass(validationTarget.getClass());
    }

    /**
     * Returns the ConstrainedClass that provides access to the property 
     * constraints for this class.
     */
    public ConstrainedClass getConstrainedClass() {
        return constrainedClass;
    }

    /**
     * Validates this object (JavaBean instance) including all its properties
     * and any custom validations.
     * Will call the {@link #customValidation customValidation} to do custom
     * validations, only when their are no property constraint violations.
     * @return the ObjectValidationErrors or null if the object is valid
     */
    public ObjectValidationErrors validate() {
        if (isBasicType())
            return null;

        List<PropertyValidationErrors> propertyErrorList =
                new ArrayList<PropertyValidationErrors>(5);

        Set<String> properties = constrainedClass.getConstrainedProperties();
        for (String propertyName : properties) {
            try {
                PropertyValidationErrors propertyErrors = validateProperty(propertyName);
                if (propertyErrors != null) {
                    assert propertyErrors.isPropertyValid();
                    propertyErrorList.add(propertyErrors);
                }
            } catch (IntrospectionException e) {
                // we should not get these sorts of errors in this case because
                // we are working on a set of properties that are known to be
                // valid. If assertion checking is active then we will catch this.
                // Otherwise we will ignore it and move on
                assert false : "Unexpected Error " + e.getMessage();
            }
        }

        /*
        * If all the properties are valid then call the custom validation
        * method
        */
        List<String> customErrors = null;
        if (propertyErrorList.isEmpty())
           customErrors = customValidation();

        /*
        * If there is some kind of error then return an ObjectValidationErrors
        * object wrapping the errors or else return null
        */
        return (propertyErrorList.isEmpty() &&
                (customErrors == null || customErrors.isEmpty()) ? null :
            new ObjectValidationErrors(validationTarget, propertyErrorList,
                customErrors));
    }

    /**
     * Validates the property with the given name. The current value 
     * of the property is used.
     * @return the PropertyValidationErrors or null if the property is valid
     * @throws IntrospectionException if there are problems accessing the property
     * @throws IllegalArgumentException if propertyName is null
     */
    public PropertyValidationErrors validateProperty(String propertyName)
            throws IntrospectionException {
        return validateProperty(constrainedClass, propertyName,
                getValue(propertyName));
    }

    /**
     * Checks whether the supplied value is a valid value for the given property
     * according to the constraints supplied via the ConstrainedClass.
     * This method allows values to be tested for validity without needing an 
     * instance of a ValidatableObject (or validationTarget). This is useful for
     * checking values before updating the corresponding property in a bean 
     * instance.
     * @return the PropertyValidationErrors or null if the property is valid
     * @throws IntrospectionException if there are problems accessing the property
     * @throws IllegalArgumentException if constrainedClass is null or 
     * propertyName is null
     */
    public static PropertyValidationErrors
        validateProperty(ConstrainedClass constrainedClass,
                         String propertyName, Object value)
            throws IntrospectionException {

        if (constrainedClass == null)
            throw new IllegalArgumentException("null is not a legal value for constrainedClass");
        if (propertyName == null)
            throw new IllegalArgumentException("null is not a legal value for propertyName");

        /*
         * First check for constraints that this class has placed on this property
         */
        Set<Annotation> constraints = constrainedClass.getConstraints(propertyName);

        List<ConstraintViolation> violations =
                new ArrayList<ConstraintViolation>(5);

        checkConstraints(value, constraints, violations);

        /*
        * Now check within the object itself. i.e. if this is a non basic type
        * property then is the actual object itself valid.
        */
        List<ObjectValidationErrors> objectValidationErrors = Collections.emptyList();
        if (value != null)
            objectValidationErrors = validatePossibleCollection(value);

        return (violations.isEmpty() && objectValidationErrors.isEmpty()) ? null :
            new PropertyValidationErrors(propertyName, value, violations,
                objectValidationErrors);
    }

    /**
     * Override this method to perform custom validations such as cross field
     * error checks and other validations that cannot be done via property
     * constraints. This method will only be called if there are no property
     * constraint violations for this object.
     * @return a list of error messages or null if no custom errors
     */
    protected List<String> customValidation() {
        return null;
    }

    /*
    * TODO: Consider adding exception throwing variants of the validation methods.
    * Q: should it be checked or runtime?
    * One use would be for enforcing validity in property setters etc e.g.
    *
    * @MaxLength(10) public void setStreet(String street) {
    *   ensurePropertyValidity("street", street);
    *   this.street = street;
    * }
    */
//    public void ensureValidity() {
//        ObjectValidationErrors errors = validate();
//        if (errors != null)
//            throw new ObjectValidationException(errors);
//    }
//    public void ensurePropertyValidity(String propertyName, Object value) {
//        PropertyValidationErrors errors = validateProperty(propertyName, value);
//        if (errors != null)
//            throw new PropertyValidationException(errors);
//    }

    /**
     * This is a convenience method. Because properties may or may not be a
     * collection, it helps to have a uniform way of dealing with them.
     * This method determines whether they are a collection or not and
     * performs the validation accordingly.
     * @param value the object instance that we are checking
     * @return the list of ObjectValidationErrors for those objects that are 
     * not valid or an empty list if all valid.
     */
    private static List<ObjectValidationErrors>
            validatePossibleCollection(Object value) {
        if (value == null)
            return Collections.emptyList();

        if (value instanceof Collection)
            return validateCollection((Collection<?>)value);

        // else do normal object validation and wrap in a collection
        List<ObjectValidationErrors> errorList = Collections.emptyList();
        ValidatableObject vo = getValidatableObject(value);
        if (vo != null) {
            ObjectValidationErrors errors = vo.validate();
            if (errors != null) {
                // the object was invalid so add the error to the list
                assert errors.isObjectValid();
                errorList = new ArrayList<ObjectValidationErrors>(1);
                errorList.add(errors);
            }
        }

        return errorList;
    }


    /**
     * Validates the given collection of object by validating all its members.
     * @param collection the collection of object instance that we are checking
     * @return the list of ObjectValidationErrors for those objects that are 
     * not valid or an empty list if all valid.
     */
    private static List<ObjectValidationErrors>
            validateCollection(Collection<?> collection) {
        if (collection == null)
            return Collections.emptyList();

        List<ObjectValidationErrors> errorList =
                new ArrayList<ObjectValidationErrors>();

        for (Object o : collection) {
            ValidatableObject vo = getValidatableObject(o);
            if (vo != null) {
                ObjectValidationErrors errors = vo.validate();
                if (errors != null) {
                    // the object was invalid so add the error to the list
                    assert errors.isObjectValid();
                    errorList.add(errors);
                }
            }
        }

        if (errorList.isEmpty())
            errorList = Collections.emptyList();

        return errorList;
    }

    private static ValidatableObject getValidatableObject(Object o) {
        assert o != null;

        ValidatableObject vo = null;
        if (o instanceof ValidatableObject)
            vo = (ValidatableObject)o;
        else {
            /*
             * We don't want to create any ValidatableObjects and corresponding
             * ConstrainedClasses for classes we know won't have constraints.
             * For a start this is anything in java.* or javax.*. The main
             * goal is at least all the basic types.
             * TODO: Are there other important classes to exclude for validation
             * other than java.* & javax.*?
             * TODO: Is there a neater way to avoid validating basic types like 
             * Strings etc
             */
            if (o.getClass().getPackage().toString().startsWith("java"))
                vo = null;
            vo = new ValidatableObject(o);
        }

        return vo;
    }

    private Object getValue(String propertyName)
            throws IntrospectionException {
        assert propertyName != null;

        /**
         * Get the property value via the getter method
         */
        try {
            PropertyDescriptor[] descs =
                Introspector.getBeanInfo(validationTarget.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor desc : descs) {
                if (desc.getName().equals(propertyName)) {
                    Method read = desc.getReadMethod();
                    if (read == null)
                        throw new IntrospectionException(propertyName);

                    return read.invoke(validationTarget, (Object[])null);
                }
            }
        } catch (InvocationTargetException e) {
            throw new IntrospectionException(propertyName);
        } catch (IllegalAccessException e) {
            throw new IntrospectionException(propertyName);
        }

        throw new IntrospectionException(propertyName);
    }

    private static void checkConstraints(Object value,
                                         Set<Annotation> annotations, List<ConstraintViolation> violations)
            throws IntrospectionException {

        assert annotations != null;
        assert violations != null;

        for (Annotation a : annotations) {
            ConstraintValidator v =
                ValidatorFactory.getInstance().getValidatorFor(a.annotationType());

            if (v != null) {
                assert v.getSupportedConstraintTypes().contains(a.annotationType());

                ConstraintViolation violation = v.validate(value, a);
                if (violation != null)
                    violations.add(violation);
            }
        }
    }

    private boolean isBasicType() {
        Object o = validationTarget;
        return (o instanceof String) || (o instanceof Number) ||
                (o instanceof Boolean) || (o instanceof Character) ||
                (o instanceof Void) || (o instanceof Class);
    }

}

