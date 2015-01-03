package test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import common.*;
import circle.*;

public class TstEgonet {
	public static void main(String[] args) {
		Egonet e = new Egonet();
		e.readFolder("../egonets", " ");
		HashMap<Integer, UserEgonet> users = e.getUsers();
		System.out.println("Friends number");
		for(UserEgonet u : users.values()){
			System.out.println(u.getId() + " " + u.getFriendsNumber());
		}
		//DB output
		e.writeToFile("UserEgonets.txt",e.getUsers(),",");

		//write allfriendsinOne...
		//writeAllFriendsInOneCircle("AllFriendsInOneCircle.txt", users.values());
	}

	public static void writeEgonetToFile(String targetFileName, ArrayList<UserEgonet> users){
		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			for(UserEgonet elem : users){
				int owner = elem.getId();
				HashMap<Integer, HashSet<Integer>> rels = elem.getRelations();
				for(Entry<Integer, HashSet<Integer>> e : rels.entrySet()){
					Integer from = e.getKey();
					HashSet<Integer> tos = e.getValue();
					for(Integer t : tos){
						writer.println(owner + " " + from.intValue() + " " + t.intValue());
					}
				}
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void writeAllFriendsInOneCircle(String targetFileName, Collection<UserEgonet> users){
		try{
			PrintWriter writer = new PrintWriter(targetFileName, "UTF-8");
			writer.println("UserId,Predicted");
			for(UserEgonet ue : users){
				String s = ue.getId() + ",";
				for(Integer f : ue.getFriends()){
					s += (f + " ");
				}
				writer.println( s.trim() );
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}