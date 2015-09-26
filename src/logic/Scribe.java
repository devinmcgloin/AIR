package logic;

import org.apache.log4j.Logger;
import pa.Node;

/**
 * Created by Blazej on 8/28/2015.
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
            logger.error("You cannot add an overflow node as a value. Although this may change in future.");
            return null;
        }


        //Keep in mind length of nodes given.
        //Anything above 3 implies OF.
        //IF 3 exactly, B, K, V is still possible.
        //IF 2, high likelyhood of B, K or B, V being triggered.


//        //-- check if second to last node is a Key to the Value.
        if (SetLogic.xISyP(tail, keyTmp)) {


            //That's great news! Now we just gotta find where we can add this K:V pair within the OF and all branches.


        }

//        Search Alternate
//        just first find anywhere the value can be put in the OF tree,
//                then filter on the keys/path.
//
//
// -- FALSE K:V
//        K might be a node in OF tree.
//         ---> if V is a stopping point, implies remaining nodes are for the use of OF branch finding.
//          OR--->will have to figure out correct K to classify it under

//        it actually IS a K:V   -- ask if V is K?


        return target;

    }


    public static Node addKey(Node base, Node key) {

        //if key has no ^LP, it's honestly probably just something this base has.
        //could be a value.

        return base;

    }


}
