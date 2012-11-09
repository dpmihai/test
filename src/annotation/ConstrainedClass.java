package annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.beans.*;


/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 2:14:25 PM
 */

/**
 * A class to provide access to Annotation based property constraints.
 * <p>
 * ConstrainedClass supports constraints that are added to the read & write
 * methods (getters & setters) of JavaBean properties. It does not support
 * constraints added to either the field of the property or the method parameter
 * of the setter.
 * </p>
 * <p>
 * The following shows an example of how to apply a
 * {@link annotation.MaxLength MaxLength} constraint to a
 * property setter and {@link annotation.MinLength MinLength}
 * to the corresponding getter. Note it is better practice to apply all the
 * constraints to either the setter or the getter, rather than both, as shown here
 * purely for illustration.
 * <pre><code> @MaxLength(25)
 * public void setStreet(String street) { ... }
 *
 * {@literal @MinLength(10)}
 * public String getStreet() { ... }
 * </code></pre>
 * For details about how to apply a particular constraint, see the documentation
 * for that constraint.
 * </p>
 * <p>
 * The following code snippet walks through some of the key methods in
 * ConstrainedClass to give and idea of what they do.<br/>
 * <code>
 * <pre>
 * // get a ConstrainedClass for our Address class
 * ConstrainedClass cc = ConstrainedClass.getConstrainedClass(Address.class);
 *
 * // get the set of names for properties (of Address) that have constraints applied
 * {@code Set<String> props = cc.getConstrainedProperties();}
 *
 * // now iterate through them
 * for (String propName : props) {
 *   // look up the constraints that are attached to the property whose name is
 *   // propName (e.g. could be the street property).
 *   // Note you don't need to call getConstrainedProperties before calling
 *   // getConstraints if you know the name of the property
 *   {@code Set<Annotation> constraints = cc.getConstraints(propName);}
 *
 *   // now iterate through the constraints
 *   for (Annotation constraint : constraints) {
 *     // we now have an individual constraint - e.g. MaxLength
 *     if (constraint instanceof MaxLength) {
 *       MaxLength ml = (MaxLength)constraint;
 *       int l = ml.value();
 *       // we now have the value for this MaxLength constraint (e.g. 25 characters)
 *     }
 *   }
 * }
 * </pre>
 * </code>
 * </p>
 * @see Constraint
 * @see MaxLength
 * @see MinLength
 * @see MaxInclusive
 * @see MinInclusive
 * @see MaxOccurs
 * @see MinOccurs
 * @see Mandatory
 * @see Pattern
 * 
 */
public class ConstrainedClass {
    /*
     * We don't want to be holding on top ConstrainedClass instances for classes
     * that are no longer used. To avoid this we use a WeakHashMap.
     * TODO: Make sure the WeakHashMap works as expected in that it holds on
     * to objects only as long as they are used - not easy to test.
     */
    private static Map<Class, ConstrainedClass> constrainedClasses =
            Collections.synchronizedMap(new WeakHashMap<Class, ConstrainedClass>());

    private Map<String, Set<Annotation>> propertyConstraintMap;
    private Set<String> constrainedProperties;
    private Class<?> thisClass;


    private ConstrainedClass(Class<?> c) {
        thisClass = c;
        buildConstraintMap();
    }

    /**
     * Returns the ConstrainedClass for the given Class.
     * @param theClass the class to be wrapped
     * @return the ConstrainedClass that wraps the given class
     * @throws IllegalArgumentException if theClass is null
     */
    public static ConstrainedClass getConstrainedClass(Class<?> theClass) {
        if (theClass == null)
            throw new IllegalArgumentException("null is not a legal value " +
                    "for theClass");

        /*
         * We make an effort to avoid the same ConstrainedClass being created
         * more than once (i.e. on different threads) as there is potentially
         * a fair bit of reflection that goes on during construction. The
         * technique we use is to take a lock on theClass (e.g. Address.class)
         * and hold it while we create the associated ConstrainedClass.
         * Note this looks very much like the double checked lock trick, which
         * is known to be broken. However, in this case the class state that is
         * being changed are entries in a synchronised map.
         * If we removed the "synchronized (theClass)" statement, the code would
         * still be thread safe, but we could get cases where we were creating
         * unecessary copies of ConstrainedClass objects. As the process of
         * constructing an instance of ConstrainedClass for a given class should
         * always yield the same result, this is not a problem other than,
         * potentially, a temporary waste of cpu and memory.
         */
        ConstrainedClass cc = constrainedClasses.get(theClass);
        if (cc == null) {
            synchronized (theClass) {
                /*
                 * Now that we have the lock on the underlying class we just
                 * do a quick check that noone got in ahead of us.
                 */
                cc = constrainedClasses.get(theClass);
                if (cc == null) {
                    /*
                     * OK still doesn't exist so create it and put it in the map.
                     */
                    cc = new ConstrainedClass(theClass);
                    constrainedClasses.put(theClass, cc);
                }
            }
        }

        return cc;
    }


    /**
     * Returns true if the annotation is a Constraint. I.e. Contains the
     * Constraint marker annotation.
     * @param annotation the annotation to test
     * @throws IllegalArgumentException if annotation is null
     */
    public static boolean isConstraint(Annotation annotation) {
        if (annotation == null)
            throw new IllegalArgumentException("null is not a legal value for annotation");
        return annotation.annotationType().isAnnotationPresent(Constraint.class);
    }

    /**
     * Returns the immutable set of constraints for a given property. The
     * constraints may be annotated on the getter method and setter method and
     * may be anywhere within the class hierarchy.
     * The result is a set of constraints that must all be satisfied for the
     * property to be valid.
     * @param propertyName name of the property
     * @return the set (immutable) of constraints (annotations) or an empty set
     * if no constraints on that property
     * @throws IntrospectionException if there are problems accessing the property
     * @throws IllegalArgumentException if propertyName is null
     */
    public Set<Annotation> getConstraints(String propertyName)
            throws IntrospectionException {
        if (propertyName == null)
            throw new IllegalArgumentException("null is not a legal value for propertyName");

        Set<Annotation> c = propertyConstraintMap.get(propertyName);
        if (c == null)
            throw new IntrospectionException("propertyName");

        return c;
    }


    /**
     * Returns the set (immutable) of the properties that have constraints associated
     * with them.
     */
    public Set<String> getConstrainedProperties() {
        /*
         * Safe to return as it is immutable.
         */
        return constrainedProperties;
    }

    /**
     * Creates a map from property name to Set of constraints (for that property).
     * This is to save so cpu cycles that would be incurred by reflecting each time.
     */
    private void buildConstraintMap() {
        Map<String, Set<Annotation>> m = new HashMap<String, Set<Annotation>>();
        Set<String> s = new HashSet<String>();

        try {
            PropertyDescriptor[] descs =
                    Introspector.getBeanInfo(thisClass).getPropertyDescriptors();
            for (PropertyDescriptor desc : descs) {
                String propName = desc.getName();
                Set<Annotation> constraints = new HashSet<Annotation>();

                /**
                 * Add any constraints that are on the getter method.
                 */
                Method read = desc.getReadMethod();
                if (read != null) {
                    addConstraints(constraints, thisClass, read.getName(),
                        read.getParameterTypes());
                }

                /**
                 * Add any constraints that are on the setter method.
                 * Note these will take precedence for the same constraint
                 * types that were on the setter.
                 */
                Method write = desc.getWriteMethod();
                if (write != null) {
                    addConstraints(constraints, thisClass, write.getName(),
                        write.getParameterTypes());
                }

                /*
                 * This is a fixed set of constraints so we turn it into an
                 * unmodifiable set. Note an the special empty set is used in
                 * the hope that this is more efficient.
                 */
                if (!constraints.isEmpty()) {
                    constraints = Collections.unmodifiableSet(constraints);
                    s.add(propName);
                }
                else
                    constraints = Collections.emptySet();

                m.put(propName, constraints);
            }
        } catch (IntrospectionException e) {
        }

        /*
         * This is a fixed map of properties with constraints so we turn it into an
         * unmodifiable map. Note the special empty map is used in
         * the hope that this is more efficient.
         */
        if (!m.isEmpty())
            propertyConstraintMap = Collections.unmodifiableMap(m);
        else
            propertyConstraintMap = Collections.emptyMap();

        /*
         * This is a fixed set of property names that have associated constraints
         * so we turn it into an unmodifiable set. Note the special empty set is
         * used in the hope that this is more efficient.
         */
        if (!s.isEmpty())
            constrainedProperties = Collections.unmodifiableSet(s);
        else
            constrainedProperties = Collections.emptySet();
    }

    private void addConstraints(Set<Annotation> constraints,
            Class<?> thisClass, String methodName,
            Class<?>[] methodParameterTypes ) {
        assert constraints != null;
        assert thisClass != null;
        assert methodName != null;
        assert methodParameterTypes != null;


        /*
         * TODO: handling of multiple instances of the same constraint.
         * It is possible for many instances of say MaxLength to be added to a
         * property (eg several times on one method, on both getters & setters,
         * throughout the inheritence hierarchy).
         * This can lead to problems in
         *   - error message reporting. eg several messages saying that the
         *     MaxLength has been violated and each giving a different value for
         *     max
         *   - integration w/ jsf etc. What will they make of multiple maxlength
         *     validators?
         */
        try {
            Method method = thisClass.getDeclaredMethod(methodName, methodParameterTypes);

            /**
             * Add any constraints that are on the method
             */
            if (method != null) {
                addConstraints(constraints, method.getAnnotations());

                /*
                 * Could support constraints on setter method parameters but
                 * not alot of value really
                 */
                // addConstraints(constraints, method.getParameterAnnotations()[0]);
            }
        } catch (NoSuchMethodException e) { /* ignore */ }

        /*
         * Alas we are not done yet. If we have overridden the setter method
         * then we may have annotations on the super class method. So call
         * recursively with our super class until we are at the top
         */
        Class<?> superClass = thisClass.getSuperclass();
        if (superClass != null)
            addConstraints(constraints, superClass, methodName, methodParameterTypes);
    }

    private void addConstraints(Set<Annotation> constraints, Annotation[] annotations) {
        assert constraints != null;
        assert annotations != null;

        for (Annotation a : annotations) {
            if (isConstraint(a))
                constraints.add(a);
        }
    }
}
