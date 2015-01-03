package santa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class Scheduler {
	
	public static final String ELF_FIRST_AVALABLE_AT = "2014 01 01 09 00";
	public static final String TECHNICAL_ENDDATE = "2016 01 01 00 01";
	public static final String DATE_FORMAT = "yyyy MM dd HH mm";
	public static final int FIRST_SANC_HOUR = 9;
	public static final int LAST_SANC_HOUR = 19;
	public static final int SANCPERIOD_IN_MINUTES = 600;
	public static final int UNSANCPERIOD_IN_MINUTES = 840;
	public static final int CALENDAR_DAY_IN_MINUTES = 1440;
	public static int ELF_NUMBER;
	
	public static Calendar schCalendar;
	public static SimpleDateFormat dateFormat;
	public static Date elfFirstAvailableAt;
	public static Date technicalEndDate;
	
	public static boolean elfStartWorkInUnsanc;
	
	public static void init(){
		schCalendar = new GregorianCalendar();
		try{
			dateFormat = new SimpleDateFormat(Scheduler.DATE_FORMAT);
			elfFirstAvailableAt = dateFormat.parse(Scheduler.ELF_FIRST_AVALABLE_AT);
			technicalEndDate = dateFormat.parse(Scheduler.TECHNICAL_ENDDATE);
			elfStartWorkInUnsanc = false;
		} catch(ParseException e){
			System.out.println("Scheduler :: ParseException with format= " + dateFormat + ", date=" + Scheduler.ELF_FIRST_AVALABLE_AT);
			e.printStackTrace();
		}
	}
	
	public static long[] splitDurationMinute(Date startTime, long durationMinute){
		Calendar cal = new GregorianCalendar();
		cal.setTime(startTime);
		
		long sanc = 0, unsanc = 0;
		
		if(Scheduler.isSanctioned(startTime)){
			
			Calendar caleod = new GregorianCalendar();
			caleod.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			caleod.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			caleod.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			caleod.set(Calendar.HOUR_OF_DAY, Scheduler.LAST_SANC_HOUR);
			caleod.set(Calendar.MINUTE, 0);
			caleod.set(Calendar.SECOND, 0);
			caleod.set(Calendar.MILLISECOND, 0);
			
			long d1 = (caleod.getTimeInMillis() - startTime.getTime()) / 60000;
			
			sanc = durationMinute;
			unsanc = 0;
			if(d1 < durationMinute){
				sanc = d1;
				//determine entire Sancioned+Unsanctioned periods
				long k = (durationMinute - d1) / Scheduler.CALENDAR_DAY_IN_MINUTES;
				long remainder = durationMinute - d1 - k * Scheduler.CALENDAR_DAY_IN_MINUTES;
				sanc += (k * Scheduler.SANCPERIOD_IN_MINUTES);
				unsanc += (k * Scheduler.UNSANCPERIOD_IN_MINUTES);
				//now we consumed the rest of the sanctioned period, if any
				if(remainder < Scheduler.UNSANCPERIOD_IN_MINUTES){
					unsanc += remainder;
				} else {
					unsanc += Scheduler.UNSANCPERIOD_IN_MINUTES;
					remainder -= Scheduler.UNSANCPERIOD_IN_MINUTES;
					sanc += remainder;
				}
				// sanc and unsanc properly calculated
			}
		} // endIf isSanctioned(startTime)
		else {
			Date nextSancTime = Scheduler.nextSactionedMinute(startTime);
			
			Calendar cal2 = new GregorianCalendar();
			cal2.setTime(nextSancTime);
			
			Calendar calbod = new GregorianCalendar();
			calbod.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
			calbod.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
			calbod.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));
			calbod.set(Calendar.HOUR_OF_DAY, Scheduler.FIRST_SANC_HOUR);
			calbod.set(Calendar.MINUTE, 0);
			calbod.set(Calendar.SECOND, 0);
			calbod.set(Calendar.MILLISECOND, 0);
			
			Calendar caleod = new GregorianCalendar();
			caleod.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
			caleod.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
			caleod.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));
			caleod.set(Calendar.HOUR_OF_DAY, Scheduler.LAST_SANC_HOUR);
			caleod.set(Calendar.MINUTE, 0);
			caleod.set(Calendar.SECOND, 0);
			caleod.set(Calendar.MILLISECOND, 0);
			
			long d0 = (calbod.getTimeInMillis() - startTime.getTime()) / 60000;
			long d1 = Scheduler.SANCPERIOD_IN_MINUTES;
			
			unsanc = durationMinute;
			sanc = 0;
			
			if(d0 < durationMinute){
				long durationMinute0 = durationMinute - d0;
				if(durationMinute0 < d1){
					sanc = durationMinute0;
				} else {
					sanc += d1;
					
					//determine entire Sancioned+Unsanctioned periods
					long k = (durationMinute0 - d1) / Scheduler.CALENDAR_DAY_IN_MINUTES;
					long remainder = durationMinute0 - d1 - k * Scheduler.CALENDAR_DAY_IN_MINUTES;
					sanc += (k * Scheduler.SANCPERIOD_IN_MINUTES);
					unsanc += (k * Scheduler.UNSANCPERIOD_IN_MINUTES);
					//now we consumed the rest of the sanctioned period, if any
					if(remainder < Scheduler.UNSANCPERIOD_IN_MINUTES){
						unsanc += remainder;
					} else {
						unsanc += Scheduler.UNSANCPERIOD_IN_MINUTES;
						remainder -= Scheduler.UNSANCPERIOD_IN_MINUTES;
						sanc += remainder;
					}
				}
			}
			
		} // endIf startTime is NOT sanctioned
		
		long[] ret = new long[]{sanc,unsanc};
		
		return ret;
	}
	
	public static Date nextSactionedMinute(Date inStartDate){
		Calendar cal = new GregorianCalendar();
		cal.setTime(inStartDate);
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int mn = cal.get(Calendar.MINUTE);
		
		if(hr < Scheduler.FIRST_SANC_HOUR){
			Calendar calbod = new GregorianCalendar();
			calbod.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			calbod.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			calbod.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			calbod.set(Calendar.HOUR_OF_DAY, Scheduler.FIRST_SANC_HOUR);
			calbod.set(Calendar.MINUTE, 0);
			calbod.set(Calendar.SECOND, 0);
			calbod.set(Calendar.MILLISECOND, 0);
			return calbod.getTime();
		} else if(hr >= Scheduler.LAST_SANC_HOUR 
				|| (hr == (Scheduler.LAST_SANC_HOUR-1) && mn == 59) ){
			Calendar calbod = new GregorianCalendar();
			calbod.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			calbod.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			calbod.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
			calbod.set(Calendar.HOUR_OF_DAY, Scheduler.FIRST_SANC_HOUR);
			calbod.set(Calendar.MINUTE, 0);
			calbod.set(Calendar.SECOND, 0);
			calbod.set(Calendar.MILLISECOND, 0);
			return calbod.getTime();
		} else {
			return inStartDate;
		}
		
	}
	
	public static Date addLongToDate(Date startTime, long minutesPassed){
		Calendar cal = new GregorianCalendar();
		cal.setTime(startTime);
		cal.add(Calendar.MINUTE, (int)minutesPassed);
		return cal.getTime();
	}
	
	public static long getRealDuration(long durMin, double productivity){
		return (long)Math.ceil( (double)durMin / productivity );
	}
	
	public static boolean isSanctioned(Date d){
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		int mn = cal.get(Calendar.MINUTE);
		
		return (   (hr >= Scheduler.FIRST_SANC_HOUR && hr < Scheduler.LAST_SANC_HOUR)
		        || (hr == Scheduler.LAST_SANC_HOUR && mn <= 59)
		       );
	}
}
