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

public class Egonet {

	//Egonet = undirected graph of a specific user's friends
	//  vertex: a friends's ID (Integer)
	//  edge: two friends knowing each other

	private HashMap<Integer, UserEgonet> users;

	public void readFolder(String folderName, String sep){
		users = new HashMap<Integer, UserEgonet>();
		int cnt = 0;

		try{
			   File folder = new File(folderName);
			   for(File fileEntry : folder.listFiles()){

				   if (!fileEntry.isDirectory()) {
			            Integer key = getIdFromFilename(fileEntry.getName());
			            UserEgonet newUser = processFile(key, fileEntry);
			            users.put(key, newUser);
			            cnt++;
			       }
			   }
	    } catch(Exception e) {
		 	e.printStackTrace();
	    }
	    System.out.println("Egonets: " + cnt + " files read.");
	}

	public HashMap<Integer, UserEgonet> getUsers(){return users;}

	public UserEgonet getEgonet(Integer user){return users.get(user);}

	private Integer getIdFromFilename(String fname){
		return new Integer( fname.substring(0, fname.indexOf(".")) );
	}

	private UserEgonet processFile(Integer userKey, File fileEntry) throws IOException, FileNotFoundException {
		BufferedReader input = new BufferedReader(new FileReader(fileEntry));
		UserEgonet ret = new UserEgonet(userKey);
		try{

			String line = null;
            while (( line = input.readLine()) != null){
            	processLine(ret, line);
            }
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			input.close();
		}

		return ret;
	}

	private void processLine(UserEgonet targetUser, String line){
		boolean isFirst = true;
		String[] strVec = line.split(" ");
		Integer aFriend = null;
		for(String str : strVec){
			if(isFirst){
				//name of the circle
				aFriend = new Integer(str.substring(0, str.indexOf(":")));
				targetUser.addFriend(aFriend);
				isFirst = false;
			} else {
				Integer f = new Integer(str);
				targetUser.addRelation(aFriend, f);
			}
		}
	}

	public int knowEachOtherInAnotherEgonet(int owner, int user1, int user2){
	   int ret = 0;
	   for(Entry<Integer, UserEgonet> elem : users.entrySet()){
	      if(elem.getKey().intValue() != owner){
	         UserEgonet enet = elem.getValue();
	         ret += (enet.knowEachOther(user1, user2) ? 1 : 0);
	      }
	   }
	   return ret;
	}

	public static void writeToFile(String targetFileName, HashMap<Integer, UserEgonet> inEgoNets, String sep){
		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			for(Entry<Integer, UserEgonet> user : inEgoNets.entrySet()){
				Integer userId = user.getKey();
				UserEgonet egonet = user.getValue();

				HashMap<Integer, HashSet<Integer>> relations = egonet.getRelations();
				for(Entry<Integer, HashSet<Integer>> rs : relations.entrySet()){
				   Integer r1 = rs.getKey();
				   HashSet<Integer> fs = rs.getValue();
				   for(Integer member : fs){
				      writer.println(userId.intValue() + sep + r1.intValue() + sep + member.intValue());
				   }
				}
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}