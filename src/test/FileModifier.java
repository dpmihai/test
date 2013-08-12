package test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileModifier {
		
	public static void main(String[] args) {
		try {
			File file = new File("D:/Public/test/example.txt");
			FileOutputStream fos = new FileOutputStream(file);			
			fos.write("My first line in file\n".getBytes());
			fos.write("Second line in file #F{COL}# with changes\n".getBytes());
			fos.write("Third line in file\n".getBytes());
			fos.close();
			
			String all = readFileAsString(file.getAbsolutePath());
			all = all.replace("#F{COL}#", "1234");
			fos = new FileOutputStream(file);
			fos.write(all.getBytes());
			fos.close();
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			return fileData.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}		
	}
}
