package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import circle.FeatureRepo;
import circle.Main;
import circle.UserCircle;
import feature.*;

public class AllSchoolsInOneCircle extends Model {

	public static String MODEL_NAME = "ALL_SCHOOLS_IN_ONE_CIRCLE";

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();
		UserCircle c = new UserCircle(user);

		//obtain friends list
		HashSet<Integer> friends = Main.egoNet.getEgonet(user).getFriends();

		//obtain schools list
		HashSet<Feature> schools = Main.userFeatures.get(user).getFeaturesByType(FeatureEducationSchool.class);

		HashMap<Integer, HashSet<Integer>> schoolmateList = new HashMap<Integer, HashSet<Integer>>();
		for(Feature school : schools){
			HashSet<Integer> schoolmates = new HashSet<Integer>();
			for(Integer friend : friends){
				if(FeatureRepo.general_feature_repo.get(school).contains(friend)){
					schoolmates.add(friend);
				}
			}//next friend
			schoolmateList.put(school.getID(), schoolmates);
		}//next school

		//create the remainder of the friends as a new school with id=-1
		HashSet<Integer> friendsCopy = new HashSet<Integer>();
		friendsCopy.addAll(friends);
		for(HashSet<Integer> aCircle : schoolmateList.values()){
			friendsCopy.removeAll(aCircle);
		}
		schoolmateList.put(new Integer(-1), friendsCopy);
		for(HashSet<Integer> aCircle : schoolmateList.values()){
			UserCircle cc = new UserCircle(user);
			cc.addMembers(aCircle);
			ret.add(cc);
		}

		return ret;
	}

}
