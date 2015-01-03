package test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collection;

import circle.*;
import model.*;
import feature.*;

public final class TstModelResultsStatistics{

	public static HashMap<Integer, HashMap<String, ModelResultStatistics>> stats;

   public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Main.main(new String[]{"-featuresFile=features.txt"
				              ,"-egonetsDir=egonets"
				              ,"-circlesDir=Circles"
				              //,"-model=MostFriendsInCommon"
				              //,"-model=MostFriendPlusCrony"
				              //,"-model=MFCPlus"
				              ,"-model=noSuch"
				              });
		System.out.println("Data loading finished");

		Collection<Integer> testSet = Main.userCircles.getAllCircles().keySet();
		//Collection<Integer> testSet = Util.getArrayList(new int[]{239,345,611,1968,2255,4406,16203,27022});
		//Collection<Integer> testSet = Util.getArrayList(new int[]{239,345,611});
		//Collection<Integer> testSet = Util.getArrayList(new int[]{239});
		//Collection<Integer> testSet = Util.getArrayList(new int[]{2738});

		stats = new HashMap<Integer, HashMap<String, ModelResultStatistics>>();

		double thrs = 0.92;
		int maxmems = 1;
		int minCMF = -1;
		testModels(testSet, thrs, maxmems, minCMF);
		printStatistics();

		System.out.println("Test finished");
	}

   public static void testModels(Collection<Integer> users, double threshold, int maxmems, int minCMF){

      for(int user : users){

         //True values
         Model md;

         /*
			//OneCircleForAll
			md = new AllFriendsInOneCircle();
			addResult(user, md, AllFriendsInOneCircle.MODEL_NAME);

			//OneCirclePerSchool
			md = new AllSchoolsInOneCircle();
			addResult(user, md, AllSchoolsInOneCircle.MODEL_NAME);

			//ByPopularFeatures
			md = new ByPopularFeatures();
			addResult(user, md, ByPopularFeatures.MODEL_NAME);


			//MostPopularPair
         md =  new MostPopularPair();
			addResult(user, md, MostPopularPair.MODEL_NAME);



			//ConnectionBased
         md =  new ConnectionBased( threshold );
			addResult(user, md, ConnectionBased.MODEL_NAME);


			//UserIdPlusOne
         md =  new UserIdPlusOne();
			addResult(user, md, UserIdPlusOne.MODEL_NAME);


			//HasMostFriends
			HasMostFriends.LOG_ENABLED = true;
         md =  new HasMostFriends(maxmems);
			addResult(user, md, HasMostFriends.MODEL_NAME);

			//MostFriendPlusCrony
			MostFriendPlusCrony.LOG_ENABLED = true;
         md =  new MostFriendPlusCrony(threshold);
			addResult(user, md, MostFriendPlusCrony.MODEL_NAME);

			//MostFriendsInCommon
			MostFriendsInCommon.LOG_ENABLED = true;
	         md =  new MostFriendsInCommon(minCMF);
				addResult(user, md, MostFriendsInCommon.MODEL_NAME);

			//MFCPlus orig
			//MFCPlus.LOG_ENABLED = true;
	         md =  new MFCPlus(-1, 0.275, 155, 41, 5);
				addResult(user, md, MFCPlus.MODEL_NAME + "_orig");

			//MFCPlus with 2nd pass
			//MFCPlus.LOG_ENABLED = true;
	         md =  new MFCPlus(-1, 0.275, 155, 41, 5, 10);
				addResult(user, md, MFCPlus.MODEL_NAME + "_enlg");
*/				
			//MFCPlus with 2nd pass    
			//MFCPlus.LOG_ENABLED = true;
	         md =  new MFCPlus(-1, 0.275, 155, 41, 5, 10, 20);
				addResult(user, md, MFCPlus.MODEL_NAME + "_2ndp");
				
			//MFCPlus with 2nd pass    
			//MFCPlus.LOG_ENABLED = true;
	         md =  new MFCPlus(-1, 0.275, 150, 44, 5, 44);
				addResult(user, md, MFCPlus.MODEL_NAME + "_1002");


			System.out.println("\tdone:\t"+user);

		}//next user

   }

   private static void addResult(Integer user, Model mdl, String modelName){
      //calc statistics
      ModelResultStatistics mrs = new ModelResultStatistics(modelName, user);
      ArrayList<UserCircle> ests = mdl.getUserCirclesEstimation(user);
      mrs.setEstimations(ests);

      //add it to where it belongs
      HashMap<String, ModelResultStatistics> rs = stats.get(user);
      if(rs == null){
         rs = new HashMap<String, ModelResultStatistics>();
      }
      rs.put(modelName, mrs);
      stats.put(user, rs);
      /*
      System.out.println("CircleFeaturesVectors for USER=" + user);
      for(UserCircle c : ests){
    	  System.out.println("c=" + c);
    	  CircleFeaturesVector cfv = new CircleFeaturesVector(user, c);
    	  printCircleFeatures(cfv, 2);
      }
      */

      //System.out.println("MRS added: user=" + user + ", model=" + modelName + ", res=" + mrs.getDiffPoints() );
   }

   private static void printStatistics(){
      for(Integer user : stats.keySet()){
         HashMap<String, ModelResultStatistics> hm = stats.get(user);
         for(String mdl : hm.keySet()){
            ModelResultStatistics mrs = hm.get(mdl);
            System.out.println(mrs.toString("\t", true, true));
         }//next model
      }//next user
   }

   private static void printCircleFeatures(CircleFeaturesVector cfv, int thrs){
	   for(Entry<Feature, Integer> e : cfv.getVector(thrs).entrySet()){
		   Feature f = e.getKey();
		   int i = e.getValue().intValue();
		   System.out.println(f.toString() + "->" + i);
	   }//next e
   }

}