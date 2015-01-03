package circle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class UserCircles {

	//HashMap<Integer,ArrayList<HashSet<Integer>>> circles;
	HashMap<Integer,ArrayList<UserCircle>> circles;

	public void readFolder(String folderName, String sep){
		//circles = new HashMap<Integer,ArrayList<HashSet<Integer>>>();
		circles = new HashMap<Integer,ArrayList<UserCircle>>();

		try{
				int cnt = 0;
			   File folder = new File(folderName);
			   for(File fileEntry : folder.listFiles()){

				   if (!fileEntry.isDirectory()) {
			            Integer key = getIdFromFilename(fileEntry.getName());
			            processFile(key, fileEntry);
			            cnt++;
			       }
			   }//next fileEntry
			   System.out.println("Circles: " + cnt + " files read.");
	    } catch(Exception e) {
		 	e.printStackTrace();
	    }
	}

	public HashMap<Integer,Integer> getCircleNumber(){
		HashMap<Integer,Integer> ret = new HashMap<Integer,Integer>();

		//for(Entry<Integer, ArrayList<HashSet<Integer>>> e : circles.entrySet()){
		for(Entry<Integer, ArrayList<UserCircle>> e : circles.entrySet()){
			Integer key = e.getKey();
			//ArrayList<HashSet<Integer>> ucs = e.getValue();
			ArrayList<UserCircle> ucs = e.getValue();
			ret.put(key, new Integer(ucs.size()));
		}

		return ret;
	}

	private void processFile(Integer userKey, File fileEntry) throws IOException, FileNotFoundException {
		BufferedReader input = new BufferedReader(new FileReader(fileEntry));;
		try{

			String line = null;
			//ArrayList<HashSet<Integer>> ucs = new ArrayList<HashSet<Integer>>();
			ArrayList<UserCircle> ucs = new ArrayList<UserCircle>();
            while (( line = input.readLine()) != null){
            	//HashSet<Integer> circle = processLine(line);
            	UserCircle circle = processLine(line, userKey);
            	ucs.add(circle);
            }
            circles.put(userKey, ucs);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}

	private Integer getIdFromFilename(String fname){
		return new Integer( fname.substring(0, fname.indexOf(".")) );
	}

	//private HashSet<Integer> processLine(String line){
	private UserCircle processLine(String line, Integer owner){
		//HashSet<Integer> ret = new HashSet<Integer>();
		UserCircle ret = new UserCircle(owner);

		boolean isFirst = true;
		String[] strVec = line.split(" ");
		for(String str : strVec){
			if(isFirst){
				//name of the circle -> skip this one
				isFirst = false;
			} else {
				//ret.add(new Integer(str));
				ret.addMember(new Integer(str));
			}
		}

		return ret;
	}

	//public ArrayList<HashSet<Integer>> getCirclesForUser(Integer user){
	public ArrayList<UserCircle> getCirclesForUser(Integer user){
		return  circles.get(user);
	}

	//public HashMap<Integer,ArrayList<HashSet<Integer>>> getAllCircles(){return circles;}
	public HashMap<Integer,ArrayList<UserCircle>> getAllCircles(){return circles;}

	public int inSameCircleElsewhere(int owner, int user1, int user2){
	   int ret = 0;
	   for(Entry <Integer,ArrayList<UserCircle>> elem : circles.entrySet()){
	      if(elem.getKey().intValue() != owner){
	         for(UserCircle circ : elem.getValue()){
	            ret += (circ.isMember(new Integer(user1)) && circ.isMember(new Integer(user2)) ? 1 : 0);
	         }
	      }
	   }
	   return ret;
	}

	//public static void writeToFile(String targetFileName, HashMap<Integer,ArrayList<HashSet<Integer>>> inCircles, String sep){
	public static void writeToFile(String targetFileName, HashMap<Integer,ArrayList<UserCircle>> inCircles, String sep){
		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			//for(Entry<Integer,ArrayList<HashSet<Integer>>> user : inCircles.entrySet()){
			for(Entry<Integer,ArrayList<UserCircle>> user : inCircles.entrySet()){
				Integer userId = user.getKey();
				//ArrayList<HashSet<Integer>> cs = user.getValue();
				ArrayList<UserCircle> cs = user.getValue();

				int csCnt = 1;
				//for(HashSet<Integer> circle : cs){
				for(UserCircle circle : cs){
				   for(Integer member : circle.getMembers()){
						writer.println(userId.intValue() + sep + csCnt + sep + member.intValue());
				   }
				   csCnt++;
				}
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
