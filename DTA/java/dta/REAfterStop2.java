package dta;

public class REAfterStop2 {

	public static final double[] SECS_TO_MEASURE = new double[]{1.0,2.0,3.0,4.0,5.0};
	//public static final double[] VELOCITIES_TO_HIT = new double[]{1.0};

	private int startSec;
	private double lenSec;
	private double lenMeter;
	private double finalVelocity;
	private boolean[] hitSec;
	private double[] measuredVelocity;

	public REAfterStop2(RouteElement startRE, int inStartSec) {
		init();
		startSec = inStartSec;
		addRE(startRE);
	}

	public REAfterStop2(){
		init();
	}

	private void init(){
		lenSec = 0.0;
		lenMeter = 0.0;
		finalVelocity = 0.0;
		hitSec = new boolean[SECS_TO_MEASURE.length];
		for(int i=0; i<SECS_TO_MEASURE.length; i++){
			hitSec[i] = false;
		}
		measuredVelocity = new double[SECS_TO_MEASURE.length];
	}

	public void addRE(RouteElement inRE){
		lenSec += 1.0;
		lenMeter += inRE.vecLen();
		finalVelocity = inRE.vecLen();
		for(int i=0; i<SECS_TO_MEASURE.length; i++){
			if(hitSec[i]==false && lenSec >= SECS_TO_MEASURE[i]){
				hitSec[i] = true;
				measuredVelocity[i] = lenMeter;
			}
		}//see next threshold
	}

	public int startSec(){return startSec;}

	public double lenSec(){return lenSec;}
	public double lenMeter(){return lenMeter;}
	public double finalVelocity(){return finalVelocity;}

	public boolean[] hitSec(){return hitSec;}
	public double[] measuredVelocity(){return measuredVelocity;}

	public void setParams(REAfterStop2 template){
		this.startSec = template.startSec();
		this.finalVelocity = template.finalVelocity();
		this.lenMeter = template.lenMeter();
		this.lenSec = template.lenSec();
		boolean[] thv = template.hitSec();
		double[] ths = template.measuredVelocity();
		for(int i=0; i<SECS_TO_MEASURE.length; i++){
			this.hitSec[i] = thv[i];
			this.measuredVelocity[i] = ths[i];
		}
	}

	public REAfterStop2 copyRE(){
		REAfterStop2 ret = new REAfterStop2();
		ret.setParams(this);
		return ret;
	}

	public String toString(){
		return "[" + startSec
				+ "," + Util.dts(lenSec)
				+ "," + Util.dts(lenMeter)
				+ "," + Util.dts(finalVelocity)
				+ ",{" + Util.dblVecToString(measuredVelocity)
				+ "}]"
				;
	}
}
