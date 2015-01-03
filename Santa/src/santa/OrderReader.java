package santa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class OrderReader {

	private String sep;	
	private BufferedReader inputReader;
	private boolean moreLines;
	
	private Date lastOrderDate;
	private long maxDuration;
	
	public OrderReader(String fileName, String sep){
		
		this.sep = sep;
		inputReader = null;
		lastOrderDate = null;
		try {
			lastOrderDate = Scheduler.dateFormat.parse("2013 12 31 23 59");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		maxDuration = 0;
		
		try {
			inputReader =  new BufferedReader(new FileReader(fileName));
			moreLines = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
	}
	
	public void closeReader(){
		try {
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Toy> readOrders(int numLines){
		
		ArrayList<Toy> ret = new ArrayList<Toy>();
		int cnt = 0;
		try {
            String line = inputReader.readLine(); //drop header
            while (( line = inputReader.readLine()) != null 
            		&& cnt < numLines){
                ret.add(createToy(line, sep));
                cnt++;
            } 
            if(line==null){
            	moreLines = false;
            }
            return ret;
        } catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	
	}
	
	public Toy createToy(String ordLine, String sep){
		String[] strvec = ordLine.split(sep);
		int id = new Integer(strvec[0]).intValue();
		Date orderTime = null;
		long durationMinute = new Long(strvec[2]).longValue();
		if(durationMinute > this.maxDuration){
			this.maxDuration = durationMinute;
		}
		try {
			orderTime = Scheduler.dateFormat.parse(strvec[1]);
			if(orderTime.compareTo(this.lastOrderDate) > 0){
				this.lastOrderDate = orderTime;
			}
			return new Toy(id, orderTime, durationMinute);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean hasMoreLines(){return moreLines;}
	
	public String getStatistics(){
		String ret = "last orderDate=" + Scheduler.dateFormat.format(this.lastOrderDate)
				   + ", max duration=" + this.maxDuration;
		return ret;
	}
	
	public static void log(String s){
		System.out.println(s);
	}
}
