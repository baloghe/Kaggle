package feature;

public final class ManualFeature extends Feature{
   public ManualFeature(String featType, Integer featId){
      id = featId;
      type = featType;
   }

    public String getType(){return this.type;}

    public boolean hasRepo(){return false;}
}