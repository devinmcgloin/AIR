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
            if(n==null){
                logger.error("Null node.");
                return null;
            }

            //All nodes should exist in DB (implied, might be necessary to check at later time).
        }

        //Val shouldn't be an OF node.
        if(tail.toString().contains("^")){
            logger.error("You cannot add an overflow node as a value. Although this may change in future.");
            return null;
        }


//        -- if two nodes ( BaseOF, Key )           OR   BaseOF, Val (of a Key inside BaseOF)
        if(nodes.length == 2){
            return addKey(nodes[0], nodes[1]);
        }


//        -- check if second to last node is a Key to the Value.
        if( SetLogic.xISyP(tail, keyTmp) ){
            //That's great news! Now we just gotta find where we can add this K:V pair within the OF and all branches.

            //Keep in mind a Key or OF might be a ^LP of the Key that we're actually trying to add. Fucked up right?
            //ahha. this is fine. i'm not losing it. i'm fine.



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
//        if it's a K in the base node, then chances are you're trying to OF?
//        it actually IS a K:V

//     If regular search didn't work...check invisible branches. (Parent's OF branches)
//        -- Once adding V to K, check in ^LP if that V should go in an OF node...




        //full crazy search.





        return target;

    }


    //private static ArrayList<Node> ghostNodes()

    private static ArrayList<Node> crazySearch(Node[] nodes){

        ArrayList<Node> contenders = new ArrayList<Node>();





        return contenders;

    }

    public static Node addKey(Node base, Node key){

        //if key has no ^LP, it's honestly probably just something this base has.
        //could be a value.

        return base;

    }




}
