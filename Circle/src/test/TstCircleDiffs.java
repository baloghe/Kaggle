package test;

import java.util.ArrayList;

import model.*;
import feature.*;
import circle.*;

public class TstCircleDiffs {
	public static void main(String[] args) {
		testCircleDiff();
		testCircleDiff2();
		testUserCirclesDiff();
		testUserCirclesDiff2();
	}

	public static void testCircleDiff(){
		System.out.println("--- testCircleDiff  -- START");

		UserCircle uc1 = new UserCircle(1);
		uc1.addMember(new Integer(10));
		uc1.addMember(new Integer(20));
		uc1.addMember(new Integer(30));

		UserCircle uc2 = new UserCircle(2);
		uc2.addMember(new Integer(20));
		uc2.addMember(new Integer(30));
		uc2.addMember(new Integer(40));

		System.out.println("Diff=" + Util.getCircleDifference(uc1, uc2));

		System.out.println("--- testCircleDiff  -- END");
	}

	public static void testCircleDiff2(){
		System.out.println("--- testCircleDiff2  -- START");

		UserCircle uc1 = new UserCircle(1);
		uc1.addMember(new Integer(10));
		uc1.addMember(new Integer(20));
		uc1.addMember(new Integer(30));

		UserCircle uc2 = new UserCircle(2);
		uc2.addMember(new Integer(20));
		uc2.addMember(new Integer(30));

		System.out.println("Diff=" + Util.getCircleDifference(uc1, uc2));

		System.out.println("--- testCircleDiff2  -- END");
	}

	public static void testUserCirclesDiff(){
		System.out.println("--- testUserCirclesDiff  -- START");

		ArrayList<UserCircle> lst1 = new ArrayList<UserCircle>();
		UserCircle uc11 = new UserCircle(1);
		uc11.addMember(new Integer(10));
		uc11.addMember(new Integer(20));
		uc11.addMember(new Integer(30));
		lst1.add(uc11);

		UserCircle uc12 = new UserCircle(1);
		uc12.addMember(new Integer(20));
		uc12.addMember(new Integer(30));
		lst1.add(uc12);

		UserCircle uc13 = new UserCircle(1);
		uc13.addMember(new Integer(40));
		uc13.addMember(new Integer(50));
		uc13.addMember(new Integer(60));
		lst1.add(uc13);

		ArrayList<UserCircle> lst2 = new ArrayList<UserCircle>();
		UserCircle uc2A = new UserCircle(2);
		uc2A.addMember(new Integer(40));
		uc2A.addMember(new Integer(50));
		lst2.add(uc2A);

		UserCircle uc2B = new UserCircle(2);
		uc2B.addMember(new Integer(10));
		uc2B.addMember(new Integer(20));
		uc2B.addMember(new Integer(30));
		uc2B.addMember(new Integer(40));
		lst2.add(uc2B);

		System.out.println("Diff=" + Util.getUserCirclesDifference(lst1, lst2));

		System.out.println("--- testUserCirclesDiff  -- END");
	}

	public static void testUserCirclesDiff2(){
		System.out.println("--- testUserCirclesDiff2  -- START");

		ArrayList<UserCircle> lst1 = new ArrayList<UserCircle>();
		UserCircle uc11 = new UserCircle(1);
		uc11.addMember(new Integer(10));
		uc11.addMember(new Integer(20));
		lst1.add(uc11);

		UserCircle uc12 = new UserCircle(1);
		uc12.addMember(new Integer(30));
		uc12.addMember(new Integer(40));
		uc12.addMember(new Integer(50));
		uc12.addMember(new Integer(60));
		uc12.addMember(new Integer(70));
		uc12.addMember(new Integer(80));
		uc12.addMember(new Integer(90));
		uc12.addMember(new Integer(100));
		uc12.addMember(new Integer(110));
		lst1.add(uc12);

		ArrayList<UserCircle> lst2 = new ArrayList<UserCircle>();
		UserCircle uc2A = new UserCircle(2);
		uc2A.addMember(new Integer(30));
		uc2A.addMember(new Integer(40));
		lst2.add(uc2A);

		//Util.LOG_ENABLED = true;
		System.out.println("Diff=" + Util.getUserCirclesDifference(lst1, lst2));

		System.out.println("--- testUserCirclesDiff2  -- END");
	}
}
