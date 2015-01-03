package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import common.*;
import circle.*;

public class TstGetCirclesNum {
	public static void main(String[] args) {
		UserCircles ucs = new UserCircles();
		ucs.readFolder("Circles", " ");
		HashMap<Integer,Integer> circ = ucs.getCircleNumber();
		for(Entry<Integer, Integer> e : circ.entrySet()){
			System.out.println(e.getKey() + " " + e.getValue());
		}

		UserCircles.writeToFile("UserCircles.csv", ucs.getAllCircles(), ",");
	}
}
