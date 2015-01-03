package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import circle.*;
import feature.*;

public class ByPopularFeatures extends Model {

	public static String MODEL_NAME = "BY_POPULAR_FEATURES";

	public static double CONN_RATIO = 0.8;

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();
		//obtain popular features
		UserEgonet enet = Main.egoNet.getEgonet(user);

		int estCircNum = estimateCircleNum(user);
		if(estCircNum < 1){
			return (new AllSchoolsInOneCircle()).getUserCirclesEstimation(user);
		}

		HashMap<Feature, Integer> popFeats = enet.getPopularFeatures(estCircNum);

		//create a set for all unassigned friends
		HashSet<Integer> unasFriends = new HashSet<Integer>();
		unasFriends.addAll(enet.getFriends());

		//STEP 1:
		//create one circle per feature by adding friends with the same feature
		for(Feature feat : popFeats.keySet()){
			UserCircle newCirc = new UserCircle(user);
			for(Integer friend : enet.getFriends()){
				if( Main.userFeatures.get(friend).getFeatures().contains(feat) ){
					newCirc.addMember(friend);
				}
			}

			//add circle to the list
			ret.add(newCirc);
			//remove its members from the unassigned friends list
			unasFriends.removeAll(newCirc.getMembers());
		}

		//STEP 2:
		//enlist further members based on high connectivity
		for(UserCircle c : ret){
			for(Integer friend1 : enet.getFriends()){
				int connCnt = 0;
				for(Integer friend2 : c.getMembers()){
					if(enet.knowEachOther(friend1, friend2)){
						connCnt++;
					}
				}//next friend2
				//add new member if he/she knows enough existing member
				if( (double)connCnt / (double)c.getMemberCount() > CONN_RATIO ){
					c.addMember(friend1);
					unasFriends.remove(friend1);
				}
			}//next friend1
		}//next circle

		//STEP 3:
		//further analyze unassigned friends based on connectivity


		return ret;
	}

	public int estimateCircleNum(Integer user){

		UserFeatures feats = Main.userFeatures.get(user);

		//orig vars
		int v0 = (feats.getGender() > -1 ? 1 : 0)
				       + (feats.getHometownId() > -1 ? 1 : 0)
				       + (feats.getLocationId() > -1 ? 1 : 0)
				       + (feats.getPolitical() > -1 ? 1 : 0)
				       + (feats.getReligion() > -1 ? 1 : 0)
				       ; //UniqueProv
		int v1 = feats.getFeaturesByType(FeatureWorkEmployer.class).size();//WEN_NUM
		int v2 = feats.getFeaturesByType(FeatureWorkLocation.class).size();//WLN_NUM
		int v4 = feats.getFeaturesByType(FeatureLanguage.class).size();//LN_NUM
		int v5 = feats.getFeaturesByType(FeatureEducationClass.class).size();//ECN_NUM
		int v6 = feats.getFeaturesByType(FeatureEducationDegree.class).size();//EDN_NUM
		int v7 = feats.getFeaturesByType(FeatureEducationSchool.class).size();//ESN_NUM
		int v8 = Main.egoNet.getEgonet(user).getFriendsNumber();//FRIEND_NUM


		//derived vars
		int v0_pr_v1 = v0 * v1;
		int v0_pr_v2 = v0 * v2;
		int v0_pr_v6 = v0 * v6;
		int v0_pr_v7 = v0 * v7;
		int v0_pr_v8 = v0 * v8;
		int v1_pr_v2 = v1 * v2;
		int v1_pr_v5 = v1 * v5;
		int v1_pr_v7 = v1 * v7;
		int v1_pr_v8 = v1 * v8;
		int v2_pr_v5 = v2 * v5;
		int v2_pr_v6 = v2 * v6;
		int v2_pr_v7 = v2 * v7;
		int v2_pr_v8 = v2 * v8;
		int v4_pr_v7 = v4 * v7;
		int v5_pr_v7 = v5 * v7;
		int v5_pr_v8 = v5 * v8;
		int v6_pr_v8 = v6 * v8;
		int v7_pr_v8 = v7 * v8;
		int vsq0 = v0 * v0;
		int vsq1 = v1 * v1;
		int vsq2 = v2 * v2;
		int vsq6 = v6 * v6;
		int vsq7 = v7 * v7;
		int vsq8 = v8 * v8;


		double[] vars = new double[]{
				 1.0
				,(double)v0
				,(double)v1
				,(double)v2
				,(double)v6
				,(double)v7
				,(double)v8
				,(double)v0_pr_v1
				,(double)v0_pr_v2
				,(double)v0_pr_v6
				,(double)v0_pr_v7
				,(double)v0_pr_v8
				,(double)v1_pr_v2
				,(double)v1_pr_v5
				,(double)v1_pr_v7
				,(double)v1_pr_v8
				,(double)v2_pr_v5
				,(double)v2_pr_v6
				,(double)v2_pr_v7
				,(double)v2_pr_v8
				,(double)v4_pr_v7
				,(double)v5_pr_v7
				,(double)v5_pr_v8
				,(double)v6_pr_v8
				,(double)v7_pr_v8
				,(double)vsq0
				,(double)vsq1
				,(double)vsq2
				,(double)vsq6
				,(double)vsq7
				,(double)vsq8
		};
		double[] params = new double[]{
				  4.481253
				, -0.127626
				, -0.429938
				, -1.761834
				, -7.039395
				, 2.892621
				, -0.003430
				, -0.211578
				, 1.885740
				, 0.387588
				, -0.584390
				, 0.004268
				, 0.179958
				, 2.534633
				, 0.567090
				, 0.007988
				, 1.490291
				, -0.211024
				, -3.142792
				, -0.006564
				, -0.320801
				, 0.158669
				, -0.030257
				, -0.019111
				, -0.004176
				, 0.014075
				, -0.510450
				, 1.758776
				, 6.769028
				, 0.437266
				, 0.000018
		};

		double ret = 0.0;
		for(int i=0; i<vars.length; i++){
			ret += (vars[i] * params[i]);
		}

		return (new Double(Math.floor(ret))).intValue();
	}
}
