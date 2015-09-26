package logic;

import funct.StrRep;
import memory.Notepad;
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
    protected ArrayList<GhostNode> allGNodes;
    protected ArrayList<GhostNode> contenders = new ArrayList<GhostNode>();
    private GhostNode root;


    public GhostTree(Node root) {
        if (root == null) {
            logger.warn("You cannot start the GhostTree with a null node.");
            return;
        }
        if (Node.getKeys(root) == null || Node.getKeys(root).size() == 0) {
            logger.warn("You cannot start the GhostTree with a dud node.");
            return;
        }
        this.root = new GhostNode(root);

        //Construct the Tree
        allGNodes = new ArrayList<GhostNode>();
        allGNodes.add(this.root);
        ArrayList<GhostNode> gnodesInThisBranch = new ArrayList<GhostNode>();
        gnodesInThisBranch.add(this.root);
        constructTree(this.root, null); // todo should go nodes be passed in?
    }

    private void constructTree(GhostNode base, ArrayList<GhostNode> gnodesInThisBranch) {


        //Nodes in the tree will be sorted alphabetically.
        ArrayList<String> keyStrings = Node.getKeys(base.getOriginNode());
        Collections.sort(keyStrings);
        if (gnodesInThisBranch == null) {
            gnodesInThisBranch = new ArrayList<GhostNode>();
        }
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


        //FUCK FUCK FUCK
        //If it's creating a OF node (bmw^door) it checks ^lp (car) to see if (car^door) is something that exists. Uses that for structure.

        for (String k : keyStrings) {
            //Check if key is a carrot header
            if (k.startsWith("^"))
                continue;
            //Alright, either it's a K:V for Height:Node^Height
            //So if it's a String Representable Key, you gotta play it safe.
            //Or it's going to be LP:LC
            //Actually either way it's going to be LP:LC since K:V in string rep keys get changed to nodes upon pulling out of db. Check with Devin on this.
            //Either way, the Keys are Nodes you can get in the DB.
            Node t = Notepad.searchByTitle(k);

            if (t == null) {
                logger.warn("Couldn't find a node with the title: " + k);
                continue;
            }

            GhostNode gkey = new GhostNode(t);
            base.addKid(gkey);
            allGNodes.add(gkey);


            //You need to search the Origin Node's Keys for Values. (Since you can't use the Height Key Node to get Devin's Height.)
            //MAKE SURE TO CATCH THE NULL
            String val = base.getVal(gkey);

            if (val == null) {
                //We have to ghost it.
                //------------------------------- GHOST PART OF THE GHOST TREE ----------------------------------------------//

                //--------DIFFERENT STOPS

                //System.out.println("hmmmmmm" + ( gkey.getOriginNode() == null) );

                //If the Key we stopped on is String Representable, we need to STOP. Do not get CI. That is a cognitive postulating brain thing.
                if (StrRep.isKeyStringRepresentable(gkey.getOriginNode()) || LDATA.isLdata(gkey.toString())) {
                    continue;
                }

                //OF High Level Stop #1
                //If K is either a ^lc of the Origin or if K literally is the Origin.
                if (xISy(gkey, base))
                    continue;


                //OF High Level Stop #2
                boolean cont = false;
                for (GhostNode pastg : gnodesInThisBranch) {
                    if (xISy(gkey, pastg))
                        cont = true;
                }
                if (cont)
                    continue;


                //ANOTHER STOP (OPTIONAL)
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
                String ofTitle = base.toString() + "^" + gkey.toString();
                Node of = new Node(ofTitle);

                if (of == null) {
                    logger.warn("How could this possibly be null? I'm creating a new node!");
                }


                //FUCK FUCK FUCK now we need an inheritance method that doesn't automatically trigger ^lc ^lp shit frantically.
                //Or a temp whiteboard. Or a way to delete from whiteboard. Or literally anything.
                //Discussed with Devin. You will continue to use the xLIKEy method. Then, when user confirms OF existence, trigger the xISy.
                of = SetLogic.xLikey(of, gkey.getOriginNode());

                //Then, send that new GhostOF/LC to be branched out as a continuation of the ghost tree.
                GhostNode gOF = new GhostNode(of);

                //Mark the key that's in this branch.
                gnodesInThisBranch.add(gkey);
                allGNodes.add(gOF);

                gkey.addKid(gOF);   //add as a val to the current key for sure.
                constructTree(gOF, gnodesInThisBranch);

                continue;
            } // -------------------------------------- END GHOST PART OF GHOST TREE -------------------

            //FUCK FUCK FUCK FUCK
            //IF VAL NOT NULL, BUT STRING REP (in string form) send to string rep to create into a tmp node, store as val, contiue

            //Or it's going to be a LC node of the LP   (which we check below in the fuck just in case)
            Node t2 = Notepad.searchByTitle(val);

            if (t2 == null) {
                logger.warn("Couldn't find a node with the title: " + val);
                continue;
            }

            GhostNode lc = new GhostNode(t2);

            //Just to make sure...
            if (!xISy(lc, gkey))
                logger.warn("Huh that's weird..." + val + " isn't a logical child of the key: " + gkey.toString());

            //Take this lc node and add it as the only child of the current gkey.
            gkey.addKid(lc);
            //MARK ALL THE FUCKING KEYS THAT ARE IN THIS BRANCH.
            gnodesInThisBranch.add(gkey);

            // Then, send that LC to be branched out as a continuation of the ghost tree.
            constructTree(lc, gnodesInThisBranch);

            //IT'S LIKE THIS TREE WORKS IN THREE PART INTERVALS.
            //YOU SEND IN THE ROOTS AND IT CREATES THE TREE'S KEYS
            // THEN IT LOOKS BACK INTO THE ROOT'S KEY'S VALUES
            // THEN IT EITHER CREATES GHOST VALUES, OR SAVES VALUES AND REALIZES IT'S THE END OF THE BRANCH
            // OR IT REALIZES THERE'S A LC THERE AS A VALUE / GHOSTOF NODE AND SENDS THAT TO CREATE THE NEXT INTERVAL OF THE TREE.


        }   //END LOOP THROUGH KEYS


    }


    /**
     * If this node can go as a LC child somewhere in the GhostTree, we will keep that branch.
     * For the time being, the use of the filter branches method should be from leaf to root.
     * First give the values (what you truly want to add). Then add additional contextual nodes.
     * <p>
     * If you are using filterBranches, make sure to clearContenders() first.
     *
     * @param node - this node (or a lp) must be located somewhere in a contending branch.
     */
    public void filterBranches(Node node) {
        //FUCK FUCK FUCK
        //Don't forget to remove every one of those nodes that you eliminate from that branch from the whiteboard.

        GhostNode gnode = new GhostNode(node);

        //The first time you use filterBranches, it will find branches within the whole tree that contain your node.
        //Then it sends that node in the tree to "contenders"
        if (contenders.size() == 0) {
            for (GhostNode g : allGNodes) {

                if (xISy(gnode, g)) {
                    logger.debug("We got:  " + g.toString());
                    contenders.add(g);
                }
            }
        } else {//If there already are contenders, simply filter on whether or not one of the contenders has the node in its branch.
            ArrayList<GhostNode> keepPlease = new ArrayList<>();
            for (GhostNode c : contenders) {
                if (c.containsInBranch(gnode)) {
                    keepPlease.add(c);
                } else {
                    //contenders.remove(c);     NOT SURE IF WORKS WHILE ITERATING, DON'T RISK IT.
                }

            }
            //Now clear the past contenders, add in the ones that had hits in the branch (keepPlease)
            contenders = keepPlease;
        }


    }


    public void clearContenders() {
        //FUCK FUCK FUCK
        //Don't forget to remove every one of those nodes that you eliminate from that branch from the whiteboard.
        //Or should we do this when a new tree is created? Need to talk to Dev about staging & tmps.
        contenders.clear();
    }

    public ArrayList<GhostNode> getContenders() {
        return contenders;
    }


    public boolean xISy(GhostNode lc, GhostNode lp) {
        if (lc.compareTo(lp) == 0) //just in case it's literally the same node.
            return true;
        return SetLogic.xISyP(lc.node, lp.node);
    }

    public String toString() {
        String treeString = export(root).toString();

        return treeString;
    }


    protected StringBuilder export(GhostNode node) {
        StringBuilder DBout = new StringBuilder();
        DBout.append(node.toString() + "\n");

        logger.debug(node.getLevel());

        if (node.getLevel() == 0) {
            String buffer = "    ";
            GhostNode tmp;
            if (node.getKids() == null) {
                System.out.println(node.toString());
                return DBout;
            }
            for (GhostNode child : node.getKids()) {
                DBout.append(buffer + child.toString() + "\n");
                DBout.append(exportRec(child, buffer));
            }
            return DBout;
        }
        return DBout;
    }

    /**
     * @param node
     * @param buffer
     * @return
     */
    private StringBuilder exportRec(GhostNode node, String buffer) {
        StringBuilder DBout = new StringBuilder();
        buffer += "    ";

        for (GhostNode child : node.getKids()) {
            DBout.append(buffer + child.toString() + "\n");
            DBout.append(exportRec(child, buffer));
        }
        return DBout;
    }


    public class GhostNode implements Comparable<GhostNode> {

        private Node node;
        private ArrayList<GhostNode> kids = new ArrayList<GhostNode>();
        private GhostNode parent;


        public GhostNode(Node node) {
            if (node == null || Node.getKeys(node) == null || Node.getKeys(node).size() == 0) {
                logger.warn("You cannot create a GhostNode with a dud node: " + node.toString());
                return;
            }

            this.node = node;

        }

        public GhostNode getParent() {
            return parent;
        }

        public void setParent(GhostNode parent) {
            this.parent = parent;
        }

        public String getVal(GhostNode key) {

            Node n = this.getOriginNode();
            String k = key.toString();

            return Node.get(n, k);

        }

        public void addKid(GhostNode kid) {
            kid.setParent(this);
            kids.add(kid);

        }

        /**
         * Checks to see if current node has node as ancestor in the branch.
         *
         * @param ancestor
         * @return
         */
        protected boolean containsInBranch(GhostNode ancestor) {
            GhostNode tmp = parent;
            while (tmp != root) {          //checks to see if c has gnode in branch (going backwards towards root)
                if (xISy(tmp, ancestor)) {
                    return true;
                }
                tmp = tmp.getParent();
            }
            return false;
        }

        public int getLevel() {
            if (this.equals(root))
                return 0;
            else
                return parent.getLevel() + 1;
        }

        protected Node getOriginNode() {
            return this.node;
        }

        protected ArrayList<GhostNode> getKids() {
            return kids;
        }


        @Override
        public String toString() {
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
