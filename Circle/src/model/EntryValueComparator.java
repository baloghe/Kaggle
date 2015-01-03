package model;

import java.util.Comparator;
import java.util.Map.Entry;

public final class EntryValueComparator<K,V extends Comparable> implements Comparator<Entry<K,V>> {

   	@Override
	public int compare(Entry<K,V> o1, Entry<K,V> o2) {
		//reverse ordering
		return (o2.getValue()).compareTo(o1.getValue());
	}

}