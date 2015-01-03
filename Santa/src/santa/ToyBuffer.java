package santa;

import java.util.Collection;
import java.util.HashSet;

public class ToyBuffer {
	
	public static int TOY_BUFFER_CAPACITY = 1000;

	private HashSet<Toy> toySet;

	public ToyBuffer(Collection<Toy> toys){
		toySet = new HashSet<Toy>(toys);
	}
	
	public void addToys(Collection<Toy> toys){
		toySet.addAll(toys);
	}
	
	public boolean isEmpty(){
		return toySet.isEmpty();
	}
	
}
