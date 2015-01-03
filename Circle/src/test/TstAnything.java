package test;

import java.util.HashSet;
import java.util.HashMap;
import circle.*;
import common.*;
import feature.*;

public class TstAnything {

	public static void main(String[] args) {
	   Integer[][] arr = new Integer[4][2];
	   for(int i=0; i<arr.length; i++){
	      for(int j=0; j<arr[i].length; j++){
	         arr[i][j] = new Integer(10*i + j);
	         System.out.println("arr["+i+"]["+j+"] = " + arr[i][j]);
	      }
	   }
	}

}
