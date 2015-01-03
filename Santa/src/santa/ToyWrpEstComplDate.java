package santa;

import java.util.Date;

public class ToyWrpEstComplDate {

	public Date estComplDate;
	public Toy toy;
	
	public ToyWrpEstComplDate(Toy toy){
		this.toy = toy;
		this.estComplDate = Scheduler.addLongToDate(this.toy.getOderTime(), this.toy.getDurationMinute());
	}
}
