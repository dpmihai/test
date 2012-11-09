package version;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/*
 *   Java 1.2 uses major version 46
 *   Java 1.3 uses major version 47
 *   Java 1.4 uses major version 48
 *   Java 5 uses major version 49
 *   Java 6 uses major version 50
 *   Java 7 uses major version 51
 */

public class ClassVersion {
	
	public static void testClass(String className) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(className);
			parseJavaClassFile(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void parseJavaClassFile(InputStream classByteStream) throws Exception {
		DataInputStream dataInputStream = new DataInputStream(classByteStream);
		int magicNumber = dataInputStream.readInt();
		if (magicNumber == 0xCAFEBABE) {
			int minorVer = dataInputStream.readUnsignedShort();
			int majorVer = dataInputStream.readUnsignedShort();
			// do something here with major & minor numbers
			System.out.println("Minor version = " + minorVer);
			System.out.println("Major version = " + majorVer);
		}
	}
	
	public static void main(String[] args) {
		testClass("D:/Public/jarcheck/com/mindprod/jarcheck/JarCheck.class");
	}


}
