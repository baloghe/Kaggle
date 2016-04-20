package dta;

public class RoutePattern {

	public static final int TYPE_ACCEL = 1;
	public static final int TYPE_DECEL = -1;
	public static final int TYPE_MIXED = 0;
	public static final int TYPE_AFTERSTOP = 10;
	public static final int TYPE_BREAK = 20;
	public static final int TYPE_UNDEF = -100;
	
	//private ArrayList<RouteElement> elems;
	
	private int type;
	
	private double sumAngleDiff;
	private double sumAbsAngleDiff;
	private double prodAccel;
	
	private double minVecLen;
	private double maxVecLen;
	private double sumVecLen;
	private double avgVecLen;
	private double sumVecLenSq;
	private double stdVecLen;
	private double cntIdle;
	private double idleRatio;
	
	private int accelNum;
	private int decelNum;
	private int elemNum;
	
	public RoutePattern(RouteElement e){
		//elems = new ArrayList<RouteElement>();
		
		type = RoutePattern.TYPE_UNDEF;
		accelNum = 0;
		decelNum = 0;
		elemNum = 0;
		
		sumAngleDiff = 0.0;
		sumAbsAngleDiff = 0.0;
		prodAccel = 1.0;
		minVecLen = e.vecLen();
		maxVecLen = e.vecLen();
		sumVecLen = 0.0;
		avgVecLen = 0.0;
		sumVecLenSq = 0.0;
		cntIdle = 0.0;
		idleRatio = 0.0;
		
		addElement(e);
	}
	
	public void addElement(RouteElement e){
		//elems.add(e);
		elemNum++;
		updateStats(e);
	}
	
	private void updateStats(RouteElement e){
		sumAngleDiff += e.vecAngleDiff();
		sumAbsAngleDiff += Math.abs(e.vecAngleDiff());
		prodAccel *= e.accel();
		minVecLen = ( e.vecLen() < minVecLen ? e.vecLen() : minVecLen );
		maxVecLen = ( e.vecLen() > maxVecLen ? e.vecLen() : maxVecLen );
		sumVecLen += e.vecLen();
		avgVecLen = sumVecLen / (double)elemNum;
		sumVecLenSq += ( e.vecLen()*e.vecLen() );
		cntIdle += (elemNum>1 && e.vecLen() > Route.IDLE_METER_THRESHOLD ? 0 : 1);
		idleRatio = cntIdle / (double)elemNum;
	}
	
	public void closeStats(){
		/** eval type */
		this.type = RoutePattern.TYPE_MIXED;
		if(accelNum > 0 && decelNum == 0){
			this.type = RoutePattern.TYPE_ACCEL;
		} else if(accelNum == 0 && decelNum > 0) {
			this.type = RoutePattern.TYPE_DECEL;
		}
		
		/** calc std = e(x^2) - e^2(x) */
		stdVecLen = Math.sqrt(sumVecLenSq/(double)elemNum - avgVecLen*avgVecLen);
		
		/** free space */
		// this.elems=null;
	}
	
	
	public int type(){return type;}
	
	public double sumAngleDiff(){return sumAngleDiff;}
	public double sumAbsAngleDiff(){return sumAbsAngleDiff;}
	public double prodAccel(){return prodAccel;}
	
	public double minVecLen(){return minVecLen;}
	public double maxVecLen(){return maxVecLen;}
	public double sumVecLen(){return sumVecLen;}
	public double avgVecLen(){return avgVecLen;}
	public double sumVecLenSq(){return sumVecLenSq;}
	public double stdVecLen(){return stdVecLen;}
	
	public String toString(){
		return "[size=" + elemNum + ", type=" + type 
				+ ", sumAngleDiff=" + dts(sumAngleDiff)
				+ ", sumAbsAngleDiff=" + dts(sumAbsAngleDiff)
				+ ", prodAccel=" + dts(prodAccel)
				+ ", minVecLen=" + dts(minVecLen)
				+ ", maxVecLen=" + dts(maxVecLen)
				+ ", sumVecLen=" + dts(sumVecLen)
				+ ", avgVecLen=" + dts(avgVecLen)
				+ ", sumVecLenSq=" + dts(sumVecLenSq)
				+ ", stdVecLen=" + dts(stdVecLen)
				+ ", cntIdle=" + dts(cntIdle)
				+ ", idleRatio=" + dts(idleRatio)
				+ "]";
	}
	
	private static String dts(double d){
		return Double.toString(Math.round(d * 1000.0) / 1000.0);
	}
}
