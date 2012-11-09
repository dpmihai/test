package util;

import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Apr 24, 2007
 * Time: 5:43:40 PM
 */
public class DateUtil {

    /** Get number of days between two dates
     *
     *  @param first first date
     *  @param second second date
     *  @return number of days if first date less than second date,
     *       0 if first date is bigger than second date,
     *       1 if dates are the same
     *
     */
      public static int getNumberOfDays(Date first, Date second)
      {
          Calendar c = Calendar.getInstance();
          int result = 0;
          int compare = first.compareTo(second);
          if (compare > 0) return 0;
          if (compare == 0) return 1;

          c.setTime(first);
          int firstDay = c.get(Calendar.DAY_OF_YEAR);
          int firstYear = c.get(Calendar.YEAR);
          int firstDays = c.getActualMaximum(Calendar.DAY_OF_YEAR);

          c.setTime(second);
          int secondDay = c.get(Calendar.DAY_OF_YEAR);
          int secondYear = c.get(Calendar.YEAR);

          // if dates in the same year
          if (firstYear == secondYear)
          {
              result = secondDay-firstDay+1;
          }

          // different years
          else
          {
              // days from the first year
              result += firstDays - firstDay + 1;

              // add days from all years between the two dates years
              for (int i = firstYear+1; i< secondYear; i++)
              {
                  c.set(i,0,0);
                  result += c.getActualMaximum(Calendar.DAY_OF_YEAR);
              }

              // days from last year
              result += secondDay;
          }

          return result;
      }

    /** Get elapsedtime between two dates
     *
     *  @param first first date
     *  @param second second date
     *  @return null if first date is after second date
     *          an integer array of three elemets ( days, hours minutes )
     */
     public static int[] getElapsedTime(Date first, Date second) {
         if (first.compareTo(second) == 1 ) {
            return null;
         }
         int difDays = 0;
         int difHours = 0;
         int difMinutes = 0;

         Calendar c = Calendar.getInstance();
         c.setTime(first);
         int h1 = c.get(Calendar.HOUR_OF_DAY);
         int m1 = c.get(Calendar.MINUTE);

         c.setTime(second);
         int h2 = c.get(Calendar.HOUR_OF_DAY);
         int m2 = c.get(Calendar.MINUTE);

         if (sameDay(first, second)) {
             difHours = h2 - h1;
         } else {
             difDays = getNumberOfDays(first, second)-1;
             if (h1 >= h2) {
                difDays--;
                difHours = (24 - h1) + h2;
             } else {
                difHours = h2 - h1;
             }
         }

         if (m1 >= m2) {
             difHours--;
             difMinutes = (60 - m1) + m2;
         } else {
             difMinutes = m2 - m1;
         }

         int[] result = new int[3];
         result[0] = difDays;
         result[1] = difHours;
         result[2] = difMinutes;
         return result;
     }

    
    public static int getDayHours(Date date, TimeZone tz) {
        Calendar c = Calendar.getInstance(tz);
        c.setTime(date);
        int hours = 24;
        for (int i = 0; i <= 23; i++) {
            c.set(Calendar.HOUR_OF_DAY, i);
            if (c.get(Calendar.HOUR_OF_DAY) > i) {
                // ex: 3 becomes 4 (summer solstice)
                hours--;
            } else if (c.get(Calendar.HOUR_OF_DAY) < i) {
                // autumn equinox
                hours++;
            }
        }
        return hours;
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


    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(2007, 2, 25, 3, 0);  // summer solstice in 2007
        Date d1 = c.getTime();

        System.out.println(d1 + "  hours=" + getDayHours(d1, TimeZone.getTimeZone("Europe/Bucharest")));
//        c.set(2007, 3, 22, 18, 0);
//        Date d2 = c.getTime();
//
//        System.out.println("d1="+d1);
//        System.out.println("d2="+d2);
//        int[] r = getElapsedTime(d1, d2);
//        System.out.println(r[0] + " days " + r[1] + " hours " + r[2] + " minutes");
    }
}
