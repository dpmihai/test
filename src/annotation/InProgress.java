package annotation;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 5:57:34 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Marker annotation to indicate that a method or class
 * is still in progress.
 * <p/>
 * By default, annotations are not included in Javadoc -- a fact worth remembering when you spend a lot of time
 * annotating a class, detailing what's left to do, what it does correctly, and otherwise documenting its behavior
 * Use Documented annotation, annotation's retention must be RUNTIME. This is a required aspect of using the Documented
 * annotation type. Javadoc loads its information from class files (not source files), using a virtual machine.
 * The only way to ensure that this VM gets the information for producing Javadoc from these class files is to specify
 * the retention of RetentionPolicy.RUNTIME. As a result, the annotation is kept in the compiled class file
 * and is loaded by the VM; Javadoc then picks it up and adds it to the class's HTML documentation.
 * <p/>
 * Suppose that you mark a class as being in progress, through InProgress annotation.
 * No problem, right? This will even show up in the Javadoc if you've correctly applied the Documented meta-annotation.
 * Now, suppose you write a new class and extend the in-progress class. Easy enough, right? But remember that
 * the superclass is in progress. If you use the subclass, and even look at its documentation, you get no indication
 * that anything is incomplete. You would expect to see that the InProgress annotation is carried through to subclasses
 * -- that it's inherited -- but it isn't. You must use the Inherited meta-annotation to specify the behavior you want
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InProgress {
}