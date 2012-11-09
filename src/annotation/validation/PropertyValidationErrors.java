package annotation.validation;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents validation errors for a single property of an instance of a JavaBean. 
 * Contains a list of the constraint violations for that property.
 * Additionally, for properties that are not basic types (i.e. primitives and their 
 * object counterparts), there may also be validation errors for the
 * object that was bound to the property during validation. For collection
 * type properties there may be a collection of these ObjectValidationErrors.
 * @see ConstraintViolation
 * @see ObjectValidationErrors
 *
 * @author Anders Holmgren
 */
public class PropertyValidationErrors {
    private String propertyName;
    private Object propertyValue;
    
    // TODO: Consider changing to sets, as order is not important and 
    // probably duplicates are not meaningful?
    private List<ConstraintViolation> constraintViolations;    
    private List<ObjectValidationErrors> objectValidationErrors;
    

    /**
     * Constructs a PropertyValidationErrors object for the given property name
     * which contains the value that was validated and any constraint violations
     * and object validation errors for that property.
     * @param propertyName the name of the property that was validated
     * @param propertyValue the value of the property used in the validation
     * @param constraintViolations any violations of constraints placed on that 
     * property within this class or null if no violations
     * @param objectValidationErrors any validation errors associated with object 
     * (or collection of objects) that was bound to the property, or null if none. 
     * These errors come from validating the objects themselves which may include 
     * violations of constraints 
     * placed on properties within these objects.
     * @throws IllegalArgumentException if propertyName is null
     */
    public PropertyValidationErrors(String propertyName, Object propertyValue,
            List<ConstraintViolation> constraintViolations, 
            List<ObjectValidationErrors> objectValidationErrors) {
        if (propertyName == null)
            throw new IllegalArgumentException("null is not a legal value for propertyName");

        this.propertyName = propertyName;
        this.propertyValue = propertyValue;

        if (constraintViolations == null)
            this.constraintViolations = Collections.emptyList();
        else
            this.constraintViolations = Collections.unmodifiableList(constraintViolations);

        if (objectValidationErrors == null)
            this.objectValidationErrors = Collections.emptyList();
        else
            this.objectValidationErrors = Collections.unmodifiableList(objectValidationErrors);
    }


    /**
     * Returns true if the property that was validated had no validation errors
     * @return a boolean value indicating whether the property was valid or not
     */
    public boolean isPropertyValid() {
        return constraintViolations.isEmpty() && objectValidationErrors.isEmpty();
    }

    /**
     * The name of the property that was validated
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * The value of the property that was used for the validation.
     * @return the property value
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * An unmodifiable list of constraint violations detected during validation of the
     * property
     * @return the constraint violations
     */
    public List<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    /**
     * An unmodifiable list of object validation errors for objects bound to the property.
     * Note for simple properties there is at most one object bound to the property, but
     * for properties that are collections, there maybe several objects and therefore
     * potentially more than one object validation results. There will be one 
     * ObjectValidationErrors for each object instance that had any validation errors.
     * Any objects that did not have validation errors will not have a result returned
     * within the list
     * @return the list of ObjectValidationErrors or an empty list if no object validation 
     * errors occured
     */
    public List<ObjectValidationErrors> getObjectValidationErrors() {
        return objectValidationErrors;
    }

    /**
     * Overrides toString from java.lang.Object to provide a summary of the
     * validation result. This is intended largely for logging.
     * @return a textual summary of the result
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Property " + propertyName + " is " + 
                (isPropertyValid() ? "" : "NOT ") + "valid.");
        if (!isPropertyValid()) {
            if (!constraintViolations.isEmpty()) {
                buf.append("\nThe following constraint violations were detected");
                for (ConstraintViolation error : constraintViolations)
                    buf.append("\n").append(error.getMessage());
            }
            if (!objectValidationErrors.isEmpty()) {
                buf.append("\nThe object associated with the property was not valid.");
                for (ObjectValidationErrors error : objectValidationErrors)
                    buf.append("\n").append(error.toString());
                buf.append("\n");
            }
        }
        return buf.toString();
    }
}
