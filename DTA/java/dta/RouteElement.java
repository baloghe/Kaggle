package dta;

public class RouteElement {

	private double x, y;
	private double prevX, prevY;
	private double[] gplot;  //for G-plot

	private double vecLen, vecAngle;

	private double accel, accel2, lataccel, vecAngleDiff;
	
	private double prevVecLen, lag2SpdDiff, lag3SpdDiff;

	public RouteElement(String str, String sep, RouteElement prevElement){
		//System.out.println("RouteElement :: str=" + str);
		String[] sv = str.split(sep);
		x = new Double(sv[0]).doubleValue();
		y = new Double(sv[1]).doubleValue();
		calc(prevElement);
	}

	private void calc(RouteElement pe){
		prevX = 0.0;
		prevY = 0.0;
		gplot = new double[2];
		vecLen = 0.0;
		vecAngle = 0.0;
		accel = 1.0;
		accel2 = 0.0;
		lataccel = 0.0;
		vecAngleDiff = 0.0;
		
		prevVecLen = 0.0;
		lag2SpdDiff = 0.0;
		lag3SpdDiff = 0.0;
		if(pe != null){
			prevX = pe.x();
			prevY = pe.y();
			vecLen = Math.sqrt( (x-prevX)*(x-prevX) + (y-prevY)*(y-prevY) );
			/*vecAngle = Math.atan2(y, x) - Math.atan2(prevY, prevX);*/
			vecAngle = Math.atan2(y-prevY, x-prevX);
			prevVecLen = pe.vecLen();
			if(pe.vecLen != 0){
				accel = vecLen / prevVecLen;
				vecAngleDiff = vecAngle - pe.vecAngle();
			}
			accel2 = vecLen - prevVecLen;
			calcGPlot(pe);
			
			lag2SpdDiff = vecLen - pe.prevVecLen();
			lag3SpdDiff = vecLen - pe.lag2SpdDiff();
		}

	}

	private void calcGPlot(RouteElement pe){
		double pprevX = pe.prevX(), pprevY = pe.prevY();
		double dx1 = prevX - pprevX, dy1 = prevY - pprevY;
		double dx2 = x - prevX, dy2 = y - prevY;
		double dx3 = x - pprevX, dy3 = y - pprevY;
		double a = Math.sqrt( dx1*dx1 + dy1*dy1 );
		double b = Math.sqrt( dx3*dx3 + dy3*dy3 );
		double c = Math.sqrt( dx2*dx2 + dy2*dy2 );

		double rdiv =(a+b+c) * (a+b-c) * (a+c-b) * (b+c-a);
		if(rdiv > 0.00001){
			rdiv = Math.sqrt(rdiv);
			double r = a*b*c / rdiv;
			lataccel = ( (vecLen + pe.vecLen()) / 2 ) * ( (vecLen + pe.vecLen()) / 2 ) / r ;
			gplot[0] = (vecAngle < 0.0 ? -lataccel : lataccel );
			gplot[1] = accel2;
		}
	}

	public double x(){return x;}
	public double y(){return y;}
	public double prevX(){return prevX;}
	public double prevY(){return prevY;}
	public double vecLen(){return vecLen;}
	public double vecAngle(){return vecAngle;}
	public double accel(){return accel;}
	public double accel2(){return accel2;}
	public double lataccel(){return lataccel;}
	public double vecAngleDiff(){return vecAngleDiff;}
	public double[] gplot(){return gplot;}
	
	public double lag2SpdDiff(){return lag2SpdDiff;}
	public double lag3SpdDiff(){return lag3SpdDiff;}
	public double prevVecLen(){return prevVecLen;}

	public String toString(){
		return "[act=("+Util.dts(x)+" , "+Util.dts(y)+"), prev=("+Util.dts(prevX)+", "+Util.dts(prevY)+"),"
				+ " len="+Util.dts(vecLen)+", ang="+Util.dts(vecAngle)+", accel="+Util.dts(accel)
				+", accel2="+Util.dts(accel2) + ", vecAngleDiff="+Util.dts(vecAngleDiff)+"]";
	}

}
