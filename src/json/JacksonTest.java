package json;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JacksonTest {
    public static void main(String argv[]) {
        try {
        	JacksonTest jsonExample = new JacksonTest();
            jsonExample.testJackson();
        } catch (Exception e){
            System.out.println("Exception " + e);
        }       
    }
    public void testJackson() throws IOException {      
    	
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("Client", "Popescu");
        map.put("Project", new Object[] {"Anytime CRM", "Anytime Sales"});
        map.put("Date", new Date());
    	
        ObjectMapper mapper = new ObjectMapper();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(df);

                
        StringWriter writer = new StringWriter(); 
        mapper.writeValue(writer, map);
        
        System.out.println("Written json : " + writer.toString());        
        
        //JsonFactory factory = new JsonFactory();
        //mapper = new ObjectMapper(factory);        
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap<String,Object> map2 = mapper.readValue(writer.toString(), typeRef);
        System.out.println("Read from json : " + map2);
        
        Date date = (Date)map2.get("Date");
        System.out.println(date);
        
        	
                
    }   

}
