package encrypt;

import java.security.Key;

public class AESCryptoKey implements CryptoKeyable {
	
	private Key key;
	
	public AESCryptoKey(Key key) {
		this.key = key;
	}

	@Override
	public Key getKey() {
		return key;
	}

}
