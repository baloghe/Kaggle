package santa;

import java.util.Comparator;

public class ToySortByOrderTime implements Comparator<Toy>{

	public int compare(Toy o1, Toy o2) {
		return o1.getOderTime().compareTo(o2.getOderTime());
	}

}
