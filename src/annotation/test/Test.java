package annotation.test;

import annotation.MaxLength;
import annotation.MinLength;
import annotation.Constraint;
import annotation.ConstrainedClass;
import annotation.validation.ValidatableObject;
import annotation.validation.PropertyValidationErrors;
import annotation.validation.ObjectValidationErrors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 1:30:39 PM
 */
public class Test {


    public static void main(String[] args) {
        Address a = new Address();
        a.setStreet("123456789");
        a.setNumber(0);
        ValidatableObject v = new ValidatableObject(a);

        // validate just the street property
//        try {            
//            PropertyValidationErrors propertyErrors = v.validateProperty("street");
//            System.out.println(propertyErrors);
//        } catch (IntrospectionException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        // validate whole bean
        ObjectValidationErrors objectErrors = v.validate();
        System.out.println(objectErrors);



    }
}
