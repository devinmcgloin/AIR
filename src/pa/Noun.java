package pa;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 7/12/15.
 * This is the class that sets up the libraries and deals with NBNs.
 * These are the only methods that can be called in the libraries that are built on top of NBN.
 *
 * This gives us flexibility later down the line, and allows us to change this class to redirect to a different system for processing NBN. If we called functions on NBN then we would never be able to switch from an object based system, as there would be loads of legacy calls to NBN.
 * Here we can direct to any implementation of NBN ideas just be refactoring the types of whats being sent in rather than the calls and code syntax.
 * comment to push
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
        ArrayList<String> returnItem = new ArrayList<String>();
        returnItem.add(node.getTitle());
        return returnItem;
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

    public static ArrayList<String> search(NBN node, String key){
        return null;
    }

    public static String simpleSearch(NBN node, String key){
        //Have to make sure it accounts for overflow.

        if(node == null)
            return "^Null Node";

        if(key == null || key.equals("^N/A"))
            return "^No Key";   //if no key, try triggering overflowSearch and filtering on context.


        ArrayList<String> x = Noun.get(node, key);
        if(x == null){
            return "^No Value"; //if no value, try tracing up to logical parents to get a spread of likelihood.
            //(best guess search)
        }
        else{
            return x.get(0);
        }

    }

    public static ArrayList<NBN> getLogicalParents(NBN node){
        if(node == null){
            return null;
        }

        ArrayList<NBN> parents = new ArrayList<NBN>();
        ArrayList<String> tmp = node.get("^logicalParents");
        if(tmp ==null)
            return null;
        for(String title: tmp){
            NBN foo = PA.getNoun(title);
            if(foo == null) {
                System.out.println("NOUN: Couldn't find node: " + title);
                continue;
            }
            parents.add( foo );
        }
        return parents;
    }

    public static ArrayList<NBN> getLogicalChildren(NBN node){
        if(node == null){
            return null;
        }

        ArrayList<NBN> children = new ArrayList<NBN>();
        ArrayList<String> tmp = node.get("^logicalChildren");
        if(tmp ==null)
            return null;
        for(String title: tmp){
            NBN foo = PA.getNoun(title);
            if(foo == null) {
                System.out.println("NOUN: Couldn't find node: " + title);
                continue;
            }
            children.add(foo);
        }
        return children;
    }



    //Returns an arraylist of OFlowed NBNs that contain the key.
    public static ArrayList<NBN> overflowSearch(NBN node, String key){
        //Couldn't find Value, must account for overflow.
        ArrayList<String> keys = Noun.getKeys(node);
        ArrayList<String> tmpOF = new ArrayList<String>();
        ArrayList<NBN> OFlows = new ArrayList<NBN>();

        //Look into all current keys for overflow nodes
        for( String k: keys ){
            if(k.startsWith("^"))
                continue;
            String value =  simpleSearch(node, k);
            if(value.contains("+")){
                tmpOF.add(value);
            }
        }




        //Search the overflown nodes for the key.
        for( String OFTitle :tmpOF ){
            NBN tmp  = PA.getNoun(OFTitle);
            String val = simpleSearch(tmp, key);
            if( !val.startsWith("^") ){
                //Found a valid value for the key!
                OFlows.add(tmp);
            }
            if ( val.equals("^No Value")){
                //Key was still found and a success
                OFlows.add(tmp);
            }

            //Otherwise, try finding deeper levels!
            ArrayList<NBN> otherSuccesses = overflowSearch(tmp, key);
            for(NBN success : otherSuccesses ){
                OFlows.add(success);
            }

        }
        return OFlows;
    }

}
