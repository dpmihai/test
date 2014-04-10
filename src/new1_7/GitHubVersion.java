package new1_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GitHubVersion {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		String versionNotFound = "NA";
		String currentVersion = "0.8";
		String url = "https://github.com/decebals/wicket-dashboard/releases";
		//String url = "https://github.com/nextreports/nextreports-designer/releases";
		String versionUrlStart = "https://github.com/decebals/wicket-dashboard/archive/release-";
		String versionUrlEnd = ".zip";
		
		System.setProperty("java.net.useSystemProxies","true");
//		System.setProperty("http.proxyHost", "192.168.16.7");
//		System.setProperty("http.proxyPort", "128");
		
		String lastVersion = getLastVersion(url);
		
		System.out.println("Last release is : " + lastVersion);
		if (versionNotFound.equals(lastVersion)) {
			System.out.println("You are up to date.");
		} else {	
			String downloadUrl = versionUrlStart + lastVersion + versionUrlEnd;
			System.out.println("Last release download url : " + downloadUrl);

			int status = compareVersions(currentVersion, lastVersion);
			System.out.println("Current version is : " + currentVersion);
			if (status < 0) {
				System.out.println("A new release is found : " + lastVersion);
			} else {
				System.out.println("You are up to date.");
			}
		}
	}
	
	public static String getLastVersion(String url) throws IOException {
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);

		StringBuffer sbuf=new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

		String res = in.readLine();
		System.out.println(" Response from github "+res);
		while ((res = in.readLine()) != null){
		       sbuf.append(res);
		}
		in.close();

		
		// <a href="/decebals/wicket-dashboard/archive/release-0.9.zip" rel="nofollow">
		String regex = "release-([\\d\\.]+)\\.zip";		
		System.out.println(" Total Data received  "+sbuf);
		String html = sbuf.toString();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(html);		
		//while(m.find()){
		// first is last release
		if (m.find()) {
			String release = m.group(1);
		    return release;
		}
		return "NA";
	}
	
	public static int compareVersions(String v1, String v2) {
		String[] v1a = v1.split("\\.");
		String[] v2a = v2.split("\\.");
		Integer v1M = Integer.parseInt(v1a[0]);
		Integer v2M = Integer.parseInt(v2a[0]);
		if (v1M < v2M) {
			return -1;
		} else if (v1M > v2M) {
			return 1;
		} else {
			Integer v1min = Integer.parseInt(v1a[1]);
			Integer v2min = Integer.parseInt(v2a[1]);
			if (v1min < v2min) {
				return -1;
			} else if (v1min > v2min) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	
	

}
