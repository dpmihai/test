package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

	private Pattern pattern;
	private Matcher matcher;

	/*
	  	( # Start of group
	  	(?=.*\d) # must contains one digit from 0-9
		(?=.*[a-z]) # must contains one lowercase characters
		(?=.*[A-Z]) # must contains one uppercase characters
		(?=.*[@#$%]) # must contains one special symbols in the list “@#$%”
		. # match anything with previous condition checking
		{6,20} # length at least 6 characters and maximum of 20
		) # End of group
		
		?= – means apply the assertion condition, meaningless by itself, always work with other combination

		Whole combination is means, 6 to 20 characters string with at least one digit, 
		one upper case letter, one lower case letter and one special symbol (“@#$%”)
	 */
	private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20}";		
	
	public PasswordValidator() {
		pattern = Pattern.compile(PASSWORD_PATTERN);
	}

	/**
	 * Validate password with regular expression
	 * 
	 * @param password password for validation
	 * @return true valid password, false invalid password
	 */
	public boolean validate(final String password) {

		matcher = pattern.matcher(password);
		return matcher.matches();

	}
	
	public static void main(String[] args) {
		String  p = "a12Taaaa";
		String p1 = "gh@sdcdcdsc";
		String p2 = "q12#Ass&";
		PasswordValidator pv = new PasswordValidator();
		
		System.out.println(p + " : " + pv.validate(p));
		System.out.println(p1 + " : " + pv.validate(p1));
		System.out.println(p2 + " : " + pv.validate(p2));
	}
}