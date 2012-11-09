package encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptTest {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String password = "next$";
		String text = "$P{Project}=[1,2,3]&$P{Name}=[1,2]";
		
		//encrypt
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(password);
		String myEncryptedText = textEncryptor.encrypt(text);
		myEncryptedText = URLEncoder.encode(myEncryptedText, "UTF-8");
		
		System.out.println("encrypted = " + myEncryptedText);

		// decrypt
		myEncryptedText = URLDecoder.decode(myEncryptedText, "UTF-8");
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword(password);
		String plainText = textEncryptor2.decrypt(myEncryptedText);
		
		System.out.println("decrypted = " + plainText);
	}

}
