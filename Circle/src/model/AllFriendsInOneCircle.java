package model;

import java.util.ArrayList;

import circle.Main;
import circle.UserCircle;

public class AllFriendsInOneCircle extends Model {

	public static String MODEL_NAME = "ALL_FRIENDS_IN_ONE_CIRCLE";

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();
		UserCircle c = new UserCircle(user);

		for(Integer friend : Main.egoNet.getEgonet(user).getFriends()){
			c.addMember(friend);
		}
		ret.add(c);

		return ret;
	}

}
