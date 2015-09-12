package logic;

import funct.StringRepresentation;
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
        constructTree(this.root);
    }

    private void constructTree(GhostNode node){


        ArrayList<String> keyStrings = Node.getKeys( node.getOriginNode() );
        ArrayList<GhostNode> keyNodes = new ArrayList<GhostNode>();

        //Nodes in the tree will be sorted alphabetically.
        Collections.sort(keyStrings);
        for(String k : keyStrings ){
            //Alright, either it's a K:V for Height:179 cm
            //So if it's a String Representable Key, you gotta play it safe.
            //Either way it's a node you can get.
            Node t = Whiteboard.searchByTitle(k);
            GhostNode gkey = new GhostNode(t);
            node.addKid(gkey);


            //You need to search the Origin Node's Keys for Values. Since you can't use the Height Key Node to get Devin's Height.

            //MAKE SURE TO CATCH THE NULL
            String val = node.getVal(gkey);

            if(val == null){
                //We are done with this section of the ghost tree!!!
                //No wait, no we're not, we have to ghost it.

                //If the Key we stopped on is String Representable, we need to get its confidence interval.
                if( StringRepresentation.isKeyStringRepresentable(gkey.getOriginNode())){

                    //get confidence interval of ghost value

                    //add that ghost ci val to the gkey as its only child
                    //skip this branch.

                }

                //Otherwise, we need to create a GhostOF node as a LC of the Key and send that to be created as the rest of the tree.

                continue;
            }

            //Now either this "Val" is going to be string representable
            if(StringRepresentation.isStringRepresentation(val)){

                //Turn the string rep into an actual node

                //Add it as that key's child, skip this branch.

            }

            //Or it's going to be  a LC node of the LP

            //Get that kiddo, add it as the only child to this gkey, send that kiddo to continue having its tree be made.


            //IT'S LIKE THIS TREE WORKS IN THREE PART INTERVALS.
            //YOU SEND IN THE ROOTS AND IT CREATES THE TREE'S KEYS
            // THEN IT LOOKS BACK INTO THE ROOT'S KEY'S VALUES
            // THEN IT EITHER CREATES GHOST VALUES, OR SAVES VALUES AND REALIZES IT'S THE END OF THE BRANCH
            // OR IT REALIZES THERE'S A LC THERE AS A VALUE / GHOSTOF NODE AND SENDS THAT TO CREATE THE NEXT INTERVAL OF THE TREE.




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



    private class GhostNode implements Comparable<GhostNode> {

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

        public String getVal( GhostNode key){

            Node n = this.getOriginNode();
            String k = key.toString();

            return Node.get(n, k);

        }

        public void addKid(GhostNode kid){
            kids.add(kid);

        }

        protected Node getOriginNode(){
            return this.node;
        }

        public boolean keepInTree(GhostNode lc, GhostNode lp){
            if(lc.compareTo(lp) == 0) //just in case it's literally the same node.
                return true;
            return SetLogic.xISyP(lc.node, lp.node);
        }

        @Override
        public String toString(){
            return this.node.toString();
        }

        @Override   //Compares titles of the two nodes.
        public int compareTo(GhostNode o) {
            String me = this.node.toString();
            String you = o.node.toString();
            return me.compareTo(you);
        }
    }

}
