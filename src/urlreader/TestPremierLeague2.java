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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestPremierLeague2 {
	
	private static final String URL_NAME = "http://www.premierleague.com/en-gb.html";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM HH:mm", Locale.ENGLISH);	
	
	private static final Date getStartDate() {
		Calendar c = Calendar.getInstance();
		c.set(2015, Calendar.AUGUST, 7);
		return c.getTime();
	}
	
	private static final Date getEndDate() {
		Calendar c = Calendar.getInstance();
		c.set(2015, Calendar.AUGUST, 11);
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
							
							
							Element dateNode = gameNode.select("div.megamenu-date > span").first();	
							Date d = setCurrentYear(sdf.parse(dateNode.text()));							
							
//							System.out.println(startDate + "   " + d +  "   " + endDate);
							if (insideInterval(d, startDate, endDate)) {	
								System.out.println("---  GAME  ---");
								JsonGame jg = new JsonGame();
								jg.setDate(d);					
								
								Element statusNode = gameNode.select("div.megamenu-status").first();
								if ("FT".equals(statusNode.text())) {
									jg.setStatus("Finished");
								} else {
									jg.setStatus("Not Finished");
								}					
								
								ListIterator<Element> matchNodes = gameNode.select("div.megamenu-matchName > span").listIterator();						
								int i=0;
								while (matchNodes.hasNext()) {							
									Element matchNode = matchNodes.next();
									switch (i) {
										case 0: jg.setHosts(matchNode.text());									
												break;									
										case 1: String score = matchNode.text();
												if ("v".equals(score)) {
													jg.setHostsScore(0);
													jg.setGuestsScore(0);
												} else {
													String s[] = score.split("-");
													if (s.length == 2) {
														jg.setHostsScore(Integer.parseInt(s[0]));
														jg.setGuestsScore(Integer.parseInt(s[1]));
													} else {
														throw new Exception("Score format is wrong : " + score);
													}
												}									
												break;
										case 2: jg.setGuests(matchNode.text());								 	
												break;		
									}								
									i++;
								}
								System.out.println(jg);
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
