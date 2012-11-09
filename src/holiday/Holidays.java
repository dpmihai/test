package holiday;

import java.util.*;

public class Holidays {

	public static Date getNewYearsDayObserved(int nYear) {
		int nX;
		int nMonth = 0; // January
		int nMonthDecember = 11; // December
		Date dtD;

		dtD = getDate(nYear, nMonth, 1);
		nX = getDayOfWeek(dtD);
		if (nYear > 1900) {
			nYear -= 1900;
		}
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 2);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thrusday
		case 5: // Friday
			return getDate(nYear, nMonth, 1);
		default:
			// Saturday, then observe on friday of previous year
			return getDate(--nYear, nMonthDecember, 31);
		}
	}

	public static Date getNewYearsDay(int nYear) {
		return getDate(nYear, Calendar.JANUARY, 1);
	}

	public static Date getRobertELeeDay(int nYear) {
		return getDate(nYear, Calendar.JANUARY, 18);
	}

	public Date getMartinLutherKingObserved(int nYear) {
		// Third Monday in January
		Date dtD = getDate(nYear, Calendar.JANUARY, 1);
		int nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, Calendar.JANUARY, 16);
		case 1: // Monday
			return getDate(nYear, Calendar.JANUARY, 15);
		case 2: // Tuesday
			return getDate(nYear, Calendar.JANUARY, 21);
		case 3: // Wednesday
			return getDate(nYear, Calendar.JANUARY, 20);
		case 4: // Thrusday
			return getDate(nYear, Calendar.JANUARY, 19);
		case 5: // Friday
			return getDate(nYear, Calendar.JANUARY, 18);
		default: // Saturday
			return getDate(nYear, Calendar.JANUARY, 17);
		}
	}

	public static Date getGroundhogDay(int nYear) {
		return getDate(nYear, Calendar.FEBRUARY, 8);
	}

	public static Date getAbrahamLincolnsBirthday(int nYear) {
		return getDate(nYear, Calendar.FEBRUARY, 12);
	}

	public static Date getValentinesDay(int nYear) {
		return getDate(nYear, Calendar.FEBRUARY, 14);
	}

	public static Date getPresidentsDayObserved(int nYear) {
		// Third Monday in February
		Date dtD = getDate(nYear, Calendar.FEBRUARY, 1);
		int nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, Calendar.FEBRUARY, 16);
		case 1: // Monday
			return getDate(nYear, Calendar.FEBRUARY, 15);
		case 2: // Tuesday
			return getDate(nYear, Calendar.FEBRUARY, 21);
		case 3: // Wednesday
			return getDate(nYear, Calendar.FEBRUARY, 20);
		case 4: // Thrusday
			return getDate(nYear, Calendar.FEBRUARY, 19);
		case 5: // Friday
			return getDate(nYear, Calendar.FEBRUARY, 18);
		default: // Saturday
			return getDate(nYear, Calendar.FEBRUARY, 17);
		}
	}

	public static Date SaintPatricksDay(int nYear) {
		return getDate(nYear, Calendar.MARCH, 17);
	}

	public static Date getGoodFridayObserved(int nYear) {
		// Get Easter Sunday and subtract two days
		int nEasterMonth = 0;
		int nEasterDay = 0;
		int nGoodFridayMonth = 0;
		int nGoodFridayDay = 0;
		Date dEasterSunday;

		dEasterSunday = getEasterSunday(nYear);
		nEasterMonth = getMonth(dEasterSunday);
		nEasterDay = getDayOfMonth(dEasterSunday);
		if (nEasterDay <= 3 && nEasterMonth == 3) // Check if <= April 3rd
		{
			switch (nEasterDay) {
			case 3:
				nGoodFridayMonth = nEasterMonth - 1;
				nGoodFridayDay = nEasterDay - 2;
				break;
			case 2:
				nGoodFridayMonth = nEasterMonth - 1;
				nGoodFridayDay = 31;
				break;
			case 1:
				nGoodFridayMonth = nEasterMonth - 1;
				nGoodFridayDay = 31;
				break;
			default:
				nGoodFridayMonth = nEasterMonth;
				nGoodFridayDay = nEasterDay - 2;
			}
		} else {
			nGoodFridayMonth = nEasterMonth;
			nGoodFridayDay = nEasterDay - 2;
		}

		return getDate(nYear, nGoodFridayMonth, nGoodFridayDay);
	}

	// incorrect for 2009 ( a week early)
	// see http://www.java2s.com/Open-Source/Java-Document/Internationalization-Localization/icu4j/com/ibm/icu/util/EasterHoliday.java.htm
	// for more accurate
	public static Date getEasterSunday(int nYear) {
		/*
		 * Calculate Easter Sunday
		 * 
		 * Written by Gregory N. Mirsky
		 * 
		 * Source: 2nd Edition by Peter Duffett-Smith. It was originally from
		 * Butcher's Ecclesiastical Calendar, published in 1876. This algorithm
		 * has also been published in the 1922 book General Astronomy by Spencer
		 * Jones; in The Journal of the British Astronomical Association
		 * (Vol.88, page 91, December 1977); and in Astronomical Algorithms
		 * (1991) by Jean Meeus.
		 * 
		 * This algorithm holds for any year in the Gregorian Calendar, which
		 * (of course) means years including and after 1583.
		 * 
		 * a=year%19 b=year/100 c=year%100 d=b/4 e=b%4 f=(b+8)/25 g=(b-f+1)/3
		 * h=(19*a+b-d-g+15)%30 i=c/4 k=c%4 l=(32+2*e+2*i-h-k)%7
		 * m=(a+11*h+22*l)/451 Easter Month =(h+l-7*m+114)/31 [3=March, 4=April]
		 * p=(h+l-7*m+114)%31 Easter Date=p+1 (date in Easter Month)
		 * 
		 * Note: Integer truncation is already factored into the calculations.
		 * Using higher percision variables will cause inaccurate calculations.
		 */

		int nA = 0;
		int nB = 0;
		int nC = 0;
		int nD = 0;
		int nE = 0;
		int nF = 0;
		int nG = 0;
		int nH = 0;
		int nI = 0;
		int nK = 0;
		int nL = 0;
		int nM = 0;
		int nP = 0;
		int nEasterMonth = 0;
		int nEasterDay = 0;

		// Calculate Easter
		boolean uncorrect = false;
		if (nYear < 1900) {
			// if year is in java format put it into standard
			// format for the calculation
			nYear += 1900;
			uncorrect = true;
		}
		nA = nYear % 19;
		nB = nYear / 100;
		nC = nYear % 100;
		nD = nB / 4;
		nE = nB % 4;
		nF = (nB + 8) / 25;
		nG = (nB - nF + 1) / 3;
		nH = (19 * nA + nB - nD - nG + 15) % 30;
		nI = nC / 4;
		nK = nC % 4;
		nL = (32 + 2 * nE + 2 * nI - nH - nK) % 7;
		nM = (nA + 11 * nH + 22 * nL) / 451;

		// [3=March, 4=April]
		nEasterMonth = (nH + nL - 7 * nM + 114) / 31;
		--nEasterMonth;
		nP = (nH + nL - 7 * nM + 114) % 31;

		// Date in Easter Month.
		nEasterDay = nP + 1;

		// Uncorrect for our earlier correction.
		if (uncorrect) {
			nYear -= 1900;
		}

		// Populate the date object...
		return getDate(nYear, nEasterMonth, nEasterDay);
	}

	public static Date getEasterMonday(int nYear) {
		int nEasterMonth = 0;
		int nEasterDay = 0;
		int nMonthMarch = 2; // March
		int nMonthApril = 3; // April
		Date dEasterSunday = getEasterSunday(nYear);
		nEasterMonth = getMonth(dEasterSunday);
		nEasterDay = getDayOfMonth(dEasterSunday);
		if (nEasterMonth == nMonthMarch || nEasterDay == 31) {
			return getDate(nYear, nMonthApril, 1);
		} else {
			return getDate(nYear, nEasterMonth, ++nEasterDay);
		}
	}
	
	public static Date getFlowersDay(int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(getEasterSunday(year));
		c.add(Calendar.DAY_OF_MONTH, -7);
		return c.getTime();
	}
	
	public static Date getRusaliiDay(int year) {		
		// 50 days from saturday before easter
		Calendar c = Calendar.getInstance();
		c.setTime(getEasterSunday(year));
		c.add(Calendar.DAY_OF_MONTH, 49);
		return c.getTime();
	}

	public static Date getMemorialDayObserved(int nYear) {
		// Last Monday in May
		Date dtD = getDate(nYear, Calendar.MAY, 31);
		int nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, Calendar.MAY, 25);
		case 1: // Monday
			return getDate(nYear, Calendar.MAY, 31);
		case 2: // Tuesday
			return getDate(nYear, Calendar.MAY, 30);
		case 3: // Wednesday
			return getDate(nYear, Calendar.MAY, 29);
		case 4: // Thrusday
			return getDate(nYear, Calendar.MAY, 28);
		case 5: // Friday
			return getDate(nYear, Calendar.MAY, 27);
		default: // Saturday
			return getDate(nYear, Calendar.MAY, 26);
		}
	}

	public static Date getIndependenceDayObserved(int nYear) {		
		Date dtD = getDate(nYear, Calendar.JULY, 4);
		int nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, Calendar.JULY, 5);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thrusday
		case 5: // Friday
			return getDate(nYear, Calendar.JULY, 4);
		default:
			// Saturday
			return getDate(nYear, Calendar.JULY, 3);
		}
	}

	public static Date getIndependenceDay(int nYear) {		
		return getDate(nYear, Calendar.JULY, 4);
	}

	public static Date CanadianCivicHoliday(int nYear) {
		// First Monday in August
		int nX;
		int nMonth = 7; // August
		Date dtD;

		dtD = getDate(nYear, nMonth, 1);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 2);
		case 1: // Monday
			return getDate(nYear, nMonth, 1);
		case 2: // Tuesday
			return getDate(nYear, nMonth, 7);
		case 3: // Wednesday
			return getDate(nYear, nMonth, 6);
		case 4: // Thrusday
			return getDate(nYear, nMonth, 5);
		case 5: // Friday
			return getDate(nYear, nMonth, 4);
		default: // Saturday
			return getDate(nYear, nMonth, 3);
		}
	}

	public static Date LaborDayObserved(int nYear) {
		// The first Monday in September
		int nX;
		int nMonth = 8; // September
		Date dtD;

		dtD = getDate(nYear, 9, 1);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 2);
		case 1: // Monday
			return getDate(nYear, nMonth, 7);
		case 2: // Tuesday
			return getDate(nYear, nMonth, 6);
		case 3: // Wednesday
			return getDate(nYear, nMonth, 5);
		case 4: // Thrusday
			return getDate(nYear, nMonth, 4);
		case 5: // Friday
			return getDate(nYear, nMonth, 3);
		default: // Saturday
			return getDate(nYear, nMonth, 2);
		}
	}

	public static Date ColumbusDayObserved(int nYear) {
		// Second Monday in October
		int nX;
		int nMonth = 9; // October
		Date dtD;

		dtD = getDate(nYear, nMonth, 1);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 9);
		case 1: // Monday
			return getDate(nYear, nMonth, 15);
		case 2: // Tuesday
			return getDate(nYear, nMonth, 14);
		case 3: // Wednesday
			return getDate(nYear, nMonth, 13);
		case 4: // Thrusday
			return getDate(nYear, nMonth, 12);
		case 5: // Friday
			return getDate(nYear, nMonth, 11);
		default: // Saturday
			return getDate(nYear, nMonth, 10);
		}

	}

	public static Date Halloween(int nYear) {
		int nMonth = 9;
		// October 31st
		return getDate(nYear, nMonth, 31);
	}

	public static Date USElectionDay(int nYear) {
		// First Tuesday in November
		int nX;
		int nMonth = 10; // November
		Date dtD;

		dtD = getDate(nYear, nMonth, 1);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 3);
		case 1: // Monday
			return getDate(nYear, nMonth, 2);
		case 2: // Tuesday
			return getDate(nYear, nMonth, 1);
		case 3: // Wednesday
			return getDate(nYear, nMonth, 7);
		case 4: // Thrusday
			return getDate(nYear, nMonth, 6);
		case 5: // Friday
			return getDate(nYear, nMonth, 5);
		default: // Saturday
			return getDate(nYear, nMonth, 4);
		}
	}

	public static Date VeteransDayObserved(int nYear) {
		// November 11th
		int nMonth = 10; // November
		return getDate(nYear, nMonth, 11);
	}

	public static Date RememberenceDayObserved(int nYear) {
		// Canadian version of Veterans Day
		return VeteransDayObserved(nYear);
	}

	public static Date ThanksgivingObserved(int nYear) {
		int nX;
		int nMonth = 10; // November
		Date dtD;

		dtD = getDate(nYear, nMonth, 1);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 26);
		case 1: // Monday
			return getDate(nYear, nMonth, 25);
		case 2: // Tuesday
			return getDate(nYear, nMonth, 24);
		case 3: // Wednesday
			return getDate(nYear, nMonth, 23);
		case 4: // Thrusday
			return getDate(nYear, nMonth, 22);
		case 5: // Friday
			return getDate(nYear, nMonth, 28);
		default: // Saturday
			return getDate(nYear, nMonth, 27);
		}
	}

	public static Date ChristmasDayObserved(int nYear) {
		int nX;
		int nMonth = 11; // December
		Date dtD;

		dtD = getDate(nYear, nMonth, 25);
		nX = getDayOfWeek(dtD);
		switch (nX) {
		case 0: // Sunday
			return getDate(nYear, nMonth, 26);
		case 1: // Monday
		case 2: // Tuesday
		case 3: // Wednesday
		case 4: // Thrusday
		case 5: // Friday
			return getDate(nYear, nMonth, 25);
		default:
			// Saturday
			return getDate(nYear, nMonth, 24);
		}
	}

	public static Date ChristmasDay(int nYear) {		
		return getDate(nYear, Calendar.DECEMBER, 25);
	}	

	public static Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Date getLastFriday(int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month + 1, 1);
		cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_WEEK) % 7 + 1));
		return cal.getTime();
	}		
	
	public static void main(String[] args) {
		for (int i=2008; i<2022; i++) {
			System.out.println("easter sunday for " + i + " = " + getEasterSunday(i));
			System.out.println("    florii = " + getFlowersDay(i));
			System.out.println("    rusalii = " + getRusaliiDay(i));
		}
	}

}
