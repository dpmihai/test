package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestReplace {
	
//	public static void main(String[] args) {
////		String s = "w234frT5";
////		s = s.replaceAll("\\D", "");
////		System.out.println("s = " + s);
//		
//		String s = "SysInd > 100 ${val}";
//		s = s.replaceAll("\\$\\{val\\}", "435");
//		System.out.println("s = " + s);
//	}
	
	
	public static void main(String[] args) {
		//String s = "66666666;ASOCIATIA DE PROPR. \"KISELEFF A8; SCARA 6\";NU;180;180;0;0;0;0;0;133;s-a inceput procedura de executare silita;3;47 ";
		String s= "30743554; ROYAL RECYCLING INTERNATIONAL SRL;NU;7836;7836;7836;0;0;0;0;0;s-a inceput procedura de executare silita;3;0";

		String regex = "(([^;]*\".+\"[^;]*)|([^;\"]+));*";
		System.out.println("s = " + s);
		
		Pattern patt = Pattern.compile(regex);
		Matcher m = patt.matcher(s);
		while(m.find()) {			
			String e = m.group(1);
			System.out.println(e);
		}	
	}

}
