package model;

import java.util.Comparator;
import java.util.HashSet;

import circle.*;
import feature.*;

public class MFCSecondPassComparator implements Comparator<Integer> {

   private boolean ascending;
   private Integer owner;

   public MFCSecondPassComparator(){
      ascending = true;
      owner = new Integer(-1);
   }

   public MFCSecondPassComparator(boolean inAscending, Integer inOwner){
      ascending = inAscending;
      owner = inOwner;
   }

   @Override
	public int compare(Integer o1, Integer o2) {
	   int numFeats1 = 0, numFeats2 = 0;
	   int numFriends1 = 0, numFriends2 = 0;

	   numFeats1 = getNumFeatures(o1);
	   numFeats2 = getNumFeatures(o2);

	   numFriends1 = Main.egoNet.getEgonet(owner).numFriendsInEgonet(o1.intValue());
	   numFriends2 = Main.egoNet.getEgonet(owner).numFriendsInEgonet(o2.intValue());

	   if(ascending){
			if(numFeats1==numFeats2){
			   return numFriends1 - numFriends2;
			} else return numFeats1 - numFeats2;
	   } else {
	      if(numFeats1==numFeats2){
	         return numFriends2 - numFriends1;
			} else return numFeats2 - numFeats1;
	   }
	}

	private int getNumFeatures(Integer user){
	   return getNumFeaturesByType(user, FeatureEducationClass.class)
	        + getNumFeaturesByType(user, FeatureLanguage.class)
	        + getNumFeaturesByType(user, FeatureWorkEmployer.class)
	        + getNumFeaturesByType(user, FeatureWorkLocation.class)
	        ;
	}

	private int getNumFeaturesByType(Integer user, Class featType){
	   HashSet<Feature> s = Main.userFeatures.get(user).getFeaturesByType(featType);
	   if(s==null){
	      return 0;
	   } else return s.size();
	}

}