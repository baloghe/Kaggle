package model;

import java.util.ArrayList;

import circle.*;
import feature.*;

public final class UserIdPlusOne extends Model {

	public static String MODEL_NAME = "USER_ID_PLUS_ONE";

	public UserIdPlusOne(){}

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

		UserCircle cc = new UserCircle( new Integer(user) );
		cc.addMember( new Integer(user+1) );
		ret.add(cc);

    	System.out.println("UserIdPlusOne :: " + cc.toString());

		return ret;
	}
}