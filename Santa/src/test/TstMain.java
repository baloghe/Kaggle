package test;

import santa.Main;
/*
import santa.Scheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
*/

public class TstMain {
	
	private static long startTime;

	public static void main(String[] args) {
		/*
		System.out.println("1.012345 = " + Main.formatDouble(1.012345));
		Scheduler.init();
		
		long realdur = Scheduler.getRealDuration(1, 1.044);
		System.out.println("realdur = " + realdur);
		
		try {
			Date tstStart = Scheduler.dateFormat.parse("2014 10 10 09 00");
			Date tstEnd = Scheduler.addLongToDate(tstStart, 1);
			System.out.println("tstStart=" + Scheduler.dateFormat.format(tstStart)
					+ " + realdur=" + realdur + " == "
					+ "tstEnd=" + Scheduler.dateFormat.format(tstEnd));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
		startTime = System.currentTimeMillis();
		
		//fileName, bufSize, elfNum
		Main.main(new String[]{
				  	 "toys_rev2.csv"
				 	,"10000"
				 	,"900"
			      });
		
		//problematic toy...: 7419306,2014 10 28 13 1,22232
		
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 60000;
		System.out.println("TstMain :: duration = " + duration + " min");
	}
	
}
