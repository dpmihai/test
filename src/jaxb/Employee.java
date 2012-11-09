package jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
 
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee
{
    public static void main(String args[]) throws JAXBException
    {
        final Employee john = new Employee();
        john.setId(1);
        john.setFirstName("John");
        john.setMiddleName("Robert");
        john.setLastName("Doe");
        john.setBirthDate(JaxbDateSerializer.getBirthDate("03-27-1965"));
 
        // write it out as XML
        final JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
        StringWriter writer = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // to format resulted xml, otherwise it is on a single line
        marshaller.marshal(john, writer);
 
        // read it from XML
        final Employee johnRead = (Employee) jaxbContext.createUnmarshaller().unmarshal(new StringReader(writer.toString()));
        if (john.equals(johnRead)) {   
        	// write the new object out as XML again.
            writer = new StringWriter();
            marshaller.marshal(johnRead, writer);
            System.out.println("johnRead was identical to john: \n" + writer.toString());
        } else {
            System.out.println("john and johnRead are not equal");
            System.out.println(john);
            System.out.println(johnRead);
        }
    }
 
    @XmlAttribute
    private int id;
     
    @XmlElement
    private String firstName;
 
    @XmlElement 
    private String middleName;
     
    @XmlElement
    private String lastName;
    
    @XmlElement
    @XmlJavaTypeAdapter(JaxbDateSerializer.class) 
    // by default date format is yyyy-MM-ddTHH:mm:ss, to change it we create an XmlAdapter
    private Date birthDate;
 
    public Employee() {
    }
 
    public int getId(){
        return id;
    }
 
    public void setId(int id){
        this.id = id;
    }
 
    public String getLastName(){
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public String getMiddleName(){
        return middleName;
    }
 
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }        
        
    public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Employee employee = (Employee) o;
 
        if (id != employee.id) return false;
        if (firstName != null ? !firstName.equals(employee.firstName) :
            employee.firstName != null) return false;
        if (lastName != null ? !lastName.equals(employee.lastName) :
            employee.lastName != null) return false;
        if (middleName != null ? !middleName.equals(employee.middleName) :
            employee.middleName != null) return false;
        if (birthDate != null ? !birthDate.equals(employee.birthDate) :
            employee.birthDate != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }
 
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", middleName='" + middleName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birthDate='" + birthDate + '\'' +
            '}';
    }
}
