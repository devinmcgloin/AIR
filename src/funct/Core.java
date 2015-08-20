package funct;

import org.apache.log4j.Logger;
import util.keyVal;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/20/15.
 * For small utility methods
 */
public class Core {

    static Logger logger = Logger.getLogger(Core.class);


    public static void println(String s){
        System.out.println(s);
    }

    public static void print(String s){
        System.out.print(s);
    }

    public static boolean all(boolean[] list){
        for(boolean bool : list){
            if(!bool)
                return false;
        }
        return true;
    }

    public static <T> boolean checkArray(ArrayList<T> terms){
        for(Object item : terms ){
            if(item == null)
                return false;
        }
        return true;
    }


    public static Object getVal(ArrayList<keyVal> pairs, String key){
        for(keyVal pair : pairs){
            if(pair.getKey().equals(key))
                return pair.getVal();
        }
        return null;
    }
}
