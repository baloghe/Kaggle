package santa;

import java.util.Date;

public class Elf {
	
	public static final double STARTING_PRODUCTIVITY = 1.0;
	public static final double PROD_INCREASE = 1.02;
	public static final double PROD_DECREASE = 0.90;
	public static final double PROD_MIN = 0.25;
	public static final double PROD_MAX = 4.0;
	
	private int id;
	private double productivity;
	private Date availableNext;
	
	public Elf(int inId){
		this.id = inId;
		productivity = Elf.STARTING_PRODUCTIVITY;
		availableNext = Scheduler.elfFirstAvailableAt;
	}
	
	public Elf(int inId, double startProd){
		this.id = inId;
		productivity = startProd;
		availableNext = Scheduler.elfFirstAvailableAt;
	}
	
	public Elf(int inId, double startProd, Date firstAvailNext){
		this.id = inId;
		this.productivity = startProd;
		this.availableNext = firstAvailNext;
	}
	
	public int getId(){return id;}
	
	public double getProductivity(){return productivity;}
	
	public Date getAvailableNext(){return availableNext;}
	
	public double updateProductivity(double sanctionedHrs, double unsanctionedHrs){
		
		double prod = this.productivity 
				* Math.pow(Elf.PROD_INCREASE , sanctionedHrs) 
				* Math.pow(Elf.PROD_DECREASE , unsanctionedHrs);
		
		if(prod < Elf.PROD_MIN){
			this.productivity = Elf.PROD_MIN;
		} else if(prod > Elf.PROD_MAX){
			this.productivity = Elf.PROD_MAX;
		} else {
			this.productivity = prod;
		}
		return productivity;
	}
	
	public void assignToy(Toy toy, Date startDate){
		long durMin = toy.getDurationMinute();
		//long durMinReal = (long)Math.ceil( (double)durMin / (double)this.productivity ); 
		long durMinReal = Scheduler.getRealDuration(durMin, this.productivity);
		long[] split = Scheduler.splitDurationMinute(startDate, durMinReal);
		if(split[1] == 0){
			//completion in Sanctioned interval
			this.availableNext = Scheduler.addLongToDate(startDate, durMinReal);
		} else {
			//Unsanctioned interval has been used
			//  complete the work first
			this.availableNext = Scheduler.addLongToDate(startDate, durMinReal);
			//  look for next day's first operating hour
			this.availableNext = Scheduler.nextSactionedMinute(this.availableNext);
			//  see how many working days are needed to have a proper rest
			long restRemainder = split[1];
			long wdNeeded = (long)Math.floor((double)split[1] / (double)Scheduler.SANCPERIOD_IN_MINUTES);
			if(wdNeeded > 0){
				this.availableNext = Scheduler.addLongToDate(this.availableNext, (long) wdNeeded * Scheduler.CALENDAR_DAY_IN_MINUTES);
				restRemainder -= (wdNeeded * Scheduler.CALENDAR_DAY_IN_MINUTES);
			}
			//  apply rest
			this.availableNext = Scheduler.addLongToDate(this.availableNext, restRemainder);
			//  elf is only available at next sanctioned period...
			this.availableNext = Scheduler.nextSactionedMinute(this.availableNext);
		}
		//update prod
		this.updateProductivity( (double)(split[0] / 60.0) , (double)(split[1] / 60.0));
	}
	
	public Elf copy(){
		return new Elf(this.id, this.productivity, this.availableNext);
	}
	
	public String toString(){
		return "[" + id + ", " + Scheduler.dateFormat.format(availableNext) + ", " + productivity + "]";
	}
}
