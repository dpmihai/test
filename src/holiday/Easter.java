package holiday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Easter {
	
	public static final int EASTER_ORTHODOX = 1;
	public static final int EASTER_CATHOLIC = 2;

	/**
	 * Compute the day of the year that Easter falls on. Step names E1 E2 etc.,
	 * are direct references to Knuth, Vol 1, p 155. 
	 * 
	 * @Note: It's not proven correct, although it gets the right answer for years
 	 * around the present.
 	 * 
 	 * @Note: Orthodox Easter is then EasterDay of EasterMonth in the Julian
     * Calendar. You will need to add the correct offset to obtain the 
     * date in the Gregorian Calendar. From Julian Mar 1, 1900, to Julian
     * Feb 29, 2100, the correction is to add 13 days to the Julian date to
     * obtain the Gregorian date
     * 
     * http://www.smart.net/~mmontes/ortheast.html
     * http://www.assa.org.au/edm.html#List20
	 * 
	 * @param year year
	 * @param easterType catholic or orthodox
	 * @return easter day
	 * 
	 * @exception IllegalArgumentexception If the year is before 1582 (since the algorithm
	 * only works on the Gregorian calendar).
	 */
	public static final Calendar getEasterDay(int year, int easterType) {
		
		if (easterType == EASTER_CATHOLIC) {
			if (year <= 1582) {
				throw new IllegalArgumentException("Algorithm invalid before April 1583");
			}
			int golden, century, x, z, d, epact, n;

			golden = (year % 19) + 1; /* E1: metonic cycle */
			century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
			x = (3 * century / 4) - 12; /* E3: leap year correction */
			z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
			d = (5 * year / 4) - x - 10;
			epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
			if ((epact == 25 && golden > 11) || epact == 24) {
				epact++;
			}	
			n = 44 - epact;
			n += 30 * (n < 21 ? 1 : 0); /* E6: */
			n += 7 - ((d + n) % 7);
			if (n > 31) {
				/* E7: */			
				return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
			} else {
				return new GregorianCalendar(year, 3 - 1, n); /* March */
			}
		} else {
			
			// G is the Golden Number-1
			// I is the number of days from 21 March to the Paschal full moon
			// J is the weekday for the Paschal full moon (0=Sunday, 1=Monday, etc.)
			// L is the number of days from 21 March to the Sunday on or before the Pascal full moon (a number between -6 and 28)			
			int G = year % 19;
			int I = (19*G + 15) % 30;
			int J = (year + year/4 + I) % 7;
			int L = I - J;
			int EasterMonth = 3 + (L + 40)/44;
			int EasterDay = L + 28 - 31*(EasterMonth/4);
			GregorianCalendar cal = new GregorianCalendar(year, EasterMonth-1, EasterDay);
			cal.add(Calendar.DAY_OF_YEAR, 13); // see note for orthodox easter
			return cal;
		}
	}
	
	public static final Calendar getSecondEasterDay(int year, int easterType) {
		Calendar cal = getEasterDay(year, easterType);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal;
	}
	 
	/**
	 * Get the seventh Sunday after Easter (rusalii)
	 * 
	 * @param year year
	 * @param easterType catholic or orthodox
	 * @return seventh sunday after easter
	 */
	public static final Calendar getWhitsun(int year, int easterType) {
		 Calendar cal = getEasterDay(year, easterType);
		 cal.add(Calendar.DAY_OF_YEAR, 49);
		 return cal;
	}
	
	public static final Calendar getDayAfterWhitsun(int year, int easterType) {
		 Calendar cal = getWhitsun(year, easterType);
		 cal.add(Calendar.DAY_OF_YEAR, 1);
		 return cal;
	}
	
	/**
	 * Get romanian holidays
	 * @param year
	 * @return romanian holidays
	 */
	public static List<Date> getRomanianHolydays(int year) {
		List<Date> result = new ArrayList<Date>();
		result.add(getDate(year, Calendar.JANUARY, 1));
		result.add(getDate(year, Calendar.JANUARY, 2));
		
		result.add(getEasterDay(year, EASTER_ORTHODOX).getTime());
		result.add(getSecondEasterDay(year, EASTER_ORTHODOX).getTime());
		
		result.add(getDate(year, Calendar.MAY, 1));
		
		result.add(getWhitsun(year, EASTER_ORTHODOX).getTime());
		result.add(getDayAfterWhitsun(year, EASTER_ORTHODOX).getTime());
		
		result.add(getDate(year, Calendar.AUGUST, 15));
		
		result.add(getDate(year, Calendar.DECEMBER, 1));
		
		result.add(getDate(year, Calendar.DECEMBER, 25));
		result.add(getDate(year, Calendar.DECEMBER, 26));
		
		return result;
	}
	
	public static Date getDate(int year, int month, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();;
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}	

	/** Main program, when used as a standalone application */
	public static void main(String[] argv) {
//		for (int i = 2008; i < 2022; i++) {
//			Calendar orthodox = Easter.getEasterDay(i, EASTER_ORTHODOX);
//			Calendar catholic = Easter.getEasterDay(i, EASTER_CATHOLIC);
//			System.out.println("year=" + i + " : " + orthodox.getTime() + "  <>  " + catholic.getTime() + "   rus="+getWhitsun(i, EASTER_ORTHODOX).getTime());
//		}
		System.out.println(getRomanianHolydays(2011));
	}
}
