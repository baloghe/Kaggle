package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.PriorityQueue;

import circle.*;
import feature.*;

public final class MostFriendPlusCrony extends Model {

	public static String MODEL_NAME = "MOST_FRIEND_PLUS_CRONY";

   public static boolean LOG_ENABLED = false;

   private double cbThreshold;

   public MostFriendPlusCrony(double inCbThreshold){
      cbThreshold = inCbThreshold;
   }

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

		HasMostFriends mHmf = new HasMostFriends(1);
		ArrayList<UserCircle> hmf = mHmf.getUserCirclesEstimation(user);
		UserCircle c1 = hmf.get(0);
		Integer mpf = null;
		for(Integer i : c1.getMembers()){
		   mpf = i;
		}

		/*
		ConnectionBased mCb = new ConnectionBased(cbThreshold);
		mCb.calcConnections(user);
		Integer crony = mCb.getMostConnectedFriend(user, mpf);
		*/

		Integer crony = getUserWithMostCommonFriends(user, mpf);

		UserCircle cc = new UserCircle(user);
		cc.addMember(mpf);
		if(crony != null){
		   cc.addMember(crony);
		}

		log("MostFriendPlusCrony :: " + cc);

		ret.add(cc);

		return ret;
	}

	public Integer getUserWithMostCommonFriends(Integer owner, Integer friend){
	   UserEgonet enet = Main.egoNet.getEgonet(owner);
	   HashSet<Integer> ffs = enet.getRelations().get(friend);

	   int maxCF = 0;
	   Integer maxF = null;
	   for(Integer f : ffs){
	      int i = enet.numCommonFriends(friend.intValue() , f.intValue());
	      if( i > maxCF ){
	         maxCF = i;
	         maxF = f;
	      }//endif
	   }//next f

	   return maxF;
	}

   private void log(String s){
      if(LOG_ENABLED){System.out.println(s);}
   }
}