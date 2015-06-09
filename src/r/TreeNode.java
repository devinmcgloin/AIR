package r;

import java.lang.reflect.Array;
import java.util.*;

/**
 * TODO Search for child nodes - not urgent
 * TODO update addresses for each node.
 */
public class TreeNode implements Comparable<TreeNode> {

    public String name;
    public String address;
    public TreeNode parent;
    public List<TreeNode> children;
    private List<TreeNode> elementsIndex;

    /**
     *
     * @param name
     */
    public TreeNode(String name) {
        this.name = name;
        this.children = new LinkedList<TreeNode>();
        this.elementsIndex = new LinkedList<TreeNode>();
        this.elementsIndex.add(this);

    }

    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {

        this.name = name;

    }

    public String[] splitAddress(){
        return address.split("/");
    }

    public ArrayList<String> getAllNames(){
        ArrayList<String> names = new ArrayList<String>();
        names.add(getName());
        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
        for(TreeNode child : getAllChildren(allChildren)){
            names.add(child.getName());
        }
        return names;
    }

    public String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address +"/";
    }
    protected void updateAddress() {
        address = parent.getAddress() + name + "/";
    }

    /**
     * x
     * @return
     */
    public ArrayList<String> getAllAddresses(){
        ArrayList<String> addresses = new ArrayList<String>();
        addresses.add(getName());
        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
        for(TreeNode child : getAllChildren(allChildren)){
            addresses.add(child.getAddress());
        }
        return addresses;
    }

    public TreeNode getParent() {
        if(parent != null)
            return parent;
        return null;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public ArrayList<String> getChildrenString(){
        ArrayList<String> a = new ArrayList<String>();
        for(TreeNode child : children ){
            a.add(child.getName());
        }
        return a;

    }

    public void removeChild(TreeNode childToRemove){
        if(childToRemove == null)
            return;
        children.remove(childToRemove);
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public ArrayList<TreeNode> getAllChildren(ArrayList<TreeNode> a){
        for (TreeNode child : children){
            if(isLeaf())
                a.add(child);
            else{
                a.add(child);
                child.getAllChildren(a);
            }
        }
        return a;
    }

    protected boolean insertParent(TreeNode n){
        if(n==null){	//n is the new parent of current
            return false;
        }

        parent.addChild(n);	//make n a child of current's parent.
        parent.removeChild(this); //n's parent removes the old child.
        n.addChild(this);	//n gets current as a child
        this.parent = n;
        return true;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    //TODO: ....does this work?
    public boolean addChild(String child) {
        TreeNode childNode = new TreeNode(child);
        return addChild(childNode);
    }

    /**
     *
     * @param childNode
     * @return
     */
    public boolean addChild(TreeNode childNode){
        childNode.parent = this;
        childNode.updateAddress();
        childNode.elementsIndex = elementsIndex;
        if(!children.contains(childNode)) {
            this.children.add(childNode);
            this.registerChildForSearch(childNode);
        }
        return true;
    }

    /**
     * TODO: See if this is enough for our searching needs, if not it may be useful for narrowing results.
     * TODO: Wtf is this doing.
     * @param cmp
     * @return
     */
    public TreeNode findTreeNode(Comparable<String> cmp) {
        for (TreeNode element : this.elementsIndex) {
            String elData = element.name;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    /**
     *
     * @param node
     */
    private void registerChildForSearch(TreeNode node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node); //TODO: Why do this?
    }

    @Override
    public java.lang.String toString() {
        return name != null ? name.toString() : "[name null]";
    }



    /**
     *
     * Makes the current node the specifiedChild.
     * @param specifiedChild - node to be changed to.
     * @return boolean
     */
    public TreeNode getChild(String specifiedChild){
        TreeNode tmp = getContains(specifiedChild);

        for(TreeNode child : children){
            if(child.equals(tmp)){
                return child;
            }

        }
        return null;
    }


    /**
     * TODO: rewrite using hash
     * Checks if current node contains specified node inside children.
     * @param node
     * @return TreeNode<String>
     */
    private TreeNode getContains(String node){
        for(TreeNode child : children){
            if(shallowEquals(child, node)) {
                return child;
            }
        }
        return null;
    }

    /**
     * TODO: rewrite using hash
     * Checks if current node contains specified node inside children.
     * @param node
     * @return TreeNode<String>
     */
    public boolean contains(String node){
        for(TreeNode child : children){
            if(shallowEquals(child, node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * TODO: rewrite using hash
     * QA on containsAll
     * @param node
     * @return
     */
    public boolean containsAll(String node){
        for(TreeNode child : children){
            if(shallowEquals(child, node))
                return true;
            else if(isLeaf())
                return false;
            containsAll(child.name);
        }
        return false;
    }

    /**
     * equals compares name only, and ignores all other attributes.
     * @param nodeA
     * @param nodeB
     * @return
     */
    public boolean shallowEquals (TreeNode nodeA, String nodeB){
        Boolean result = nodeA.name.equals(nodeB);
//		System.out.println("shallowEquals Result: " + result);
        return result;
    }

    /**
     * TODO: remove from hash
     * @param node
     */
    public void delChild(String node){
        children.remove(contains(node));
    }

    public boolean isBaseNode(){
        return getLevel() == 2;
    }

    @Override
    public int compareTo(TreeNode n){
        return this.address.compareTo(n.address);
    }

    public TreeNode getBaseNode(){
        TreeNode n = this;
        if( n.getParent().getParent().getName() == null)
            return null;
        while(! ( this.getParent().getParent().getName().equals("R")  )){
            n = n.getParent();
        }
        return n;

    }



}
