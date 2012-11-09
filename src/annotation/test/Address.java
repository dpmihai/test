package annotation.test;

import annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 26, 2006
 * Time: 1:58:44 PM
 */
@InProgress
public class Address {

    private String street;
    private Integer number;

    public String getStreet() {
        return street;
    }

    @Mandatory
    @MinLength(10)
    @MaxLength(20)
    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    @Mandatory
    @MinInclusive(1)
    @GTodo(
        severity=GTodo.Severity.DOCUMENTATION,
        item="Document this method",
        assignedTo="Jon Stevens",
        dateAssigned="04/26/2006"
    )
    public void setNumber(Integer number) {
        this.number = number;
    }
}
