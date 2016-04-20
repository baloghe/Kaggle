package dta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Route {

	public static int PATTERN_LENGTH = 10;
	public static int PATTERN_OVERLAP = 3;
	public static double IDLE_METER_THRESHOLD = 0.25;

	public static double VLENPATTERN_LENGTH = 1000.0;
	public static double VLENPATTERN_OVERLAP_RATIO = 0.3;

	public static double FIXLENPATTERN_LENGTH = 10.0;

	public static final int SPEED_NTILES_NUM = 10;
	public static final int ROTATION_NTILES_NUM = 10;
	public static final int ACCEL_NTILES_NUM = 10;
	public static final int TGSPEED_NTILES_NUM = 10;
	public static final int STDSPEED_NTILES_NUM = 10;

	public static final int LAGSPD_NTILES_NUM = 10;
	public static final int GPDST_NTILES_NUM = 10;

	public static double[][] GPAREAS = new  double[][]{
				 new double[]{-0.5,-0.5,0.5,0.5}
				,new double[]{-0.5,0.5,0.5,1.0}
				,new double[]{0.5,-0.5,1.0,0.5}
				,new double[]{-0.5,-1.0,0.5,-0.5}
				,new double[]{-1.0,-0.5,-0.5,0.5}
				,new double[]{-0.5,1.0,0.5,2.0}
				,new double[]{0.5,0.5,1.5,1.5}
				,new double[]{1.0,-0.5,2.0,0.5}
				,new double[]{0.5,-1.5,-0.5,1.5}
				,new double[]{-0.5,-2.0,0.5,-1.0}
				,new double[]{-1.5,-1.5,-0.5,-0.5}
				,new double[]{-2.0,-0.5,-1.0,0.5}
				,new double[]{-1.5,0.5,-0.5,1.5}
			};

	private String routeKey;
	private int driver;
	private ArrayList<RouteElement> elems;
	private ArrayList<RoutePattern> patterns;
	private ArrayList<VLenPattern> vlpatterns;

	private ArrayList<double[]> fixLenCoords;
	private double tmpLen;
	private double[] fixLastCoord;

	private double lenSec;
	private double lenMeter;
	private double avgSpeed;
	private double stdSpeed;
	private double sumVecLenSq;
	private double idleSecs;
	private double idleRatio;
	private double maxSpeed;
	private int selfCrossNum; //to be calculated!!!
	private double beeLine;

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double dimRatio;

	private double[] speedNTiles;
	private double[] rotNTiles;
	private double[] accNTiles;
	private double[] accNTiles2;
	private double[] lataccNTiles;

	private double[] lag2spdNTiles;
	private double[] lag3spdNTiles;
	
	private double[] gplotRatios;
	private double[] gpdstNTiles;

	private double[] speedNTilesPat;
	private double[] tgSpeedNTilesPat;
	private double[] rotNTilesPat;
	private double[] accNTilesPat;
	private double[] stdSpeedNTilesPat;

	private RoutePattern tmpPattern;
	private RoutePattern tmpPattern2;
	private int ptrncnt;
	private double[] lastCoord;
	private VLenPattern tmpVlp1;
	private VLenPattern tmpVlp2;
	private Metric metric;

	public Route(Integer key, Integer inRouteID, File fileEntry){
		routeKey = key + "_" + inRouteID;
		driver = key.intValue();

		resetRouteStats();
		ptrncnt = 0;
		lastCoord = null;
		tmpPattern = null;
		tmpPattern2 = null;
		tmpVlp1 = null;
		tmpVlp2 = null;
		tmpLen = 0.0;
		lastCoord = null;
		metric = new EuclidianMetric();

		try{
			processFile(fileEntry);
		} catch(Exception e) {
			log("ERROR at key=" + key + ", fileEntry=" + fileEntry.getName());
			e.printStackTrace();
		}
	}

	private void log(String s){
		System.out.println(s);
	}

	private void processFile(File fileEntry) throws IOException, FileNotFoundException{
		BufferedReader input = new BufferedReader(new FileReader(fileEntry));
		try{
			elems = new ArrayList<RouteElement>();
			patterns = new ArrayList<RoutePattern>();
			vlpatterns = new ArrayList<VLenPattern>();
			fixLenCoords = new ArrayList<double[]>();
			String line = input.readLine(); //drop file header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				processLine(line, cnt);
				cnt++;
			}
			this.closeStats();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			input.close();
			//System.out.println("Route " + this.routeKey + " = " + this.statsToString());
			//System.out.println("    " + Util.dblVecToString(this.accNTiles));
		}
	}

	private void resetRouteStats(){
		lenSec = 0.0;
		lenMeter = 0.0;
		idleSecs = 0.0;
		avgSpeed = 0.0;
		stdSpeed = 0.0;
		sumVecLenSq = 0.0;
		idleRatio = 0.0;
		maxSpeed = 0.0;
		minX = 0.0;
		maxX = 0.0;
		minY = 0.0;
		maxY = 0.0;
		dimRatio=0.0;
		speedNTiles = new double[Route.SPEED_NTILES_NUM];
		rotNTiles = new double[Route.ROTATION_NTILES_NUM];
		accNTiles = new double[Route.ACCEL_NTILES_NUM];
		lataccNTiles = new double[Route.ACCEL_NTILES_NUM];
		accNTiles2 = new double[Route.ACCEL_NTILES_NUM];

		lag2spdNTiles = new double[Route.LAGSPD_NTILES_NUM];
		lag3spdNTiles = new double[Route.LAGSPD_NTILES_NUM];
		//gpdstNTiles = new double[Route.GPDST_NTILES_NUM];
		//gplotRatios = new double[Route.GPAREAS.length];

		speedNTilesPat = new double[Route.SPEED_NTILES_NUM];
		tgSpeedNTilesPat = new double[Route.TGSPEED_NTILES_NUM];
		rotNTilesPat = new double[Route.ROTATION_NTILES_NUM];
		accNTilesPat = new double[Route.ACCEL_NTILES_NUM];
		stdSpeedNTilesPat = new double[Route.STDSPEED_NTILES_NUM];
	}

	private void closeStats(){
		double[] s = new double[elems.size()];
		double[] r = new double[elems.size()];
		double[] a = new double[elems.size()];
		double[] a2 = new double[elems.size()];
		double[] la = new double[elems.size()];
		double[] l2s = new double[elems.size()];
		double[] l3s = new double[elems.size()];
		int cnt = 0;
		for(RouteElement re : elems){
			s[cnt] = re.vecLen();
			r[cnt] = re.vecAngleDiff();
			a[cnt] = re.accel();
			a2[cnt] = re.accel2();
			la[cnt] = re.lataccel();
			l2s[cnt] = re.lag2SpdDiff();
			l3s[cnt] = re.lag3SpdDiff();
			cnt++;
		}

		double[] spat = new double[patterns.size()];
		double[] tpat = new double[patterns.size()];
		double[] rpat = new double[patterns.size()];
		double[] apat = new double[patterns.size()];
		double[] mpat = new double[patterns.size()];
		int ptcnt = 0;
		for(RoutePattern rp : patterns){
			spat[ptcnt] = rp.avgVecLen();
			tpat[ptcnt] = rp.avgVecLen() * rp.sumAngleDiff();
			rpat[ptcnt] = rp.sumAngleDiff();
			apat[ptcnt] = rp.prodAccel();
			mpat[ptcnt] = rp.stdVecLen();
			ptcnt++;
		}
		lenSec--;
		avgSpeed = lenMeter / lenSec;

		speedNTiles = Util.calcNtiles(s, Route.SPEED_NTILES_NUM);
		rotNTiles = Util.calcNtiles(r, Route.ROTATION_NTILES_NUM);
		accNTiles = Util.calcNtiles(a, Route.ACCEL_NTILES_NUM);
		lataccNTiles = Util.calcNtiles(la, Route.ACCEL_NTILES_NUM);
		accNTiles2 = Util.calcNtiles(a2, Route.ACCEL_NTILES_NUM);

		lag2spdNTiles = Util.calcNtiles(l2s, Route.LAGSPD_NTILES_NUM);
		lag3spdNTiles = Util.calcNtiles(l3s, Route.LAGSPD_NTILES_NUM);
		
		calcGPFigures();

		if(Main.CREATE_SIMPLE_ROUTE_PATTERNS){
			speedNTilesPat = Util.calcNtiles(spat, Route.SPEED_NTILES_NUM);
			tgSpeedNTilesPat = Util.calcNtiles(tpat, Route.TGSPEED_NTILES_NUM);
			rotNTilesPat = Util.calcNtiles(rpat, Route.ROTATION_NTILES_NUM);
			accNTilesPat = Util.calcNtiles(apat, Route.ACCEL_NTILES_NUM);
			stdSpeedNTilesPat = Util.calcNtiles(mpat, Route.STDSPEED_NTILES_NUM);
		}

		/** calc std = e(x^2) - e^2(x) */
		stdSpeed = Math.sqrt(sumVecLenSq/(double)(elems.size()-1) - avgSpeed*avgSpeed);
		RouteElement laste = elems.get(elems.size()-1);
		beeLine = Math.sqrt( laste.x()*laste.x() + laste.y()*laste.y() );

		//dim ratio: first align to X
		double[][] alx = Util.alignToX( getCoords() );
		double amaxx = 0.0, amaxy = 0.0, aminx = 0.0, aminy = 0.0;
		for(int i=0; i<alx.length; i++){
			if(alx[i][0] > amaxx) amaxx = alx[i][0];
			if(alx[i][1] > amaxy) amaxy = alx[i][1];
			if(alx[i][0] < aminx) aminx = alx[i][0];
			if(alx[i][1] < aminy) aminy = alx[i][1];
		}
		dimRatio = ( (amaxx - aminx) > (amaxy - amaxy) ? (amaxy - aminy)/(amaxx - aminx) : (amaxx - aminx)/(amaxy - aminy) );

		//fixLen: add last coord
		//System.out.println("fixLenCoords :: attempt to add Last");
		if(Main.CREATE_FIXLEN_ROUTE){
			fixLenCoords.add(new double[]{fixLastCoord[0],fixLastCoord[1]});
			//System.out.println("fixLenCoords :: last added");
		}
	}

	private void updateRouteStats(RouteElement e){
		lenSec += 1.0;
		lenMeter += e.vecLen();
		idleSecs += (elems.size()>1 && e.vecLen() > Route.IDLE_METER_THRESHOLD ? 0 : 1);
		maxSpeed = (e.vecLen() > maxSpeed ? e.vecLen() : maxSpeed);
		idleRatio = idleSecs / (elems.size()-1);
		sumVecLenSq += (e.vecLen()*e.vecLen());
		double ax=e.x(), ay=e.y();
		if(ax > maxX) maxX = ax; if(ay > maxY) maxY = ay;
		if(ax < minX) minX = ax; if(ay < minY) minY = ay;
	}

	private void updateVLenPatterns(double[] inCoords){
		double dist = 0.0;
		if(lastCoord != null){
			dist = metric.getDistance(lastCoord, inCoords);
		}

		if(tmpVlp1 == null){
			//create pattern1 for the first time
			tmpVlp1 = new VLenPattern(inCoords, metric);
		} else if(tmpVlp1.getMeasureableLength() + dist > Route.VLENPATTERN_LENGTH){
			//close pattern1 and start a new one
			tmpVlp1.addElement(inCoords);
			this.vlpatterns.add(tmpVlp1);
			tmpVlp1 = new VLenPattern(inCoords, metric);
		} else if(tmpVlp2 == null && tmpVlp1.getMeasureableLength() > Route.VLENPATTERN_LENGTH * (1 - Route.VLENPATTERN_OVERLAP_RATIO)){
			//create pattern2 for the first time
			tmpVlp2 = new VLenPattern(inCoords, metric);
		} else if(tmpVlp2 != null && tmpVlp2.getMeasureableLength() + dist > Route.VLENPATTERN_LENGTH){
			//close pattern2 and start a new one
			tmpVlp2.addElement(inCoords);
			this.vlpatterns.add(tmpVlp2);
			tmpVlp2 = new VLenPattern(inCoords, metric);
		}else {
			//add coords to the available patterns
			tmpVlp1.addElement(inCoords);
			if(tmpVlp2 != null){
				tmpVlp2.addElement(inCoords);
			}
		}
		//anyway...
		lastCoord = inCoords;
	}

	private void updateFixLenCoords(double[] inCoords){
		double dist = 0.0;
		if(fixLastCoord != null){
			dist = metric.getDistance(fixLastCoord, inCoords);
		}

		if(fixLenCoords.size()==0){ //first element = Origin
			fixLenCoords.add(new double[]{inCoords[0] , inCoords[1]});
		} else if(tmpLen + dist < FIXLENPATTERN_LENGTH){ //add it to tmpLen and discard coord
			tmpLen += dist;
		} else if(tmpLen + dist >= FIXLENPATTERN_LENGTH){
			//some interpolation needed...
			double remLen = tmpLen + dist - FIXLENPATTERN_LENGTH;
			//add interpolation points until remainder is less then FixedLen
			while(remLen > FIXLENPATTERN_LENGTH){
				double[] ipt = Util.interpolate(fixLastCoord, inCoords
								, 1.0 - remLen / dist  );
				fixLenCoords.add(new double[]{ipt[0],ipt[1]});
				//System.out.println("added ipt=" + Util.dblVecToString(ipt) + ", remLen=" + Util.dts(remLen));
				remLen -= FIXLENPATTERN_LENGTH;
			}
			//finally remember to remaining remLen
			tmpLen = remLen;
		}

		fixLastCoord = inCoords;
		//System.out.println("tmpLen=" + Util.dts(tmpLen) + ", lastCoord=" + Util.dblVecToString(fixLastCoord));
	}

	private void updatePatterns(RouteElement e){
		ptrncnt++;

		if(ptrncnt == Route.PATTERN_LENGTH){
			ptrncnt = 0;
			tmpPattern.closeStats();
			patterns.add(tmpPattern);
			tmpPattern = new RoutePattern(e);
		} else if(ptrncnt == (Route.PATTERN_LENGTH - Route.PATTERN_OVERLAP + 1) ){
			if(tmpPattern2 != null){
				tmpPattern2.closeStats();
				patterns.add(tmpPattern2);
			}
			tmpPattern2 = new RoutePattern(e);
		} else if(tmpPattern==null){
			tmpPattern = new RoutePattern(e);
		} else {
			tmpPattern.addElement(e);
			if(tmpPattern2 != null){
				tmpPattern2.addElement(e);
			}
		}
	}

	private void processLine(String line, int cnt){
		RouteElement e = new RouteElement(line, Main.DRIVER_COORD_SEPARATOR
				, (cnt==0 ? null : elems.get(cnt-1)) );
		elems.add(e);
		updateRouteStats(e);
		if(Main.CREATE_SIMPLE_ROUTE_PATTERNS){
			updatePatterns(e);
		}

		//VLen
		if(Main.CREATE_VLEN_ROUTE_PATTERNS){
			updateVLenPatterns(new double[]{e.x() , e.y()});
		}

		//Fix len route
		if(Main.CREATE_FIXLEN_ROUTE){
			updateFixLenCoords(new double[]{e.x() , e.y()});
		}
		//System.out.println("Route " + this.routeKey + " elem("+cnt+")= " + e.toString());
	}

	public String statsToString(){
		return "[lenSec=" + Util.dts(lenSec)
				+ ", lenMeter=" + Util.dts(lenMeter)
				+ ", avgSpeed=" + Util.dts(avgSpeed)
				+ ", stdSpeed=" + Util.dts(stdSpeed)
				+ ", maxSpeed=" + Util.dts(maxSpeed)
				+ ", idleSecs=" + Util.dts(idleSecs)
				+ ", idleRatio=" + Util.dts(idleRatio)
				+ ", dimRatio=" + Util.dts(dimRatio)
				+ ", beeLine=" + Util.dts(beeLine)
				;
	}

	public ArrayList<RouteElement> getElems(){return this.elems;}

	public double[][] getCoords(){
		double[][] ret = new double[elems.size()][2];
		int i=0;
		for(RouteElement re : elems){
			ret[i][0] = re.x();
			ret[i][1] = re.y();
			i++;
		}
		return ret;
	}

	public double[][] getFixLenCoords(){
		double[][] ret = new double[fixLenCoords.size()][2];
		int i=0;
		for(double[] re : fixLenCoords){
			ret[i] = re;
			i++;
		}
		return ret;
	}

	public double getLenSec(){return this.lenSec;}
	public double getLenMeter(){return this.lenMeter;}
	public double getAvgSpeed(){return this.avgSpeed;}
	public double getStdSpeed(){return this.stdSpeed;}
	public double getIdleSecs(){return this.idleSecs;}
	public double getIdleRatio(){return this.idleRatio;}
	public double getMaxSpeed(){return this.maxSpeed;}
	public double getDimRatio(){return this.dimRatio;}
	public double getBeeLine(){return this.beeLine;}

	public double[] getSpeedNTiles(){return speedNTiles;}
	public double[] getRotationNTiles(){return rotNTiles;}
	public double[] getAccelNTiles(){return accNTiles;}
	public double[] getLatAccelNTiles(){return lataccNTiles;}
	public double[] getAccel2NTiles(){return accNTiles2;}

	public double[] getLag2SpdNTiles(){return lag2spdNTiles;}
	public double[] getLag3SpdNTiles(){return lag3spdNTiles;}

	public double[] getSpeedNTilesPat(){return speedNTilesPat;}
	public double[] getTgSpeedNTilesPat(){return tgSpeedNTilesPat;}
	public double[] getStdSpeedNTilesPat(){return stdSpeedNTilesPat;}
	public double[] getRotationNTilesPat(){return rotNTilesPat;}
	public double[] getAccelNTilesPat(){return accNTilesPat;}
	
	public double[] getGPlotRatios(){return gplotRatios;}
	public double[] getGpDstNTiles(){return gpdstNTiles;}

	public String getKey(){return routeKey;}

	public int getDriver(){return driver;}

	public ArrayList<VLenPattern> getVLenPatterns(){return this.vlpatterns;}

	public void calcGPFigures(){
		gplotRatios = new double[GPAREAS.length];
		ArrayList<Double> dsts = new ArrayList<Double>();
		int cnt = 0;
		for(RouteElement re : elems){
			double[] gp = re.gplot();
			int idx = getGPAreaIndex(gp);
			if(gp[0] != 0.0 && gp[1] != 0.0){
				//locate areas of interest
				if(idx >= 0){
					gplotRatios[idx] += 1.0;
				}
				cnt++;
			}
			Double len = new Double( Math.sqrt(gp[0]*gp[0] + gp[1]*gp[1]) );
			dsts.add(len);
		}

		for(int i=0; i<gplotRatios.length; i++){
			gplotRatios[i] /= (double)cnt;
		}
		
		double[] data = new double[dsts.size()];
		for(int i=0; i<dsts.size(); i++){
			data[i] = dsts.get(i).doubleValue();
		}
		gpdstNTiles = Util.calcNtiles(data, Route.GPDST_NTILES_NUM);

	}
	
	private static int getGPAreaIndex(double[] inGP){
		int ret = -1;
		for(int i=0; i<GPAREAS.length; i++){
		   double[] corners = GPAREAS[i];
			if(   corners[0] <= inGP[0] && corners[1] <= inGP[1]
			   && inGP[0] < corners[2]  && inGP[1] < corners[3]){
			      return i;
			}
		}
		return ret;
	}
}
