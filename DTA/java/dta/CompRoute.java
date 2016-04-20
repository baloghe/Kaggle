package dta;

public class CompRoute {
	public Route r1;
	public Route r2;
	public double dist;
	private String key;
	
	public CompRoute(Route inR1, Route inR2, double inDist){
		r1 = inR1;
		r2= inR2;
		dist = inDist;
		
		String[] k1 = r1.getKey().split("_");
		String[] k2 = r2.getKey().split("_");
		int d1 = new Integer(k1[0]) * 10000 + new Integer(k1[1]);
		int d2 = new Integer(k2[0]) * 10000 + new Integer(k2[1]);
		key = (d1 < d2 ? r1.getKey() + "_" + r2.getKey() : r2.getKey() + "_" + r1.getKey() );
	}
	
	public String getKey(){return key;}
	
	public int hashCode(){
		return key.hashCode();
	}
}