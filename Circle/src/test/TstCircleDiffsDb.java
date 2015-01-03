package test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collection;

import circle.*;
import model.*;

public final class TstCircleDiffsDb{

   public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Main.main(new String[]{"-featuresFile=../features.txt"
				              ,"-egonetsDir=../egonets"
				              ,"-circlesDir=../Circles"
				              //,"-model=HasMostFriends"
				              ,"-model=noSuch"
				              });
		System.out.println("Data loading finished");

		int user=611;
		int[] cms = new int[]{696,639};

		ArrayList<UserCircle> cs = new ArrayList<UserCircle>();
		cs.add(getCircle(user, cms));

		Util.LOG_ENABLED = true;
		int diff = Util.getUserCirclesDifference(Main.userCircles.getCirclesForUser(user) , cs);
		System.out.println("main :: diff=" + diff);

		System.out.println("Test finished");
	}

	public static UserCircle getCircle(int user, int[] members){
	   UserCircle ret = new UserCircle(user);
	   for(int i : members){
	      ret.addMember(new Integer(i));
	   }
	   return ret;
	}

	public static void calcTuples(int tlen, int user){

	}
}