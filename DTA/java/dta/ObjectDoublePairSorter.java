package dta;

import java.util.Comparator;

public class ObjectDoublePairSorter implements Comparator<ObjectDoublePair>{

	public boolean descending = false;

	public ObjectDoublePairSorter(){
		descending = false;
	}

	public ObjectDoublePairSorter(boolean inDescending){
		descending = inDescending;
	}

	@Override
	public int compare(ObjectDoublePair o1, ObjectDoublePair o2) {
		if(descending){
			return new Double(o2.weight).compareTo(new Double(o1.weight));
		} else {
			return new Double(o1.weight).compareTo(new Double(o2.weight));
		}
	}
}
