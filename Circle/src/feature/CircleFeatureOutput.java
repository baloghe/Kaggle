package feature;

public final class CircleFeatureOutput{

   private Integer user1;
   private Integer user2;
   private ManualFeature feat;
   private int commonFeat;
   private int sameCircle;

   public CircleFeatureOutput(Integer inU1, Integer inU2, ManualFeature inFeat, int inCommonFeat, int inSameCircle){
      user1 = inU1;
      user2 = inU2;
      feat = inFeat;
      commonFeat = inCommonFeat;
      sameCircle = inSameCircle;
   }

   public String toString(String sep){
      return user1 + sep + user2 + sep + feat.getType() + sep + feat.getID() + sep + commonFeat + sep + sameCircle;
   }

   public boolean isCommonFeature(){return (commonFeat > 0);}
}