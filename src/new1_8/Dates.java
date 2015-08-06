package new1_8;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.TemporalAdjusters.*;

public class Dates {
	
	public static void main(String[] args) {
				
		LocalDate date = LocalDate.of(2014, Month.FEBRUARY, 25); // 2014-02-25		
		// first day of february 2014 (2014-02-01)
		LocalDate firstDayOfMonth = date.with(firstDayOfMonth());		 
		// last day of february 2014 (2014-02-28)
		LocalDate lastDayOfMonth = date.with(lastDayOfMonth());
		// next sunday (2014-03-02)
		LocalDate nextSunday = date.with(next(DayOfWeek.SUNDAY));		
		System.out.println("Day = " + date +  "  " + date.getDayOfWeek());
		System.out.println("First day of month = " + firstDayOfMonth);
		System.out.println("Last day of month = " + lastDayOfMonth);
		System.out.println("Next Sunday = " + nextSunday);
		System.out.println("");
		
		
		// sixtyFourth day of 2014 (2014-03-05)
		date = Year.of(2014).atDay(64);
		System.out.println("64th day of 2014 is = " + date);
		System.out.println("");
		
		
		// periods		 
		LocalDate firstDate = LocalDate.of(2010, 5, 17); // 2010-05-17
		LocalDate secondDate = LocalDate.of(2015, 3, 7); // 2015-03-07
		Period period = Period.between(firstDate, secondDate);		 
		int days = period.getDays(); // 18
		int months = period.getMonths(); // 9
		int years = period.getYears(); // 4	
		long justDays = ChronoUnit.DAYS.between(firstDate, secondDate);
		System.out.println("Period between " + firstDate + " and " + secondDate);		
		System.out.println("years="+years + " months="+months + " days="+days);
		System.out.println("just days = " + justDays);		
		System.out.println("");
		
		
	}

}
