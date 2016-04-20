package dta;

import java.util.Comparator;

public class IntDoublePairSorter implements Comparator<IntDoublePair>{

	@Override
	public int compare(IntDoublePair o1, IntDoublePair o2) {
		return new Double(o1.d).compareTo(new Double(o2.d));
	}

}
