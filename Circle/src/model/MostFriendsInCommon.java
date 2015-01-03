package model;

import java.util.ArrayList;

import circle.Main;
import circle.UserCircle;
import circle.UserEgonet;
import feature.*;

public class MostFriendsInCommon extends Model {

	public static String MODEL_NAME = "MOST_FRIENDS_IN_COMMON";

	public static boolean LOG_ENABLED = false;

	private int minCommonFeatures;


	public MostFriendsInCommon(int inMinCommonFeatures){
		minCommonFeatures = inMinCommonFeatures;
	   }

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

		//get the two users with the most common friends within the egonet
		UserEgonet enet = Main.egoNet.getEgonet(user);
		Integer mf1 = new Integer(-1);
		Integer mf2 = new Integer(-1);
		int maxCFNum = 0;
		int maxFNum = 0;
		for(Integer f1 : enet.getFriends()){
			for(Integer f2 : enet.getFriends()){
				if(f1.intValue() < f2.intValue()){
					int yy = enet.numCommonFriends(f1, f2);
					if(yy >= maxCFNum){

						int x1 = enet.numFriendsInEgonet(f1);
						int x2 = enet.numFriendsInEgonet(f2);
						int mx = (x1 > x2 ? x1 : x2);

						if(yy > maxCFNum || mx > maxFNum){
							maxCFNum = yy;
							maxFNum = mx;

							mf1 = f1;
							mf2 = f2;
						}
					}
				}
			}//next f2
		}//next f1
		int mx1 = enet.numFriendsInEgonet(mf1);
		int mx2 = enet.numFriendsInEgonet(mf2);
		log("MostFriendsInCommon :: mf1=" + mf1 + ", mf2=" + mf2 + ", maxCFNum=" + maxCFNum);

		//number of common features
		CommonFeaturesVector cfv = new CommonFeaturesVector(user, mf1, mf2);
		int mutEduSchoolNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureEducationSchool.class);
		int mutLangNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureLanguage.class);
		int mutWorkEmpNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureWorkEmployer.class);
		int mutWorkLocNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureWorkLocation.class);
		int cmf = mutEduSchoolNum + mutLangNum + mutWorkEmpNum + mutWorkLocNum;
		log("\tMostFriendsInCommon :: mutEduSchoolNum=" + mutEduSchoolNum
				+ ", mutLangNum=" + mutLangNum + ", mutWorkEmpNum=" + mutWorkEmpNum
				+ ", mutWorkLocNum=" + mutWorkLocNum + ", cmf=" + cmf);

		//construct circle
		UserCircle c = new UserCircle(user);
		if(mx1 < mx2){
			c.addMember(mf2);
		} else {
			c.addMember(mf1);
		}
		if(cmf > this.minCommonFeatures){
			if(c.isMember(mf2)){
				c.addMember(mf1);
			} else {
				c.addMember(mf2);
			}
		}
		ret.add(c);

		log("\tc="+c);

		return ret;
	}

	private void log(String s){
	      if(LOG_ENABLED){System.out.println(s);}
	   }
}
