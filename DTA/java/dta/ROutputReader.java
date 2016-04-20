package dta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.HashMap;

public final class ROutputReader {

   private HashMap<String,Double> results;

	public ROutputReader(String folderName){
		results = new HashMap<String,Double>();

		try{
			int cnt = 0;
			File folder = new File(folderName);
			for(File fileEntry : folder.listFiles()){
				/*System.out.println("ROutputReader :: fileEntry.getName()=" + fileEntry.getName()
						+ ", substr(0,4)=" +fileEntry.getName().substring(0,4)
						+ ", fileEntry.isDirectory()=" + fileEntry.isDirectory() );*/

				if (!fileEntry.isDirectory() && fileEntry.getName().substring(0,4).equalsIgnoreCase("est.")) {
					Integer drv = new Integer(fileEntry.getName().replace("est.","").replace(".csv",""));
					processFile(drv, fileEntry);
					//System.out.println("ROutputReader :: xx.length=" + xx.length);

					cnt++;
				} else {
					//System.out.println("ROutputReader :: wrong file found: " + fileEntry.getName());
				}
			}//next fileEntry
			System.out.println("ROutputReader :: " + cnt + " files processed");
		} catch(Exception e) {
		 	e.printStackTrace();
		}

	}

	private void processFile(Integer drv, File inFile){
		try{
			BufferedReader input = new BufferedReader(new FileReader(inFile));
			String line = input.readLine(); //drop header
			int cnt = 0;
			while (( line = input.readLine()) != null){
				String[] drv_route = line.split(",");
				Integer rt = new Integer(drv_route[2]);
				Double est = new Double(drv_route[3]);

				String key = drv + "_" + rt;
				results.put(key, est);

				cnt++;
			}
			input.close();
		}catch(Exception e){
			System.out.println("ROUTES_SET_TO_1 could not be opened!");
			e.printStackTrace();
		}
	}

	public HashMap<String,Double> getResults(){return results;}
}