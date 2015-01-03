package santa;

import java.util.Date;

public class AllocationSchemeElement {

	private Date startDate;
	private Date complDate;
	private Elf elf;
	private Toy toy;
	
	public AllocationSchemeElement(Elf inElf, Toy inToy, Date inStartDate){
		this.elf = inElf.copy();
		this.toy = inToy;
		this.startDate = inStartDate;
		long durMinReal = (long)Math.floor( (double)this.toy.getDurationMinute() / (double)this.elf.getProductivity() );
		this.complDate = Scheduler.addLongToDate(this.startDate, durMinReal);
	}
	
	public Date getStartDate(){return startDate;}
	public Date getComplDate(){return complDate;}
	public Elf getElf(){return elf;}
	public Toy getToy(){return toy;}
	
	public String toString(){
		return "[elf=" + elf.getId() + ", toy=" + toy.getId()
				+ ", start=" + Scheduler.dateFormat.format(startDate)
				+ ", compl=" + Scheduler.dateFormat.format(complDate) + "]";
	}
}
