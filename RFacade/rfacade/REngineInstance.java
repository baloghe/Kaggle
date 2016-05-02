package rfacade;

import org.rosuda.JRI.Rengine;

/**
 * Provides connection to R by using the rJava (JRI) interface:
 * 		org.rosuda.JRI.Rengine
 */
public class REngineInstance {

	private Rengine ENGINE;
	
	/**
	 * Constructor call:
	 * 		new Rengine (new String [] {"–vanilla"}, false, null)
	 */
	public REngineInstance(){
		if(ENGINE != null){
			endEngine();
		}
		ENGINE = new Rengine (new String [] {"–vanilla"}, false, null);
		if (!ENGINE.waitForR()){
		    System.out.println ("Cannot load R");
		    ENGINE = null;
		    return;
		}
	}
	
	/**
	 * stop engine
	 */
	public void endEngine(){
		ENGINE.end();
		ENGINE = null;
	}
	
	/**
	 * engine instance
	 * @return engine instance
	 */
	public Rengine getEngine(){
		return ENGINE;
	}
	
}