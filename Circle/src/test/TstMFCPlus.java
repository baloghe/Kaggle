package test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collection;

import circle.*;
import model.*;

public final class TstMFCPlus {
   public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Main.main(new String[]{"-featuresFile=features.txt"
				              ,"-egonetsDir=egonets"
				              ,"-circlesDir=Circles"
				              //,"-model=MostFriendsInCommon"
				              //,"-model=MostFriendPlusCrony"
				              ,"-model=MFCPlus"
				              //,"-model=noSuch"
				              });
		System.out.println("Data loading finished");

		//Collection<Integer> testSet = Main.userCircles.getAllCircles().keySet();
		//Collection<Integer> testSet = Util.getArrayList(new int[]{239,345,611,1968,2255,4406,16203,27022});
		//Collection<Integer> testSet = Util.getArrayList(new int[]{239,345,611});
		Collection<Integer> testSet = Util.getArrayList(new int[]{239});

		System.out.println("Original  -- START");
		testParams(testSet);
		System.out.println("With ratios  -- START");
		testParamsWithRatios(testSet);

		System.out.println("Test finished");
   }

   public static void testParams(Collection<Integer> users){

      double thrsFrom = 0.300, thrsTo = 0.325, thrsStep = 0.025;
      int maxcrsFrom = 54, maxcrsTo = 56, maxcrsStep = 1;
      int maxchgFrom = 40, maxchgTo = 45, maxchgStep = 1;
      int maxCFVFrom = 5, maxCFVTo = 6, maxCFVStep = 1;
      int maxenlgFrom = 44, maxenlgTo = 45, maxenlgStep = 1;
      //double maxenlgFrom = 0.1, maxenlgTo = 0.2, maxenlgStep = 0.01;
      int num2passFrom = -1, num2passTo = 0, num2passStep = 1;
      double num2ratioFrom = 0.0, num2ratioTo = 0.1, num2ratioStep = 0.1;

      //winning combination: {0.4, 14, 50} ->
      //winning combination: {0.275, 25, 38} -> 16549
      //0.275	29	56	16506
      //0.3	    48	56	16363
      //0.3     54  56  3     16226
      //MINDIFF: 0.275	155	41	5	15732 -> 20140928
      //MINDIFF: 0.275	153	46	5	44	15366
      //MINDIFF: 0.275	150	44	5	44	15355
      //ugyanez: 0.275	150	44	5	44	-1	0.0	15355

		int minDiff = Integer.MAX_VALUE;
		String minSettings = "";
      for(double thrs = thrsFrom; thrs < thrsTo; thrs += thrsStep){
         for(int maxcrs = maxcrsFrom; maxcrs < maxcrsTo; maxcrs += maxcrsStep){
            for(int maxchg = maxchgFrom; maxchg < maxchgTo; maxchg += maxchgStep){
            	for(int maxCFV = maxCFVFrom; maxCFV < maxCFVTo; maxCFV += maxCFVStep){
	               for(int maxenlg = maxenlgFrom; maxenlg < maxenlgTo; maxenlg += maxenlgStep){
            		//for(double maxenlg = maxenlgFrom; maxenlg < maxenlgTo; maxenlg += maxenlgStep){
	                  for(int num2pass = num2passFrom; num2pass < num2passTo; num2pass += num2passStep){
	                	  for(double num2ratio = num2ratioFrom; num2ratio < num2ratioTo; num2ratio += num2ratioStep){

					            int diff = testParamSet(users, -1, thrs, maxcrs, maxchg, maxCFV, maxenlg, num2pass, num2ratio);
					            System.out.println(thrs +"\t"+ maxcrs +"\t"+ maxchg +"\t"+ maxCFV +"\t"+ maxenlg +"\t"+ num2pass +"\t"+ num2ratio +"\t"+ diff);
					            if(minDiff > diff){
					               minDiff = diff;
					               minSettings = thrs +"\t"+ maxcrs +"\t"+ maxchg +"\t"+ maxCFV +"\t"+ maxenlg +"\t"+ num2pass +"\t"+ num2ratio;
					            }
	                	  }//num2ratio
	                  }//num2pass
	               }//maxenlg
            	}//maxCFV
	         }//maxchg
         }//maxcrs
      }//thrs

      System.out.println("MINDIFF: " + minSettings + "\t" + minDiff);

   }

   public static void testParamsWithRatios(Collection<Integer> users){

      double thrsFrom = 0.275, thrsTo = 0.300, thrsStep = 0.025;
      double maxcrsFrom = 0.8, maxcrsTo = 1.0, maxcrsStep = 0.1;
      int maxchgFrom = 44, maxchgTo = 45, maxchgStep = 1;
      int maxCFVFrom = 5, maxCFVTo = 6, maxCFVStep = 1;
      int maxenlgFrom = 44, maxenlgTo = 45, maxenlgStep = 1;
      //double maxenlgFrom = 0.1, maxenlgTo = 0.2, maxenlgStep = 0.01;
      int num2passFrom = -1, num2passTo = 0, num2passStep = 1;
      double num2ratioFrom = 0.0, num2ratioTo = 0.1, num2ratioStep = 0.1;

      //winning combination: {0.4, 14, 50} ->
      //winning combination: {0.275, 25, 38} -> 16549
      //0.275	29	56	16506
      //0.3	    48	56	16363
      //0.3     54  56  3     16226
      //MINDIFF: 0.275	155	41	5	15732 -> 20140928
      //MINDIFF: 0.275	153	46	5	44	15366
      //MINDIFF: 0.275	150	44	5	44	15355
      //ugyanez: 0.275	150	44	5	44	-1	0.0	15355

		int minDiff = Integer.MAX_VALUE;
		String minSettings = "";
      for(double thrs = thrsFrom; thrs < thrsTo; thrs += thrsStep){
         for(double maxcrs = maxcrsFrom; maxcrs < maxcrsTo; maxcrs += maxcrsStep){
            for(int maxchg = maxchgFrom; maxchg < maxchgTo; maxchg += maxchgStep){
            	for(int maxCFV = maxCFVFrom; maxCFV < maxCFVTo; maxCFV += maxCFVStep){
	             for(int maxenlg = maxenlgFrom; maxenlg < maxenlgTo; maxenlg += maxenlgStep){
            		//for(double maxenlg = maxenlgFrom; maxenlg < maxenlgTo; maxenlg += maxenlgStep){
	                  for(int num2pass = num2passFrom; num2pass < num2passTo; num2pass += num2passStep){
	                	  for(double num2ratio = num2ratioFrom; num2ratio < num2ratioTo; num2ratio += num2ratioStep){

					            int diff = testParamSetWithRatios(users, -1, thrs, maxcrs, maxchg, maxCFV, maxenlg, num2pass, num2ratio);
					            System.out.println(thrs +"\t"+ maxcrs +"\t"+ maxchg +"\t"+ maxCFV +"\t"+ maxenlg +"\t"+ num2pass +"\t"+ num2ratio +"\t"+ diff);
					            if(minDiff > diff){
					               minDiff = diff;
					               minSettings = thrs +"\t"+ maxcrs +"\t"+ maxchg +"\t"+ maxCFV +"\t"+ maxenlg +"\t"+ num2pass +"\t"+ num2ratio;
					            }
	                	  }//num2ratio
	                  }//num2pass
	               }//maxenlg
            	}//maxCFV
	         }//maxchg
         }//maxcrs
      }//thrs

      System.out.println("MINDIFF: " + minSettings + "\t" + minDiff);

   }

   public static int testParamSet(Collection<Integer> users, int inMinCommonFeatures, double inMinRelFrsThrs
                                  , int inMaxCircles, int inMaxChanges, int inMaxCFV, int inMaxEnlg, int inNum2Pass, double inNum2Ratio){

      int cumDiff = 0;
      for(int user : users){

			//MFCPlus.LOG_ENABLED = true;
			Model md =  new MFCPlus(inMinCommonFeatures, inMinRelFrsThrs
					, inMaxCircles, inMaxChanges, inMaxCFV, inMaxEnlg, inNum2Pass, inNum2Ratio);
			ModelResultStatistics mrs = new ModelResultStatistics("MFCPlus", user);
			ArrayList<UserCircle> ests = md.getUserCirclesEstimation(user);
			mrs.setEstimations(ests);
			cumDiff += mrs.getDiffPoints();

      }

      return cumDiff;
   }

   public static int testParamSetWithRatios(Collection<Integer> users, int inMinCommonFeatures, double inMinRelFrsThrs
                                  , double inMaxCircles, int inMaxChanges, int inMaxCFV, int inMaxEnlg, int inNum2Pass, double inNum2Ratio){

      int cumDiff = 0;
      for(int user : users){

			//MFCPlus.LOG_ENABLED = true;
			Model md =  new MFCPlus(inMinCommonFeatures, inMinRelFrsThrs
					, inMaxCircles, inMaxChanges, inMaxCFV, inMaxEnlg, inNum2Pass, inNum2Ratio);
			ModelResultStatistics mrs = new ModelResultStatistics("MFCPlus", user);
			ArrayList<UserCircle> ests = md.getUserCirclesEstimation(user);
			mrs.setEstimations(ests);
			cumDiff += mrs.getDiffPoints();

      }

      return cumDiff;
   }

   public static int testParamSet(Collection<Integer> users, int inMinCommonFeatures, double inMinRelFrsThrs
           , int inMaxCircles, int inMaxChanges, int inMaxCFV, double inMaxEnlg, int inNum2Pass, double inNum2Ratio){

		int cumDiff = 0;
		for(int user : users){

		//MFCPlus.LOG_ENABLED = true;
		Model md =  new MFCPlus(inMinCommonFeatures, inMinRelFrsThrs
		, inMaxCircles, inMaxChanges, inMaxCFV, inMaxEnlg, inNum2Pass, inNum2Ratio);
		ModelResultStatistics mrs = new ModelResultStatistics("MFCPlus", user);
		ArrayList<UserCircle> ests = md.getUserCirclesEstimation(user);
		mrs.setEstimations(ests);
		cumDiff += mrs.getDiffPoints();

		}

		return cumDiff;
	}
}