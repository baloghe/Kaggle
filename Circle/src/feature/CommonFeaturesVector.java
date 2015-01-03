package feature;

import circle.*;

import java.util.ArrayList;
import java.util.HashSet;

public class CommonFeaturesVector {

   public int owner;
   public int user1;
   public int user2;

   public CommonFeaturesVector(int inOwner, int inUser1, int inUser2){
      this.owner = inOwner;
      this.user1 = inUser1;
      this.user2 = inUser2;
   }

   public int[] getVector(){

      	//Owner features
		int ownerGender = Main.userFeatures.get(new Integer(owner)).getGender();
		int ownerHometownId = Main.userFeatures.get(new Integer(owner)).getHometownId();
		int ownerUniqueProv = Main.userFeatures.get(new Integer(owner)).numUiqueFeaturesProvided();
		int ownerNonUniqueTypes = Main.userFeatures.get(new Integer(owner)).getNonUniqueFeatures().size();
		int ownerNonUniqueProv = Main.userFeatures.get(new Integer(owner)).numNonuniqueFeaturesProvided();

		int ownerEduClassNum = Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationClass.class).size();
		int ownerEduClassWithNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationClassesWith.class).size();
		int ownerEduConcNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationConcentration.class).size();
		int ownerEduDegreeNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationDegree.class).size();
		int ownerEduSchoolNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationSchool.class).size();
		int ownerEduWithNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationWith.class).size();
		int ownerEduYearNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureEducationYear.class).size();
		int ownerLangNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureLanguage.class).size();
		int ownerWorkEmpNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkEmployer.class).size();
		int ownerWorkLocNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkLocation.class).size();
		int ownerWorkPosNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkPosition.class).size();
		int ownerWorkProjNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkProject.class).size();
		int ownerWorkProjWNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkProjectsWith.class).size();
		int ownerWorkWNum =Main.userFeatures.get(new Integer(owner)).getFeaturesByType(FeatureWorkWith.class).size();

		int ownerFriendNum =Main.egoNet.getEgonet(owner).getFriendsNumber();

		//Mutual measures
		int knowInUserEgonet = (Main.egoNet.getEgonet(owner).knowEachOther(user1, user2) ? 1 : 0);
		int knowInOtherEgonet = Main.egoNet.knowEachOtherInAnotherEgonet(owner,user1,user2);
		int inSameCircleElsewhere = Main.userCircles.inSameCircleElsewhere(owner,user1,user2);
		int commonFriendsEgonet = Main.egoNet.getEgonet(owner).numCommonFriends(user1,user2);

		//shared features num
		int mutGender = getMutualGender(user1,user2);
		int mutHonetownId = getMutualHometownId(user1,user2);
		int mutLocationId = getMutualLocationId(user1,user2);
		int mutPolitical = getMutualPolitical(user1,user2);
		int mutReligion = getMutualReligion(user1,user2);

		int mutEduClassNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationClass.class);
		int mutEduClassWithNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationClassesWith.class);
		int mutEduConcNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationConcentration.class);
		int mutEduDegreeNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationDegree.class);
		int mutEduSchoolNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationSchool.class);
		int mutEduWithNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationWith.class);
		int mutEduYearNum = numMutualNonuniqueFeatures(user1,user2,FeatureEducationYear.class);
		int mutLangNum = numMutualNonuniqueFeatures(user1,user2,FeatureLanguage.class);
		int mutWorkEmpNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkEmployer.class);
		int mutWorkLocNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkLocation.class);
		int mutWorkPosNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkPosition.class);
		int mutWorkProjNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkProject.class);
		int mutWorkProjWNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkProjectsWith.class);
		int mutWorkWNum = numMutualNonuniqueFeatures(user1,user2,FeatureWorkWith.class);

		//Outcome
		int inSameCircle = 0;
		ArrayList<UserCircle> circs = Main.userCircles.getCirclesForUser(new Integer(owner));
		if(circs != null){
			for(UserCircle circ : circs ){
			   if(circ.isMember(new Integer(user1)) && circ.isMember(new Integer(user2))){
			      inSameCircle = 1;
			      break;
			   }
			}//next circ
		}//endif

      return new int[]{
         /*1-10*/
         	 owner
			,user1
			,user2
			,ownerGender
			,ownerHometownId
			,ownerUniqueProv
			,ownerNonUniqueTypes
			,ownerNonUniqueProv
			,ownerEduClassNum
			,ownerEduClassWithNum
			/*11-20*/
			,ownerEduConcNum
			,ownerEduDegreeNum
			,ownerEduSchoolNum
			,ownerEduWithNum
			,ownerEduYearNum
			,ownerLangNum
			,ownerWorkEmpNum
			,ownerWorkLocNum
			,ownerWorkPosNum
			,ownerWorkProjNum
			/*21-30*/
			,ownerWorkProjWNum
			,ownerWorkWNum
			,ownerFriendNum

			,knowInUserEgonet
			,knowInOtherEgonet
			,inSameCircleElsewhere
			,commonFriendsEgonet

			,mutGender
			,mutHonetownId
			,mutLocationId
			/*31-40*/
			,mutPolitical
			,mutReligion
			,mutEduClassNum
			,mutEduClassWithNum
			,mutEduConcNum
			,mutEduDegreeNum
			,mutEduSchoolNum
			,mutEduWithNum
			,mutEduYearNum
			,mutLangNum
			/*41-46*/
			,mutWorkEmpNum
			,mutWorkLocNum
			,mutWorkPosNum
			,mutWorkProjNum
			,mutWorkProjWNum
			,mutWorkWNum
			/*47 == OUTPUT*/
			,inSameCircle
      };

   }

   public int getMutualGender(int u1, int u2){
      int r1 = Main.userFeatures.get(new Integer(u1)).getGender();
      int r2 = Main.userFeatures.get(new Integer(u2)).getGender();
      return (r1>-1 && r1==r2 ? 1 : 0);
   }

   public int getMutualHometownId(int u1, int u2){
      int r1 = Main.userFeatures.get(new Integer(u1)).getHometownId();
      int r2 = Main.userFeatures.get(new Integer(u2)).getHometownId();
      return (r1>-1 && r1==r2 ? 1 : 0);
   }

   public int getMutualLocationId(int u1, int u2){
      int r1 = Main.userFeatures.get(new Integer(u1)).getLocationId();
      int r2 = Main.userFeatures.get(new Integer(u2)).getLocationId();
      return (r1>-1 && r1==r2 ? 1 : 0);
   }

   public int getMutualPolitical(int u1, int u2){
      int r1 = Main.userFeatures.get(new Integer(u1)).getPolitical();
      int r2 = Main.userFeatures.get(new Integer(u2)).getPolitical();
      return (r1>-1 && r1==r2 ? 1 : 0);
   }

   public int getMutualReligion(int u1, int u2){
      int r1 = Main.userFeatures.get(new Integer(u1)).getReligion();
      int r2 = Main.userFeatures.get(new Integer(u2)).getReligion();
      return (r1>-1 && r1==r2 ? 1 : 0);
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
    	  //System.out.println("CommonFeaturesVector :: NullPointerException for u1=" + u1 + ", u2=" + u2);
    	  return 0;
      }
   }
}