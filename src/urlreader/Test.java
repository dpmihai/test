package urlreader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class Test {

	// https://statsfc.com/developers#api-results
	//
	// http://api.statsfc.com/[COMPETITION]/results.json?team=[TEAM_NAME]&from=[FROM_DATE]&to=[TO_DATE]&limit=[LIMIT]&key=[YOUR_API_KEY]
	
    // team String, is the path of a team if you want only their results, e.g., manchester-city, liverpool
    // from Date, is the date to get results from, e.g., 2012-09-01
    // to Date, is the date to get results to, e.g., 2012-12-31
    // limit Integer, is the maximum number of results to return
	// key=free (limited to 100 requests per day

	private static final String URL_NAME = "http://api.statsfc.com/premier-league/results.json?from=2012-11-17&to=2012-11-20&key=free";
	private static final String JSON_NOT_FOUND = "{\"error\":\"No results found\"}";
	private static final String regex = "\"home\":\"([a-zA-Z ]+)\",\"away_id\":[\\d]+,\"away\":\"([a-zA-Z ]+)\",\"status\":\"([a-zA-Z]+)\",\"halftime\":\\[\\d,\\d\\],\"fulltime\":\\[(\\d),(\\d)\\],\"extratime\"";

	public static void main(String[] args) {
		try {
			URL url = new URL(URL_NAME);
			if (url != null) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.16.7", 128));
				URLConnection connection = url.openConnection(proxy);
				if (connection != null) {
					InputStream stream = connection.getInputStream();
					if (stream != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(stream));
						String inputLine;
						StringBuilder sb = new StringBuilder();
						while ((inputLine = br.readLine()) != null) {
							sb.append(inputLine);
						}
						String data = sb.toString();
						if (JSON_NOT_FOUND.equals(data)) {
							System.out.println("*** No data");
						} else {
							System.out.println(data);
							
							Pattern patt = Pattern.compile(regex);
							Matcher m = patt.matcher(data);
							while(m.find()) {
								String home = m.group(1);
								//System.out.println(home);
								String away = m.group(2);		
								String status = m.group(3);
								String hs = m.group(4);
								String gs = m.group(5);
								System.out.println(home + " : " + away +  "    " + status + " " + hs + " " + gs);																
							} 													
						}						
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
