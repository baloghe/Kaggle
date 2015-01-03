package santa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Date;

public class AllocationScheme {

	private Date complDate;
	
	private Collection<Elf> origElves;
	private Collection<Toy> origToys;
	
	private PriorityQueue<ToyWrpEstComplDate> qEstComplDate;
	
	private ArrayList<AllocationSchemeElement> program;
	
	public AllocationScheme(Collection<Toy> toys, Collection<Elf> elves){
		log("AllocationScheme :: toys=" + toys.size() + ", elves=" + elves.size());
		
		origElves = elves;
		origToys = toys;
		
		complDate = Scheduler.technicalEndDate;
		program = null;
	}
	
	public static void log(String s){
		//System.out.println(s);
	}
	
	protected void allocateGreedy(){
		//order toys by their estimated completion date
		qEstComplDate = new PriorityQueue<ToyWrpEstComplDate>(origToys.size(), new SortToyWrpEstComplDate());
		for(Toy t : origToys){
			ToyWrpEstComplDate wrp = new ToyWrpEstComplDate(t);
			qEstComplDate.add(wrp);
		}
		log("AllocationScheme.allocateGreedy :: origToys sorted, qEstComplDate=" + qEstComplDate.size());
		//copy elves
		HashSet<Elf> wrkElves = new HashSet<Elf>();
		for(Elf e : origElves){
			wrkElves.add(e.copy());
		}
		log("AllocationScheme.allocateGreedy :: wrkElves created");
		//create empty program
		program = new ArrayList<AllocationSchemeElement>();
		//for each toy assign the elf that is expected to finish the toy first
		log("AllocationScheme.allocateGreedy :: Iteration START");
		while(!qEstComplDate.isEmpty()){
			Toy t = qEstComplDate.poll().toy;
			log("   toy=" + t.toString());
			Date eststrt = Scheduler.technicalEndDate;
			Date estcmpl = Scheduler.technicalEndDate;
			Elf chosenElf = null;
			for(Elf e : wrkElves){
				log("      elf=" + e.toString());
				Date tmpstart = e.getAvailableNext();
				if(t.getOderTime().compareTo(tmpstart) > 0){
					tmpstart = Scheduler.nextSactionedMinute(t.getOderTime());
				}
				//long durMinReal = (long)Math.ceil( (double)t.getDurationMinute() / (double)e.getProductivity() );
				long durMinReal = Scheduler.getRealDuration(t.getDurationMinute(), e.getProductivity());
				Date tmpest = Scheduler.addLongToDate(tmpstart, durMinReal);
				//minsearch + update elf in question
				if(tmpest.compareTo(estcmpl) < 0){
					estcmpl = tmpest;
					eststrt = tmpstart;
					chosenElf = e;
					log("         elf is better, new estcmpl=" + Scheduler.dateFormat.format(estcmpl));
				}
			}//next Elf e
			//add to program
			if(chosenElf != null){
				log("      chosenElf=" + chosenElf.toString());
				chosenElf.assignToy(t, eststrt);
				program.add(new AllocationSchemeElement(chosenElf, t, eststrt));
				//update overall scheme complDate
				if(this.complDate.compareTo(estcmpl) < 0){
					this.complDate = estcmpl;
				}
			} else {
				System.out.println("AllocationScheme.allocateGreedy() :: no elf for toy=" + t.toString());
			}
		}//next Toy t
		
		//finally: sort program by StartTime
		Collections.sort(program, new ASESortByStartDate());
	}
	
	public Date getComplDate(){return complDate;}
	
	public ArrayList<AllocationSchemeElement> getSchedule(){return program;}
}