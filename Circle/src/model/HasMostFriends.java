package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.PriorityQueue;

import circle.*;
import feature.*;

public final class HasMostFriends extends Model {

   public static String MODEL_NAME = "HAS_MOST_FRIENDS";

   public static boolean LOG_ENABLED = false;

   private int maxMembers;

   public HasMostFriends(int inMaxMembers){
      maxMembers = inMaxMembers;
   }

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
	   ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

	   UserCircle c = new UserCircle(user);
	   UserCircle c2 = new UserCircle(user);
	   Integer[] pops = getMaxFriendsUsers(user);
	   for(int i=0; i<pops.length; i++){
	      c.addMember(pops[i]);
	      c2.addMember(pops[i]);
	   }
	   ret.add(c);
	   //ret.add(c2);

	   //System.out.println("HasMostFriends :: " + c.toString());
	   /*
	   UserCircle cc = new UserCircle(user);
	   Integer[] pops = getMaxFriendsUsers(user);
	   for(int i=0; i<pops.length; i++){
	      UserCircle c = new UserCircle(user);
	      c.addMember(pops[i]);
	      ret.add(c);

	      cc.addMember(pops[i]);
	   }
	   ret.add(cc);
	   */

		log("HasMostFriends :: " + c);

	   return ret;
	}

	private Integer[] getMaxFriendsUsers(Integer owner){
	   UserEgonet enet = Main.egoNet.getEgonet(owner);
	   PriorityQueue<PairIntegerDouble> pq = new PriorityQueue<PairIntegerDouble>(enet.getFriendsNumber(), new PairIntegerDoubleComparator());
	   for(Integer friend : enet.getFriends()){
	      HashMap<Integer, HashSet<Integer>> rels = Main.egoNet.getEgonet(owner).getRelations();
	      double fnum = 0.0;
	      if(rels!=null){
	      		HashSet<Integer> fs = rels.get(friend);
	      		if(fs!=null){
	      		   fnum = (double)fs.size();
	      		}
	      }
	      pq.add( new PairIntegerDouble(friend,fnum) );
	   }
	   Integer[] ret = new Integer[maxMembers];
	   for(int i=0; i<maxMembers; i++){
	      ret[i] = pq.poll().key;
	   }
	   return ret;
	}

	private void log(String s){
      if(LOG_ENABLED){System.out.println(s);}
   }
}