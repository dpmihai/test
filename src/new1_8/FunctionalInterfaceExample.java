package new1_8;

public class FunctionalInterfaceExample {
	
	public static void main(String[] args) {
		
		NextInterface ni = x -> x.indexOf("next") > 0;
		
		System.out.println(ni.isNext("/next/test"));
		System.out.println(ni.isNext("/jasper/test"));
	}

}
