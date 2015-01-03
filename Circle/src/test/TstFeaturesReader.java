package test;

import common.*;
import circle.*;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.ArrayList;

public class TstFeaturesReader {
	public static void main(String[] args) {
		//INIT REPO
		FeatureRepo.init();

		Reader<UserFeatures> rd = new Reader<UserFeatures>(UserFeatures.class);
		ArrayList<UserFeatures> lst = null;
		try{
			lst = rd.read("../features.txt", " ");
			System.out.println("Different features per user");
			HashSet<String> nus = new HashSet<String>();
			PrintWriter writer = new PrintWriter("../DiffFeaturesPerUser.txt", "UTF-8");
			for(UserFeatures elem : lst){
				nus.addAll( elem.getNonUniqueFeatures() );
				//System.out.println(elem.getId() + " " + elem.getDifferentFeaturesNum().size());
				writer.println(elem.getId() + " " + elem.getDifferentFeaturesNum().size());
			}
			writer.close();
			System.out.println("NonUniqueFeatures=" + nus.toString());
		} catch(Exception e){
			e.printStackTrace();
		}

		repoToFiles(lst);
	}

	public static void repoToFiles(ArrayList<UserFeatures> lst){
	   //UniqueFeatures
	   try{
	      PrintWriter writer = new PrintWriter("UniqueFeatures.txt", "UTF-8");
		   for(UserFeatures elem : lst){
				writer.println(elem.uniqueFeaturesToString("\t"));
			}
			writer.close();
	   } catch(Exception e){
			e.printStackTrace();
		}

	   //NonUniqueFeatures
	   /*
		FeatureRepo.writeToFile("WorkEmployers.txt", FeatureRepo.work_employer_ids, "\t");
		FeatureRepo.writeToFile("WorkLocations.txt", FeatureRepo.work_location_ids, "\t");
		FeatureRepo.writeToFile("WorkProjects.txt", FeatureRepo.work_projects_ids, "\t");
		FeatureRepo.writeToFile("EducationSchools.txt", FeatureRepo.education_school_ids, "\t");
		FeatureRepo.writeToFile("EducationClasses.txt", FeatureRepo.education_classes_ids, "\t");
		FeatureRepo.writeToFile("EducationDegrees.txt", FeatureRepo.education_degree_ids, "\t");
		FeatureRepo.writeToFile("Languages.txt", FeatureRepo.languages_ids, "\t");
		*/
		FeatureRepo.writeToFile("EducationWith.txt", FeatureRepo.education_with_ids, "\t");
		FeatureRepo.writeToFile("EducationClassesWith.txt", FeatureRepo.education_classes_with_ids, "\t");
		FeatureRepo.writeToFile("WorkWith.txt", FeatureRepo.work_with_ids, "\t");
		FeatureRepo.writeToFile("WorkProjectsWith.txt", FeatureRepo.work_projects_with_ids, "\t");
	}
}
