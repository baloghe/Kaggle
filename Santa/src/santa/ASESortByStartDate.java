package santa;

import java.util.Comparator;

public class ASESortByStartDate implements Comparator<AllocationSchemeElement> {

	public int compare(AllocationSchemeElement o1, AllocationSchemeElement o2) {
		return o1.getStartDate().compareTo(o2.getStartDate());
	}
	
}