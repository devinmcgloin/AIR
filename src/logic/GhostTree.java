package logic;

import org.apache.log4j.Logger;
import pa.Node;

import java.util.ArrayList;

/**
 * Created by Blazej on 8/29/2015.
 * Shit's ins@n3.
 * It's a Node with keys in it.
 */
public class GhostTree {

    static Logger logger = Logger.getLogger(LDATA.class);

    private GhostNode root;

    public GhostTree(Node root){
        if(root == null) {
            logger.warn("You cannot start the GhostTree with a null node.");
            return;
        }
        if(Node.getKeys(root) == null || Node.getKeys(root).size() == 0){
            logger.warn("You cannot start the GhostTree with a dud node.");
            return;
        }
        this.root = new GhostNode(root);
    }

    private void constructTree(){


        ArrayList<String> keyStrings = Node.getKeys( root.node );



    }

    /**
     * Eliminates Branches that have absolutely nothing to do with the nodes passed in.
     * @param nodes
     */
    public void eliminateBranches(Node[] nodes){
        //Change all nodes to ghostNode

        //Run through the tree through the keepInTree(node from list, some node in tree)
        //It's basically an extension of the xISyP + it checks if two nodes have same title.
    }



    private class GhostNode implements Comparable<GhostNode>{

        private Node node;
        private ArrayList<GhostNode> kids;


        public GhostNode(Node node){
            if(node == null || Node.getKeys(node) == null || Node.getKeys(node).size() == 0 ){
                logger.warn("You cannot create a GhostNode with a dud node.");
                return;
            }

            this.node = node;
            ArrayList kids = new ArrayList<GhostNode>();
        }

        public void addKid(){

        }

        public boolean keepInTree(GhostNode lc, GhostNode lp){
            if(lc.compareTo(lp) == 0)
                return true;
            return SetLogic.xISyP(lc.node, lp.node);
        }

        @Override
        public int compareTo(GhostNode o) {
            //Compares titles of the two nodes.
            String me = this.node.toString();
            String you = o.node.toString();

            return me.compareTo(you);

        }
    }

}
