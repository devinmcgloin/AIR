package logic;

import org.apache.log4j.Logger;
import pa.Node;

import java.util.ArrayList;
import java.util.Arrays;

/**
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
        if(tail.toString().contains("^")){
            logger.error("You cannot add an overflow node as a value. Although this may most definitely change in future.");
            return null;
        }


        //Keep in mind length of nodes given.
        //Anything above 3 implies OF.
        //IF 3 exactly, B, K, V is still possible.
        //IF 2, high likelihood of B, K or B, V being triggered.


        //Get all base
        ArrayList<Node> baseNodes = searchHighLevel( new ArrayList<Node>(Arrays.asList(nodes)) );

        if(baseNodes == null || baseNodes.size() == 0){
            logger.error("Couldn't find any nodes in a single branch given that combination.");
            //FUCK Could also imply there was a Key that should exist but doesn't.
            return null;
        }

        //If two last nodes are KV pair, just find a place to put them.
        boolean kvTail = false;
        if( SetLogic.xISyP(tail, keyTmp) ){
            kvTail = true;
        }

        if( baseNodes.size() == 1){
            //Check for KV
        }



//
//
// -- FALSE K:V
//        K might be a node in OF tree.
//         ---> if V is a stopping point, implies remaining nodes are for the use of OF branch finding.
//          OR--->will have to figure out correct K to classify it under

//        it actually IS a K:V   -- ask if V is K?








        return target;

    }

    public static Node addKey(Node base, Node key){

        //if key has no ^LP, it's honestly probably just something this base has.
        //could be a value.

        return base;

    }


}
