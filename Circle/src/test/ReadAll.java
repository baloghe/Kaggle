package test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import common.*;
import circle.*;
import feature.*;

public class ReadAll {

	public static ArrayList<UserFeatures> diffFeatPerUser = null;
	public static Egonet ego = null;
	public static UserCircles ucs = null;

	public static void main(String[] args) {
		//INIT REPO
		FeatureRepo.init();

		//read inputs
		readInputs();
		System.out.println("Reading done.");

		//enquiries...
		getSchoolmates(new Integer(239));
		getSchoolmates(new Integer(345));
		getSchoolmates(new Integer(3735));
		getSchoolmates(new Integer(4406));
		System.out.println("Enquiries done.");

		//do it in big...
		HashMap<Integer, ArrayList<HashSet<Integer>>> ucbs = buildCirclesPerSchool();
		circlesToFile("OneCirclePerSchool.csv", ucbs);
		System.out.println("OneCirclePerSchool done.");
	}

	public static void readInputs(){
		Reader<UserFeatures> rd = new Reader<UserFeatures>(UserFeatures.class);
		try{
			//Read features.txt
			diffFeatPerUser = rd.read("features.txt", " ");

			//read Egonets
			ego = new Egonet();
			ego.readFolder("egonets", " ");

			//read circles
			ucs = new UserCircles();
			ucs.readFolder("Circles", " ");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void getSchoolmates(Integer user){
		//obtain friends list
		UserEgonet unet = ego.getEgonet(user);
		HashSet<Integer> friends = unet.getFriends();

		//obtain schools list
		UserFeatures ufs = null;
		for(UserFeatures uf : diffFeatPerUser){
			if(uf.getId() == user.intValue()){
				ufs = uf;
				break;
			}
		}
		//HashSet<Integer> schools = ufs.education_school_ids;
		HashSet<Feature> schools = ufs.getFeaturesByType(FeatureEducationSchool.class);

		//for each friend: drop if none of the schools matches
		HashMap<Integer, HashSet<Integer>> schoolmateList = new HashMap<Integer, HashSet<Integer>>();
		for(Feature school : schools){
			HashSet<Integer> schoolmates = new HashSet<Integer>();
			HashSet<Integer> candidates = FeatureRepo.general_feature_repo.get(school);
			//System.out.println("User=" + user + ", school=" + school.getID() + ", candidate num=" + candidates.size());
			for(Integer friend : friends){
				
				if(FeatureRepo.education_school_ids.get(school.getID()).contains(friend)){
					schoolmates.add(friend);
					//System.out.println("education_school_ids :: friend=" + friend + " added for user=" + user + ", school=" + school.getID());
				}
				
				if(candidates.contains(friend)){
					schoolmates.add(friend);
					//System.out.println("general_feature_repo :: friend=" + friend + " contained for user=" + user + ", school=" + school.getID());
				}
				
			}
			schoolmateList.put(school.getID(), schoolmates);
		}

		//print out results
		for(Entry<Integer, HashSet<Integer>> e : schoolmateList.entrySet()){
			System.out.println("Schoolmates of User=" + user.intValue() + ", schoolID=" + e.getKey() + " -> mates num=" + e.getValue().size());
			/*
			for(Integer u : e.getValue()){
				System.out.println(u.intValue());
			}
			*/
			//print out intersection number with different circles
			//ArrayList<HashSet<Integer>> cs = ucs.getCirclesForUser(user);
			ArrayList<UserCircle> cs = ucs.getCirclesForUser(user);
			int cnt = 0;
			//for(HashSet<Integer> circle : cs){
			for(UserCircle circle : cs){
				HashSet<Integer> intersection = getIntersection(circle.getMembers(), e.getValue());
				System.out.println("  Circle" + (++cnt) + ": " + intersection.size() );
			}
		}


	}

	private static HashSet<Integer> getIntersection(Collection<Integer> a, Collection<Integer> b){
		HashSet<Integer> ret = new HashSet<Integer>();
		for(Integer x : a){
			for(Integer y : b){
				if(x.intValue() == y.intValue()){
					ret.add(x);
				}
			}
		}
		return ret;
	}

	public static HashMap<Integer, ArrayList<HashSet<Integer>>> buildCirclesPerSchool(){
		HashMap<Integer, ArrayList<HashSet<Integer>>> ret = new HashMap<Integer, ArrayList<HashSet<Integer>>>();

		//for all users
		for(Entry<Integer, UserEgonet> elem : ego.getUsers().entrySet()){
			Integer uid = elem.getKey();

			//obtain friends list
			UserEgonet unet = elem.getValue();
			HashSet<Integer> friends = unet.getFriends();

			//obtain schools list
			UserFeatures ufs = null;
			for(UserFeatures uf : diffFeatPerUser){
				if(uf.getId() == uid.intValue()){
					ufs = uf;
					break;
				}
			}
			//HashSet<Integer> schools = ufs.education_school_ids;
			HashSet<Feature> schools = ufs.getFeaturesByType(FeatureEducationSchool.class);

			//for each friend: drop if none of the schools matches
			HashMap<Integer, HashSet<Integer>> schoolmateList = new HashMap<Integer, HashSet<Integer>>();
			//for(Integer school : schools){
			for(Feature school : schools){
				HashSet<Integer> schoolmates = new HashSet<Integer>();
				for(Integer friend : friends){
					/*
					if(FeatureRepo.education_school_ids.get(school.getID()).contains(friend)){
						schoolmates.add(friend);
					}
					*/
					if(FeatureRepo.general_feature_repo.get(school).contains(friend)){
						schoolmates.add(friend);
					}
					
				}
				schoolmateList.put(school.getID(), schoolmates);
			}

			//create the remainder of the friends as a new school with id=-1
			HashSet<Integer> friendsCopy = new HashSet<Integer>();
			for(Integer f : friends){
				friendsCopy.add(new Integer(f.intValue()));
			}
			for(HashSet<Integer> aCircle : schoolmateList.values()){
				friendsCopy.removeAll(aCircle);
			}
			schoolmateList.put(new Integer(-1), friendsCopy);

			//List of circles for THIS user is now in schoolmateList
			ArrayList<HashSet<Integer>> circleList = new ArrayList<HashSet<Integer>>();
			for(HashSet<Integer> aCircle : schoolmateList.values()){
				circleList.add(aCircle);
			}
			ret.put(uid, circleList);
		}//next user

		return ret;
	}

	public static void circlesToFile(String targetFileName, HashMap<Integer, ArrayList<HashSet<Integer>>> circles){

		//remove users in training set
		Collection<Integer> trainingUsers = ucs.getAllCircles().keySet();

		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			writer.println("UserId,Predicted");
			for(Entry<Integer, ArrayList<HashSet<Integer>>> e : circles.entrySet()){
				if(!trainingUsers.contains(e.getKey())){
					String s = e.getKey().intValue() + ",";
					boolean isFirst = true;
					for(HashSet<Integer> circle : e.getValue()){
						boolean isFirstInCircle = true;
						for(Integer f : circle){
							if(isFirstInCircle){
								if(isFirst){
									isFirst = false;
								} else {
									s += ";";  //insert circle separator in front of all circles except the first
								}
								s += f;
								isFirstInCircle = false;
							} else {
								s += (" " + f);
							}
						}
					}//next circle
					writer.println( s.trim() );
				}//end if
			}//next user
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
