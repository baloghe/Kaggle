package feature;

public class FeatureFactory {
	public FeatureFactory(){}
	public static Feature getInstance(String key, Integer id){
		if(key.equalsIgnoreCase( "work;employer;id")){
			return new FeatureWorkEmployer(id);
		} else if(key.equalsIgnoreCase( "work;location;id")){
			return new FeatureWorkLocation(id);
		} else if(key.equalsIgnoreCase( "work;projects;id")){
			return new FeatureWorkProject(id);
		} else if(key.equalsIgnoreCase( "education;school;id")){
			return new FeatureEducationSchool(id);
		} else if(key.equalsIgnoreCase( "education;classes;id")){
			return new FeatureEducationClass(id);
		} else if(key.equalsIgnoreCase( "education;degree;id")){
			return new FeatureEducationDegree(id);
		} else if(key.equalsIgnoreCase( "languages;id")){
			return new FeatureLanguage(id);
		} else if(key.equalsIgnoreCase( "education;classes;with;id")){
			return new FeatureEducationClassesWith(id);
		} else if(key.equalsIgnoreCase( "education;with;id")){
			return new FeatureEducationWith(id);
		} else if(key.equalsIgnoreCase( "education;year;id")){
			return new FeatureEducationYear(id);
		} else if(key.equalsIgnoreCase( "work;position;id")){
			return new FeatureWorkPosition(id);
		} else if(key.equalsIgnoreCase( "work;projects;with;id")){
			return new FeatureWorkProjectsWith(id);
		} else if(key.equalsIgnoreCase( "work;with;id")){
			return new FeatureWorkWith(id);
		} else {
		   //return new ManualFeature(key, id);
		   return null;
		}
		//return null;
	}

	public ManualFeature getManualFeatureInstance(String elem){
	   elem = elem.trim();
	   int pos = elem.lastIndexOf(";");
		if(pos < 0){System.out.println("elem=" + elem);}
		String key = elem.substring(0, pos);
		Integer value = new Integer(elem.substring(pos+1));
	   return new ManualFeature(key, value);
	}
}
