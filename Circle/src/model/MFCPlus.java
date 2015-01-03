package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

import circle.Main;
import circle.UserCircle;
import circle.UserEgonet;
import feature.*;

public final class MFCPlus extends Model {

	public static String MODEL_NAME = "MFC_PLUS";

	public static boolean LOG_ENABLED = false;

	private int minCommonFeatures; //min num of common features btw selected members
	private double minRelFrsThrs;  //additional member must have this time * avg(CMF) with existing members
	private int maxCircles;
	private int maxChanges;
	private int minCFV;
	private int enlgNum = 3;
	private int num2pass;

	public static int MAX_CRS_CAP = 150;

	private double maxCirclesRatio;
	private double enlgRatio;
	private double num2ratio;

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, int inMaxCircles){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = 10;
		minCFV = 2;
		enlgNum = 3;
		num2pass = 0;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, int inMaxCircles, int inMaxChanges){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = 2;
		enlgNum = 3;
		num2pass = 0;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs
			, int inMaxCircles, int inMaxChanges, int inMinCFV){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = 3;
		num2pass = 0;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs
			, int inMaxCircles, int inMaxChanges, int inMinCFV, int inEnlgNum){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = inEnlgNum;
		num2pass = 0;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, int inMaxCircles
			, int inMaxChanges, int inMinCFV, int inEnlgNum, int inNum2Pass){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = inEnlgNum;
		num2pass = inNum2Pass;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, int inMaxCircles
			, int inMaxChanges, int inMinCFV, int inEnlgNum, int inNum2Pass, double inNum2Ratio){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = inEnlgNum;
		num2pass = inNum2Pass;
		num2ratio = inNum2Ratio;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, int inMaxCircles
			, int inMaxChanges, int inMinCFV, double inEnlgRatio, int inNum2Pass, double inNum2Ratio){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = inMaxCircles;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = -1;
		enlgRatio = inEnlgRatio;
		num2pass = inNum2Pass;
		num2ratio = inNum2Ratio;
	}

	public MFCPlus(int inMinCommonFeatures, double inMinRelFrsThrs, double inMaxCirclesRatio
			, int inMaxChanges, int inMinCFV, double inEnlgRatio, int inNum2Pass, double inNum2Ratio){
		minCommonFeatures = inMinCommonFeatures;
		minRelFrsThrs = inMinRelFrsThrs;
		maxCircles = -1;
		maxCirclesRatio = inMaxCirclesRatio;
		maxChanges = inMaxChanges;
		minCFV = inMinCFV;
		enlgNum = -1;
		enlgRatio = inEnlgRatio;
		num2pass = inNum2Pass;
		num2ratio = inNum2Ratio;
	}

	public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
		//translate ratios to actual number if necessary
		if(num2pass<0){
			num2pass = (int)(num2ratio * Main.egoNet.getEgonet(user).getFriendsNumber());
		}

		if(enlgNum<0){
			enlgNum = (int)(enlgRatio * Main.egoNet.getEgonet(user).getFriendsNumber());
		}

		if(maxCircles<0){
			maxCircles = (int)(maxCirclesRatio * Main.egoNet.getEgonet(user).getFriendsNumber());
			if(maxCircles > MAX_CRS_CAP){
			   maxCircles = MAX_CRS_CAP;
			}
			log("getUserCirclesEstimation :: user=" + user + ", maxCirclesRatio=" + maxCirclesRatio
			+ ", maxCircles=" + maxCircles);
		}
		if(maxCircles<0){
			maxCircles = 2;
		}

		//START estimation
		//get the two users with the most common friends within the egonet
		Integer[][] cands = getCircleCandidates(user);
		log("\tMFCPlus :: cands=");
		log(matrixToString(cands));

		//number of common features per candidate pairs
		int[] cmf = new int[maxCircles];
		for(int i=0; i<cands.length; i++){

			Integer mf1 = new Integer(cands[i][0]);
			Integer mf2 = new Integer(cands[i][1]);
			CommonFeaturesVector cfv = new CommonFeaturesVector(user, mf1, mf2);
			int mutEduSchoolNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureEducationSchool.class);
			int mutLangNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureLanguage.class);
			int mutWorkEmpNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureWorkEmployer.class);
			int mutWorkLocNum = cfv.numMutualNonuniqueFeatures(mf1,mf2,FeatureWorkLocation.class);
			cmf[i] = mutEduSchoolNum + mutLangNum + mutWorkEmpNum + mutWorkLocNum;
			log("\tMostFriendsInCommon :: mutEduSchoolNum=" + mutEduSchoolNum
					+ ", mutLangNum=" + mutLangNum + ", mutWorkEmpNum=" + mutWorkEmpNum
					+ ", mutWorkLocNum=" + mutWorkLocNum + ", cmf=" + cmf[i]);
		}

		//construct circle
		ArrayList<UserCircle> ret = createCircles(user, cands, cmf);
		log("resulting circles:");
		int i = 1;
		for(UserCircle c : ret){
		   log("c" + i + "=" + c);
		   i++;
		}

		//Enlarge circles based on their feature vectors
		log("MFCPlus.getUserCirclesEstimation :: Enlargement START");
		for(UserCircle c : ret){
			c = enlargeCircle(c);
		}
		log("MFCPlus.getUserCirclesEstimation :: Enlargement STOP");


		//apply a second pass (if required) as follows:
		//	take num2pass friends having the biggest number of features not yet "encircled"
		//	assign them to one of the existing circles FIRST based on their features
		//		SECOND based on #friends in the circle
		if(num2pass > 0){
			ret = applySecondPass(ret, user);
		}

		return ret;
	}

	private ArrayList<UserCircle> createCircles(Integer user, Integer[][] cands, int[] cmf){
		ArrayList<UserCircle> tot = new ArrayList<UserCircle>();

		//add all pairs into separate circle
		for(int i=0; i<cands.length; i++){
			UserCircle c = new UserCircle(user);
			if(cands[i][0] >= 0){
				c.addMember(new Integer(cands[i][0]));
			}
			if(cands[i][1] >= 0){
				c.addMember(new Integer(cands[i][1]));
			}
			if(c.getMemberCount() > 0){
				tot.add(c);
			}
		}

		boolean moved = true;
		int cnt = 0;
		while(moved && cnt < maxChanges){
			moved = false;

			for(UserCircle c1 : tot){
				for(UserCircle c2 : tot){
					if(c1.getMemberCount() > 0 && c2.getMemberCount() > 0
							&& !(c1.toString().equalsIgnoreCase(c2.toString())) ){

						double avgC1 = getAvgCommonFriends(c1);
						double avgC2 = getAvgCommonFriends(c2);
						double avgToC = avgC1;
						UserCircle fromC = c2;
						UserCircle toC = c1;
						if(avgC1 < avgC2){
							fromC = c1; toC = c2; avgToC = avgC2;
						}
						log("\t\t\tfromC=" + fromC + ", toC=" + toC + ", avgToC=" + avgToC);
						double maxCF = 0.0;
						Integer movMem = null;
						for(Integer m : fromC.getMembers()){
							double actCF = getAvgCommonFriends(user, m, toC);
							log("\t\t\t\tm=" + m + ", actCF=" + actCF);
							if(actCF > maxCF && actCF > minRelFrsThrs * avgToC){
								maxCF = actCF;
								movMem = m;
							}//endif
						}//next m

						if(movMem != null){
							log("\t\t\tprior to move: fromC=" + fromC + ", toC=" + toC);
							fromC.remMember(movMem);
							toC.addMember(movMem);
							moved = moved || true;
							log("\t\t\tafter move: fromC=" + fromC + ", toC=" + toC);
						}//endif


					}//end if
				}//next c2
			}//next c1

			log("moved=" + moved);
			cnt++;
		}//wend

		//construct result
		ArrayList<UserCircle> ret = new ArrayList<UserCircle>();
		for(UserCircle c : tot){
			if(c.getMemberCount()>0){
				ret.add(c);
			}
		}

		return ret;
	}

	public ArrayList<UserCircle> applySecondPass(ArrayList<UserCircle> inCircles, Integer user){
		//TBD!!!

		//identify unused friends
		HashSet<Integer> frs = new HashSet<Integer>();
		for(Integer f : Main.egoNet.getEgonet(user).getFriends()){
			frs.add(f);
		}
		for(UserCircle c : inCircles){
			for(Integer f : c.getMembers()){
				frs.remove(f);
			}
		}
		ArrayList<Integer> afrs = new ArrayList<Integer>();
		afrs.addAll(frs);
		log("applySecondPass :: afrs.size=" + afrs.size());
		//unused friends in ArrayList<Integer> afrs
		//leave if afrs is empty
		if(afrs.size()<1){
			return inCircles;
		}

		//sot them according to a good comparator...
		Collections.sort(afrs, new MFCSecondPassComparator(false, user) );

		for(int i=0; i<2 && i < afrs.size(); i++){
			log("\tfriend_"+i+"="+afrs.get(i));
		}

		//for the first <num2pass> friends: allocate them to the most suitable existing circle
		UserCircle selcirc = null;
		for(int cnt = 0; cnt < afrs.size() && cnt < num2pass; cnt++){
		   Integer friend = afrs.get(cnt);
		   log("eval f="+friend+" --START");
		   selcirc = null;
		   int maxCFeats = 0;    //common features
		   int maxCFriends = 0;  //common friends
		   for(UserCircle candc : inCircles){
		      int actCFeats = 0;
		      int actCFriends = 0;
		      //evaluate candidate circle
		      for(Integer f : candc.getMembers()){
		         actCFeats += ( this.numMutualNonuniqueFeatures(friend.intValue(), f.intValue(), FeatureEducationSchool.class)
		                      + this.numMutualNonuniqueFeatures(friend.intValue(), f.intValue(), FeatureLanguage.class)
		                      + this.numMutualNonuniqueFeatures(friend.intValue(), f.intValue(), FeatureWorkEmployer.class)
		                      + this.numMutualNonuniqueFeatures(friend.intValue(), f.intValue(), FeatureWorkLocation.class)
		         			);
					actCFriends += Main.egoNet.getEgonet(user).numCommonFriends(friend.intValue(), f.intValue());
				log("\t\tcandC f="+f+",actCFeats="+actCFeats+",actCFriends="+actCFriends);
		      }//next candc.f
		      log("\tcandC total actCFeats="+actCFeats+",actCFriends="+actCFriends);
		      //choose the best so far
		      if(actCFeats > maxCFeats || (actCFeats == maxCFeats && actCFriends > maxCFriends) ){
		         selcirc = candc;
		         maxCFeats = actCFeats;
		         maxCFriends = actCFriends;
		         log("\tcandC is better -> chosen");
		      }
		   }//next candc
		   //assign friend to the best gircle found
		   if(selcirc != null){
			   selcirc.addMember(friend);
		   }
		   log("eval f="+friend+" --STOP");
		}

		return inCircles;
	}

	private UserCircle enlargeCircle(UserCircle c){
		Integer owner = c.getOwnerID();
		UserEgonet enet = Main.egoNet.getEgonet(owner);
		CircleFeaturesVector cfv = new CircleFeaturesVector(owner, c);
		HashMap<Feature, Integer> vec = cfv.getVector(minCFV);

		log("enlargeCircle :: c="+c);
		if(vec.entrySet().size() < 1){
			log("\tVEC empty, return...");
			return c;
		}

		//gather the most capable user matching the vector outside the circle...
		//first: number of matching features
		//second: number of common friends with users already in the circle

		Integer[][] cands = new Integer[enlgNum][3];
		//fill with dummy
		for(int i=0; i<enlgNum; i++){
			cands[i][0] = new Integer(-1);
			cands[i][1] = new Integer(0);
			cands[i][2] = new Integer(0);
		}

		//calc
		int cnt = 0;
		int fthrs = 0; //first threshold
		int sthrs = 0; //second threshold
		for(Integer i : enet.getFriends()){
			if(!c.isMember(i)){
				int fact = numCommonFeatures(vec, i);
				int sact = (int)(10*getAvgCommonFriends(owner, i, c));

				if(fact > fthrs || (fact == fthrs && sact > sthrs) ){
					//change min
					int minidx = getMinIdx_Enlarge(cands);
					cands[minidx][0] = i;
					cands[minidx][1] = fact;
					cands[minidx][2] = sact;

					//reset thresholds
					minidx = getMinIdx_Enlarge(cands);
					fthrs = cands[minidx][1];
					sthrs = cands[minidx][2];

					//log
					cnt++;
					if(cnt<5){
						log("\tadded to cands: " + i);
					}
				}//endif
			}//endif
		}//next i
		log("\tresulting cands:");
		log(matrixToString(cands));

		//add MAX to the existing circle
		/*
		int maxidx = getMaxIdx_Enlarge(cands);
		Integer newmemb = cands[maxidx][0];
		if(newmemb.intValue() >= 0){
			c.addMember(newmemb);
			log("\tnew member added: " + newmemb);
		}
		*/
		//add them all to the existing circle
		for(int i=0; i<cands.length; i++){
		   Integer newmemb = cands[i][0];
			if(newmemb.intValue() >= 0){
				c.addMember(newmemb);
				log("\tnew member added: " + newmemb);
			}
		}

		return c;
	}

	private int numCommonFeatures(HashMap<Feature, Integer> vec, Integer u){
		int ret = 0;
		for(Feature f : vec.keySet()){
			HashSet<Feature> s = Main.userFeatures.get(new Integer(u)).getFeaturesByType(f.getClass());
			ret += (s.contains(f) ? 1 : 0);
		}
		return ret;
	}

	private int getMinIdx_Enlarge(Integer[][] mat){
		   int first = Integer.MAX_VALUE;  //least number of matching features
		   int second = Integer.MAX_VALUE; //number of common friends with users already in the circle
		   int ret = -1;

		   for(int i=0; i<mat.length; i++){
		      int actFirst = mat[i][1];
		      int actSecond = mat[i][2];
		      if(actFirst < first || (actFirst==first && actSecond < second) ){
		         ret = i;
		         first = actFirst;
		         second = actSecond;
		      }
		   }
		   return ret;
		}

		private int getMaxIdx_Enlarge(Integer[][] mat){
		   int first = Integer.MIN_VALUE;  //least number of matching features
		   int second = Integer.MIN_VALUE; //number of common friends with users already in the circle
		   int ret = -1;

		   for(int i=0; i<mat.length; i++){
		      int actFirst = mat[i][1];
		      int actSecond = mat[i][2];
		      if(actFirst > first || (actFirst==first && actSecond > second) ){
		         ret = i;
		         first = actFirst;
		         second = actSecond;
		      }
		   }
		   return ret;
		}

	private double getAvgCommonFriends(Integer user, Integer friend, UserCircle c){
	   double ret = 0.0;

	   UserEgonet enet = Main.egoNet.getEgonet(user);
	   int cnt = 0;
	   for(Integer m : c.getMembers()){
	      ret += enet.numCommonFriends(friend, m);
	      cnt++;
	   }
	   ret /= (double)cnt;

	   return ret;
	}

	private double getAvgCommonFriends(UserCircle c){
		   double ret = 0.0;
		   if(c.getMemberCount()<2){
			   return 0.0;
		   }

		   UserEgonet enet = Main.egoNet.getEgonet(c.getOwnerID());
		   int cnt = 0;
		   for(Integer m1 : c.getMembers()){
			   for(Integer m2 : c.getMembers()){
				   if(m1.intValue() < m2.intValue()){
					   ret += enet.numCommonFriends(m1, m2);
					   cnt++;
				   }//endif
			   }//next m2
		   }//next m1
		   ret /= (double)cnt;

		   return ret;
		}

	private Integer[][] getCircleCandidates(Integer user){
	   Integer[][] ret = new Integer[maxCircles][5]; //in rows: i, j, CommonFriends(i,j), Friends(i), Friends(j)

	   //assign starting values
	   for(int i=0; i<ret.length; i++){
	      for(int j=0; j<ret[i].length; j++){
	         if(j<2){
	            ret[i][j] = -1;
	         } else {
	            ret[i][j] = 0;
	         }
	      }
	   }

	   //Calc
	   UserEgonet enet = Main.egoNet.getEgonet(user);
		Integer mf1 = new Integer(-1);
		Integer mf2 = new Integer(-1);

		//entry thresholds
		int maxCFNum = 0;
		int maxFNum = 0;
		int cnt = 0;
		for(Integer f1 : enet.getFriends()){
			for(Integer f2 : enet.getFriends()){
				if(f1.intValue() < f2.intValue()){
					int yy = enet.numCommonFriends(f1, f2);
					if(yy >= maxCFNum){

						int x1 = enet.numFriendsInEgonet(f1);
						int x2 = enet.numFriendsInEgonet(f2);
						int mx = (x1 > x2 ? x1 : x2);

						if(yy > maxCFNum || mx > maxFNum){
						   //search item to be replaced in the array:
						   int idx = getMinimumIndex(ret);

						   //replace item
						   ret[idx][0] = f1;
						   ret[idx][1] = f2;
						   ret[idx][2] = new Integer(yy);
						   ret[idx][3] = new Integer(x1);
						   ret[idx][4] = new Integer(x2);

						   //update entry thresholds
							idx = getMinimumIndex(ret);
							maxCFNum = ret[idx][2];
							maxFNum = (ret[idx][3] > ret[idx][4] ? ret[idx][3] : ret[idx][4]);

						   if(cnt < 10){
						   		log("\t\tf1=" + f1 + ", f2=" + f2 + ", maxCFNum=" + maxCFNum + ", maxFNum=" + maxFNum + ", mat=");
						   		log(matrixToString(ret));
						   }
						   cnt++;
						}//end if
					}//end if
				}//end if
			}//next f2
		}//next f1

	   //return what was requested
	   return ret;
	}

	private int getMinimumIndex(Integer[][] mat){
	   int first = Integer.MAX_VALUE;  //least numCommonFriends
	   int second = Integer.MAX_VALUE; //least friends
	   int ret = -1;

	   for(int i=0; i<mat.length; i++){
	      int actFirst = mat[i][2];
	      int actSecond = (mat[i][3] > mat[i][4] ? mat[i][3] : mat[i][4]);
	      if(actFirst < first || (actFirst==first && actSecond < second) ){
	         ret = i;
	         first = actFirst;
	         second = actSecond;
	      }
	   }
	   return ret;
	}

	private int getMaximumIndex(Integer[][] mat){
	   int first = Integer.MIN_VALUE;  //least numCommonFriends
	   int second = Integer.MIN_VALUE; //least friends
	   int ret = -1;

	   for(int i=0; i<mat.length; i++){
	      int actFirst = mat[i][2];
	      int actSecond = (mat[i][3] > mat[i][4] ? mat[i][3] : mat[i][4]);
	      if(actFirst > first || (actFirst==first && actSecond > second) ){
	         ret = i;
	         first = actFirst;
	         second = actSecond;
	      }
	   }
	   return ret;
	}

	private void log(String s){
      if(LOG_ENABLED){System.out.println(s);}
   }

   private String matrixToString(Integer[][] arr){
      String ret = "";
      for(int i=0; i<arr.length; i++){
         String row = "";
         for(int j=0; j<arr[i].length; j++){
            row += (arr[i][j] + " ");
         }
         ret += (row + "\n");
      }
      return ret;
   }

   public int numMutualNonuniqueFeatures(int u1, int u2, Class featType){
      int ret = 0;
      try{
	      HashSet<Feature> s1 = Main.userFeatures.get(new Integer(u1)).getFeaturesByType(featType);
	      HashSet<Feature> s2 = Main.userFeatures.get(new Integer(u2)).getFeaturesByType(featType);
	      for(Feature feat : s1){
	         ret += (s2.contains(feat) ? 1 : 0);
	      }
	      return ret;
      } catch (NullPointerException ne){
    	  //System.out.println("MFCPlus.numMutualNonuniqueFeatures :: NullPointerException for u1=" + u1 + ", u2=" + u2);
    	  return 0;
      }
   }
}