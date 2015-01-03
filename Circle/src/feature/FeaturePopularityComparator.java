package feature;

import java.util.Comparator;
import java.util.Map.Entry;

public class FeaturePopularityComparator implements Comparator<Entry<Feature,Integer>> {

	@Override
	public int compare(Entry<Feature, Integer> o1, Entry<Feature, Integer> o2) {
		//reverse ordering
		return o2.getValue().compareTo(o1.getValue());
	}

}
