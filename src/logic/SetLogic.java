package logic;


import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by devinmcgloin on 6/3/15.
 * Set logic needs to create sets of like information
 * TODO: If we want a list of cities, no need to hash search for them, just go to the city node and getCarrot all of its logical children. (Ideally)
 * TODO: Implement or filtering.
 * To implement filter on multiple functions or expressions and take the union of all resulting sets.
 */
public final class SetLogic {

    static Logger logger = Logger.getLogger(SetLogic.class);

    private SetLogic(){}

    public static boolean isValid(String key, String val) {

    }


    public static ArrayList<Node> filter(ArrayList<Node> nodes, ArrayList<String> isConditions, ArrayList<String> hasConditions, ArrayList<Node> LDATAConditions) {
        nodes = isFilter(nodes, isConditions);
        nodes = hasFilter(nodes, hasConditions);
        nodes = LDATAFilter(nodes, LDATAConditions);
        return nodes;
    }


    public static ArrayList<Node> isFilter(ArrayList<Node> nodes, ArrayList<String> isConditions){
        //Is filter
        for(String term : isConditions) {
            nodes = isFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<Node> isFilter(ArrayList<Node> nodes, String isCondition){
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()){
            Node option = iterator.next();
            //Is filter

            if (!Node.isP(option, isCondition.trim())) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<Node> hasFilter(ArrayList<Node> nodes, ArrayList<String> hasConditions){
        //Is filter
        for(String term : hasConditions) {
            nodes = hasFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<Node> hasFilter(ArrayList<Node> nodes, String hasCondition){
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()){
            Node option = iterator.next();
            //Is filter

            if (!Node.hasP(option, hasCondition.trim())) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<Node> LDATAFilter(ArrayList<Node> nodes, ArrayList<Node> LDATAConditions) {
        //Is filter
        for (Node term : LDATAConditions) {
            nodes = LDATAFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<Node> LDATAFilter(ArrayList<Node> nodes, Node LDATACondition) {
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()){
            Node option = iterator.next();
            //Is filter
            if (!LDATA.isValid(option, LDATACondition)) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<Node> getSetMembers (Node node){

        return Node.getLogicalChildren(node);

    }

    /**
     * Returns an arraylist of nodes that can be sent to getsetmembers in order to generate teh full set of those nodes.
     * @param node
     * @return
     */
    public static ArrayList<Node> getSets (Node node){
        return Node.getLogicalParent(node);
    }

    public static boolean xISyP(Node x, Node y){

        if(y == null || x == null)
            return false;

        //Get the logical children of the "parent" node
        ArrayList<String> logicalChildren = Node.getCarrot(y, "^logicalChildren");

        //If there are no logical children, clearly this is false.
        if(logicalChildren == null || logicalChildren.isEmpty()){
            return false;
        }

        //If the title of the logical child is contained in the logicalChildren of the logical parent, everything is fine.
        if( logicalChildren.contains( Node.getTitle(x) ) ){
            return true;
        }

        //Technically we should also check to make sure that there isn't information written in reverse. In case something wasn't reflexively written in.

        return false;
    }

    /**
     * todo - LDATA INHERIT VALUES,
     * @param x - the child
     * @param y - the parent
     * @return
     */
    public static Node xINHERITy(Node x, Node y){

        //SR-71 Blackbird   INHERITS        supersonic jet
        x = Node.add(x, "^logicalParents", Node.getTitle(y));

        //supersonice jet   gets        SR-71 Blackbird     as a child
        y = Node.add(y, "^logicalChildren", Node.getTitle(x));

        //Additional logic:
        //Now the child gets all the keys from the parent, with the exception of the carrot headers. (Even though those should be fine...)
        ArrayList<String> keys = Node.getKeys(y);
        for(String key: keys){
            if (!Node.getCarrot(x, "^notKey").contains(key)) {
                x = Node.add(x, key);
            }
        }
//TODO: eval this to see if it would be better to return both.
        PA.put(y);

        return x;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static Node xLikey(Node x, Node y){
        ArrayList<String> keys = Node.getKeys(y);
        for(String key: keys){
            x = Node.add(x, key);
        }
        //TODO: eval this to see if it would be better to return both.
        return x;
    }

    /*
    TODO How to do comparison of nodes to determine which ones are the same?
    TODO How to decide which ones get placed in the resulting set?
     */

    public ArrayList<Node> intersection(ArrayList<Node> setA, ArrayList<Node> setB){
        ArrayList<Node> finalSet = new ArrayList<>();
        for(Node node : setA){
            if(memberP(setB, node)){
                //TODO should merge nodes here.
                finalSet.add(node);
            }
        }
        return finalSet;
    }

    public ArrayList<Node> difference(ArrayList<Node> setA, ArrayList<Node> setB){
        ArrayList<Node> finalSet = new ArrayList<>();

        //Node from setA is not present in setB
        for(Node node : setA){
            if(!memberP(setB, node)){
                finalSet.add(node);
            }
        }
        //Node from setB is not present in setA
        for(Node node : setB){
            if(!memberP(setA, node)){
                finalSet.add(node);
            }
        }
        return finalSet;
    }

    public ArrayList<Node> union(ArrayList<Node> setA, ArrayList<Node> setB){
        ArrayList<Node> finalSet = new ArrayList<>();
        for(Node node : setA){
            if(memberP(setB, node)){
                //TODO merge
            }else{
                finalSet.add(node);
            }
        }
        for(Node node : setB){
            if(memberP(setA, node)){
                //TODO merge
            }else{
                finalSet.add(node);
            }
        }
        return finalSet;
    }

    /**
     * setA is superset of setB
     * @param setA
     * @param setB
     * @return
     */
    public boolean supersetP(ArrayList<Node> setA, ArrayList<Node> setB){
        for(Node node : setB){
            if(!memberP(setA, node)){
                return false;
            }
        }
        return true;
    }

    /**
     * setA is subset of setB
     * @param setA
     * @param setB
     * @return
     */
    public boolean subsetP(ArrayList<Node> setA, ArrayList<Node> setB){
        for(Node node : setA){
            if(!memberP(setB, node)){
                return false;
            }
        }
        return true;
    }

    public boolean memberP(ArrayList<Node> set, Node node){
        for(Node member : set){
            if(Node.getTitle(member).equals(Node.getTitle(node))){
                return true;
            }
        }
        return false;
    }

}
