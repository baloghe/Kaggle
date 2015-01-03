package circle;

import java.util.HashSet;

import common.InitFromString;

public class UserFriends implements InitFromString {
	
	private int id;
	private HashSet<Integer> friends;
	
	public void init(String inStr, String sep) {
		friends = new HashSet<Integer>();
		
		boolean isFirst = true;
		String[] strvec = inStr.split(sep);
		for(String elem : strvec){
			if(isFirst){
				this.id = (new Integer(elem.substring(0, elem.length()-1) )).intValue();
				isFirst = false;
			} else {
				Integer friend = new Integer(elem.trim());
				friends.add(friend);
			}
		}
	}
	
	public int getId(){return id;}
	
	public int getFriendsNum(){return friends.size();}
	
	public String friendsToString(String sep){
		String ret = "";
		for(Integer friend : friends){
			ret += (friend.intValue() + sep);
		}
		return ret;
	}
}
