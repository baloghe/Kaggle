package santa;

import java.util.Comparator;

public class ToySortByDuration implements Comparator<Toy>{

	public int compare(Toy o1, Toy o2) {
		long l1 = o1.getDurationMinute();
		long l2 = o2.getDurationMinute();
		if(l1 < l2) return -1;
		else if(l1 > l2) return 1;
		else return 0;
	}
	
}