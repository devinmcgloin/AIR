package logic;

import memory.Whiteboard;
import org.apache.log4j.Logger;
import pa.Node;

import java.util.ArrayList;
import java.util.Collections;

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

        //Construct the Tree
        constructTree();
    }

    private void constructTree(){

        //First, take the root.
        //Its children are either Keys that are string representable and should have a LData interpretable Val
        //  or they are LPs (actual nodes).
        ArrayList<String> keyStrings = Node.getKeys( root.node );
        ArrayList<Node> keyNodes = new ArrayList<Node>();

        //Nodes in the tree will be sorted alphabetically.
        Collections.sort(keyStrings);
        for(String k : keyStrings ){
            Whiteboard.searchByTitle(k);
        }


    }

    /**
     * Eliminates Branches that have absolutely nothing to do with the nodes passed in.
     * @param nodes
     */
    public void eliminateBranches(Node[] nodes){
        //Change all nodes to ghostNode

        //Run through the tree through the keepInTree(node from list, some node in tree)
        //It's basically an extension of the xISyP + it checks if two nodes have same title.


        //Will have to run through this backwards... It's very strange...I'm not really sure how to organize this tree.
        //We have to start from the leaves? Or should we start from the root? Cause we can trim the tree at leaves or just
        //try cutting off entire branches. (Which might be faster). THink about how you want to organize and construct this tree whilst thinking about what you'll do with it.

        //Don't forget to remove every one of those nodes that you eliminate from that branch from the whiteboard.


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
            if(lc.compareTo(lp) == 0) //just in case it's literally the same node.
                return true;
            return SetLogic.xISyP(lc.node, lp.node);
        }

        @Override   //Compares titles of the two nodes.
        public int compareTo(GhostNode o) {
            String me = this.node.toString();
            String you = o.node.toString();
            return me.compareTo(you);
        }
    }

}
