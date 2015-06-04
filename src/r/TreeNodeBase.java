package r;

/**
 * This is only a wrapper for a TreeNode so we can use a different "compare" algorithim.
 * These are the firstest nodes that are returned by R. They are from the "BASE" dimension (2).
 * We return these nodes in hash search so that PA's can begin to take what they need from it.
 *
 * Orange //BASE
 *     Orange+Fruit
 *     Orange+Color
 * As seen in the example above, PA's WILL be responsible for seeing is a base node is a header.
 *
 * Devin McGloin //BASE
 *     has
 *
 * Update: They no longer extend TreeNode. They just hold the TreeNode that they "represent" and
 * 	return that TreeNode once we're done ranking 'em. It's a wrapper for a tree node.
 *
 * @author Blazej
 *
 */
public class TreeNodeBase implements Comparable<TreeNodeBase>{

    private int rank = 0;
    private TreeNode origin;    //the TreeNode we're wrapping

    TreeNodeBase(TreeNode n){
        origin = n;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getRank(){
        return rank;
    }

    public TreeNode getOrigin(){
        return origin;
    }

    /**
     * They are ranked by how many other search terms it contains (including itself).
     * hashSearch(Strawberry, red, green, sweet)
     * BASE = Strawberry  rank = 4 (contains all four search terms)	 *
     */
    @Override
    public int compareTo(TreeNodeBase n) {
        //Stored from largest to smallest. Seems counter intuitive but it's not.
        if(this.rank<n.rank)
            return 1;
        if(this.rank>n.rank)
            return -1;

        return 0;
    }





}
