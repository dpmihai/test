package annotation.validation;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents validation errors for a JavaBean instance. Includes the validation 
 * errors for all of the JavaBean instance's properties that are invalid as well
 * as any errors resulting from custom validation code.
 * @see PropertyValidationErrors
 *
 * @author Anders Holmgren
 */
public class ObjectValidationErrors {
    private Object theObject;

    // TODO: Consider changing to sets, as order is not important and 
    // probably duplicates are not meaningful?
    private List<PropertyValidationErrors> propertyErrors;
    
    private List<String> customObjectErrors;
    

    /**
     * Constructs an ObjectValidationErrors for the object (JavaBean instance)
     * that was validated and the list of validation results for properties that
     * had validation errors and a list of error messages resulting from
     * custom validation code.
     * @param theObject the JavaBean instance that was validated
     * @param propertyErrors the validation results for invalid properties
     * @param customObjectErrors custom error messages for the object
     * @throws IllegalArgumentException if theObject is null
     */
    public ObjectValidationErrors(Object theObject,
            List<PropertyValidationErrors> propertyErrors, 
            List<String> customObjectErrors) {
        if (theObject == null)
            throw new IllegalArgumentException("null is not a legal value for theObject");
        
        this.theObject = theObject;
        
        if (propertyErrors == null)
            this.propertyErrors = Collections.emptyList();
        else
            this.propertyErrors = Collections.unmodifiableList(propertyErrors);

        if (customObjectErrors == null)
            this.customObjectErrors = Collections.emptyList();
        else
            this.customObjectErrors = 
                    Collections.unmodifiableList(customObjectErrors);
    }


    /**
     * Constructs an ObjectValidationErrors for the object (JavaBean instance)
     * that was validated and the list of validation results for properties that
     * had validation errors.
     * @param theObject the JavaBean instance that was validated
     * @param propertyErrors the validation results for invalid properties
     * @throws IllegalArgumentException if theObject is null
     */
    public ObjectValidationErrors(Object theObject,
            List<PropertyValidationErrors> propertyErrors) {
        this(theObject, propertyErrors, null);
    }
    
    
    /**
     * Returns true if the object is valid (i.e. there were no validation errors
     * for the object)
     */
    public boolean isObjectValid() {
        return propertyErrors.isEmpty() && customObjectErrors.isEmpty();
    }

    /**
     * Returns the object that was validated
     */
    public Object getTheObject() {
        return theObject;
    }

    /**
     * Returns an immutable list of PropertyValidationErrors for properties
     * that were not valid or an empty list otherwise
     */
    public List<PropertyValidationErrors> getPropertyErrors() {
        return propertyErrors;
    }
    
    /**
     * Returns an immutable list of custom object errors for that were generated
     * when the associated object was validated. These are for non property
     * constraint type errors such as custom cross field or other validation 
     * checks
     */
    public List<String> getCustomObjectErrors() {
        return customObjectErrors;
    }
    
    /**
     * Overrides toString from java.lang.Object to provide a summary of the
     * validation result. This is intended largely for logging.
     * @return a textual summary of the result
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Object " + theObject.toString() + " is " + 
            (isObjectValid() ? "" : "NOT ") + "valid.");
        if (!isObjectValid()) {
            if (!propertyErrors.isEmpty()) {
                buf.append("\nThe following property constraint violations were detected");
                for (PropertyValidationErrors error : propertyErrors)
                    buf.append("\n").append(error.toString());
            }
            if (!customObjectErrors.isEmpty()) {
                buf.append("\nThe following custom object errors were detected");
                for (String error : customObjectErrors)
                    buf.append("\n").append(error);
            }
        }
        return buf.toString();
    }    
}
