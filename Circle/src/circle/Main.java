package circle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import model.*;
import common.Reader;

public class Main {

	public static HashMap<Integer, UserFeatures> userFeatures;
	public static UserCircles userCircles;
	public static Egonet egoNet;

	public static boolean LOG_ENABLED = true;

	public static void main(String[] args) {
		String featuresFile="";
		String circlesDir = "";
		String egonetsDir = "";
		String modelToUse = "";

		for(String s : args){
			if(s.substring(0, 13).equalsIgnoreCase("-featuresFile")){
				featuresFile = s.substring(14);
				log("featuresFile=" + featuresFile);
			} else if(s.substring(0, 11).equalsIgnoreCase("-circlesDir")) {
				circlesDir = s.substring(12);
				log("circlesDir=" + circlesDir);
			} else if(s.substring(0, 11).equalsIgnoreCase("-egonetsDir")) {
				egonetsDir = s.substring(12);
				log("egonetsDir=" + egonetsDir);
			} else if(s.substring(0, 6).equalsIgnoreCase("-model")) {
				modelToUse = s.substring(7);
				log("modelToUse=" + modelToUse);
			} else {
				log("s.substring(0, 13)=" + s.substring(0, 13) + ", (14->)="+s.substring(14));
			}
		}

		init(featuresFile, circlesDir, egonetsDir);

		//try out
		Collection<Integer> userSet = Main.egoNet.getUsers().keySet();
		//userSet.removeAll(Main.userCircles.getAllCircles().keySet());

		if(modelToUse.equalsIgnoreCase("AllFriendsInOneCircle")){
			testAllFiendsInOneCircleModel(userSet);
		} else if(modelToUse.equalsIgnoreCase("AllSchoolsInOneCircle")) {
			testAllSchoolsInOneCircleModel(userSet);
		} else if(modelToUse.equalsIgnoreCase("ByPopularFeatures")) {
			testByPopularFeatures(userSet);
		} else if(modelToUse.equalsIgnoreCase("ConnBased")) {
			testConnBased(userSet);
		} else if(modelToUse.equalsIgnoreCase("HasMostFriends")) {
			testHasMostFriends(userSet);
		} else if(modelToUse.equalsIgnoreCase("MostFriendPlusCrony")) {
			testMostFriendPlusCrony(userSet);
		} else if(modelToUse.equalsIgnoreCase("MostFriendsInCommon")) {
			testMostFriendsInCommon(userSet);
		} else if(modelToUse.equalsIgnoreCase("MFCPlus")) {
			testMFCPlus(userSet);
		} else {
			log("No model has been selected: modelToUse=" + modelToUse);
		}
	}

	private static void log(String s){
	   if(LOG_ENABLED){System.out.println(s);}
	}

	public static void init(String featuresFile, String circlesDir, String egonetsDir){
		//INIT REPO
		FeatureRepo.init();

		//READ INPUTS
		Reader<UserFeatures> rd = new Reader<UserFeatures>(UserFeatures.class);
		try{
			//Read features.txt
			ArrayList<UserFeatures> diffFeatPerUser = rd.read(featuresFile, " ");
			System.out.println("Features read in.");
			userFeatures = new HashMap<Integer, UserFeatures>();
			for(UserFeatures uf : diffFeatPerUser){
				userFeatures.put(new Integer(uf.getId()), uf);
			}

			//read Egonets
			egoNet = new Egonet();
			egoNet.readFolder(egonetsDir, " ");

			//read circles (training set)
			userCircles = new UserCircles();
			userCircles.readFolder(circlesDir, " ");

			//batch calc common features in training set
			for(Entry<Integer, ArrayList<UserCircle>> elem : Main.userCircles.getAllCircles().entrySet()){
				for(UserCircle uc : elem.getValue()){
					uc.calcCommonFeatures();
				}
			}

		} catch (FileNotFoundException fnf){
			System.out.println("Files & dirs provided: " + featuresFile + "," + egonetsDir + "," + circlesDir);
			fnf.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void testAllFiendsInOneCircleModel(Collection<Integer> users){
		log("--- AllFiendsInOneCircleModel  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		for(Integer user : users){
			Model m = new AllFriendsInOneCircle();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
		}

		Util.printModelEstimates("AllFriendsInOne.csv", estimate);
		log("--- AllFiendsInOneCircleModel  -- END");
	}

	public static void testAllSchoolsInOneCircleModel(Collection<Integer> users){
		log("--- AllSchoolsInOneCircleModel  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		for(Integer user : users){
			Model m = new AllSchoolsInOneCircle();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
		}

		Util.printModelEstimates("AllSchoolsInOne.csv", estimate);
		log("--- AllSchoolsInOneCircleModel  -- END");
	}

	public static void testByPopularFeatures(Collection<Integer> users){
		log("--- ByPopularFeatures  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		for(Integer user : users){
			Model m = new ByPopularFeatures();
			ArrayList<UserCircle> circleSet = m.getUserCirclesEstimation(user);
			estimate.add(circleSet);
		}

		Util.printModelEstimates("ByPopularFeatures.csv", estimate);
		log("--- ByPopularFeatures  -- END");
	}

	public static void testConnBased(Collection<Integer> users){
		log("--- ConnBased  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		int totTrainDiff = 0;
		for(Integer user : users){
			ConnectionBased md = new ConnectionBased(0.90);
			ArrayList<UserCircle> circleSet = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
			if(train != null){
				int diff = Util.getUserCirclesDifference(circleSet, train);
				totTrainDiff += diff;
			}
			estimate.add(circleSet);
		}

		Util.printModelEstimates("ConnBased.csv", estimate);
		log("--- ConnBased  totTrainDiff=" + totTrainDiff);
		log("--- ConnBased  -- END");
	}

	public static void testHasMostFriends(Collection<Integer> users){
		log("--- HasMostFriends  -- START");
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		int totTrainDiff = 0;
		for(Integer user : users){
			HasMostFriends md = new HasMostFriends(3);
			ArrayList<UserCircle> circleSet = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
			if(train != null){
				int diff = Util.getUserCirclesDifference(circleSet, train);
				totTrainDiff += diff;
			}
			estimate.add(circleSet);
		}

		Util.printModelEstimates("HasMostFriends.csv", estimate);
		log("--- HasMostFriends  totTrainDiff=" + totTrainDiff);
		log("--- HasMostFriends  -- END");
	}


	public static void testMostFriendPlusCrony(Collection<Integer> users){
		log("--- MostFriendPlusCrony  -- START");
		//log("\tusers.size=" + users.size());
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		int totTrainDiff = 0;
		for(Integer user : users){
			MostFriendPlusCrony md = new MostFriendPlusCrony(0.92);
			ArrayList<UserCircle> circleSet = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
			if(train != null){
				int diff = Util.getUserCirclesDifference(circleSet, train);
				totTrainDiff += diff;
			}
			//log("\tuser="+user+" -> circleSet.size=" + circleSet.size());
			estimate.add(circleSet);
		}
		//log("\testimate.size=" + estimate.size());

		Util.LOG_ENABLED = true;
		Util.printModelEstimates("MostFriendPlusCrony.csv", estimate);
		Util.LOG_ENABLED = false;
		log("--- MostFriendPlusCrony  totTrainDiff=" + totTrainDiff);
		log("--- MostFriendPlusCrony  -- END");
	}

	public static void testMostFriendsInCommon(Collection<Integer> users){
		log("--- MostFriendsInCommon  -- START");
		//log("\tusers.size=" + users.size());
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		int totTrainDiff = 0;
		for(Integer user : users){
			MostFriendsInCommon md = new MostFriendsInCommon(-1);
			ArrayList<UserCircle> circleSet = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
			if(train != null){
				int diff = Util.getUserCirclesDifference(circleSet, train);
				totTrainDiff += diff;
			}
			//log("\tuser="+user+" -> circleSet.size=" + circleSet.size());
			estimate.add(circleSet);
		}
		//log("\testimate.size=" + estimate.size());

		Util.LOG_ENABLED = true;
		Util.printModelEstimates("MostFriendsInCommon.csv", estimate);
		Util.LOG_ENABLED = false;
		log("--- MostFriendsInCommon  totTrainDiff=" + totTrainDiff);
		log("--- MostFriendsInCommon  -- END");
	}

	public static void testMFCPlus(Collection<Integer> users){
		log("--- MFCPlus  -- START");
		//log("\tusers.size=" + users.size());
		HashSet<ArrayList<UserCircle>> estimate = new HashSet<ArrayList<UserCircle>>();

		int totTrainDiff = 0;
		for(Integer user : users){
			MFCPlus md = new MFCPlus(-1, 0.725, 40, 40, 5, 44);
			ArrayList<UserCircle> circleSet = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
			if(train != null){
				int diff = Util.getUserCirclesDifference(circleSet, train);
				totTrainDiff += diff;
			}
			//log("\tuser="+user+" -> circleSet.size=" + circleSet.size());
			estimate.add(circleSet);
		}
		//log("\testimate.size=" + estimate.size());

		Util.LOG_ENABLED = true;
		Util.printModelEstimates("MFCPlus.csv", estimate);
		Util.LOG_ENABLED = false;
		log("--- MFCPlus  totTrainDiff=" + totTrainDiff);
		log("--- MFCPlus  -- END");
	}
}
