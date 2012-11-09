package encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class BlowfishTest {

	public static void main(String args[]) {
		String dataToEncrypt = "Data to encrypt";
		String encryptionKey = "the key";
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), "Blowfish");
		String encryptedData = "";
		byte[] encryptedBytes;
		Cipher cipher;
		try {
			// Get cipher instance for Blowfish algorithm
			cipher = Cipher.getInstance("Blowfish");
			
			// encrypt
			cipher.init(Cipher.ENCRYPT_MODE, key);			
			encryptedBytes = cipher.doFinal(dataToEncrypt.getBytes("UTF-8"));			
			encryptedData = Hex.encodeHexString(encryptedBytes);
			System.out.println("encrypted data : " + encryptedData);
			
			// decrypt
			cipher.init(Cipher.DECRYPT_MODE, key);			
			byte[] decodedBytes = Hex.decodeHex(encryptedData.toCharArray());
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
			System.out.println("decrypted data : " + new String(decryptedBytes));
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
}
