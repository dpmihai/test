package encrypt;

import java.io.FileOutputStream;
import java.security.KeyStore;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Assert;
import org.junit.Test;

public class AESTest {

	@Test
	public void testEncryptRandomKey() throws Exception {
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();
		Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(key));
		String enc = crypto.encrypt("Andy");
		Assert.assertEquals("Andy", crypto.decrypt(enc));

		SecretKey anotherKey = KeyGenerator.getInstance("AES").generateKey();
		Cryptographical anotherInst = AESCryptoImpl.initialize(new AESCryptoKey(anotherKey));
		String anotherEncrypt = anotherInst.encrypt("Andy");
		Assert.assertEquals("Andy", anotherInst.decrypt(anotherEncrypt));

		Assert.assertFalse(anotherEncrypt.equals(enc));
	}
	
	@Test
	public void testEncrypt() throws Exception {
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();

		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, null);
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
		ks.setEntry("mykey", skEntry, new KeyStore.PasswordProtection("mykeypassword".toCharArray()));
		FileOutputStream fos = new FileOutputStream("agb50.keystore");
		ks.store(fos, "somepassword".toCharArray());
		fos.close();

		Cryptographical crypto = AESCryptoImpl.initialize(new AESCryptoKey(key));
		String enc = crypto.encrypt("Andy");
		Assert.assertEquals("Andy", crypto.decrypt(enc));

		// alternatively, read the keystore file itself to obtain the key

		Cryptographical anotherInst = AESCryptoImpl.initialize(new AESCryptoKey(key));
		String anotherEncrypt = anotherInst.encrypt("Andy");
		Assert.assertEquals("Andy", anotherInst.decrypt(anotherEncrypt));

		Assert.assertTrue(anotherEncrypt.equals(enc));
	}

}
