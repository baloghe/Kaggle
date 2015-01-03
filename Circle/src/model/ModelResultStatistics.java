package model;

import java.util.ArrayList;
import java.util.HashSet;

import circle.*;

public class ModelResultStatistics {

   private String model;
   private Integer user;

   private int estCircleNum;
   private int estFriendsInCircleNum;
   private int estFIC_falsePos;
   private int estFIC_falseNeg;

   private int trueFriendsNum;
   private int trueCircleNum;
   private int trueFriendsInCircleNum;

   private int diffPoints;

   public ModelResultStatistics(String inModel, Integer inUser){
      model = inModel;
      user = inUser;

      calcTrueValues();
   }

   public void setEstimations(ArrayList<UserCircle> cs){
      estCircleNum = cs.size();
      estFriendsInCircleNum = calcFriendsInCirclesNum(cs);
      diffPoints = calcDiffPoints(cs);

      //calc FIC false positives and negatives
      ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
      if(train != null){
         //gather users
         HashSet<Integer> ficTrain = new HashSet<Integer>();
         HashSet<Integer> ficEst = new HashSet<Integer>();
         for(UserCircle c : train){
            ficTrain.addAll(c.getMembers());
         }
         for(UserCircle c : cs){
            ficEst.addAll(c.getMembers());
         }
         //do comparison
         estFIC_falsePos = 0;
         estFIC_falseNeg = 0;
         for(Integer f : ficEst){
            if(!ficTrain.contains(f)){
               estFIC_falsePos++;
            }
         }//next friend in ficTrain
         for(Integer f : ficTrain){
            if(!ficEst.contains(f)){
               estFIC_falseNeg++;
            }
         }//next friend in ficEst
      }
   }

	private int calcDiffPoints(ArrayList<UserCircle> cs){
	   int ret = 0;
	   ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);
      if(train != null){
      		ret = Util.getUserCirclesDifference(cs, train);
      }
      return ret;
	}

   private void calcTrueValues(){
      //get friends number
      UserEgonet enet = Main.egoNet.getEgonet(user);
      if(enet != null){
         trueFriendsNum = enet.getFriendsNumber();
      } else {
         System.out.println("User=" + user + ": Friends number cannot be retrieved!");
         trueFriendsNum = 0;
      }

      trueCircleNum = 0;
      trueFriendsInCircleNum = 0;

      ArrayList<UserCircle> cs = Main.userCircles.getCirclesForUser(user);
      if(cs != null){
         trueCircleNum = cs.size();
      } else {
         //nothing to check here...
         System.out.println("User=" + user + ": True circles cannot be retrieved!");
         return;
      }

      trueFriendsInCircleNum = calcFriendsInCirclesNum(cs);
   }

   public int getDiffPoints(){return diffPoints;}

   private int calcFriendsInCirclesNum(ArrayList<UserCircle> cs){
      HashSet<Integer> fic = new HashSet<Integer>();
      for(UserCircle c : cs){
         fic.addAll(c.getMembers());
      }
      return fic.size();
   }

   public String toString(String sep, boolean showUser, boolean showModel){
      String ret = "";
      if(showUser){
         ret += (user + sep);
      }
      if(showModel){
         ret += (model + sep);
      }

      //true figures
      ret += (trueFriendsNum + sep);
      ret += (trueCircleNum + sep);
      ret += (trueFriendsInCircleNum + sep);
      ret += (estCircleNum + sep);
      ret += (estFIC_falsePos + sep);
      ret += (estFIC_falseNeg + sep);
      ret += (diffPoints + sep);

      return ret;
   }

   public String toString(){return toString(";",true,true);}
}