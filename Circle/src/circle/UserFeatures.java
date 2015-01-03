package circle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ArrayList;

import common.InitFromString;
import feature.*;

public class UserFeatures implements InitFromString {

	private int id;
	private HashMap<String, Integer> featurenum;
	private HashSet<String> nonUniqueFeatures;

	private String origString;
	private String origSep;

	//Unique features
	private int gender;
	private int hometown_id;
	private int location_id;
	private int political;
	private int religion;

	//Nonunique features
	private HashSet<Feature> features;

	public void init(String inStr, String sep) {
		featurenum = new HashMap<String, Integer>();
		nonUniqueFeatures = new HashSet<String>();
		origString = inStr;
		origSep = sep;

		//init unique features with -1
		gender = -1;
		hometown_id = -1;
		location_id = -1;
		political = -1;
		religion = -1;

		//create nonunique features' collections
		features = new HashSet<Feature>();

		boolean isFirst = true;
		String[] strvec = inStr.split(sep);
		for(String elem : strvec){
			if(isFirst){
				this.id = (new Integer(elem.trim())).intValue();
				isFirst = false;
				//System.out.println("ID: " + this.id);
			} else {
				int pos = elem.lastIndexOf(";");
				if(pos < 0){System.out.println("elem=" + elem);}
				String key = elem.substring(0, pos);
				Integer value = new Integer(elem.substring(pos+1));
				this.addFeature(key, value);
				//System.out.println("   feature added: " + key + " -> " + value);
			}
		}
	}

	protected void addFeature(String key, Integer value){
		if(featurenum.containsKey(key)){
			int cnt = featurenum.get(key).intValue();
			featurenum.put(key, new Integer(cnt+1));
			nonUniqueFeatures.add(key);
		} else {
			featurenum.put(key, new Integer(1));
		}

		//Anyway have a closer look at the feature...

		//switch(key){
			//Unique?
			if(key.equalsIgnoreCase("gender")){
				gender = value.intValue();
				features.add(new ManualFeature(key, value));
			} else if(key.equalsIgnoreCase("hometown;id")){
				hometown_id = value.intValue();
				features.add(new ManualFeature(key, value));
			} else if(key.equalsIgnoreCase( "location;id")){
				location_id = value.intValue();
				features.add(new ManualFeature(key, value));
			} else if(key.equalsIgnoreCase( "political")){
				political = value.intValue();
				features.add(new ManualFeature(key, value));
			} else if(key.equalsIgnoreCase( "religion")){
				religion = value.intValue();
				features.add(new ManualFeature(key, value));

			//Nonunique?
			} else {
				Feature feat = FeatureFactory.getInstance(key, value);
				if(feat != null){
					features.add(feat);
					if(feat.hasRepo()){
						FeatureRepo.addFeature(new Integer(this.id), feat);
					}
				}
			}
		//} //end switch

	}


	public int getId(){return this.id;}

	public HashSet<String> getNonUniqueFeatures(){return nonUniqueFeatures;}

	public HashMap<String, Integer> getDifferentFeaturesNum(){return featurenum;}

	//UniqueFeatures
	public int getGender(){return gender;}
	public int getHometownId(){return hometown_id;}
	public int getLocationId(){return location_id;}
	public int getPolitical(){return political;}
	public int getReligion(){return religion;}

	public int numUiqueFeaturesProvided(){
	   return (gender > -1 ? 1 : 0)
	         +(hometown_id > -1 ? 1 : 0)
	         +(location_id > -1 ? 1 : 0)
	         +(political > -1 ? 1 : 0)
	         +(religion > -1 ? 1 : 0)
	         ;
	}

	public String uniqueFeaturesToString(String sep){
	   return (this.id + sep + this.gender + sep + this.hometown_id + sep + this.location_id + sep + this.political + sep + this.religion);
	}

	//NonUniqueFeatures
	public HashSet<Feature> getFeatures(){return features;}

	public int numNonuniqueFeaturesProvided(){
	   int ret = 0;
	   for(Entry<String,Integer> elem : featurenum.entrySet()){
	      ret += elem.getValue().intValue();
	   }
	   return ret;
	}

	public HashSet<Feature> getFeaturesByType(Class ftype){
		HashSet<Feature> ret = new HashSet<Feature>();
		for(Feature f : features){
			//System.out.println("f=" + f.toString());
			if(f.getClass() == ftype){
				ret.add(f);
				//System.out.println("getFeaturesByType :: id=" + id + ", f=" + f.toString());
			}
		}
		return ret;
	}

	public int hashCode(){return id;}

	public boolean equals(Object obj){
		if(obj instanceof Integer){
			return (id == ((Integer)obj).intValue());
		} else if(obj instanceof UserFeatures) {
			return (id == ((UserFeatures)obj).getId());
		} else return false;
	}

	public ArrayList<String> getOrigFeatures(){
	   ArrayList<String> ret = new ArrayList<String>();

	   boolean isFirst = true;
		String[] strvec = origString.split(origSep);
		for(String elem : strvec){
			if(isFirst){
				isFirst = false;
			} else {
			   /*
				int pos = elem.lastIndexOf(";");
				if(pos < 0){System.out.println("elem=" + elem);}
				String key = elem.substring(0, pos);
				Integer value = new Integer(elem.substring(pos+1));
				this.addFeature(key, value);
				*/
				ret.add(elem.trim());
			}
		}

	   return ret;
	}
}
