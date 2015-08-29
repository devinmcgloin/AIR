package logic;

import org.apache.log4j.Logger;
import pa.Node;

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


//        -- if two nodes ( BaseOF, Key )
        if(nodes.length == 2){
            return addKey(nodes[0], nodes[1]);
        }


//        -- check if second to last node is a Key to the Value.




        //The second to last node must be checked as a contender for a Key in a K:V pair.
        Node keyTmp = nodes[nodes.length-2];




        return target;

    }

    public static Node addKey(Node base, Node key){

        return base;

    }


}
