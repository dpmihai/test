package urlreader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestPremierLeague {
	
	private static final String URL_NAME = "http://www.premierleague.com/en-gb.html";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM HH:mm", Locale.ENGLISH);	
	
	private static final Date getStartDate() {
		Calendar c = Calendar.getInstance();
		c.set(2014, Calendar.AUGUST, 16);
		return c.getTime();
	}
	
	private static final Date getEndDate() {
		Calendar c = Calendar.getInstance();
		c.set(2014, Calendar.AUGUST, 18);
		return c.getTime();
	}
	
	 public static boolean sameDay(Date dateOne, Date dateTwo) {
	       if ((dateOne == null) || (dateTwo == null)) {
	           return false;
	       }

	       Calendar cal = Calendar.getInstance();
	       cal.setTime(dateOne);
	       int year = cal.get(Calendar.YEAR);
	       int day = cal.get(Calendar.DAY_OF_YEAR);

	       cal.setTime(dateTwo);
	       int year2 = cal.get(Calendar.YEAR);
	       int day2 = cal.get(Calendar.DAY_OF_YEAR);

	       return ( (year == year2) && (day == day2) );
	   }
	 
	 public static Date floor(Date d) {
	       Calendar c = Calendar.getInstance();
	       c.setTime(d);
	       c.set(Calendar.HOUR_OF_DAY, 0);
	       c.set(Calendar.MINUTE, 0);
	       c.set(Calendar.SECOND, 0);
	       c.set(Calendar.MILLISECOND, 0);
	       return c.getTime();
	   }
	 
	 public static boolean after(Date d1, Date d2) {
	        d1 = floor(d1);
	        Calendar c1 = Calendar.getInstance();
	        c1.setTime(d1);

	        d2 = floor(d2);
	        Calendar c2 = Calendar.getInstance();
	        c2.setTime(d2);

	        return c1.after(c2);
	    }

	    public static boolean before(Date d1, Date d2) {
	        d1 = floor(d1);
	        Calendar c1 = Calendar.getInstance();
	        c1.setTime(d1);

	        d2 = floor(d2);
	        Calendar c2 = Calendar.getInstance();
	        c2.setTime(d2);

	        return c1.before(c2);
	    }
	    
	    public static boolean insideInterval(Date d, Date d1, Date d2) {
	    	return (sameDay(d1, d) || before(d1, d)) && (sameDay(d, d2) || before(d, d2));
	    }
	 
	 public static Date setCurrentYear(Date date) {
		 Calendar cal = Calendar.getInstance();
		 int year = cal.get(Calendar.YEAR);
		 cal.setTime(date);
		 cal.set(Calendar.YEAR, year);
		 return cal.getTime();
	 }
	
	public static void main(String[] args) {
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // hours:minutes are London time -> convert them to bucharest time
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
						String html = sb.toString();						
						Document doc = Jsoup.parse(html);
						Date startDate = getStartDate();
						Date endDate = getEndDate();						
						ListIterator<Element> gameNodes = doc.select("li.megamenu-match").listIterator();
						while (gameNodes.hasNext()) {							
							Element gameNode = gameNodes.next();
							String gameHtml = gameNode.html();
							//System.out.println(gameHtml);
							
							System.out.println("---  GAME  ---");
							Element dateNode = gameNode.select("div.megamenu-date > span").first();	
							Date d = setCurrentYear(sdf.parse(dateNode.text()));							
							
//							System.out.println(startDate + "   " + d +  "   " + endDate);
							if (insideInterval(d, startDate, endDate)) {							
								System.out.println("Date: " + d);
								
								Element statusNode = gameNode.select("div.megamenu-status").first();						
								System.out.println("Status: " + statusNode.text());
								
								ListIterator<Element> matchNodes = gameNode.select("div.megamenu-matchName > span").listIterator();	
								System.out.print("Match: ");
								int i=0;
								while (matchNodes.hasNext()) {							
									Element matchNode = matchNodes.next();
									switch (i) {
										case 0: System.out.print("Home=" + matchNode.text());
												break;									
										case 1: System.out.print(" Score=" + matchNode.text());
												break;
										case 2: System.out.print(" Guests=" + matchNode.text());
												break;		
									}								
									i++;
								}
								System.out.println();
								
								System.out.println();
							}														
						}

						
//						if (JSON_NOT_FOUND.equals(data)) {
//							System.out.println("*** No data");
//						} else {
//							System.out.println(data);
//							
//							Pattern patt = Pattern.compile(regex);
//							Matcher m = patt.matcher(data);
//							while(m.find()) {
//								String home = m.group(1);
//								//System.out.println(home);
//								String away = m.group(2);		
//								String status = m.group(3);
//								String hs = m.group(4);
//								String gs = m.group(5);
//								System.out.println(home + " : " + away +  "    " + status + " " + hs + " " + gs);																
//							} 													
//						}						
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
