package test;

import dta.*;

public class TstRoutePattern {

	public static void main(String[] args){
		
		tstAccel();
		tstDecel();
		tstIdle();
	}
	
	public static void tstAccel(){
				
		RouteElement r0 = new RouteElement("0.0,0.0",",",null);
		RouteElement r1 = new RouteElement("0.0,1.0",",",r0);
		RouteElement r2 = new RouteElement("1.0,2.0",",",r1);
		RouteElement r3 = new RouteElement("3.0,2.0",",",r2);
		RouteElement r4 = new RouteElement("5.0,1.0",",",r3);
		RouteElement r5= new RouteElement("7.0,-1.0",",",r4);
		RouteElement r6 = new RouteElement("7.0,-4.0",",",r5);
		RouteElement r7 = new RouteElement("4.0,-3.0",",",r6);
		RouteElement r8 = new RouteElement("1.0,-1.0",",",r7);
		RouteElement r9 = new RouteElement("0.0,4.0",",",r8);
		RouteElement r10 = new RouteElement("0.0,10.0",",",r9);
		
		RouteElement[] elems = new RouteElement[]{
				 r1,r2,r3,r4,r5,r6,r7,r8,r9,r10
			};
		
		RoutePattern rp = null;
		for(RouteElement e : elems){
			System.out.println("e=" + e);
			if(rp == null){
				rp = new RoutePattern(e);
			} else {
				rp.addElement(e);
			}
			System.out.println("rp=" + rp);
		}
		
		rp.closeStats();
		System.out.println("FINALLY ACCEL");
		System.out.println("rp=" + rp);
		
	}
	
	public static void tstDecel(){
		
		RouteElement r0 = new RouteElement("0.0,10.0",",",null);
		RouteElement r1 = new RouteElement("0.0,4.0",",",r0);
		RouteElement r2 = new RouteElement("1.0,-1.0",",",r1);
		RouteElement r3 = new RouteElement("4.0,-3.0",",",r2);
		RouteElement r4 = new RouteElement("7.0,-4.0",",",r3);
		RouteElement r5= new RouteElement("7.0,-1.0",",",r4);
		RouteElement r6 = new RouteElement("5.0,1.0",",",r5);
		RouteElement r7 = new RouteElement("3.0,2.0",",",r6);
		RouteElement r8 = new RouteElement("1.0,2.0",",",r7);
		RouteElement r9 = new RouteElement("0.0,1.0",",",r8);
		RouteElement r10 = new RouteElement("0.0,0.0",",",r9);
		
		RouteElement[] elems = new RouteElement[]{
				 r1,r2,r3,r4,r5,r6,r7,r8,r9,r10
			};
		
		RoutePattern rp = null;
		for(RouteElement e : elems){
			System.out.println("e=" + e);
			if(rp == null){
				rp = new RoutePattern(e);
			} else {
				rp.addElement(e);
			}
			System.out.println("rp=" + rp);
		}
		
		rp.closeStats();
		System.out.println("FINALLY DECEL");
		System.out.println("rp=" + rp);
	}
	
	public static void tstIdle(){
		RouteElement r0 = new RouteElement("0.0,0.0",",",null);
		RouteElement r1 = new RouteElement("0.0,0.5",",",r0);
		RouteElement r2 = new RouteElement("0.0,1.0",",",r1);
		RouteElement r3 = new RouteElement("1.0,1.0",",",r2);
		RouteElement r4 = new RouteElement("1.0,1.0",",",r3);
		RouteElement r5= new RouteElement("2.0,1.0",",",r4);
		
		RouteElement[] elems = new RouteElement[]{
				 r1,r2,r3,r4,r5
			};
		
		RoutePattern rp = null;
		for(RouteElement e : elems){
			System.out.println("e=" + e);
			if(rp == null){
				rp = new RoutePattern(e);
			} else {
				rp.addElement(e);
			}
			System.out.println("rp=" + rp);
		}
		
		rp.closeStats();
		System.out.println("FINALLY IDLE");
		System.out.println("rp=" + rp);
	}
}
