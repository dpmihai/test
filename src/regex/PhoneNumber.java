package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {

	public static void main(String[] args) {
		String regex = "\\d{3}([-\\s])?\\d{4}";
		String phone1="3368445";
		String phone2="021 3425";
		String phone3="021-4356";
		String badPhone="021  7893";
		
		 Pattern pattern = Pattern.compile(regex);
		 Matcher matcher = pattern.matcher(phone1);
		 System.out.println("phone1 valid=" + matcher.matches());  
		 
		 matcher = pattern.matcher(phone2);
		 System.out.println("phone2 valid=" + matcher.matches());
		 
		 matcher = pattern.matcher(phone3);
		 System.out.println("phone3 valid=" + matcher.matches());  
		 
		 matcher = pattern.matcher(badPhone);
		 System.out.println("badPhone valid=" + matcher.matches());  
	}
}
