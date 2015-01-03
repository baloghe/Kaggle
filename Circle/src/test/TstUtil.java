package test;

import java.util.ArrayList;
import java.util.HashSet;

import circle.*;

public class TstUtil {
	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}
	
	public static void test1(){
		HashSet<ArrayList<UserCircle>> tst = null;
		
		System.out.println("Test 1 - two users with 1-1 circles  -- START");
		tst = new HashSet<ArrayList<UserCircle>>();
		
		ArrayList<UserCircle> lst1 = new ArrayList<UserCircle>();
		UserCircle uc1 = new UserCircle(1);
		uc1.addMember(new Integer(10));
		uc1.addMember(new Integer(20));
		uc1.addMember(new Integer(30));
		lst1.add(uc1);
		tst.add(lst1);
		
		ArrayList<UserCircle> lst2 = new ArrayList<UserCircle>();
		UserCircle uc2 = new UserCircle(2);
		uc2.addMember(new Integer(20));
		uc2.addMember(new Integer(30));
		uc2.addMember(new Integer(40));
		lst2.add(uc2);
		tst.add(lst2);
		
		Util.printModelEstimates("tst1.txt", tst);
		System.out.println("Test 1 - two users with 1-1 circles  -- END");
	}
	
	public static void test2(){
		HashSet<ArrayList<UserCircle>> tst = null;
		
		System.out.println("Test 2 - one users with 3 circles  -- START");
		tst = new HashSet<ArrayList<UserCircle>>();
		
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
		
		tst.add(lst1);
		
		Util.printModelEstimates("tst2.txt", tst);
		System.out.println("Test 2 - one users with two circles  -- END");
	}
	
	public static void test3(){
		HashSet<ArrayList<UserCircle>> tst = null;
		
		System.out.println("Test 3 - 2 users with >1 circles  -- START");
		tst = new HashSet<ArrayList<UserCircle>>();
		
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
		
		tst.add(lst1);
		tst.add(lst2);
		
		Util.printModelEstimates("tst3.txt", tst);
		
		System.out.println("Test 3 - 2 users with >1 circles  -- END");
	}
}
