package dta;

import java.util.HashSet;

public class VLenRouteEvaluator {
	
	public static final int DTW_XTOL = 10;
	public static final int DTW_YTOL = 10;
	public static final double DTW_SLICELEN = 2.0;
	
	private Route r1;
	private Route r2;
	
	public VLenRouteEvaluator(Route inR1, Route inR2){
		r1 = inR1;
		r2 = inR2;
	}
	
	public double calcDistance(){
		//How to define the distance of two routes...?
		//At least the distance of a route from itself should equal 0.0
		//Idea:
		//	take the shorter route (=has less patterns)
		//	choose a pair for each of its patterns for the other set so that 
		//		a pattern (from a given route) can only be used once
		//	return the sum of their distances
		
		Route base = null, comp = null;
		double ret = 0.0;
		if(r1.getVLenPatterns().size() < r2.getVLenPatterns().size()){
			base = r1 ; comp = r2;
		} else {
			base = r2 ; comp = r1;
		}
		
		HashSet<VLenPattern> compset = new HashSet<VLenPattern>();
		compset.addAll(comp.getVLenPatterns());
		System.out.println("VLenEvaluator :: " + compset.size() + " patterns");
		
		for(VLenPattern bpat : base.getVLenPatterns()){
			double minlen = Double.MAX_VALUE;
			VLenPattern selpat = null;
			for(VLenPattern cpat : compset){
				DTW dcalc = new DTW( new EuclidianMetric() );
				double[][] bpc = bpat.getCoordinates();
				double[][] cpc = cpat.getCoordinates();
				/*
				System.out.println("To be evaluated : ");
				System.out.println("bpat=" + Util.dblMatToString(bpc));
				System.out.println("cpat=" + Util.dblMatToString(cpc));
				*/
				int xtol = (bpc.length < cpc.length ? cpc.length - bpc.length : bpc.length - cpc.length);
				xtol = (xtol < DTW_XTOL ? DTW_XTOL : xtol);
				int ytol = (xtol < DTW_YTOL ? DTW_XTOL : xtol);
				double actdist = dcalc.getDistance(bpat.getCoordinates(), cpat.getCoordinates(), xtol, ytol, DTW_SLICELEN);
				if(actdist < minlen){
					minlen = actdist;
					selpat = cpat;
				}
				System.out.println("minlen=" + Util.dts(minlen));
			}//next pat to compare
			compset.remove(selpat);
			ret += minlen;
			//System.out.println("ret=" + Util.dts(ret));
		}//next pattern from base
		
		return ret;
	}
}
