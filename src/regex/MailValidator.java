package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailValidator {

	public static void main(String[] args) {
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		String mail1 = "me@asf.ro";
		String mail2 = "m32@yahoo.co.uk";
		String mail3 = "dpmihai@yahoo.com";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail1);
		System.out.println("mail1 valid=" + matcher.matches());  
		
		matcher = pattern.matcher(mail2);
		System.out.println("mail2 valid=" + matcher.matches()); 
		
		matcher = pattern.matcher(mail3);
		System.out.println("mail3 valid=" + matcher.matches()); 
	}
}
