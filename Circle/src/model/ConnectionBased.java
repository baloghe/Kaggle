package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.PriorityQueue;

import circle.*;
import feature.*;

public final class ConnectionBased extends Model {

	public static String MODEL_NAME = "CONNECTION_BASED";

   private static double[] coeffs = new  double[]{
        -2.670570
		, -1.581962
		, 0.000139
		, 0.219939
		, 0.090103
		, 0.048392
		, 3.262131
		, -0.652349
		, 0.000000
		, -0.527345
		, -0.490352
		, -0.459775
		, 0.355523
		, 0.221732
		, -0.848211
		, 0.708652
		, -0.420028
		, 0.000000
		, 0.000000
		, -6.368487
		, -0.002780
		, 1.330782
		, 0.947370
		, -1.595533
		, 0.075834
		, 0.097949
		, 0.682429
		, 0.324640
		, -6.540980
		, 0.013506
		, 0.287467
		, -1.379061
		, 0.000000
		, 0.626762
		, 0.872874
		, -1.006580
		, 0.025944
		, -0.052242
		, 1.264223
		, -0.007710
		, 0.215593
		, -0.002854
		, -1.700979
		, -1.660947
   };

	private double connThreshold;

	public static int MAX_TRIALS = 0;
	public static int CIRCLE_MIN_MEMBER_COUNT = 1;
	public static double THRESHOLD_LOWERING = 0.9;

   private HashMap<String, Double> connections;
   private HashSet<Integer> usedFriends;
   private HashSet<String> circleStarters;

   public ConnectionBased(double inConnThreshold){
      connThreshold = inConnThreshold;
      usedFriends = new HashSet<Integer>();
      circleStarters = new HashSet<String>();
   }

   public ArrayList<UserCircle> getUserCirclesEstimation(Integer user){
      ArrayList<UserCircle> ret = new ArrayList<UserCircle>();

      calcConnections(user);
      usedFriends = new HashSet<Integer>();
      circleStarters = new HashSet<String>();

      int cnt = 0;
      int trialCnt = 0;
      //System.out.println("getUserCirclesEstimation :: look for first circle, user=" + user);
      UserCircle c = findNextCircleGreedy(user);
      //1 trial at the beginning too...
      if(c == null && trialCnt < ConnectionBased.MAX_TRIALS){
		  connThreshold *= ConnectionBased.THRESHOLD_LOWERING;
		  trialCnt++;
		  //System.out.println("   getUserCirclesEstimation :: first circle=null, lower threshold! user=" + user + ", new trialCnt="+trialCnt);
		  c = findNextCircleGreedy(user);
	  }

      //start loop
      //System.out.println("getUserCirclesEstimation :: START LOOP, user=" + user + ", trialCnt=" + trialCnt);
      while(c != null){
         //System.out.println("   getUserCirclesEstimation :: digest estimated circle, user=" + user);
         //add members of the circle to "used friends" in all cases
         usedFriends.addAll(c.getMembers());
    	  //perform addition only if there's enough members
    	  if(c.getMemberCount() >= CIRCLE_MIN_MEMBER_COUNT){
	    	  ret.add(c);
	    	  //usedFriends.addAll(c.getMembers());
	    	  //System.out.println("   "+(++cnt)+". circle: "+c.getMemberCount()+" members, usedFriends.size="+usedFriends.size());

    	  //enhancement: member < 6 or cnt(circles) < 4 => lower threshold max MAX_TRIALS times:
    	  } else if(c.getMemberCount() < CIRCLE_MIN_MEMBER_COUNT && trialCnt < ConnectionBased.MAX_TRIALS){
    	     //remove only if we can lower the threshold. Do not remove if we can't do that
    	     if(trialCnt<MAX_TRIALS){
    		  	usedFriends.removeAll(c.getMembers());
    	     }
    		  connThreshold *= ConnectionBased.THRESHOLD_LOWERING;
    		  trialCnt++;
    		  //System.out.println("      getUserCirclesEstimation :: circle too small, lower threshold! user=" + user + ", new trialCnt="+trialCnt);
    	  }

    	  //next
    	  //System.out.println("   IN LOOP: getUserCirclesEstimation :: look for next circle, user=" + user + ", trialCnt=" + trialCnt);
    	  c = findNextCircleGreedy(user);
    	  //if there isn't enough circles and we have also triels left then...
    	  if(c == null && ret.size() < 4 && trialCnt < ConnectionBased.MAX_TRIALS){
    		  connThreshold *= ConnectionBased.THRESHOLD_LOWERING;
    		  trialCnt++;
    		  //System.out.println("      IN LOOP: getUserCirclesEstimation :: circle is null, lower threshold! user=" + user + ", new trialCnt="+trialCnt);
    		  c = findNextCircleGreedy(user);
    	  }
      }
      //System.out.println("getUserCirclesEstimation :: LOOP FINISHED, user=" + user);

      //TBD: if still no circle have been found, add first friend to a single circle
      if(ret.size()<1){
    	  //Model m = new AllFriendsInOneCircle();
    	  //ret = m.getUserCirclesEstimation(user);
    	  UserCircle cc = new UserCircle( new Integer(user) );
    	  cc.addMember( new Integer(user+1) );
    	  ret.add(cc);
    	  //System.out.println("getUserCirclesEstimation :: Loop finished but no circle found => add {USER+1} as a circle, user=" + user);
      }

      return ret;
   }

	public Integer[] getMostPopularPair(Integer owner){
	   if(connections==null){
	      return null;
	   }

	   //select max(conn)
      double maxP = 0.0;
      String maxKey = "";
      Integer f1 = new Integer(-1);
      Integer f2 = new Integer(-1);
      for(Entry<String, Double> elem : connections.entrySet()){
         String key = elem.getKey();
         Integer x1 = new Integer( key.substring(0, key.indexOf(";")) );
         Integer x2 = new Integer( key.substring(key.indexOf(";")+1) );
         if( (!usedFriends.contains(x1)) && (!usedFriends.contains(x2))
         		&& (!circleStarters.contains(key))
         		&& elem.getValue().doubleValue() > maxP){
            maxP = elem.getValue().doubleValue();
            maxKey = key;
            f1 = x1;
            f2 = x2;
         }//end if
      }//next elem

	   return new Integer[]{f1,f2};
	}

	public Integer getMostConnectedFriend(Integer owner, Integer friend){
	   if(connections==null){
	      return null;
	   }

	   //select max(conn)
      double maxP = 0.0;
      String maxKey = "";
      Integer ff = new Integer(-1);
      for(Entry<String, Double> elem : connections.entrySet()){
         String key = elem.getKey();
         Integer x1 = new Integer( key.substring(0, key.indexOf(";")) );
         Integer x2 = new Integer( key.substring(key.indexOf(";")+1) );
         if( (!usedFriends.contains(x1)) && (!usedFriends.contains(x2))
         		&& (!circleStarters.contains(key))
         		&& (x1.intValue()==friend.intValue() || x2.intValue()==friend.intValue())
         		&& elem.getValue().doubleValue() > maxP){
            maxP = elem.getValue().doubleValue();
            maxKey = key;
            if(x1.intValue()==friend.intValue()){
               ff = x2;
            } else {
               ff = x1;
            }
         }//end if
      }//next elem

	   return ff;
	}

   private UserCircle findNextCircleGreedy(Integer owner){
      //select max(conn)
      double maxP = 0.0;
      String maxKey = "";
      Integer f1 = new Integer(-1);
      Integer f2 = new Integer(-1);
      for(Entry<String, Double> elem : connections.entrySet()){
         String key = elem.getKey();
         Integer x1 = new Integer( key.substring(0, key.indexOf(";")) );
         Integer x2 = new Integer( key.substring(key.indexOf(";")+1) );
         if( (!usedFriends.contains(x1)) && (!usedFriends.contains(x2))
         		&& (!circleStarters.contains(key))
         		&& elem.getValue().doubleValue() > maxP){
            maxP = elem.getValue().doubleValue();
            maxKey = key;
            f1 = x1;
            f2 = x2;
         }//end if
      }//next elem
      //System.out.println("         findNextCircleGreedy :: maxP="+maxP+", threshold="+connThreshold+", f1="+f1+", f2="+f2+", user=" + owner);

      //Assert we've found something
      if(f1.intValue() <= 0 || f2.intValue() <= 0){
         //System.out.println("         findNextCircleGreedy :: no two connected users found in Egonet for user=" + owner);
         return null;
      } else if(maxP <= connThreshold){
         //System.out.println("         findNextCircleGreedy :: too low maxP=" + maxP + " <= " + connThreshold);
         return null;
      }

      //put top 2 into the queue
      PriorityQueue<PairIntegerDouble> pq = new PriorityQueue<PairIntegerDouble>(10, new PairIntegerDoubleComparator());
      pq.add( new PairIntegerDouble(f1, new Double(maxP)) );
      pq.add( new PairIntegerDouble(f2, new Double(maxP)) );
      circleStarters.add(maxKey);

      //while the queue is not empty:
      //  get out top element and put it into the circle
      //  add friends being connected to it with p>threshold to the queue
      UserCircle c = new UserCircle( new Integer(owner) );
      HashSet<Integer> locUsedFriends = new HashSet<Integer>();
      //System.out.println("         findNextCircleGreedy :: New circle");
      while(!pq.isEmpty()){
         Integer f = pq.poll().key;

         //calc & display new member's avg conn
         double avgConn = getAvgConnectivity(c,f);

         //perform addition ONLY if avgConn > threshold
         if(c.getMemberCount()<1 || avgConn > connThreshold){
        	 //System.out.println("\tnew member: " + f + " -> avgConn=" + avgConn );
	         c.addMember(f);

	         //search for new members based on their connectivity to the new circle member
	          for(Entry<String, Double> elem : connections.entrySet()){
	             String key = elem.getKey();
	             Integer x1 = new Integer( key.substring(0, key.indexOf(";")) );
		         Integer x2 = new Integer( key.substring(key.indexOf(";")+1) );
		         if( (!usedFriends.contains(x1)) && (!usedFriends.contains(x2))
		              && (!locUsedFriends.contains(x1)) &&( !locUsedFriends.contains(x2))
		         		&& elem.getValue().doubleValue() > connThreshold
		         		&& (x1.intValue()==f.intValue() || x2.intValue()==f.intValue() ) ){
		            if(x1.intValue()==f.intValue()){
		               pq.add( new PairIntegerDouble(x2, elem.getValue() ) );
		               locUsedFriends.add(x2);
		            } else {
		               pq.add( new PairIntegerDouble(x1, elem.getValue() ) );
		               locUsedFriends.add(x1);
		            }
		         }//endif
	          }//next elem in connections.entrySet()
         }//endif avgConn > connThreshold
      }//wend

      //return something
      if(c.getMemberCount() > 0){
         return c;
      } else {
         return null;
      }
   }

   public void calcConnections(Integer user){
      connections = new HashMap<String, Double>();
      //walk through the user's egonet and estimate friend relationships
      UserEgonet enet = Main.egoNet.getEgonet(user);
      Collection<Integer> friends = enet.getFriends();
      for(Integer f1 : friends){
         for(Integer f2 : friends){
            if(f1.intValue() < f2.intValue()){
            	String key = f1+";"+f2;
            	connections.put(key, new Double(estRelationship(user, f1, f2)));
            }
         }//next f2
      }//next f1
   }

   public double getAvgConnectivity(UserCircle circ, Integer newMemb){
	   double ret = 0.0;
	   int cnt = 0;
       for(Integer memb : circ.getMembers()){
    	   ret += getConnection(memb, newMemb);
    	   cnt++;
       }
       if(cnt>0){
    	   ret /= (double)cnt;
       }
       return ret;
   }

   private double getConnection(Integer user1, Integer user2){
      String key = user1+";"+user2;
      if(user2.intValue() < user1.intValue()){
    	  key = user2+";"+user1;
      }
      if(connections==null){
         return -1;
      }
      Double d = connections.get(key);
      if(d==null){
         return 0;
      } else {
         return d.doubleValue();
      }
   }

   public double estRelationship(Integer owner, Integer f1, Integer f2){
      CommonFeaturesVector fvec = new CommonFeaturesVector(owner, f1, f2);
      int[] vars = fvec.getVector();
      double dd = ConnectionBased.coeffs[0]; //intercept
      for(int i=1; i<ConnectionBased.coeffs.length; i++){
         dd += ( (double)vars[i+2] ) * ConnectionBased.coeffs[i]; //add 2 to skip owner, user1, user2
      }//next i
      //take sigmoid
      dd = Util.sigmoid(dd);
      return dd;
   }
}