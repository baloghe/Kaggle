package santa;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Date;

public class Main {

	private static final String TARGET_FILENAME = "output.csv";
	private static OrderReader ordreader;
	private static PrintWriter writer;
	private static HashSet<Elf> elves;
	private static HashMap<Integer, Elf> elvesMapped;
	//private static int overlap;
	
	//Elf statistics
	private static double minElfProd;
	private static double avgElfProd;
	private static double maxElfProd;
	private static double cntElfProd;
	private static Date minElfNextAvail;
	private static Date maxElfNextAvail;
	
	public static void main(String[] args) {
		
		Scheduler.init();
		
		String fname = args[0];
		int bufsize = new Integer(args[1]).intValue();
		Scheduler.ELF_NUMBER = new Integer(args[2]).intValue();
		//overlap = new Integer(args[3]).intValue();
		
		//Output file
		try{
			writer = new PrintWriter(Main.TARGET_FILENAME, "UTF-8");
			writer.println("ToyId,ElfId,StartTime,Duration"); // header
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		elves = new HashSet<Elf>(Scheduler.ELF_NUMBER);
		elvesMapped = new HashMap<Integer, Elf>();
		for(int i=1; i<=Scheduler.ELF_NUMBER; i++){
			Elf e = new Elf(i);
			elves.add(e);
			elvesMapped.put(new Integer(i), e);
		}
		
		ordreader = new OrderReader(fname, ",");
		
		log("Main.main :: overhead done, numElves=" + elves.size());
		
		int readnum = 1000;
		int cnt = 0;
		while( cnt < readnum && ordreader.hasMoreLines() ){
			applyStrategy(cnt, bufsize);
			log("Main.main :: cnt=" + cnt + " ordReader stats=" + ordreader.getStatistics());
			cnt++;
		}//wend
		
		//finally: close the input and the output
		ordreader.closeReader();
		writer.close();
	}

	public static void log(String s){
		System.out.println(s);
	}
	
	public static void applyStrategy(int cnt, int bufsize){
		//log("Main.applyStrategy :: cnt=" + cnt + ", bufsize=" + bufsize);
		ArrayList<Toy> orders = ordreader.readOrders(bufsize);
		AllocationScheme alloc = new AllocationScheme(orders, elves);
		alloc.allocateGreedy();
		ArrayList<AllocationSchemeElement> sch = alloc.getSchedule();
		log("   Main.applyStrategy :: allocation done, programSize=" + sch.size());
		//We have a schedule, so apply it on the original elves...
		resetElfStat();
		for(AllocationSchemeElement ase : sch){
			Integer elfid = new Integer(ase.getElf().getId());
			Toy t = ase.getToy();
			Elf e = elvesMapped.get(elfid);
			e.assignToy(t, ase.getStartDate());
			//log("   elf " + elfid + " -> toy " + t.getId() + " -> elf=" + e.toString());
			updateElfStat(e);
			
			//write to the output;
			long dur = (long)Math.floor( ( ase.getComplDate().getTime() - ase.getStartDate().getTime() ) / 60000) + 1;
			String outstr = t.getId() + "," + e.getId() 
					+ "," + Scheduler.dateFormat.format(ase.getStartDate()) + "," + dur;
			writer.println(outstr);
			
			//log...
			if( t.getId()==26 || t.getId()==35 || t.getId()==11 || t.getId()==13 ){
				log(" -->> LOG ASE=" + ase.toString());
				log("        elf after assignment=" + e.toString());
			}
		}
		closeElfStat();
		log("      elfStats=" + elfStatToString() );
	}
	
	public static void resetElfStat(){
		minElfProd = Elf.PROD_MAX;
		avgElfProd = -1.0;
		maxElfProd = Elf.PROD_MIN;
		cntElfProd = 0.0;
		minElfNextAvail = Scheduler.technicalEndDate;
		try {
			maxElfNextAvail = Scheduler.dateFormat.parse("2013 12 31 23 59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateElfStat(Elf elf){
		double prod = elf.getProductivity();
		Date avail = elf.getAvailableNext();
		cntElfProd += 1.0;
		avgElfProd += prod;
		if(minElfProd > prod){
			minElfProd = prod;
		}
		if(maxElfProd < prod){
			maxElfProd = prod;
		}
		if(minElfNextAvail.compareTo(avail) > 0){
			minElfNextAvail = avail;
		}
		if(avail.compareTo(maxElfNextAvail) > 0){
			maxElfNextAvail = avail;
		}
	}
	
	public static void closeElfStat(){
		avgElfProd /= cntElfProd;
	}
	
	public static String elfStatToString(){
		String ret = "(" + Scheduler.dateFormat.format(minElfNextAvail) 
				+ " <-> " + Scheduler.dateFormat.format(maxElfNextAvail) 
				+ ") , (" + formatDouble(minElfProd)
				+ " , " + formatDouble(avgElfProd)
				+ " , " + formatDouble(maxElfProd) + ")";
		return ret;
	}
	
	public static String formatDouble(double d){
		return new Double(Math.round(d * 1000.0) / 1000.0).toString();
	}
}
