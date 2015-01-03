package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//import java.lang.reflect.ParameterizedType;

public class Reader<T extends InitFromString>{

   protected Class<T> reference;

   public Reader(Class<T> classRef){
      reference = classRef;
   }

   public ArrayList<T> read(String fileName, String sep) throws IOException, FileNotFoundException {
      ArrayList<T> ret = new ArrayList<T>();

      BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                T obs = getInstanceOfT();
                obs.init(line,sep);
                ret.add(obs);
            }
        } catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
            input.close();
        }

      return ret;
   }
   
   protected T getInstanceOfT(){
       try {
            return reference.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}