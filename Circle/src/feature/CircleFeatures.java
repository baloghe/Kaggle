package feature;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

import circle.*;

public final class CircleFeatures{

   private Integer owner;
   private Integer circleCnt;

   //private HashMap<ManualFeature,HashSet<Integer>> feats;
   private ArrayList<CircleFeatureOutput> feats;

   public CircleFeatures(Integer inOwner, Integer inCcnt){
      owner = inOwner;
      circleCnt = inCcnt;
      //feats = new HashMap<ManualFeature,HashSet<Integer>>();
   }

   public void calc(){
      UserCircle circ = Main.userCircles.getCirclesForUser(owner).get(circleCnt-1);
      if(circ==null){
         System.out.println("CircleFeatures.calc :: circ=null for owner=" + owner + ", cnt=" + circleCnt);
      }
      //otherwise...
      HashSet<Integer> friends = Main.egoNet.getEgonet(owner).getFriends();
      feats = new ArrayList<CircleFeatureOutput>();
      for(Integer f1 : friends){
         for(Integer f2: friends){
            if(f1 < f2){
               processUsers(f1, f2, circ);
            }
         }
      }
   }

   private void processUsers(Integer f1, Integer f2, UserCircle circ){
      HashMap<ManualFeature, Integer> funion = new HashMap<ManualFeature, Integer>();
      FeatureFactory ff = new FeatureFactory();

      //put all features from f1 with value=0
      ArrayList<String> uf1 = Main.userFeatures.get(f1).getOrigFeatures();
      for(String elem : uf1){
         funion.put(ff.getManualFeatureInstance(elem) , new Integer(0) );
      }

      //put all new feature from f2 with value = 0 and overwrite value := 1 for already existing features
      ArrayList<String> uf2 = Main.userFeatures.get(f2).getOrigFeatures();
      for(String elem : uf2){
         ManualFeature mf = ff.getManualFeatureInstance(elem);
         Integer num = funion.get(mf);
         if(num==null){
            funion.put( mf , new Integer(0) );
         } else {
            funion.put( mf , new Integer(1) );
         }//endif
      }//next elem

      //so now we have the union of features and an indication whether they are common for the two users
      //let's find out whether they are in the same circle
      int inSameCircle = (circ.isMember(f1) && circ.isMember(f2) ? 1 : 0);

      //add individual features to the list
      for(Entry<ManualFeature, Integer> elem : funion.entrySet()){
         ManualFeature mf = elem.getKey();
         int comFeat = elem.getValue().intValue();
         CircleFeatureOutput ret = new CircleFeatureOutput(f1,f2,mf,comFeat,inSameCircle);
         //only add features being common for the two users in question
         if(ret.isCommonFeature()){
            this.feats.add(ret);
         }
      }
   }

   public void writeToFile(PrintWriter wrt, String sep){
      for(CircleFeatureOutput cfo : feats){
         String s = owner + sep + circleCnt + sep + cfo.toString(sep);
	      if(wrt==null){
		      System.out.println( s );
		   } else {
		      wrt.println( s );
		   }
      }//next cfo
   }
}