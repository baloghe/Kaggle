package rftest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rfacade.REngineInstance;
import rfacade.RFacade;

/**
 * Test cases for RFacade
 */
public class TstRFacade {
	private static RFacade r;
	
	@BeforeClass 
	public static void setUpClass() {
		r = new RFacade( (new REngineInstance()) );
	}
	
	@AfterClass public static void tearDownClass() {
		r.off();
	}
	
	@Before
	public void cleanAllVars(){
		r.eval("rm(list=ls())");
	}
		
	@Test
	public void testAddition(){
		
		double d=12+13;
		Assert.assertEquals(d, r.calcDouble("12+13"), 0.001);
	}
	
	@Test
	public void testMultiplication(){
		
		double d=4*5;
		Assert.assertEquals(d, r.calcDouble("4*5"), 0.001);
	}
	
	@Test
	public void testDivision(){
		
		double d=5.0/35.0;
		Assert.assertEquals(d, r.calcDouble("5/35"), 0.001);
	}
	
	@Test
	public void testPower(){
		
		double d=Math.pow(2, 3);
		Assert.assertEquals(d, r.calcDouble("2^3"), 0.001);
	}
	
	@Test
	public void testSqrt(){
		
		double d=Math.sqrt(20);
		Assert.assertEquals(d, r.calcDouble("sqrt(20)"), 0.0);
	}
	
	@Test
	public void testPunktStrich(){
		
		double d=10*3+1;
		Assert.assertEquals(d, r.calcDouble("10*3+1"), 0.0);
	}
	
	@Test
	public void testExp(){
		
		double d=Math.exp(2.0);
		Assert.assertEquals(d, r.calcDouble("exp(2)"), 0.0);
	}
	
	@Test
	public void testTrigo(){
		
		double d=Math.sin(15.0);
		Assert.assertEquals(d, r.calcDouble("sin(15)"), 0.0);
	}
	
	@Test
	public void testDblVec(){
		
		double[] d = new double[]{-0.1, 1.1, 2.3, Double.MIN_VALUE, Double.MAX_VALUE};
		r.addDoubleVector("d", d);
		double[] dback=r.getDoubleVector("d");
		Assert.assertArrayEquals(d, dback, 0.0);
	}
	
	@Test
	public void testMatrix(){
		//double[][] d = { { 1.0D, 2.0D }, { 3.0D, 4.0D } };
		double[][] d = { { 2.0D, 4.0D, 3.0D }, { 1.0D, 5.0D, 7.0D }, { 5.0D, 6.0D, 7.0D } };
		double dsum = 0.0;
		for(int i=0; i<d.length; i++){
			for(int j=0; j<d[i].length; j++){
				dsum += d[i][j];
			}
		}
		
		r.addMatrix("mmm", d);
		double rdsum = r.calcDouble("msum <- sum(mmm)");
		
		Assert.assertEquals(dsum, rdsum, 0.0);
	}
	
	@Test
	public void testDataFrame(){
		double[][] d = { { 2.0D, 4.0D, 3.0D }, { 1.0D, 5.0D, 7.0D }, { 5.0D, 6.0D, 7.0D } };
		double dsum = 0.0;
		for(int i=0; i<d.length; i++){
			for(int j=0; j<d[i].length; j++){
				dsum += d[i][j];
			}
		}
		
		r.addDataFrame("df", null, new String[]{"alma", "bela", "cili"}, d);
		double rdsum = r.calcDouble("msum <- sum(df)");
		
		Assert.assertEquals(dsum, rdsum, 0.0);
	}
}
