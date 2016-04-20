package test;

import dta.RouteElement;

public class TstRouteElement {

	public static void main(String[] args){
		RouteElement r0 = new RouteElement("0.0,0.0",",",null);
		RouteElement r1 = new RouteElement("1.0,0.0",",",r0);
		RouteElement r2 = new RouteElement("2.0,1.0",",",r1);
		RouteElement r3 = new RouteElement("1.0,2.0",",",r2);
		
		System.out.println("r0=" + r0.toString());
		System.out.println("r1=" + r1.toString());
		System.out.println("r2=" + r2.toString());
		System.out.println("r3=" + r3.toString());
	}
	
}
