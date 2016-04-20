package dta;

public class RouteCompResult{
	public Integer driver;
	public Integer routeId1;
	public Integer routeId2;
	public double len1;
	public double len2;
	double distance;
	
	public RouteCompResult(Integer inDriver, Integer inRouteID1, Integer inRouteID2
			   , double inLen1, double inLen2, double inDistance){
		driver = inDriver;
		routeId1 = inRouteID1;
		routeId2 = inRouteID2;
		len1 = inLen1;
		len2 = inLen2;
		distance = inDistance;
	}
	
	public double getWeight(){
		return (len1 < len2 ? distance / len1 : distance / len2);
	}
	
	public String toString(){
		return driver + ","+routeId1 + ","+routeId2 
				+ ","+Util.dts(len1) + ","+Util.dts(len2) + ","+Util.dts(distance);
	}
}
