package test;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TstAny {
	public static void main(String[] args){
		testDate3();
	}
	
	public static void testDate(){
		String dateFormat = "yyyy MM dd HH mm";
		String inDate0 = "2014 3 29 16 52";
		String inDate1 = "2014 3 29 17 52";
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date d0 = sdf.parse(inDate0);
			Date d1 = sdf.parse(inDate1);
			long diffMinute = (d1.getTime() - d0.getTime()) / 60000;
			System.out.println("Minutes passed = " + diffMinute);
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
	}
	
	public static void testDate2(){
		String dateFormat = "yyyy MM dd HH mm";
		String inDate0 = "2014 3 29 00 00";
		String inDate1 = "2014 3 29 09 00";
		String inDate2 = "2014 3 29 19 00";
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date d0 = sdf.parse(inDate0);
			Date d1 = sdf.parse(inDate1);
			Date d2 = sdf.parse(inDate2);
			long diffMinute01 = (d1.getTime() - d0.getTime()) / 60000;
			long diffMinute02 = (d2.getTime() - d0.getTime()) / 60000;
			System.out.println("Minutes passed 01 = " + diffMinute01);
			System.out.println("Minutes passed 02 = " + diffMinute02);
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
	}
	
	public static void testDate3(){
		String dateFormat = "yyyy MM dd HH mm";
		String inDate1 = "2014 3 29 16 52";
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			
			Date d1 = sdf.parse(inDate1);
			Calendar cal = new GregorianCalendar();
			cal.setTime(d1);
			
			Calendar caleod = new GregorianCalendar();
			caleod.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			caleod.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			caleod.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			caleod.set(Calendar.HOUR_OF_DAY, 19);
			caleod.set(Calendar.MINUTE, 0);
			caleod.set(Calendar.SECOND, 0);
			caleod.set(Calendar.MILLISECOND, 0);
			
			long diffMinute = (caleod.getTimeInMillis() - d1.getTime()) / 60000;
			System.out.println("Minutes until 19:00 = " + diffMinute);
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
	}
}
