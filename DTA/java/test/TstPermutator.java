package test;

import java.util.ArrayList;

import dta.*;

public class TstPermutator {

	public static void main(String[] args) {
		Integer[] orig = new Integer[] {
				 new Integer(5)
				,new Integer(10)
				,new Integer(15)
				,new Integer(20)
			};
		
		ArrayList<Integer> origList = new ArrayList<Integer>();
		int cnt = 0;
		for(Integer n : orig){
			origList.add(orig[cnt]);
			cnt++;
		}
		
		System.out.println("orig:");
		print(orig);
		
		Integer[] perm = Main.getPermutation(origList);
		System.out.println("perm:");
		print(perm);
	}
	
	public static void print(Integer[] arr){
		String s = "[";
		int cnt = 0;
		for(Integer n : arr){
			if(cnt == 0){
				s += n;				
			} else {
				s += (", " + n);
			}
			cnt++;
		}
		s += "]";
		System.out.println(s);
	}

}
