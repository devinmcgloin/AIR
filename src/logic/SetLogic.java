package logic;


import memory.Whiteboard;
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
 * TODO ideally the filter functions would return a new list, and not modify the old one.
 */
public final class SetLogic {

    static Logger logger = Logger.getLogger(SetLogic.class);

    private SetLogic(){}

    /**
     * TODO
     *
     * @param key
     * @param val
     * @return
     */
    public static boolean isValid(String key, String val) {
        return false;
    }

    public static ArrayList<Node> filter(ArrayList<Node> nodes, ArrayList<Node> isConditions, ArrayList<Node> hasConditions, ArrayList<Node> LDATAConditions) {
        nodes = isFilter(nodes, isConditions);
        nodes = hasFilter(nodes, hasConditions);
        nodes = LDATAFilter(nodes, LDATAConditions);
        return nodes;
    }

    public static ArrayList<Node> nameFilter(ArrayList<Node> nodes, String name) {
        ArrayList<Node> returnNodes = new ArrayList<>();
        for (Node option : nodes) {
            //Is filter
            ArrayList<String> nodeNames = Node.getName(option);
            for (String nodeName : nodeNames) {
                if (nodeName.equals(name)) {
                    returnNodes.add(option);
                }
            }
        }

        return returnNodes;
    }


    public static ArrayList<Node> isFilter(ArrayList<Node> nodes, ArrayList<Node> isConditions) {
        //Is filter
        for (Node term : isConditions) {
            nodes = isFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<Node> isFilter(ArrayList<Node> nodes, Node isCondition) {
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()){
            Node option = iterator.next();
            //Is filter

            if (!xISyP(option, isCondition)) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }

    public static ArrayList<Node> hasFilter(ArrayList<Node> nodes, ArrayList<Node> hasConditions) {
        //Is filter
        for (Node term : hasConditions) {
            nodes = hasFilter(nodes, term);
        }

        return nodes;
    }

    public static ArrayList<Node> hasFilter(ArrayList<Node> nodes, Node hasCondition) {
        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()){
            Node option = iterator.next();
            //Is filter

            if (!hasP(option, hasCondition)) {
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


    /**
     * TODO good place to check with scanner.
     *
     * @param node
     * @return
     */
    public static Node getLogicalParent(Node node) {
        if(node == null){
            logger.warn("Cannot get parents of null node.");
            return null;
        }

        ArrayList<Node> parents = new ArrayList<Node>();
        ArrayList<String> tmp = Node.getCarrot(node, "^logicalParents");
        if(tmp == null) {
            logger.warn(node.toString() + " did not contain the header ^logicalParents.");
            return null;
        }
        if(tmp.size()==0){
            logger.warn( node.toString() + " did not contain any ^logicalParents." );
            return null;
        }
//        if(tmp.size()>1){
//            logger.error( node.toString() + " contained too many ^logicalParents!!!" );
//            //FUCK what now? restructure? delete one? This shouldn't even be possible.
//            return null;
//        }

        for(String title: tmp){
            Node foo = PA.searchExactTitle(title);
            if(foo == null) {
                logger.error("Couldn't find node: " + title);
                continue;
            }
            return foo;
        }
        return null;
    }

    public static ArrayList<Node> getLogicalChildren(Node node){
        if(node == null){
            logger.warn("Cannot get children of null node");
            return null;
        }

        ArrayList<Node> children = new ArrayList<Node>();
        ArrayList<String> tmp = Node.getCarrot(node, "^logicalChildren");
        if(tmp == null) {
            logger.warn(node.toString() + " did not contain the header ^logicalChildren.");
            return null;
        }
        if(tmp.size()==0){
            logger.warn(node.toString() + " does not have any ^logicalChildren.");
            return null;
        }
        for(String title: tmp){
            Node foo = PA.searchExactTitle(title);
            if(foo == null) {
                logger.error("Couldn't find node: " + title);
                continue;
            }
            children.add(foo);
        }
        return children;

    }

    public static boolean xISyP(Node lc, Node lp){

        if(lp == null || lc == null)
            return false;

        //Get the logical children of the "parent" node
        ArrayList<String> logicalChildren = Node.getCarrot(lp, "^logicalChildren");

        //If there is no ^logicalChildren header we got a whole 'nother issue.
        if(logicalChildren == null ){
            logger.warn(lp.toString() + " does not contain the header ^logicalChildren.");
            return false;
        }

        //If there are no logical children, clearly this is false.
        if(logicalChildren.isEmpty()){
            return false;
        }

        //If the title of the logical child is contained in the logicalChildren of the logical parent, everything is fine.
        if( logicalChildren.contains( Node.getTitle(lc) ) ){
            return true;
        }else{
            //Get the next logical child of the logical parent. (Maybe it's a set within a set).
            ArrayList<Node> kids = getLogicalChildren(lp);

            for(Node kid : kids){
                //Obviously these kids exist, i just got them.
                boolean tmp = xISyP(lc, kid); //FUCK god i hope these methods work
                if(tmp){
                    return tmp;
                }
            }
        }

        return false;
    }

    /**
     * LDATA INHERIT VALUES
     *
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
        Whiteboard.addNode(y);
        Whiteboard.addNode(x);

        return x;
    }

    /**
     * TODO perhaps keep this info stored in the db? Just throw it into the log and look back at it if needed.
     * @param x
     * @param y
     * @return
     */
    public static Node xLikey(Node x, Node y){
        ArrayList<String> keys = Node.getKeys(y);
        for(String key: keys){
            x = Node.add(x, key);
        }
        Whiteboard.addNode(x);
        return x;
    }

    /**
     * TODO
     * @param node
     * @param key
     * @return
     */
    public static boolean hasP(Node node, Node key) {
        return false;
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
