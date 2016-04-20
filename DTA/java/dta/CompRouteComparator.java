package dta;

import java.util.Comparator;

public class CompRouteComparator implements Comparator<CompRoute> {
	@Override
	public int compare(CompRoute o1, CompRoute o2) {
		return new Double(o1.dist).compareTo(new Double(o2.dist));
	}
}
