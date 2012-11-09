package annotation_persistence;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 6, 2007
 * Time: 11:21:14 AM
 */

import java.io.*;
import java.util.*;

//----------------------------------------------------------------------
@Exportable
class Address {
    private
    @Persistent
    String street;
    private
    @Persistent
    String city;
    private
    @Persistent
    String state;
    private
    @Persistent("zipcode")
    int zip;

    public Address(String street, String city, String state, int zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}

//----------------------------------------------------------------------
public class Test {
    @Exportable(name = "customer", description = "A Customer")
    public static class Customer {
        @Persistent
        private String name = "Allen Holub";
        @Persistent
        private Address streetAddress = new Address("1234 MyStreet", "Berkeley", "CA", 99999);
        @Persistent
        private StringBuffer notes = new StringBuffer("Notes go here ");

        private int garbage; // Is not persistant

        @Persistent
        Collection<Invoice> invoices = new LinkedList<Invoice>();

        {
            invoices.add(new Invoice(0));
            invoices.add(new Invoice(1));
        }
    }

    @Exportable
    public static class Invoice {
        private
        @Persistent
        int number;

        public Invoice(int number) {
            this.number = number;
        }
    }

    public static void main(String[] args) throws IOException {
        Customer x = new Customer();
        XmlExporter out = new XmlExporter(new PrintWriter(System.out, true));
        out.flush(x);
    }
}
