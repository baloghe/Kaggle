package santa;

import java.util.Date;

public class Toy {

	private int id;
	private Date orderTime;
	private long durationMinute;
	
	public Toy(int inId, Date inOrdTime, long inDurationMinute){
		id = inId;
		orderTime = inOrdTime;
		durationMinute = inDurationMinute;
	}
	
	public int getId(){return id;}
	public Date getOderTime(){return orderTime;}
	public long getDurationMinute(){return durationMinute;}
	
	public String toString(){
		return "[" + id + ", " + Scheduler.dateFormat.format(orderTime) + ", " + durationMinute + "]";
	}
}
