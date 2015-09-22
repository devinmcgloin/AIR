package logic;

import org.apache.log4j.Logger;
import pa.Node;

import java.util.ArrayList;

/**
 * Created by Blazej on 8/28/2015.
 */
public final class Scribe {

    static Logger logger = Logger.getLogger(LDATA.class);

    public static Node addHighLevel(Node ... nodes ){


        if(nodes == null || nodes.length < 2){
            logger.error("Add function requires at least two arguements.");
            return null;
        }

        Node target = null;     //Node that will get the change. (Might be an OF node).
        Node branchBase = nodes[0];     //The branch base
        Node tail = nodes[nodes.length-1]; //Either a key that needs to be added or a val to a key.
        Node keyTmp = nodes[nodes.length-2]; //The second to last node must be checked as a contender for a Key in a K:V pair.

        for(Node n: nodes){
            //All nodes should exist in DB (implied, might be necessary to check at later time).
            if(n==null){
                logger.error("Null node. Couldn't complete function, returning null.");
                return null;
            }
        }

        //Val shouldn't be an OF node.
        if(tail.toString().contains("^")){
            logger.error("You cannot add an overflow node as a value. Although this may change in future.");
            return null;
        }


//        -- if two nodes ( branchBase, Key )   OR   (branchBase, Val) for a key inside of base
        if(nodes.length == 2){
            // ( base, val ) - check if val can be an ans to any of the current keys.

            // ( base, key ) - ask if you want to add it as a key.

            //actually, it's not that simple.
            //there's a possibility it's something that should be restructured deeper in the node. Like an Iphone as a product of apple.
            //  or there's this crazzzy possibility that i say shelby has 5.0L Coyote
            //  and then that'll go under the Shelby Engine.
            //  BUT WHAT IIF SHELBY ENGINE IS ALREADY FILLED OUT?
            //  NOW , it'll go into the OF node, and just say that it's a 5.0L Coyote, and then it'll also have to match up info from that node to this node.
            //  This implies a different type of inheritence.

            return addKey(nodes[0], nodes[1]);
        }


//        -- check if second to last node is a Key to the Value.        //Although important, the way the churning and eliminating
        //branches would work would already account for this.
        if( SetLogic.xISyP(tail, keyTmp) ){


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

//     If regular search didn't work...check invisible branches. (Parent's OF branches)
//        -- Once adding V to K, check in ^LP if that V should go in an OF node...






        return target;

    }





    public static Node addKey(Node base, Node key){

        //if key has no ^LP, it's honestly probably just something this base has.
        //could be a value.

        return base;

    }




}
