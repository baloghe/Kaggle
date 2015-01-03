package feature;

import circle.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map.Entry;

public class CircleFeaturesVector {
	public int owner;
	public UserCircle circle;
	
	public CircleFeaturesVector(int inOwner, UserCircle c){
		owner = inOwner;
		circle = c;
	}
	
	public HashMap<Feature, Integer> getVector(int thrs){
		HashMap<Feature, Integer> full = new HashMap<Feature, Integer>();
		Class[] featTypes = new Class[]{FeatureEducationClass.class
				,FeatureEducationConcentration.class
				,FeatureEducationDegree.class
				,FeatureEducationSchool.class
				,FeatureEducationYear.class
				,FeatureLanguage.class
				,FeatureWorkLocation.class
				,FeatureWorkEmployer.class
				,FeatureWorkPosition.class
				,FeatureWorkProject.class};
		
		for(Integer u : circle.getMembers()){
			for(Class ftp : featTypes){
				UserFeatures uf = Main.userFeatures.get(new Integer(u));
				if(uf != null){
					HashSet<Feature> s = Main.userFeatures.get(new Integer(u)).getFeaturesByType(ftp);
					for(Feature f : s){
						Integer ii = full.get(f);
						if(ii == null){
							ii = new Integer(1);
						} else {
							ii = new Integer(ii.intValue() + 1);
						}
						full.put(f, ii);
					}//next f
				}//uf was null
			}//next ftp
		}//next u
		
		HashMap<Feature, Integer> ret = new HashMap<Feature, Integer>();
		for(Feature f : full.keySet()){
			Integer ii = full.get(f);
			if(ii.intValue() >= thrs){
				ret.put(f, ii);
			}//endif
		}//next f
		return ret;
	}

}
