package rfacade;

import org.rosuda.JRI.REXP;

import rfacade.REngineInstance;

/**
 * Provides a connection to R, travels data between R instance and JVM, transforms some complex operations into a simple method call.
 */
public class RFacade {

	/**
	 * engine instance
	 */
	public REngineInstance engine;
	
	/**
	 * constructs a new facade with an engine instance
	 * @param inEngInst
	 */
	public RFacade( REngineInstance inEngInst ){
		engine = inEngInst;
	}
	
	/**
	 * calculates and returns the (hopefully) double result of the given expression. The expression is simply passed on to R for evaluation 
	 * @param inCmd expression to be evaluated
	 * @return result of the expression, as evaluated by R
	 */
	public double calcDouble(String inCmd){
		return engine.getEngine().eval( inCmd ).asDouble();
	}
	
	/**
	 * sends a double[] array over to R, into the given variable name 
	 * @param inName variable name in R
	 * @param inValues array to be passed to R
	 * @return TRUE if it succeeded, FALSE otherwise
	 */
	public boolean addDoubleVector(String inName, double[] inValues){
		return engine.getEngine().assign(inName, inValues);
	}
	
	/**
	 * returns the value of a double[] array in R
	 * @param inName variable name in R
	 * @return array
	 */
	public double[] getDoubleVector(String inName){
		return engine.getEngine().eval( inName ).asDoubleArray();
	}
	
    /**
    * Creates a matrix object in R from 2D table of double
    * 	source: //http://www.lbgi.fr/wikili/index.php/JRI
    *
    * @param inMatrix the 2D table of double
    *
    * the matrix must have always the same column number on every row
    *
    * @param inName the R object name
    * @return true if assignment was succesful, false otherwise 
    *
    * To convert this R matrix to double[][], use asMatrix() method
    * or r.eval(assign).asMatrix();
    * 
    */
    public boolean addMatrix(String inName, double[][] inMatrix){
       boolean ret = false;
       REXP rx = null;
       this.remove("tmp");
       if(inMatrix.length>0){
    	   engine.getEngine().assign(inName,inMatrix[0]);
    	   rx=engine.getEngine().eval(inName + " <- matrix( " + inName + " ,nr=1)");
    	   ret = (rx!=null);
    	   //System.out.println("addMatrix :: rowsum(1)=" + this.calcDouble("sum("+inName+")") );
       } else {
           return false;
       }
       for(int i=1; i<inMatrix.length;i++){
    	   engine.getEngine().assign("tmp",inMatrix[i]);
    	   rx=engine.getEngine().eval(inName+" <- rbind(" + inName + ",matrix(tmp,nr=1))");
    	   ret = ret && (rx!=null);
    	   //System.out.println("addMatrix :: rowsum("+(i+1)+")=" + this.calcDouble("sum(tmp)") + "  , matrix Sum=" + this.calcDouble("sum("+inName+")") );
       }
       return ret;
    }
    
    /**
     * returns the value of a matrix in R in the form double[rows][cols]
     * @param inName variable name in R
     * @return double[rows][cols] values
     */
    public double[][] getMatrix(String inName){
    	return engine.getEngine().eval( inName ).asMatrix();
    }
    
    /**
     * sets row and column names of a given matrix
     * @param inMatrixName variable name in R
     * @param inRowNames array of row names
     * @param inColNames array of column names
     * @return TRUE if the operation was successful, FALSE otherwise
     */
    public boolean setMatrixDimNames(String inMatrixName, String[] inRowNames, String[] inColNames){
    	//construct row and col names by passing the String vectors first
    	boolean ret = engine.getEngine().assign("tmprn", inRowNames);
    	ret = ret && engine.getEngine().assign("tmpcn", inColNames);
    	ret = ret && this.eval( "dimnames(" + inMatrixName + ")=list(tmprn,tmpcn)" );
    	
    	return ret;
    }
    
    /**
     * evaluates an expression
     * @param inCmd expression to be evaluated
     * @return TRUE if the evaluation was successful, FALSE otherwise
     */
    public boolean eval(String inCmd){
    	return ( engine.getEngine().eval(inCmd) != null );
    }
    
    /**
     * removes a variable in R
     * @param inVarName to-be-removed variable name in R
     */
    public void remove(String inVarName){
    	this.eval("rm(" + inVarName + ")");
    }
    
    /**
     * sends over a 2D array to R and reads it into a Data Frame
     * @param inDFName name of destination Data Frame
     * @param inDFRowNames row names (NULL if no row names are to be provided)
     * @param inDFColNames column names (NULL if no column names are to be provided)
     * @param inMatrix data matrix to be transformed
     * @return TRUE if the evaluation was successful, FALSE otherwise
     */
    public boolean addDataFrame(String inDFName, String[] inDFRowNames, String[] inDFColNames, double[][] inMatrix){
    	this.remove("tmpdf");
    	//put data
    	boolean ret = this.addMatrix("tmpdf", inMatrix);
    	//System.out.println("addDataFrame :: after Matrix addition: sum(tmpdf)=" + this.calcDouble("sum(tmpdf)") );
    	//assign column names
    	ret = ret && this.setMatrixDimNames("tmpdf", inDFRowNames, inDFColNames);
    	//System.out.println("addDataFrame :: after adding dimensions: sum(tmpdf)=" + this.calcDouble("sum(tmpdf)") );
    	//data.frame
    	ret = ret && this.eval( inDFName + " <- data.frame( tmpdf )" );
    	
    	return ret;
    }
	
    /**
     * stop engine
     */
	public void off(){
		engine.endEngine();
	}

}
