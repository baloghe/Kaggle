package dta;

public class REAfterStop {

	public static final double[] VELOCITIES_TO_HIT = new double[]{0.416,1.667,3.75,6.667,10.416,15.0};
	//public static final double[] VELOCITIES_TO_HIT = new double[]{1.0};

	private int startSec;
	private double lenSec;
	private double lenMeter;
	private double finalVelocity;
	private boolean[] hitVelocity;
	private double[] hitSec;

	public REAfterStop(RouteElement startRE, int inStartSec) {
		init();
		startSec = inStartSec;
		addRE(startRE);
	}

	public REAfterStop(){
		init();
	}

	private void init(){
		lenSec = 0.0;
		lenMeter = 0.0;
		finalVelocity = 0.0;
		hitVelocity = new boolean[VELOCITIES_TO_HIT.length];
		for(int i=0; i<VELOCITIES_TO_HIT.length; i++){
			hitVelocity[i] = false;
		}
		hitSec = new double[VELOCITIES_TO_HIT.length];
	}

	public void addRE(RouteElement inRE){
		lenSec += 1.0;
		lenMeter += inRE.vecLen();
		finalVelocity = inRE.vecLen();
		for(int i=0; i<VELOCITIES_TO_HIT.length; i++){
			if(hitVelocity[i]==false && finalVelocity >= VELOCITIES_TO_HIT[i]){
				hitVelocity[i] = true;
				hitSec[i] = lenSec;
			}
		}//see next threshold
	}

	public int startSec(){return startSec;}

	public double lenSec(){return lenSec;}
	public double lenMeter(){return lenMeter;}
	public double finalVelocity(){return finalVelocity;}

	public boolean[] hitVelocity(){return hitVelocity;}
	public double[] hitSec(){return hitSec;}

	public void setParams(REAfterStop template){
		this.startSec = template.startSec();
		this.finalVelocity = template.finalVelocity();
		this.lenMeter = template.lenMeter();
		this.lenSec = template.lenSec();
		boolean[] thv = template.hitVelocity();
		double[] ths = template.hitSec();
		for(int i=0; i<VELOCITIES_TO_HIT.length; i++){
			this.hitVelocity[i] = thv[i];
			this.hitSec[i] = ths[i];
		}
	}

	public REAfterStop copyRE(){
		REAfterStop ret = new REAfterStop();
		ret.setParams(this);
		return ret;
	}

	public String toString(){
		return "[" + startSec
				+ "," + Util.dts(lenSec)
				+ "," + Util.dts(lenMeter)
				+ "," + Util.dts(finalVelocity)
				+ ",{" + Util.dblVecToString(hitSec)
				+ "}]"
				;
	}
}
