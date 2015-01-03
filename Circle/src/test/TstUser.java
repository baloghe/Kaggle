package test;

import java.util.HashSet;

import common.*;
import circle.*;

public class TstUser {

	public static void main(String[] args) {
		UserFeatures u = new UserFeatures();
		u.init("0 last_name;0 first_name;0 birthday;0 name;0 gender;0 locale;0 hometown;name;0 hometown;id;0 education;school;name;0 education;school;id;0 education;type;0 education;year;name;0 education;year;id;0 education;school;name;1 education;school;id;1 education;type;1 education;concentration;name;0 education;concentration;id;0 education;year;name;1 education;year;id;1 id;0 location;name;0 location;id;0", " ");
		HashSet<String> nuk = u.getNonUniqueFeatures();
		System.out.println(u.getId() + " -> " + nuk.toString());
	}
	
}
