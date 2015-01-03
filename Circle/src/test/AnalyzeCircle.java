package test;

import feature.*;
import circle.*;

public final class AnalyzeCircle{

   public static void main(String[] args) {
		Main.main(new String[]{"-featuresFile=../features.txt"
				              ,"-egonetsDir=../egonets"
				              ,"-circlesDir=../Circles"
				              //,"-model=ByPopularFeatures"
				              ,"-model=noSuch"
				              });
	System.out.println("Data loading finished");

	tstCircle(345, 1);  //2738 has only 37 friends forming 4 circles


	System.out.println("Test finished");
	}

   public static void tstCircle(int user, int circ){
      CircleFeatures cf = new CircleFeatures(new Integer(user),new Integer(circ));
      System.out.println("calc() started");
      cf.calc();
      System.out.println("calc() finished");
      cf.writeToFile(null, "\t");
   }
}