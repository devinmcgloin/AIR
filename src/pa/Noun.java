package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 7/12/15.
 * This is the class that sets up the libraries and deals with NBNs.
 * These are the only methods that can be called in the libraries that are built on top of NBN.
 *
 * This gives us flexibility later down the line, and allows us to change this class to redirect to a different system for processing NBN. If we called functions on NBN then we would never be able to switch from an object based system, as there would be loads of legacy calls to NBN.
 * Here we can direct to any implementation of NBN ideas just be refactoring the types of whats being sent in rather than the calls and code syntax.
 */
public final class Noun {

    /**
     * Don't let anyone instantiate this class.
     */
    private Noun() {}

    public static String getTitle(NBN node){
        return node.getTitle();
    }

    public static ArrayList<String> getName(NBN node){
        return node.get("^name");
    }

    public static ArrayList<String> getKeys(NBN node){
        return node.getKeys();
    }

    public static ArrayList<String> get(NBN node, String key){
        return node.get(key);
    }

    public static boolean hasP(NBN node, String key){
        if(node.get(key) != null )
            return true;
        else
            return false;
    }

    public static boolean isP(NBN node, String key){
        for(String entry : node.get("^is")){
            if(entry.equals(key))
                return true;
            else
                return false;
        }
        return false;
    }

    public static NBN add(NBN node, String key){ return node.add(key); }

    public static NBN add(NBN node, String key, String val ){
        return node.add(key, val);
    }

    public static NBN rm(NBN node, String key){
        return node.rm(key);
    }

    public static NBN rm(NBN node, String key, String val ){
        return node.rm(key, val);
    }

    public static NBN update(NBN node, String key, String oldVal, String newVal){
        return node.update(key, oldVal, newVal);
    }

    public static NBN add(NBN node,ArrayList<String> keys, ArrayList<String> vals){
        return node.batchAdd(keys, vals);
    }

    public static NBN rm(NBN node, ArrayList<String> keys){
        return node.batchRM(keys);
    }

    public static NBN rm(NBN node, ArrayList<String> keys, ArrayList<String> vals ){
        return node.batchRM(keys, vals);
    }

    public static NBN update(NBN node, ArrayList<String> keys, ArrayList<String> oldVals, ArrayList<String> newVals){
        return node.batchUpdate(keys, oldVals, newVals);
    }
    public static boolean nounP(String value){
        if(PA.getNoun(value) != null)
            return true;
        else
            return false;
    }

    public static String search(NBN node, String key){
        return Noun.get(node, key).get(0);
    }

}
