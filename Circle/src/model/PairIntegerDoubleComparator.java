package model;

import java.util.Comparator;

public final class PairIntegerDoubleComparator implements Comparator<PairIntegerDouble> {

	public boolean REVERSE_ORDERED;

	public PairIntegerDoubleComparator(){REVERSE_ORDERED = true;}

	public PairIntegerDoubleComparator(boolean ordering){
	   REVERSE_ORDERED = ordering;
	}

	@Override
	public int compare(PairIntegerDouble o1, PairIntegerDouble o2) {
	   if(REVERSE_ORDERED){
			//reverse ordering
			return o2.compareTo(o1);
	   } else return o1.compareTo(o2);
	}

}