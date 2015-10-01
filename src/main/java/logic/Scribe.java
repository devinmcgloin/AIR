package logic;

import funct.Pauser;
import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Blazej
 * @version 8/28/2015.
 */
public final class Scribe {

    static Logger logger = Logger.getLogger(LDATA.class);

    public static Node addHighLevel(Node... nodes) {


        if (nodes == null || nodes.length < 2) {
            logger.error("Add function requires at least two arguements.");
            return null;
        }

        Node target = null;     //Node that will get the change. (Might be an OF node).
        Node branchBase = nodes[0];     //The branch base
        Node tail = nodes[nodes.length - 1]; //Either a key that needs to be added or a val to a key.
        Node keyTmp = nodes[nodes.length - 2]; //The second to last node must be checked as a contender for a Key in a K:V pair.

        for (Node n : nodes) {
            //All nodes should exist in DB (implied, might be necessary to check at later time).
            if (n == null) {
                logger.error("Null node. Couldn't complete function, returning null.");
                return null;
            }
        }

        //Val shouldn't be an OF node.
        if (tail.toString().contains("^")) {
            logger.error("You cannot add an overflow node as a value. Although this may most definitely change in future.");
            return null;
        }


        //Keep in mind length of nodes given.
        //Anything above 3 implies OF.
        //IF 3 exactly, B, K, V is still possible.
        //IF 2, high likelihood of B, K or B, V being triggered.


        //If two last nodes are KV pair, just find a place to put them. Many checks might need that
        boolean kvTail = false;
        if (SetLogic.xISyP(tail, keyTmp)) {
            kvTail = true;
        }


        //Get all base
        ArrayList<Node> baseNodes = searchHighLevel(nodes);

        if (baseNodes == null || baseNodes.size() == 0) {
            logger.error("Couldn't find any nodes in a single branch given that combination.");
            //FUCK Could also imply there was a Key that should exist but doesn't. (Especially if it's a K:V pair.)
            //Actually if it's a KV pair we can just rerun the search with everything but the last two, ask again if it's where it should be added.
            //Actually, NVM. The way ghost tree filters branches is the same way you would "refilter" as described below.

            //Ask that^
            //if it's the case, "refilter" and run the search with ghost tree AGAIN, but this time use all but the last off the tail.
            //keep doing this until we find something. if user sends back false, then just return null and forget it,
            //the key that's missing is probably too far up the ghosttree. FUCK it's also possible we may have to RESTRUCTURE
            //this node. If we can approximate distance to like nodes, we can better understand the internal ghost structure of it.
            return null;
        }

        //The first iteration of highlevel add will ask user where they want to add something (if there is any ambiguity).
        //Only one place to put them.
        if (baseNodes.size() != 1) {
            System.out.println("Which node are you trying to add to?");
            int select = Pauser.whichOne(baseNodes);
            //FUCK am i using this correctly?
            if(select<0)
                return null;
            else
                target = baseNodes.get(select); //FUCK does the select count 0, 1, 2... or 1, 2, 3....
        }else{
            target = baseNodes.get(0); // one and only!
        }



        if(kvTail){
            //Find a place to put them in target.

            //Check node contains that key.
            if(Node.contains(target, keyTmp.toString()) ){
                //Add val to that key
                Node tmp = Node.add(target, keyTmp.toString(), tail.toString());
                //FUCK we should also check that value isn't currently filled out! Check how Node.add() works with that.
                //Ask if that was correct.
                boolean correct = Pauser.trueFalse("Is the correct format?\n" + target.toString() + "\n  " + keyTmp.toString() + "\n    " + tail.toString());
                if(correct){
                    return tmp;
                }
                else{
                    logger.error("Could not find correct place to put it. Ask Blaze. This is very, very interesting.");
                    //This means we had a K:V pair to add to a BN. So the last node was a ^lc of the second to last node and this is the only node we found.
                    return null;
                }

            } else{
                //You'll have to add the key. Then add the value to that.
                Node tmp = Node.add(target, keyTmp.toString());
                tmp = Node.add(target, keyTmp.toString(), tail.toString());

                //Ask if that's okay.
                boolean correct = Pauser.trueFalse("Is the correct format?\n" + target.toString() + "\n  " + keyTmp.toString() + "\n    " + tail.toString());
                if(correct){
                    return tmp;
                }
                else{
                    logger.error("Could not find correct place to put it. Ask Blaze. This is very, very interesting.");
                    //This means we had a K:V pair to add to a BN. So the last node was a ^lc of the second to last node and this is the only node we found.
                    return null;
                }
            }


        } else{


            //could check if it's GENERALLY a key or val in other nodes.

            //It's a value in target. Must have Key somewhere that can take it. (findKeyContender checks xISy)
            Node key = findKeyContender(target, tail);

            //It's a key in the target.
            if(key == null){
                //You'll have to add the key. Then add the value to that.
                Node tmp = Node.add(target, tail.toString());

                //Ask if that's okay.
                boolean correct = Pauser.trueFalse("Is the correct format?\n" + target.toString() + "\n  " + tail.toString());
                if(correct){
                    return tmp;
                }
                else{
                    logger.error("Could not find correct place to put it. Ask Blaze. This is very, very interesting.");
                    //This means we had non-lc lp relation anywhere within the target node, implying the tail received originally is a key for the target.
                    return null;
                }





            }else{ //It's a value in target.
                Node tmp = Node.add(target, key.toString(), tail.toString());

                //Ask if that's okay.
                boolean correct = Pauser.trueFalse("Is the correct format?\n" + target.toString() + "\n  " + key.toString() + "\n    " + tail.toString());
                if(correct){
                    return tmp;
                }
                else{
                    logger.error("Could not find correct place to put it. Ask Blaze. This is very, very interesting.");
                    //This means we had a K:V pair to add to a BN. So the last node was a ^lc of a key within the target.
                    return null;
                }

            }


        }





//
//
// -- FALSE K:V
//        K might be a node in OF tree.
//         ---> if V is a stopping point, implies remaining nodes are for the use of OF branch finding.
//          OR--->will have to figure out correct K to classify it under

//        it actually IS a K:V   -- ask if V is K?


//If you have other nodes with a key or val, you can maybe ask if you should structure them like those nodes. *shrug*



    }


    //Wait, question. Technically since it's a highlevel function it could ask you which node it is that you wanted with Pauser.
    //That way it only returns one node, like addHighLevel.
    /**
     * Takes an arrayList of nodes that must be in the order you think they are in the tree. (Technically I can write a method that
     * sorts them on its own to figure out how they lay in the tree, but I was told this wasn't a priority)
     *
     * @param args
     * @return
     */
    public static ArrayList<Node> searchHighLevel(Node... args) {
        ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(args));

        if (nodes == null || nodes.size() < 2) {
            logger.error("Add function requires at least two arguements.");
            return null;
        }

        Node target = null;     //Node that will get the change. (Might be an OF node).
        Node branchBase = nodes.get(0);     //The branch base
        Node tail = nodes.get(nodes.size() - 1); //Either a key that needs to be added or a val to a key.
        Node keyTmp = nodes.get(nodes.size() - 2); //The second to last node must be checked as a contender for a Key in a K:V pair.

        for (Node n : nodes) {
            //All nodes should exist in DB (implied, might be necessary to check at later time).
            if (n == null) {
                logger.error("Null node. Couldn't complete function, returning null.");
                return null;
            }
        }

        //Create the ghost tree
        GhostTree ghostTree = new GhostTree(branchBase);

        //Filter branches incrementally.
        for (int i = nodes.size() - 1; i > 0; i--) {
            ghostTree.filterBranches(nodes.get(i));
        }

        return ghostTree.getContendersBases();


    }

    /**
     * Same as search high level but this returns the actual values, not the keys for the values.
     *
     * @param args
     * @return
     */

    public static ArrayList<Node> searchHighLevelValues(Node... args) {
        ArrayList<Node> nodes = new ArrayList<Node>(Arrays.asList(args));

        if (nodes == null || nodes.size() < 2) {
            logger.error("Add function requires at least two arguements.");
            return null;
        }

        Node target = null;     //Node that will get the change. (Might be an OF node).
        Node branchBase = nodes.get(0);     //The branch base
        Node tail = nodes.get(nodes.size() - 1); //Either a key that needs to be added or a val to a key.
        Node keyTmp = nodes.get(nodes.size() - 2); //The second to last node must be checked as a contender for a Key in a K:V pair.

        for (Node n : nodes) {
            //All nodes should exist in DB (implied, might be necessary to check at later time).
            if (n == null) {
                logger.error("Null node. Couldn't complete function, returning null.");
                return null;
            }
        }

        //Create the ghost tree
        GhostTree ghostTree = new GhostTree(branchBase);

        //Filter branches incrementally.
        for (int i = nodes.size() - 1; i > 0; i--) {
            ghostTree.filterBranches(nodes.get(i));
        }

        return ghostTree.getContenders();


    }

    /**
     * Finds a keynode within te base that the val can be placed under.
     * @param base
     * @param val
     * @return
     */
    public static Node findKeyContender(Node base, Node val){
        for( String k: Node.getKeys(base)){
            Node key = PA.searchExactTitle(k);
            if(key == null)
                continue;
            if( SetLogic.xISyP(val, key) ){
                return key;
            }
        }

        return null;

    }




}
