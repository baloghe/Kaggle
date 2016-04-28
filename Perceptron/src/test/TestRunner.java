package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Runs small NN examples:
 * 	TstBerkeleyOnly
 * 	TstCircleInBox
 */
public class TestRunner {
	public static void main(String[] args) {
	   
		Result result;
		
		/* Berkeley homework */
		System.out.println("TestRunner :: Berkeley homework  -- Start");
		result = JUnitCore.runClasses(TstBerkeleyOnly.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("Berkeley homework :: success=" + result.wasSuccessful());
		System.out.println("TestRunner :: Berkeley homework  -- End");
		
		
		/* Circle in a box example */
		System.out.println("TestRunner :: Circle in a box (discretized), TanH  -- Start");
		result = JUnitCore.runClasses(TstCircleInBox.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("Circle in a box :: success=" + result.wasSuccessful());
		System.out.println("TestRunner :: Circle in a box (discretized), TanH  -- End");
	}
}
