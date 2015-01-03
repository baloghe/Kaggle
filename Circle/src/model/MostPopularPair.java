package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.PriorityQueue;

import circle.*;
import feature.*;

public final class MostPopularPair extends Model {

   public static String MODEL_NAME = "MOST_POPULAR_PAIR";

   public MostPopularPair(){}

   public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

		ConnectionBased cb = new ConnectionBased(0.0);
		cb.calcConnections(user);

		Integer[] pops = cb.getMostPopularPair(user);
		UserCircle c = new UserCircle(user);
		c.addMember(pops[0]);
		c.addMember(pops[1]);
		ret.add(c);

		System.out.println("MostPopularPair :: " + c.toString());

		return ret;
	}
}