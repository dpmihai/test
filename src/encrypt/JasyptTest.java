package encrypt;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptTest {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
				
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy hh:mm", Locale.getDefault());
		
		String password = "next$";
		//String text = "$P{Project}=[1,2,3]&$P{Name}=[1,2]";
		Calendar c = Calendar.getInstance();
		c.set(2012, 8, 14); // 14 September 2012
		String date = sdf.format(c.getTime());
		System.out.println("Date="+date);
		String text = "$P{FirstDate}=" + date;
		
		//encrypt
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(password);
		String myEncryptedText = textEncryptor.encrypt(text);		
		myEncryptedText = Base64.encodeBase64URLSafeString(myEncryptedText.getBytes());
		
		System.out.println("encrypted = " + myEncryptedText);

		// decrypt		
		myEncryptedText = new String(Base64.decodeBase64(myEncryptedText));
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword(password);
		String plainText = textEncryptor2.decrypt(myEncryptedText);
		
		System.out.println("decrypted = " + plainText);
		
		try {
			Date d = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--> date = " + date);
		System.out.println("--> locale = " + Locale.getDefault());
				
	}

}
