package test;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import santa.*;

public class TstScheduler {
	
	public static void main(String[] args){
		Scheduler.init();
		
		testDateAddition();
		testSplit();
		testNextAvalableMin();
		
		testSplit("2014 01 01 10 00", 720);
		testSplit("2014 01 01 18 00", 960);
		testSplit("2014 01 01 10 00", 3600);
		testSplit("2014 01 01 18 00", 3840);
	}
	
	public static void testDateAddition(){
		
		String dateFormat = "yyyy MM dd HH mm";
		String inDate0 = "2014 3 29 16 52";
		
		long minutesToAdd = 10;
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date d0 = sdf.parse(inDate0);
			
			Date d1 = Scheduler.addLongToDate(d0, minutesToAdd);
			
			System.out.println("d0 = " + sdf.format(d0) + ", d1 = d0 + " + minutesToAdd
					+ " = " + sdf.format(d1));
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
		
	}
	
	public static void testSplit(){
		
		String dateFormat = "yyyy MM dd HH mm";
		String inDate0 = "2014 3 29 16 52";
		
		long durMin = 170;
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date d0 = sdf.parse(inDate0);
			
			long[] spl = Scheduler.splitDurationMinute(d0, durMin);
			
			System.out.println("Start = " + sdf.format(d0) + ", durMin = " + durMin
					+ " => sanc = " + spl[0] + ", unsanc = " + spl[1] );
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
	}
	
	public static void testNextAvalableMin(){
		String dateFormat = "yyyy MM dd HH mm";
		String inDate0 = "2014 3 29 16 52";
		String inDate1 = "2014 3 29 19 52";
		String inDate2 = "2014 3 30 07 52";
				
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date d0 = sdf.parse(inDate0);
			Date d1 = sdf.parse(inDate1);
			Date d2 = sdf.parse(inDate2);
			
			System.out.println("d0 = " + sdf.format(d0) + " => " 
					+ sdf.format(Scheduler.nextSactionedMinute(d0)) );
			System.out.println("d1 = " + sdf.format(d1) + " => " 
					+ sdf.format(Scheduler.nextSactionedMinute(d1)) );
			System.out.println("d2 = " + sdf.format(d2) + " => " 
					+ sdf.format(Scheduler.nextSactionedMinute(d2)) );
		} catch(ParseException e){
			System.out.println("ParseException with format= " + dateFormat);
			e.printStackTrace();
		}
	}
	
	public static void testSplit(String startDateStr, long durMin){
		
		Date startDate = null;
		try {
			startDate = Scheduler.dateFormat.parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Elf elf = new Elf(1, 1.0, startDate);
		Toy toy = new Toy(1, startDate, durMin);
		
		System.out.println("testSplit1 BEGIN :: elf=" + elf.toString());
		System.out.println("                    toy=" + toy.toString());
		
		long split[] = Scheduler.splitDurationMinute(startDate, durMin);
		
		System.out.println("   Scheduler.split=[" + split[0] + ", " + split[1] + "]");
		
		elf.assignToy(toy, startDate);
		
		System.out.println("testSplit1 ENDS :: elf=" + elf.toString());
		
	}
}
