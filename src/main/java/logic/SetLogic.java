package logic;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import funct.StrRep;
import memory.Notepad;
import org.apache.log4j.Logger;
import pa.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Set logic needs to create sets of like information
 * TODO: If we want a list of cities, no need to hash search for them, just go to the city node and getCarrot all of its logical children. (Ideally)
 * Implement or filtering - filter on multiple functions or expressions and take the union of all resulting sets.
 * TODO ideally the filter functions would return a new list, and not modify the old one.
 *
 * @author devinmcgloin
 * @version 6/3/15.
 */
public final class SetLogic {

    static Logger logger = Logger.getLogger(SetLogic.class);

    private SetLogic() {
    }

    /**
     * todo QA this
     *
     * @param key
     * @param val
     * @return
     */
    public static boolean isValid(String key, String val) {
        if (StrRep.isStringRepresentation(val) && LDATA.isLdata(key))
            return true;

        Node keyNode = Notepad.searchByTitle(key);
        Node valNode = Notepad.searchByTitle(val);

        if (valNode != null && keyNode != null) {
            return xISyP(keyNode, valNode);
        }

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
            returnNodes.addAll(nodeNames.stream()
                    .filter(nodeName -> nodeName.equals(name))
                    .map(nodeName -> option)
                    .collect(Collectors.toList()));
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

        while (iterator.hasNext()) {
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

        while (iterator.hasNext()) {
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

        while (iterator.hasNext()) {
            Node option = iterator.next();
            //Is filter
            if (!LDATA.expressionIsValid(option, LDATACondition)) {
                iterator.remove();
                break;
            }

        }
        return nodes;
    }


    /**
     * TODO good place to check with scanner.
     *
     * Implement, needs to look thru logical parents and trace up.
     *
     * TODO this is all sorts of fucked up.
     * @param node
     * @return
     */
    public static ArrayList<Node> getLogicalParents(Node node) {
        ArrayList<Node> parents = new ArrayList<>();
        if (node == null) {
            logger.warn("Cannot get parents of null node.");
            return parents;
        }

        ArrayList<String> logicalParentTitles = Node.getCarrot(node, "^logicalParents");

        if (logicalParentTitles == null) {
            logger.warn(node.toString() + " did not contain the header ^logicalParents.");
            return parents;
        } else if (logicalParentTitles.size() == 0) {
            logger.warn(node.toString() + " did not contain any ^logicalParents.");
            return parents;
        }

        for (String title : logicalParentTitles) {
            Node foo = Notepad.searchByTitle(title);
            if (foo == null) {
                logger.error("Couldn't find node: " + title);
                continue;
            }
            parents.add(foo);
            getLogicalParents(foo, parents);
        }
        return parents;
    }

    private static void getLogicalParents(Node node, ArrayList<Node> list) {
        if (Node.getTitle(node).equals("node") || Node.getCarrot(node, "^logicalParents").isEmpty()) {
            return;
        } else {
            for (String title : Node.getCarrot(node, "^logicalParents")) {
                Node foo = Notepad.searchByTitle(title);
                if (foo == null) {
                    logger.error("Couldn't find node: " + title);
                    continue;
                }
                list.add(foo);
                getLogicalParents(foo, list);
            }
        }
    }

    /**
     * returns the closest parent that is one step away from the given node. Eg if Acura NSX is passed in, it should
     * return car.
     * implement
     *
     * @param node
     *
     * @return
     */
    public static Optional<Node> getLogicalParent(Node node) {
        if (node == null) {
            logger.warn("Cannot get parents of null node.");
            return Optional.empty();
        }

        ArrayList<Node> parents = new ArrayList<>();
        ArrayList<String> tmp = Node.getCarrot(node, "^logicalParents");
        if (tmp == null) {
            logger.warn(node.toString() + " did not contain the header ^logicalParents.");
            return Optional.empty();
        }
        if (tmp.size() == 0) {
            logger.warn(node.toString() + " did not contain any ^logicalParents.");
            return Optional.empty();
        }
//        if(tmp.size()>1){
//            logger.error( node.toString() + " contained too many ^logicalParents!!!" );
//            //FUCK what now? restructure? delete one? This shouldn't even be possible.
//            return null;
//        }


        //todo have to decide which node is the best representation. This is not always clear.
        for (String title : tmp) {
            Node foo = Notepad.searchByTitle(title);
            if (foo == null) {
                logger.error("Couldn't find node: " + title);
                continue;
            }
            //implement here
        }
        return Optional.empty();
    }

    public static ArrayList<Node> getLogicalChildren(Node node) {
        if (node == null) {
            logger.warn("Cannot get children of null node");
            return null;
        }

        ArrayList<Node> children = new ArrayList<>();
        ArrayList<String> tmp = Node.getCarrot(node, "^logicalChildren");
        if (tmp == null) {
            logger.warn(node.toString() + " did not contain the header ^logicalChildren.");
            return children;
        }
        if (tmp.size() == 0) {
            logger.warn(node.toString() + " does not have any ^logicalChildren.");
            return children;
        }
        for (String title : tmp) {
            Node foo = Notepad.searchByTitle(title);
            if (foo == null) {
                logger.error("Couldn't find node: " + title);
                continue;
            }
            children.add(foo);
        }
        return children;

    }

    public static boolean xISyP(Node lc, Node lp) {

        if (lp == null || lc == null)
            return false;

        //Get the logical children of the "parent" node
        ArrayList<String> logicalChildren = Node.getCarrot(lp, "^logicalChildren");

        //If there is no ^logicalChildren header we got a whole 'nother issue.
        if (logicalChildren == null) {
            logger.warn(lp.toString() + " does not contain the header ^logicalChildren.");
            return false;
        }

        //If there are no logical children, clearly this is false.
        if (logicalChildren.isEmpty()) {
            return false;
        }

        //If the title of the logical child is contained in the logicalChildren of the logical parent, everything is fine.
        if (logicalChildren.contains(Node.getTitle(lc))) {
            return true;
        } else {
            //Get the next logical child of the logical parent. (Maybe it's a set within a set).
            ArrayList<Node> kids = getLogicalChildren(lp);

            for (Node kid : kids) {
                //Obviously these kids exist, i just got them.
                boolean tmp = xISyP(lc, kid); //FUCK god i hope these methods work
                if (tmp) {
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
    public static Node xINHERITy(Node x, Node y) {

        //SR-71 Blackbird   INHERITS        supersonic jet
        x = Node.add(x, "^logicalParents", Node.getTitle(y));

        //supersonice jet   gets        SR-71 Blackbird     as a child
        y = Node.add(y, "^logicalChildren", Node.getTitle(x));

        //Additional logic:
        //Now the child gets all the keys from the parent, with the exception of the carrot headers. (Even though those should be fine...)
        ArrayList<String> keys = Node.getKeys(y);
        for (String key : keys) {
            if (!Node.getCarrot(x, "^notKey").contains(key)) {
                x = Node.add(x, key);
            }
        }
        Notepad.addNode(y);
        Notepad.addNode(x);

        return x;
    }

    /**
     * TODO perhaps keep this info stored in the db? Just throw it into the log and look back at it if needed.
     *
     * @param x
     * @param y
     * @return
     */
    public static Node xLikey(Node x, Node y) {
        ArrayList<String> keys = Node.getKeys(y);
        for (String key : keys) {
            x = Node.add(x, key);
        }
        return x;
    }

    /**
     * TODO check this implementation.
     *
     * @param node
     * @param key
     * @return
     */
    public static boolean hasP(Node node, Node key) {
        return Node.getKeys(node).contains(Node.getTitle(key));
    }

    /*
    TODO How to do comparison of nodes to determine which ones are the same?
    TODO How to decide which ones get placed in the resulting set?
     */

    public ArrayList<Node> intersection(ArrayList<Node> setA, ArrayList<Node> setB) {
        Set<Object> setAA = ImmutableSet.builder().addAll(setA).build();
        Set<Object> setBB = ImmutableSet.builder().addAll(setB).build();
        Sets.SetView<Object> intersection = Sets.intersection(setAA, setBB);

        ArrayList<Node> returnIntersection = new ArrayList<>();
        for (Object n : intersection) {
            returnIntersection.add((Node) n);
        }

        return returnIntersection;

    }

    public ArrayList<Node> difference(ArrayList<Node> setA, ArrayList<Node> setB) {
        Set<Object> setAA = ImmutableSet.builder().addAll(setA).build();
        Set<Object> setBB = ImmutableSet.builder().addAll(setB).build();
        Sets.SetView<Object> difference = Sets.difference(setAA, setBB);

        ArrayList<Node> returnDifference = new ArrayList<>();
        for (Object n : difference) {
            returnDifference.add((Node) n);
        }

        return returnDifference;
    }

    public ArrayList<Node> union(ArrayList<Node> setA, ArrayList<Node> setB) {
        Set<Object> setAA = ImmutableSet.builder().addAll(setA).build();
        Set<Object> setBB = ImmutableSet.builder().addAll(setB).build();
        Sets.SetView<Object> union = Sets.union(setAA, setBB);

        ArrayList<Node> returnUnion = new ArrayList<>();
        for (Object n : union) {
            returnUnion.add((Node) n);
        }

        return returnUnion;
    }

    /**
     * setA is superset of setB
     *
     * @param setA
     * @param setB
     * @return
     */
    public boolean supersetP(ArrayList<Node> setA, ArrayList<Node> setB) {
        Set<Object> setAA = ImmutableSet.builder().addAll(setA).build();
        Set<Object> setBB = ImmutableSet.builder().addAll(setB).build();
        return setAA.containsAll(setBB);
    }

    /**
     * setA is subset of setB
     *
     * @param setA
     * @param setB
     * @return
     */
    public boolean subsetP(ArrayList<Node> setA, ArrayList<Node> setB) {
        Set<Object> setAA = ImmutableSet.builder().addAll(setA).build();
        Set<Object> setBB = ImmutableSet.builder().addAll(setB).build();
        return setBB.containsAll(setAA);
    }

    public boolean memberP(ArrayList<Node> set, Node node) {
        Set<Object> immutableSet = ImmutableSet.builder().addAll(set).build();
        return immutableSet.contains(node);
    }

}
