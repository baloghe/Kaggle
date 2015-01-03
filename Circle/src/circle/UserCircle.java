package circle;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import feature.*;

public final class UserCircle{

   private HashSet<Integer> circle;
   private Integer owner_id;

   private HashMap<Feature, Integer> featurevec;

   public UserCircle(Integer owner){
      circle = new HashSet<Integer>();
      owner_id = owner;
   }

   public UserCircle(Integer owner, UserEgonet enet, Feature feat){
	   circle = new HashSet<Integer>();
	   owner_id = owner;

	   //find users with the given feature in the owner's egonet
	   for(Integer friend : enet.getFriends()){
		   HashSet<Feature> feats = Main.userFeatures.get(friend).getFeatures();
		   for(Feature ff : feats){
			   if(ff.equals(feat)){
				   circle.add(friend);
			   }
		   }
	   }
   }

   public Integer getOwnerID(){return owner_id;}

   public HashSet<Integer> getMembers(){return circle;}

   public int getMemberCount(){return circle.size();}

   public void addMember(Integer member){circle.add(member);}
   
   public void remMember(Integer member){circle.remove(member);}

   public void addMembers(Collection<Integer> members){circle.addAll(members);}

   public void calcCommonFeatures(){
	   featurevec = new HashMap<Feature, Integer>();
	   for(Integer user : circle){
		   UserFeatures uf = Main.userFeatures.get(user);
		   for(Feature f : uf.getFeatures()){
		      if( !f.getType().equalsIgnoreCase("feature.FeatureLanguage") ){
				   if(featurevec.containsKey(f)){
					   int cnt = featurevec.get(f).intValue();
					   featurevec.put(f, new Integer(++cnt));
				   } else {
					   featurevec.put(f, new Integer(1));
				   }
		      }//other than Language
		   }//next feature of the user
	   }//next user
   }

   public HashMap<Feature, Integer> getCommonFeatures(){return featurevec;}

   public int getUsersFriendsNumber(Integer newUser){
	   int ret = 0;
	   for(Integer friend : circle){
		   if(friend.intValue() == newUser.intValue()){
			   ret++;
		   }
	   }
	   return ret;
   }

   public boolean isMember(Integer user){
      return circle.contains(user);
   }

   public String toString(){
	   if(circle == null){
		   return "NULL";
	   } else if(circle.isEmpty()){
		   return "EMPTY";
	   } else {
		   String ret = "[";
		   boolean isFirst = true;
		   for(Integer member : circle){
			   if(!isFirst){
				   ret += ",";
			   } else {
				   isFirst = false;
			   }
			   ret += member;
		   }
		   ret += "]";
		   return ret;
	   }
   }
}