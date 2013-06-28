package regex;

public class TestReplace {
	
	public static void main(String[] args) {
		String s = "w234frT5";
		s = s.replaceAll("\\D", "");
		System.out.println("s = " + s);
	}

}
