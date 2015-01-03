package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Collection;

import model.*;
import circle.Main;
import circle.UserCircle;
import circle.UserEgonet;
import circle.Util;
import feature.Feature;
import feature.FeaturePopularityComparator;

public class TstMain {

	public static void main(String[] args) {
		Main.main(new String[]{"-featuresFile=features.txt"
				              ,"-egonetsDir=egonets"
				              ,"-circlesDir=Circles"
				              //,"-model=ByPopularFeatures"
				              ,"-model=noSuch"
				              });

		testCircle(5881);
		testEgonetPopularFeatures(5881,7);

		/*
		Collection<Integer> testSet;
		//testSet = getArrayList(new int[]{239,345,611,4406,1357,1839,1968,2255});
		testSet = Main.userCircles.getAllCircles().keySet();
		//testSet = getArrayList(new int[]{20050,21098});

		testAllFiendsInOneCircleModel( testSet );
		testAllSchoolsInOneCircleModel( testSet );
		testByPopularFeatures( testSet );

		ByPopularFeatures m = new ByPopularFeatures();
		System.out.println("Estimated circle nums:");
		for(Integer user : testSet){
			System.out.println("\t" + user + "\t" + m.estimateCircleNum(user));
		}
		*/
		//Collection<Integer> testSet = Util.getArrayList(new int[]{1968});
		Collection<Integer> testSet = Main.userCircles.getAllCircles().keySet();
		//testAllFiendsInOneCircleModel( testSet );
		//testAllSchoolsInOneCircleModel( testSet );
		testByPopularFeatures( testSet );
		
		System.out.println("Test finished");
	}

	public static void testCircle(int user){
		System.out.println("CIRCLE FEATURES for User=" + user);
		ArrayList<UserCircle> ucs = Main.userCircles.getCirclesForUser(user);
		int cnt = 0;
		for(UserCircle uc : ucs){
		   ArrayList<Entry<Feature,Integer>> sortedComFeat = new ArrayList<Entry<Feature,Integer>>();
		   sortedComFeat.addAll(uc.getCommonFeatures().entrySet());
		   Collections.sort(sortedComFeat, new FeaturePopularityComparator());
			System.out.println("\t"+"Circle " + (++cnt) + " has "
					+ uc.getMemberCount() + " members; features with cnt>1:");
			for(Entry<Feature,Integer> elem : sortedComFeat){
				int featCnt = elem.getValue().intValue();
				if(featCnt > 1){
					System.out.println("\t\t" + elem.getKey().toString() + "\t" + featCnt);
				}
			}
		}
	}

	public static void testEgonetPopularFeatures(int user, int topCnt){
		System.out.println("POPULAR FEATURES IN EGONET for User=" + user);
		UserEgonet enet = Main.egoNet.getEgonet(new Integer(user));
		HashMap<Feature, Integer> popFeats = enet.getPopularFeatures(topCnt);
		ArrayList<Entry<Feature,Integer>> sortedComFeat = new ArrayList<Entry<Feature,Integer>>();
		   sortedComFeat.addAll(popFeats.entrySet());
		   Collections.sort(sortedComFeat, new FeaturePopularityComparator());
		for(Entry<Feature, Integer> elem : sortedComFeat){
			System.out.println("\t\t"+elem.getKey().toString()+"\t"+elem.getValue());
		}
	}

	public static void testAllFiendsInOneCircleModel(Collection<Integer> users){
		System.out.println("---testAllFiendsInOneCircleModel  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		System.out.println("Estimates by users");
		int totDiff = 0;
		for(Integer user : users){
			Model m = new AllFriendsInOneCircle();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
			int actDiff = Util.getUserCirclesDifference(circleSet, Main.userCircles.getCirclesForUser(user));
			totDiff += actDiff;
			System.out.println("\t" + user + "\t" + actDiff);
		}
		System.out.println("TOTAL\t" + totDiff);

		Util.printModelEstimates("tstAllFriendsInOne.txt", estimate);
		System.out.println("---testAllFiendsInOneCircleModel  -- END");
	}

	public static void testAllSchoolsInOneCircleModel(Collection<Integer> users){
		System.out.println("---testAllSchoolsInOneCircleModel  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		System.out.println("Estimates by users");
		int totDiff = 0;
		for(Integer user : users){
			Model m = new AllSchoolsInOneCircle();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
			int actDiff = Util.getUserCirclesDifference(circleSet, Main.userCircles.getCirclesForUser(user));
			totDiff += actDiff;
			System.out.println("\t" + user + "\t" + actDiff);
		}
		System.out.println("TOTAL\t" + totDiff);

		Util.printModelEstimates("tstAllSchoolsInOne.txt", estimate);
		System.out.println("---testAllSchoolsInOneCircleModel  -- END");
	}

	public static void testByPopularFeatures(Collection<Integer> users){
		System.out.println("---testByPopularFeatures  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		System.out.println("Estimates by users");
		int totDiff = 0;
		for(Integer user : users){
			Model m = new ByPopularFeatures();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
			int actDiff = Util.getUserCirclesDifference(circleSet, Main.userCircles.getCirclesForUser(user));
			totDiff += actDiff;
			System.out.println("\t" + user + "\t" + actDiff);
		}
		System.out.println("TOTAL\t" + totDiff);

		Util.printModelEstimates("tstByPopularFeatures.txt", estimate);
		System.out.println("---testByPopularFeatures  -- END");
	}

}
