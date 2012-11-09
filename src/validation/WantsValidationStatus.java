package validation;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Aug 10, 2005 Time: 10:49:09 AM
 */
public interface WantsValidationStatus {
    void validateFailed();  // Called when a component has failed validation.
    void validatePassed();  // Called when a component has passed validation.
}