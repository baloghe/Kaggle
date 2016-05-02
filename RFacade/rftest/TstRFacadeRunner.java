package rftest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TstRFacadeRunner {


	public static void main(String[] args) {
		Result result = JUnitCore.runClasses( TstRFacade.class );
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("TstRFacade success: " + result.wasSuccessful());
	}
}
