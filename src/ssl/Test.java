package ssl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class Test {
	
	public static void main(String[] args) throws Exception {
						
		String file= "E:\\Public\\next-reports\\jssecacerts";
		System.setProperty("javax.net.ssl.trustStore", file);
		String pass = "next";
		String host = "192.168.16.86";
		int port = 8182;
		System.out.println("Loading KeyStore " + file + "...");
		InputStream in = new FileInputStream(file);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(in, pass.toCharArray());
		in.close();
		
		SSLContext context = SSLContext.getInstance("TLS");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] {tm}, null);
		SSLSocketFactory factory = context.getSocketFactory();

		System.out.println("Opening connection to " + host + ":" + port + "...");
		SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
		    System.out.println("Starting SSL handshake...");
		    socket.startHandshake();
		    socket.close();
		    System.out.println();
		    System.out.println("No errors, certificate is already trusted");
		} catch (SSLException e) {
		    System.out.println();
		    e.printStackTrace(System.out);
		}

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
		    System.out.println("Could not obtain server certificate chain");
		    return;
		}
		
		System.out.println();
		System.out.println("Server sent " + chain.length + " certificate(s):");
		System.out.println();
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		OutputStream out = new FileOutputStream(file);
		for (int i = 0; i < chain.length; i++) {
		    X509Certificate cert = chain[i];
		    System.out.println
		    	(" " + (i + 1) + " Subject " + cert.getSubjectDN());
		    System.out.println("   Issuer  " + cert.getIssuerDN());
		    sha1.update(cert.getEncoded());
		    System.out.println("   sha1    " + toHexString(sha1.digest()));
		    md5.update(cert.getEncoded());
		    System.out.println("   md5     " + toHexString(md5.digest()));
		    System.out.println();	
		    		    
			String alias = host + "-" + (i + 1);
			ks.setCertificateEntry(alias, cert);			
			ks.store(out, pass.toCharArray());	
			System.out.println("Added certificate : " + alias);
		}
		out.close();
		
		// test 
		URL url = new URL("https://" + host + ":" + port +"/nextserver");
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		connection.setHostnameVerifier(new NullHostnameVerifier());

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;

		while ((inputLine = br.readLine()) != null) {
			System.out.println(inputLine);
		}	
		in.close();

	}
	
	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SavingTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}
		

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}
	
	private static class NullHostnameVerifier implements HostnameVerifier {
	    public boolean verify(String hostname, SSLSession session) {
	        return true;
	    }
	}

	
	

}
