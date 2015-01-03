package circle;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.Map.Entry;

import feature.Feature;

public class Util {

	public static boolean LOG_ENABLED = false;

	public static ArrayList<Integer> getArrayList(int[] ints){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i=0; i<ints.length; i++){
			ret.add(new Integer(ints[i]));
		}
		return ret;
	}

	public static HashSet<Feature> getSimilarFeatures(UserFeatures f1, UserFeatures f2){
		HashSet<Feature> ret = new HashSet<Feature>();

		HashSet<Feature> f2f = f2.getFeatures();
		for(Feature f : f1.getFeatures()){
			if(f2f.contains(f)){
				ret.add(f);
			}
		}

		return ret;
	}

	public static void log(String s){
	   if(LOG_ENABLED){System.out.println(s);}
	}

	public static int getCircleDifference(UserCircle c1, UserCircle c2){
		int ret = 0;
		log("\tgetCircleDifference :: circles in comparison:");
		log("\t\tc1=" + c1.toString());
		log("\t\tc2=" + c2.toString());
		for(Integer u : c1.getMembers()){
			if(!c2.getMembers().contains(u)){
				ret++;
			}
		}
		for(Integer u : c2.getMembers()){
			if(!c1.getMembers().contains(u)){
				ret++;
			}
		}
		log("\t\tdiff=" + ret);
		return ret;
	}

	public static int getCircleIntersect(UserCircle c1, UserCircle c2){
		int ret = 0;

		log("\tgetCircleIntersect :: circles in comparison:");
		log("\t\tc1=" + c1.toString());
		log("\t\tc2=" + c2.toString());

		for(Integer u : c1.getMembers()){
			if(c2.getMembers().contains(u)){
				ret++;
			}
		}
		log("\t\tret=" + ret);
		return ret;
	}

	public static int getUserCirclesDifference_OLD(Collection<UserCircle> cs1, Collection<UserCircle> cs2){
		int ret = 0;

		HashSet<UserCircle> hs1 = new HashSet<UserCircle>();
		HashSet<UserCircle> hs2 = new HashSet<UserCircle>();
		hs1.addAll(cs1);
		hs2.addAll(cs2);

		//find best matching pairs
		while( (!hs1.isEmpty() )  && (!hs2.isEmpty()) ){
			int minVal = Integer.MAX_VALUE;
			UserCircle[] minUcp = new UserCircle[2];
			for(UserCircle c1 : hs1){
				for(UserCircle c2 : hs2){
					int actDiff = Util.getCircleDifference(c1, c2);
					if(actDiff < minVal){
						minVal = actDiff;
						minUcp[0] = c1;
						minUcp[1] = c2;
					}
				}//next hs2
			}//next hs1
			ret += minVal;
			log("getUserCirclesDifference :: selected minVal=" + minVal + " -> new ret=" + ret);
			hs1.remove(minUcp[0]);
			hs2.remove(minUcp[1]);
		}//wend hs1 or hs2 becomes empty

		//add the remainder...
		for(UserCircle c : hs1){
		   log("\tgetUserCirclesDifference :: additional unmatched circle (1)=" + c.getMemberCount());
			ret += c.getMemberCount();
		}
		for(UserCircle c : hs2){
		   log("\tgetUserCirclesDifference :: additional unmatched circle (1)=" + c.getMemberCount());
			ret += c.getMemberCount();
		}
		log("\tgetUserCirclesDifference :: TOTAL=" + ret);

		return ret;
	}

	public static int getUserCirclesDifference(Collection<UserCircle> cs1, Collection<UserCircle> cs2){
		int ret = 0;

		HashSet<UserCircle> hs1 = new HashSet<UserCircle>();
		HashSet<UserCircle> hs2 = new HashSet<UserCircle>();
		hs1.addAll(cs1);
		hs2.addAll(cs2);

		//find best matching pairs
		//best matching =
		// first: maximize number of matching members
		// second: minimize edit distance
		while( (!hs1.isEmpty() )  && (!hs2.isEmpty()) ){
			int minVal = Integer.MAX_VALUE;
			int maxMatch = Integer.MIN_VALUE;
			UserCircle[] minUcp = new UserCircle[2];
			for(UserCircle c1 : hs1){
				for(UserCircle c2 : hs2){
					int actDiff = Util.getCircleDifference(c1, c2);
					int actMatch = Util.getCircleIntersect(c1, c2);
					if(actMatch == maxMatch && actDiff < minVal){
						minVal = actDiff;
						minUcp[0] = c1;
						minUcp[1] = c2;
					} else if (actMatch > maxMatch) {
					   maxMatch = actMatch;
					   minVal = actDiff;
						minUcp[0] = c1;
						minUcp[1] = c2;
					}
				}//next hs2
			}//next hs1
			ret += minVal;
			log("getUserCirclesDifference :: selected minVal=" + minVal + ", maxMatch=" + maxMatch + " -> new ret=" + ret);
			hs1.remove(minUcp[0]);
			hs2.remove(minUcp[1]);
		}//wend hs1 or hs2 becomes empty

		//add the remainder...
		for(UserCircle c : hs1){
		   log("\tgetUserCirclesDifference :: additional unmatched circle (1)=" + c.getMemberCount());
			ret += c.getMemberCount();
		}
		for(UserCircle c : hs2){
		   log("\tgetUserCirclesDifference :: additional unmatched circle (1)=" + c.getMemberCount());
			ret += c.getMemberCount();
		}
		log("\tgetUserCirclesDifference :: TOTAL=" + ret);

		return ret;
	}

	public static void printModelEstimates(String targetFileName, Collection<ArrayList<UserCircle>> circles){
		//remove users in training set
		Collection<Integer> trainingUsers = Main.userCircles.getAllCircles().keySet();
		log("printModelEstimates :: circles.size=" + circles.size() );

		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			writer.println("UserId,Predicted");
			for(ArrayList<UserCircle> crs : circles){
				String s = "";
				boolean isFirst = true;
				Integer owner = new Integer(-1);
				for(UserCircle c : crs){
					owner = c.getOwnerID();
					if(!trainingUsers.contains(owner)){

						boolean isFirstInCircle = true;
						for(Integer f : c.getMembers()){
							if(isFirstInCircle){
								if(isFirst){
									s = c.getOwnerID().intValue() + ",";
									isFirst = false;
								} else {
									s += ";";  //insert circle separator in front of all circles except the first
								}
								s += f;
								isFirstInCircle = false;
							} else {
								s += (" " + f);
							}
						}//next member
					}//end if
				}//next circle
				if(!trainingUsers.contains(owner)){
					writer.println( s.trim() );
				}
			}//next user
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String intArrayToString(int[] inArr, String sep){
	   String ret = "";
	   for(int i=0; i<inArr.length; i++){
	      if(i==0){
	         ret += inArr[i];
	      } else {
	         ret += (sep + inArr[i]);
	      }
	   }
	   return ret;
	}

	public static double sigmoid(double z){
	   return 1.0 / (1.0 + Math.exp(z));
	}

	public static double ownExp(double x){
		x = 1d + x / 256d;
		x *= x; x *= x; x *= x; x *= x;
		x *= x; x *= x; x *= x; x *= x;
		return x;

	}
}
