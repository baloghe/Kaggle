package test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import circle.*;
import model.*;

public class TstConnectionBased {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Main.main(new String[]{"-featuresFile=../features.txt"
				              ,"-egonetsDir=../egonets"
				              ,"-circlesDir=../Circles"
				              //,"-model=ConnBased"
				              ,"-model=noSuch"
				              });
		System.out.println("Data loading finished");

		//Collection<Integer> testSet = Main.userCircles.getAllCircles().keySet();
		Collection<Integer> testSet = Util.getArrayList(new int[]{611});

		double thrs = 0.9;
		tstConnBased(testSet, thrs);

		System.out.println("Test finished");
	}

	public static void tstConnBased(Collection<Integer> users, double threshold){
		int totTrainDiff = 0;
		for(int user : users){
			ConnectionBased md = new ConnectionBased( threshold);
			ArrayList<UserCircle> circEst = md.getUserCirclesEstimation(user);
			ArrayList<UserCircle> train = Main.userCircles.getCirclesForUser(user);

			if(train != null){
				int diff = Util.getUserCirclesDifference(circEst, train);
				System.out.println(user + "\t" + diff );
				totTrainDiff += diff;
			}
		}//next user
		System.out.println("TOTAL diff=" + totTrainDiff );
	}
}
