package normalizer;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static String unAccent(String s) {
	      //
	      // JDK1.5
	      //   use sun.text.Normalizer.normalize(s, Normalizer.DECOMP, 0);
	      //
	      String temp = Normalizer.normalize(s, Normalizer.Form.NFKD);
	      System.out.println(temp);
	      Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	      return pattern.matcher(temp).replaceAll("");
	  }

	  public static void main(String args[]) throws Exception{
	      //String value = "� � � _ @";
		  String value = "âăîşșţțȚ";
	      System.out.println(StringUtils.unAccent(value));
	      // output : e a i _ @
	  }

}
