package regex;

public class TestReplaceNonAscii {
	
	public static void main(String[] args) {		
		String s = "Connexion � source de donn�es?";
		//replace all non-ascii characters or '?' with space
		String result = s.replaceAll("[^\\p{ASCII}]|[\\?]", "");
		System.out.println("s = " + s);;
		System.out.println("result = " + result);
	}
		
}
