package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackreferenceTest {
	
	public static void main(String[] args) {
		
		// The section of the input string matching the capturing group(s) is saved in memory for later recall via backreference. 
		// A backreference is specified in the regular expression as a backslash (\) followed by a digit indicating the number of
		// the group to be recalled.
		//
		// To match any 2 digits, followed by the exact same two digits
		String regex = "(\\d{2})\\1";
		String text1 = "1212";
		String text2 = "1213";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text1);
		System.out.println("text1 valid=" + matcher.matches());
		
		matcher = pattern.matcher(text2);
		System.out.println("text2 valid=" + matcher.matches());  
	}

}
