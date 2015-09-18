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
        ArrayList<GhostNode> gnodesInThisBranch = new ArrayList<GhostNode>();
        gnodesInThisBranch.add(this.root);
        constructTree(this.root, null);
    }

    private void constructTree(GhostNode base, ArrayList<GhostNode> gnodesInThisBranch){

        //Nodes in the tree will be sorted alphabetically.
        ArrayList<String> keyStrings = Node.getKeys(base.getOriginNode());
        Collections.sort(keyStrings);
        if(gnodesInThisBranch ==null){
            gnodesInThisBranch = new ArrayList<GhostNode>();
        /*We need to keep track of the nodes that we let OF so that we don't cause an infinite Ghost Tree.
        I know. It's ridiculous. I spent half a day trying to figure out the logistics of how it would be caused.
        E.g. Friend is Person, Person has Friend. Friend OFs to Person^Friend and has Friend as a Key which OFs...
        Two things could cause it:
        ONE: Lets say A is B, B is C, C is D and D has A.
        That means D passes on all its Keys (D->C->B->A) s.t. A has all of D's Keys. This will result in A has A,
        which causes an infinite loop. So just make sure A(key) isn't A(obj). (Friend (key) still qualifies as Person (Obj))
        -*So if a Key ever registers as a ^lc of the Base, do NOT continue the ghost tree.*

        TWO: Lets say z has y, y has x, x has w, and w has z.
        Now, when w is created and its z key is OFed, that z node, w^z, will have a y key that'll OF and have a x key, which
        will have a w key, which will have a z key...
        This is why we need to keep track of past keys. But we need to keep in line with an individual branch, not the entirety of the tree.
        The W node is safe to create, the z node as a key is just not safe to OF
        -*So, mark all nodes that are in branch (starting with root. If any gkey is of the past nodes in branch, cannot OF that gkey.
        (by nodes i mean keys)
        (marking the OF would be redundant and stupid (since it's a smaller piece of the Key) )
         */


        for(String k : keyStrings ){
            //Check if key is a carrot header,
            //Alright, either it's a K:V for Height:Node^Height
            //So if it's a String Representable Key, you gotta play it safe.
            //Or it's going to be LP:LC
            //Actually either way it's going to be LP:LC since K:V in string rep keys get changed to nodes upon pulling out of db.
            //Either way, the Keys are Nodes you can get in the DB.
            Node t = Whiteboard.searchByTitle(k);

            if(t==null)
                continue;

            GhostNode gkey = new GhostNode(t);
            base.addKid(gkey);




            //You need to search the Origin Node's Keys for Values. Since you can't use the Height Key Node to get Devin's Height.
            //MAKE SURE TO CATCH THE NULL
            String val = base.getVal(gkey);

            if(val == null){
                //We are done with this section of the ghost tree!!!
                //No wait, no we're not, we have to ghost it.
                //------------------------------- GHOST PART OF THE GHOST TREE ----------------------------------------------//

                //--------DIFFERENT STOPS

                //If the Key we stopped on is String Representable, we need to STOP. Do not get CI. That is a cognitive postulating brain thing.
                if( StringRepresentation.isKeyStringRepresentable(gkey.getOriginNode()) || LDATA.isLdata(gkey.toString())){
                    continue;
                }


                //OF High Level Stop #1
                //If K is either a ^lc of the Origin or if K literally is the Origin.
                if( xISy(gkey, base))
                    continue;


                //OF High Level Stop #2
                boolean cont = false;

                for (GhostNode pastg : gnodesInThisBranch){
                    if(xISy(gkey, pastg))
                        cont = true;
                }
                if(cont)
                    continue;





                //ANOTHER STOP
                //We need to stop the branch if the value is a qualitative Val that should just have a frequency Distribution or a
                //  Approximation by looking at other things close to it.

                /**\
                 * Handling Qualitative Values that shouldn't OF.
                 * Key has no has attributes, just lots of ^LC
                 *      -What if they're all OFs though? Then again, that would never happen. OFs imply building off the key.
                 * ^LC of Key have no OFs...but that probably won't hold up. Would need a significant amount, or weighing them...
                 * Go to LP, see how its LC have populated that same Key. If they rarely OF, then you should probably just stop. Other wise, you can take a freq dist. But that's more in the "questioning" thing.
                 */


                //---------END STOPS

                //Otherwise, we need to create a GhostOF node as a LC of the Key.
                //Create a new node...? Should this be via white board? Creating a OF node is a pretty unique case.
                //Well, NODE seems to have a method for creating a new node so...
                String ofTitle = base.toString() + "^" + gkey.toString();
                Node of = new Node(ofTitle);



                //FUCK FUCK FUCK now we need an inheritance method that doesn't automatically trigger ^lc ^lp shit frantically.

                // Then, send that new GhostOF/LC to be branched out as a continuation of the ghost tree.
                GhostNode gOF = new GhostNode(of);

                //MARK ALL THE FUCKING KEYS THAT ARE IN THIS BRANCH.
                gnodesInThisBranch.add(gkey);

                gkey.addKid(gOF);   //add as a val to the current key for sure.
                constructTree(gOF, gnodesInThisBranch);

                continue;
            } // -------------------------------------- END GHOST PART OF GHOST TREE -------------------

            //-----------------------false-----------------------------
            //Now either this "Val" is going to be string representable
            //This won't happen since loading in the Node will change it to a Node
//            if(StringRepresentation.isStringRepresentation(val)){
//
//                //Turn the string rep into an actual node
//
//                //Add it as that key's child, we've reached the leaf of this branch.
//
//
//                continue;
//
//            }
            //-----------------------shouldn't happen ------------------

            //Or it's going to be a LC node of the LP   (which we check below in the fuck just in case)
            Node t2 = Whiteboard.searchByTitle(val);
            GhostNode lc = new GhostNode(t2);

            //Just to make sure...
            if( !xISy(lc, gkey) )
                logger.warn("Huh that's weird..." + val + " isn't a logical child of the key: "+ gkey.toString());

            //Take this lc node and add it as the only child of the current gkey.
            gkey.addKid(lc);

            // Then, send that LC to be branched out as a continuation of the ghost tree.
            constructTree(lc);

            //IT'S LIKE THIS TREE WORKS IN THREE PART INTERVALS.
            //YOU SEND IN THE ROOTS AND IT CREATES THE TREE'S KEYS
            // THEN IT LOOKS BACK INTO THE ROOT'S KEY'S VALUES
            // THEN IT EITHER CREATES GHOST VALUES, OR SAVES VALUES AND REALIZES IT'S THE END OF THE BRANCH
            // OR IT REALIZES THERE'S A LC THERE AS A VALUE / GHOSTOF NODE AND SENDS THAT TO CREATE THE NEXT INTERVAL OF THE TREE.




        }   //END LOOP THROUGH KEYS


    }

    /**
     * Eliminates Branches that have absolutely nothing to do with the nodes passed in.
     * @param nodes
     */
    public void eliminateBranches(Node[] nodes){
        //Change all nodes to ghostNode

        //Run through the tree through the xISy(node from list, some node in tree)
        //It's basically an extension of the xISyP + it checks if two nodes have same title.


        //Will have to run through this backwards... It's very strange...I'm not really sure how to organize this tree.
        //We have to start from the leaves? Or should we start from the root? Cause we can trim the tree at leaves or just
        //try cutting off entire branches. (Which might be faster). THink about how you want to organize and construct this tree whilst thinking about what you'll do with it.

        //Don't forget to remove every one of those nodes that you eliminate from that branch from the whiteboard.


    }

    public boolean xISy(GhostNode lc, GhostNode lp){
        if(lc.compareTo(lp) == 0) //just in case it's literally the same node.
            return true;
        return SetLogic.xISyP(lc.node, lp.node);
    }




    private class GhostNode implements Comparable<GhostNode> {

        private Node node;
        private ArrayList<GhostNode> kids;
        private GhostNode parent;


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
            kid.setParent(this);
            kids.add(kid);

        }

        public void setParent(GhostNode parent){
            this.parent = parent;
        }

        protected Node getOriginNode(){
            return this.node;
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
