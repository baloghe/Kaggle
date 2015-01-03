package circle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import feature.*;

public class UserEgonet {

	private Integer id; //uder (=egonet owner) id
	private HashSet<Integer> friends;
	private HashMap<Integer, HashSet<Integer>> relations;

	public UserEgonet(Integer id){
		this.id = id;
		friends = new HashSet<Integer>();
		relations = new HashMap<Integer, HashSet<Integer>>();
	}

	public void addFriend(Integer aFriend){
		friends.add(aFriend);
	}

	public void addRelation(Integer a, Integer b){
		//ensure both friends are listed among friends
		friends.add(a);
		friends.add(b);

		//add as FROM
		addRelation(relations, a, b);
		//add as TO
		addRelation(relations, b, a);
	}

	private void addRelation(HashMap<Integer, HashSet<Integer>> edges, Integer owner, Integer newFriend){
		HashSet<Integer> fs = edges.get(owner);
		if(fs == null){
			//create
			fs = new HashSet<Integer>();
		}
		//add friend, put back
		fs.add(newFriend);
		edges.put(owner, fs);
	}

	public int getId(){return id.intValue();}

	public int getFriendsNumber(){return friends.size();}

	public HashSet<Integer>  getFriends(){return friends;}

	public HashMap<Integer, HashSet<Integer>> getRelations(){return relations;}

	public HashMap<Feature, Integer> getPopularFeatures(int topCnt){
		HashMap<Feature, Integer> ret = new HashMap<Feature, Integer>();
		for(Integer user : friends){
			UserFeatures uf = Main.userFeatures.get(user);
			for(Feature f : uf.getFeatures()){
			   if(    (!f.getType().equalsIgnoreCase("feature.FeatureLanguage"))
				   && (!f.getType().equalsIgnoreCase("gender"))
				   && (!f.getType().equalsIgnoreCase("location;id"))
				   && (!f.getType().equalsIgnoreCase("hometown;id"))
				 ){
					if(ret.containsKey(f)){
						int cnt = ret.get(f).intValue();
						ret.put(f, new Integer(++cnt));
					} else {
						ret.put(f, new Integer(1));
					}
			   }
			}//next feature of the user
		}//next user

		//put them into a priority queue
		PriorityQueue<Entry<Feature, Integer>> pq =
				new PriorityQueue<Entry<Feature, Integer>>(ret.size(), new FeaturePopularityComparator());
		pq.addAll(ret.entrySet());

		//return top ones
		HashMap<Feature, Integer> ret2 = new HashMap<Feature, Integer>();
		for(int i=0; i<topCnt; i++){
			Entry<Feature, Integer> top = pq.poll();
			ret2.put(top.getKey(), top.getValue());
		}

		return ret2;
	}

	public boolean knowEachOther(Integer friend1, Integer friend2){
		HashSet<Integer> friend1sfriends = relations.get(friend1);
		if(friend1sfriends != null){
			return friend1sfriends.contains(friend2);
		} else return false;
	}

	public int numFriendsInEgonet(int user1){
		int ret = 0;
		HashSet<Integer> s1 = relations.get(new Integer(user1));
		if(s1 != null){
			ret = s1.size();
		}
		return ret;
	}
	
	public int numCommonFriends(int user1, int user2){
	   int ret = 0;
	   HashSet<Integer> s1 = relations.get(new Integer(user1));
	   HashSet<Integer> s2 = relations.get(new Integer(user2));
	   if(s1!=null && s2 != null){
		   for(Integer m : s1){
		      ret += (s2.contains(m) ? 1 : 0);
		   }
	   }
	   return ret;
	}
}
