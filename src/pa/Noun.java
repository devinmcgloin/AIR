package pa;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 7/12/15.
 * This is the class that sets up the libraries and deals with Nodes.
 * These are the only methods that can be called in the libraries that are built on top of Node.
 *
 * This gives us flexibility later down the line, and allows us to change this class to redirect to a different system for processing Node. If we called functions on Node then we would never be able to switch from an object based system, as there would be loads of legacy calls to Node.
 * Here we can direct to any implementation of Node ideas just be refactoring the types of whats being sent in rather than the calls and code syntax.
 * comment to push
 */
public final class Noun {

    static Logger logger = Logger.getLogger(Noun.class);

    /**
     * Don't let anyone instantiate this class.
     */
    private Noun() {}

    public static String getTitle(Node node){
        return node.getTitle();
    }

    /**
     * TODO start adding things by their name, not their title.
     * @param node
     * @return
     */
    public static ArrayList<String> getName(Node node){
        return node.get("^name");
    }

    public static ArrayList<String> getKeys(Node node){
        return node.getKeys();
    }

    public static ArrayList<String> get(Node node, String key){
        return node.get(key);
    }

    public static boolean hasP(Node node, String key){
        if(node.get(key) != null )
            return true;
        else
            return false;
    }

    /**
     * TODO need to come back to this, right now it just returns false if the first one doesnt match.
     * @param node
     * @param key
     * @return
     */
    public static boolean isP(Node node, String key){
        for(String entry : node.get("^logicalParent")){
            if(entry.equals(key))
                return true;
        }
        return false;
    }

    public static Node add(Node node, String key){ return node.add(key); }

    public static Node add(Node node, String key, String val ){
        if(!validateP(key, val) || !LDATA.validateP(key, val))
            return node.add(key, val);
        logger.warn("Value not verified, original node returned.");
        return node;
    }

    public static Node rm(Node node, String key){
        node = Noun.add(node,"^notKey", key);
        return node.rm(key);
    }

    public static Node rm(Node node, String key, String val ){
        return node.rm(key, val);
    }

    public static Node update(Node node, String key, String oldVal, String newVal){
        return node.update(key, oldVal, newVal);
    }

    public static boolean nounP(String value){
        if(PA.get(value) != null)
            return true;
        else
            return false;
    }

    /**
     * TODO Have to validate from overflown node.
     * @param key
     * @param val
     * @return
     */
    public static boolean validateP(String key, String val){
        Node Nodenode = PA.get(key);
        if(Nodenode != null){
            for(Node node : getLogicalChildren(Nodenode)){
                for(String name : Noun.getName(node)){
                    if(name.equals(val)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<String> search(Node node, String key){
        return null;
    }

    public static String simpleSearch(Node node, String key){
        //Have to make sure it accounts for overflow.

        if(node == null)
            return "^Null Node";

        if(key == null || key.equals("^N/A"))
            return "^No Key";   //if no key, try triggering overflowSearch and filtering on context.


        ArrayList<String> x = Noun.get(node, key);
        if(x == null){
            return "^No Value"; //if no value, try tracing up to logical parents to get a spread of likelihood.
            //TODO (best guess search)
        }
        else{
            return x.get(0);
        }

    }

    public static ArrayList<Node> getLogicalParents(Node node){
        if(node == null){
            return null;
        }

        ArrayList<Node> parents = new ArrayList<Node>();
        ArrayList<String> tmp = node.get("^logicalParents");
        if(tmp ==null)
            return null;
        for(String title: tmp){
            Node foo = PA.get(title);
            if(foo == null) {
                logger.error("NOUN: Couldn't find node: " + title);
                continue;
            }
            parents.add( foo );
        }
        return parents;
    }

    public static ArrayList<Node> getLogicalChildren(Node node){
        if(node == null){
            return null;
        }

        ArrayList<Node> children = new ArrayList<Node>();
        ArrayList<String> tmp = node.get("^logicalChildren");
        if(tmp ==null)
            return null;
        for(String title: tmp){
            Node foo = PA.get(title);
            if(foo == null) {
                logger.error("NOUN: Couldn't find node: " + title);
                continue;
            }
            children.add(foo);
        }
        return children;
    }



    //Returns an arraylist of OFlowed Nodes that contain the key.
    public static ArrayList<Node> overflowSearch(Node node, String key){
        //Couldn't find Value, must account for overflow.
        ArrayList<String> keys = Noun.getKeys(node);
        ArrayList<String> tmpOF = new ArrayList<>();
        ArrayList<Node> OFlows = new ArrayList<>();

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
            Node tmp  = PA.get(OFTitle);
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
            ArrayList<Node> otherSuccesses = overflowSearch(tmp, key);
            for(Node success : otherSuccesses ){
                OFlows.add(success);
            }

        }
        return OFlows;
    }

}
