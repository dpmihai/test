package xstream;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jun 13, 2006
 * Time: 4:47:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person implements Serializable {
    private String name;
    //private String address;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }


    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", address='" + address + '\'' +
//                '}';
//    }
}