package r;

import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * TODO update addresses for each node.
 *
 */
public class TreeNode implements Comparable<TreeNode> {

    private String title;
    private String address;
    private TreeNode parent;
    private ArrayList<TreeNode> children;


    /**
     * @param title
     */
    public TreeNode(String title) {
        this.title = title;
        this.children = new ArrayList<TreeNode>();

    }

    /**
     * Use in case of priming ArrayList for optimization.
     * @param size
     */
    protected void setChildrenSize(int size){
        this.children = new ArrayList<TreeNode>(size);

    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add(getTitle());
        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
        for (TreeNode child : getAllChildren(allChildren)) {
            names.add(child.getTitle());
        }
        return names;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address + "/";
    }

    protected void updateAddress() {
        address = parent.getAddress() + title + "/";
    }

//    /**
//     * x
//     *
//     * @return
//     */
//    public ArrayList<String> getAllAddresses() {
//        ArrayList<String> addresses = new ArrayList<String>();
//        addresses.add(getTitle());
//        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
//        for (TreeNode child : getAllChildren(allChildren)) {
//            addresses.add(child.getAddress());
//        }
//        return addresses;
//    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

//    protected void setChildren(List<TreeNode> children) {
//        this.children = children;
//    }

    public ArrayList<String> getChildrenString() {
        ArrayList<String> a = new ArrayList<String>();
        for (TreeNode child : children) {
            a.add(child.getTitle());
        }
        return a;

    }

    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void removeChild(TreeNode childToRemove) {
        if (childToRemove == null)
            return;
        children.remove(childToRemove);
    }

    protected void removeChild(int index){
        if(index>=0){
            children.remove(index);
        }
    }

    public ArrayList<TreeNode> getAllChildren(ArrayList<TreeNode> a) {
        for (TreeNode child : children) {
            if (isLeaf())
                a.add(child);
            else {
                a.add(child);
                child.getAllChildren(a);
            }
        }
        return a;
    }

    protected boolean insertParent(TreeNode n) {
        if (n == null) {    //n is the new parent of current
            return false;
        }


        parent.addChildWithContainsCheck(n);    //make n a child of current's parent.



        parent.removeChild(this); //n's parent removes the old child.
        n.addChildWithContainsCheck(this);    //n gets current as a child
        this.parent = n;
        return true;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }


    public void addChildWithContainsCheck(TreeNode childNode){
        childNode.parent = this;
        childNode.updateAddress();
        if(contains(childNode.getTitle())) {
            System.out.printf("Dimension: %s already exists.\n", childNode.getTitle());
            return;
        }
        this.children.add(childNode);

    }


    /**
     * DOES NOT CHECK IF CHILD ALREADY EXISTS. FUCK IN CASE WE SEE DOUBLING UP AGAIN.
     * We need to make sure it adds the child to the correct place in the BS.
     *
     * @param childNode
     * @return
     */
    public void addChildBlind(TreeNode childNode) {
        childNode.parent = this;
        childNode.updateAddress();



        this.children.add(childNode);

    }

    public void insertChild(TreeNode childNode, int index){
        childNode.parent = this;
        childNode.updateAddress();

        this.children.add(index, childNode);
    }


    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }



    @Override
    public java.lang.String toString() {
        return title != null ? title.toString() : "[title null]";
    }

    /**
     * Makes the current node the specifiedChild.
     *
     * @param specifiedChild - node to be changed to.
     * @return boolean
     */
    public TreeNode getChild(String specifiedChild) {

        int index = binarySearch(specifiedChild);

        if (index < 0)
            return null;

        return children.get(index);
    }




    /**
     * TODO: rewrite using hash
     * QA on containsAll
     *
     * @param term
     * @return
     */
    public boolean containsAll(String term) {

        //TODO REMOVE THIS:
//        if(getTitle().equals(term)){
//            return true;
//        }


        //First, check its immediate children for contains.
        if(contains(term))
            return true;

        //Then loop over its children.
        for(TreeNode child : children){
            boolean tmp = child.containsAll(term);
            if(tmp)
                return tmp;
        }
        return false;
    }



    public boolean contains(String nodeName){
        //NOT SORTING CHILDREN, ASSUMES DATABASE IS SORTED.
        if(binarySearch(nodeName) >= 0)
            return true;
        return false;
    }

    public void sortChildren(){
        Collections.sort(this.children);
    }

    /**
     * You can use binarySearch if you already know the information is sorted.
     * @param nodeName
     * @return
     */
    public int binarySearch(String nodeName) {
        int low = 0;
        int high = children.size() - 1;
        int middle = 0;
        while(high >= low) {
            middle = (low + high) / 2;
            if( children.get(middle).getTitle().equals(nodeName) ) {
                return middle;
            }
            if( children.get(middle).getTitle().compareTo(nodeName) < 0 ) {
                low = middle + 1;
            }
            if( children.get(middle).getTitle().compareTo(nodeName) > 0) {
                high = middle - 1;
            }
        }

        //We couldn't find the node, however, we CAN return the index it
        //  SHOULD be placed into. (low) However, we can't return low since
        //  low might equal 0! So we return (low+1)*-1
        //  Then, we insert at (index*-1)-1 or (index+1)*-1


        return (low+1)*-1;
    }


    public boolean isBaseNode() {
        return getLevel() == 2;
    }

    @Override
    public int compareTo(TreeNode n) {
        return this.address.compareTo(n.address);
    }

    public TreeNode getBaseNode() {
        TreeNode n = this;
        if(n.isRoot()) {
            System.out.println("TreeNode: Base node function is called on root.");
            return null;
        }
        if (n.isBaseNode())
            return n;
        while (!n.isBaseNode()) {
            n = n.getParent();
        }
        return n;

    }

    public TreeNode getParent() {
        if (parent != null)
            return parent;
        return null;
    }

//    protected void setParent(TreeNode parent) {
//        this.parent = parent;
//    }



}
