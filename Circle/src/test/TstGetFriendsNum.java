package test;

import java.util.ArrayList;
import java.util.HashSet;

import circle.UserFriends;

import common.Reader;

public class TstGetFriendsNum {
	public static void main(String[] args) {
		Reader<UserFriends> rd = new Reader<UserFriends>(UserFriends.class);
		try{
			ArrayList<UserFriends> lst = rd.read("../239.egonet", " ");
			UserFriends uf = lst.get(0);
			System.out.println("First user's friends: id=" + uf.getId()
					   + " -> friends: " + uf.friendsToString(" "));
			for(UserFriends u : lst){
				System.out.println(u.getId() + " " + u.getFriendsNum());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
