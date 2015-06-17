package r;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO Search for child nodes - not urgent
 * TODO update addresses for each node.
 */
public class TreeNode implements Comparable<TreeNode> {

    private String name;
    private String address;
    private TreeNode parent;
    private List<TreeNode> children;
    private List<TreeNode> elementsIndex;

    /**
     * @param name
     */
    public TreeNode(String name) {
        this.name = name;
        this.children = new LinkedList<TreeNode>();
        this.elementsIndex = new LinkedList<TreeNode>();
        this.elementsIndex.add(this);

    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add(getName());
        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
        for (TreeNode child : getAllChildren(allChildren)) {
            names.add(child.getName());
        }
        return names;
    }

    public String getAddress() {
        return address;
    }

    protected void setAddress(String address) {
        this.address = address + "/";
    }

    protected void updateAddress() {
        address = parent.getAddress() + name + "/";
    }

    /**
     * x
     *
     * @return
     */
    public ArrayList<String> getAllAddresses() {
        ArrayList<String> addresses = new ArrayList<String>();
        addresses.add(getName());
        ArrayList<TreeNode> allChildren = new ArrayList<TreeNode>();
        for (TreeNode child : getAllChildren(allChildren)) {
            addresses.add(child.getAddress());
        }
        return addresses;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    protected void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public ArrayList<String> getChildrenString() {
        ArrayList<String> a = new ArrayList<String>();
        for (TreeNode child : children) {
            a.add(child.getName());
        }
        return a;

    }

    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    protected void setName(String name) {

        this.name = name;

    }

    protected void removeChild(TreeNode childToRemove) {
        if (childToRemove == null)
            return;
        children.remove(childToRemove);
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

        parent.addChild(n);    //make n a child of current's parent.
        parent.removeChild(this); //n's parent removes the old child.
        n.addChild(this);    //n gets current as a child
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
    protected boolean addChild(String child) {
        TreeNode childNode = new TreeNode(child);
        return addChild(childNode);
    }

    /**
     * @param childNode
     * @return
     */
    protected boolean addChild(TreeNode childNode) {
        childNode.parent = this;
        childNode.updateAddress();
        childNode.elementsIndex = elementsIndex;
        if (!children.contains(childNode)) {
            this.children.add(childNode);
            this.registerChildForSearch(childNode);
        }
        return true;
    }

    /**
     * TODO: See if this is enough for our searching needs, if not it may be useful for narrowing results. Wtf is this doing.
     *
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
     * Makes the current node the specifiedChild.
     *
     * @param specifiedChild - node to be changed to.
     * @return boolean
     */
    public TreeNode getChild(String specifiedChild) {
        TreeNode tmp = getContains(specifiedChild);

        for (TreeNode child : children) {
            if (child.equals(tmp)) {
                return child;
            }

        }
        return null;
    }

    /**
     * TODO: rewrite using hash
     * Checks if current node contains specified node inside children.
     *
     * @param node
     * @return TreeNode<String>
     */
    private TreeNode getContains(String node) {
        for (TreeNode child : children) {
            if (shallowEquals(child, node)) {
                return child;
            }
        }
        return null;
    }

    /**
     * equals compares name only, and ignores all other attributes.
     *
     * @param nodeA
     * @param nodeB
     * @return
     */
    public boolean shallowEquals(TreeNode nodeA, String nodeB) {
        Boolean result = nodeA.name.equals(nodeB);
//		System.out.println("shallowEquals Result: " + result);
        return result;
    }

    /**
     * TODO: rewrite using hash
     * QA on containsAll
     *
     * @param node
     * @return
     */
    public boolean containsAll(String term) {
        for (TreeNode child : children) {
            if (shallowEquals(child, term))
                return true;
            boolean temp = this.getChild(child.getName()).containsAll(term);
            if(temp)
                return temp;
        }
        return false;
    }

    /**
     * TODO: remove from hash
     *
     * @param node
     */
    protected void delChild(String node) {
        children.remove(contains(node));
    }

    /**
     * TODO: rewrite using hash
     * Checks if current node contains specified node inside children.
     *
     * @param node
     * @return TreeNode<String>
     */
    public boolean contains(String node) {
        for (TreeNode child : children) {
            if (shallowEquals(child, node)) {
                return true;
            }
        }
        return false;
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
            System.out.println("Base node function is called on root.");
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

    protected void setParent(TreeNode parent) {
        this.parent = parent;
    }


}
