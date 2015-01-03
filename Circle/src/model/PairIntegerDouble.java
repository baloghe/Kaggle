package model;

public final class PairIntegerDouble implements Comparable {

   public Integer key;
   public Double value;

   public PairIntegerDouble(Integer k, Double v){
      key = k;
      value = v;
   }

   public int compareTo(Object o){
      return this.value.compareTo(((PairIntegerDouble)o).value);
   }
}