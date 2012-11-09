package annotation.validation;

import java.util.regex.Matcher;
import java.lang.annotation.Annotation;
import annotation.Pattern;

/**
 * A validator for the {@link Pattern Pattern} constraint.
 * @see Pattern
 *
 * @author Anders Holmgren
 */
public class PatternValidator extends AbstractConstraintValidator {
    /**
     * Constructs a PatternValidator object.
     */
    // suppress the unchecked warning for unchecked generic array creation
    @SuppressWarnings("unchecked")
    public PatternValidator() {
        super(Pattern.class);
    }

    public ConstraintViolation validate(Object objValue, Annotation constraint) {
        ensureConstraintIsSupported(constraint);

        Pattern p = (Pattern)constraint;
        ConstraintViolation result = null;

        String value = (objValue != null ? objValue.toString() : null);
        if (value != null && !matches(value, p.value())) {
            result = new ConstraintViolation(p,
                "Pattern constraint violated. The String " + value +
                " does not conform to the pattern " + p.value());
        }

        return result;
    }

    private boolean matches(String value, String pattern) {
        /*
         * TODO: Determine whether compiling patterns each time is expensive
         * and if so consider caching.
         */
        // Need fully qualified class name to avoid name clash
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
