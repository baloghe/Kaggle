package santa;

import java.util.Comparator;

public class SortToyWrpEstComplDate implements Comparator<ToyWrpEstComplDate> {

	public int compare(ToyWrpEstComplDate o1, ToyWrpEstComplDate o2) {
		return o1.estComplDate.compareTo(o2.estComplDate);
	}
	
}
