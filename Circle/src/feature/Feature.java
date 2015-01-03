package feature;

public abstract class Feature{

   protected String type;
   protected Integer id;

   public Feature(){}

   public String getType(){return this.getClass().getName();}
   public Integer getID(){return id;}

   public String toString(){return this.getType() + ":" + id;}
   public int hashCode(){return this.toString().hashCode();}

   public boolean equals(Object obj){
	   if(obj instanceof Feature){
		   return (this.hashCode() == obj.hashCode());
	   } else return false;
   }

   public boolean hasRepo(){return true;}
}