package model;

import java.util.ArrayList;

import circle.UserCircle;

public class NoCircles extends Model {
	public static String MODEL_NAME = "NO_CIRCLES";

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();
		UserCircle c = new UserCircle(user);

		ret.add(c);

		return ret;
	}
}
