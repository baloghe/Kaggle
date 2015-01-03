package circle;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import feature.*;

public class FeatureRepo {
	public static HashMap<Integer, HashSet<Integer>> work_employer_ids;
	public static HashMap<Integer, HashSet<Integer>> work_location_ids;
	public static HashMap<Integer, HashSet<Integer>> work_projects_ids;
	public static HashMap<Integer, HashSet<Integer>> education_school_ids;
	public static HashMap<Integer, HashSet<Integer>> education_classes_ids;
	public static HashMap<Integer, HashSet<Integer>> education_concentrations_ids;
	public static HashMap<Integer, HashSet<Integer>> education_degree_ids;
	public static HashMap<Integer, HashSet<Integer>> languages_ids;

	public static HashMap<Integer, HashSet<Integer>> education_classes_with_ids;
	public static HashMap<Integer, HashSet<Integer>> education_with_ids;
	public static HashMap<Integer, HashSet<Integer>> education_year_ids;
	public static HashMap<Integer, HashSet<Integer>> work_position_ids;
	public static HashMap<Integer, HashSet<Integer>> work_projects_with_ids;
	public static HashMap<Integer, HashSet<Integer>> work_with_ids;

	public static HashMap<Feature, HashSet<Integer>> general_feature_repo;

	public static void init(){
		work_employer_ids = new HashMap<Integer, HashSet<Integer>>();
		work_location_ids = new HashMap<Integer, HashSet<Integer>>();
		work_projects_ids = new HashMap<Integer, HashSet<Integer>>();
		education_school_ids = new HashMap<Integer, HashSet<Integer>>();
		education_classes_ids = new HashMap<Integer, HashSet<Integer>>();
		education_degree_ids = new HashMap<Integer, HashSet<Integer>>();
		languages_ids = new HashMap<Integer, HashSet<Integer>>();

		education_classes_with_ids = new HashMap<Integer, HashSet<Integer>>();
		education_with_ids = new HashMap<Integer, HashSet<Integer>>();
		education_year_ids = new HashMap<Integer, HashSet<Integer>>();
		work_position_ids = new HashMap<Integer, HashSet<Integer>>();
		work_projects_with_ids = new HashMap<Integer, HashSet<Integer>>();
		work_with_ids = new HashMap<Integer, HashSet<Integer>>();

		general_feature_repo = new HashMap<Feature, HashSet<Integer>>();
	}

	public static void addFeature(Integer targetUser, Feature feat){
		HashMap<Integer, HashSet<Integer>> coll = null;
		if(feat instanceof FeatureEducationClass){
			coll = education_classes_ids;
		} else if(feat instanceof FeatureEducationConcentration){
			coll = education_concentrations_ids;
		} else if(feat instanceof FeatureEducationDegree){
			coll = education_degree_ids;
		} else if(feat instanceof FeatureEducationSchool){
			coll = education_school_ids;
		} else if(feat instanceof FeatureLanguage){
			coll = languages_ids;
		} else if(feat instanceof FeatureWorkEmployer){
			coll = work_employer_ids;
		} else if(feat instanceof FeatureWorkLocation){
			coll = work_location_ids;
		} else if(feat instanceof FeatureWorkProject){
			coll = work_projects_ids;
		} else if(feat instanceof FeatureEducationClassesWith){  //new features from 2014.08.21
			coll = education_classes_with_ids;
		} else if(feat instanceof FeatureEducationWith){
			coll = education_with_ids;
		} else if(feat instanceof FeatureEducationYear){
			coll = education_year_ids;
		} else if(feat instanceof FeatureWorkPosition){
			coll = work_position_ids;
		} else if(feat instanceof FeatureWorkProjectsWith){
			coll = work_projects_with_ids;
		} else if(feat instanceof FeatureWorkWith){
			coll = work_with_ids;
		}

		performAddition(targetUser, feat, coll);
	}

	private static void performAddition(Integer targetUser, Feature feat, HashMap<Integer, HashSet<Integer>> coll){
		if(coll != null){
			HashSet<Integer> us = coll.get(feat.getID());
			if(us==null){
				us = new HashSet<Integer>();
			}
			us.add(targetUser);
			coll.put(feat.getID(), us);
		}
		//general repo
		HashSet<Integer> usgen = general_feature_repo.get(feat);
		if(usgen==null){
			usgen = new HashSet<Integer>();
		}
		usgen.add(targetUser);
		general_feature_repo.put(feat, usgen);
	}

	public static void writeToFile(String targetFileName, HashMap<Integer, HashSet<Integer>> coll, String sep){
		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			for(Entry<Integer, HashSet<Integer>> feature : coll.entrySet()){
				Integer featId = feature.getKey();
				HashSet<Integer> members = feature.getValue();
				for(Integer member : members){
					writer.println(featId.intValue() + sep + member.intValue());
				}
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
