package test;

import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import circle.*;
import feature.*;

public final class CollectLogRegData {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Main.main(new String[]{"-featuresFile=../features.txt"
				              ,"-egonetsDir=../egonets"
				              ,"-circlesDir=../Circles"
				              //,"-model=ByPopularFeatures"
				              ,"-model=noSuch"
				              });
	System.out.println("Data loading finished");

	//tstGetVector(239,275,266,null);
	//tstEgonet(5881,new PrintWriter("data5881.txt", "UTF-8"), ",");
	writeDataToFile("D:\\Temp\\LogRegData.txt", ",");

	System.out.println("Test finished");
	}

	public static void tstGetVector(int owner, int user1, int user2, PrintWriter wrt, String sep){
	   CommonFeaturesVector fv = new CommonFeaturesVector(owner,user1,user2);
	   int[] v = fv.getVector();
	   if(wrt==null){
	      System.out.println(Util.intArrayToString(v,sep));
	   } else {
	      wrt.println(Util.intArrayToString(v,sep));
	   }
	}

	public static void tstEgonet(int owner, PrintWriter wrt, String sep){
	   UserEgonet enet = Main.egoNet.getEgonet(new Integer(owner));
	   HashSet<Integer> friends = enet.getFriends();
	   for(Integer u1 : friends){
	      for(Integer u2 : friends){
	         if(u1.intValue() < u2.intValue()){
	            tstGetVector(owner,u1.intValue(),u2.intValue(), wrt, sep);
	         }
	      }
	   }
	}

	public static void writeDataToFile(String targetFileName, String sep){
	   try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");

			int cnt = 0;
			for(Integer user : Main.userCircles.getAllCircles().keySet()){
			   tstEgonet(user.intValue(), writer, sep);
			   System.out.println((++cnt) + ":\t" + user);
			}

			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}