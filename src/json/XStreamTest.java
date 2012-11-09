package json;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class XStreamTest {
	
	// *** keeps although information about the data types which are useful when we want to recreate the objects!!
	public static void main(String[] args) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Client", "Popescu");
        map.put("Project", new Object[] {"Anytime CRM", "Anytime Sales"});
        map.put("Date", new Date());
                
        XStream xstream = new XStream(new JettisonMappedXmlDriver());       
        String json = xstream.toXML(map);
        
        System.out.println("Written json : "+ json);		
                
        xstream = new XStream(new JettisonMappedXmlDriver());		
        Map<String, Object> map2 = (Map<String, Object>)xstream.fromXML(json);		
		
        System.out.println("\nRead from json : ");
		for (String key : map2.keySet()) {
			System.out.print("key = " + key);
			System.out.print("  value = ");
			Object val = map2.get(key);
			if (val instanceof Object[]) {
				System.out.println(Arrays.asList((Object[])val));
			} else {
				System.out.println(val);
			}
		}	
				
		Date date = (Date)map2.get("Date");
		System.out.println("\nConverted date : " + date);	     
				
	}

}
