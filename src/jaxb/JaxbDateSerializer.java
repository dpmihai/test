package jaxb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import javax.xml.bind.annotation.adapters.XmlAdapter;
 
public class JaxbDateSerializer extends XmlAdapter<String, Date>{
 
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
 
    @Override
    public String marshal(Date date) throws Exception {
        return dateFormat.format(date);
    }
 
    @Override
    public Date unmarshal(String date) throws Exception {
        return dateFormat.parse(date);
    }
    
    public static Date getBirthDate(String date) {
    	try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}