package r;

import java.util.ArrayList;
import java.util.Collections;
//import java.util.List;    //TODO: hope that's okay

/**
 * A TreeNode just holds value as a name as a string.
 */
public class TreeNode implements Comparable<TreeNode> {
    public ArrayList<TreeNode> children = null;
    private String name; // EG "Accountant"
    private String address;        //gets set when declared a child of a node.
    private TreeNode parent = null;
    private TreeNode tmp = null;    //Utility Variable.


    //--------------------------CONSTRUCTORS-------------------------//
    public TreeNode() {      //TODO: is this ever used?
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
    }

    //Create a new tree node with a name.
    public TreeNode(String name) {
        this.name = name;
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
    }
    //------------------------END CONSTRUCTORS-----------------------//

    /* TODO: rename node. think about effects that has on hash search.
    ^^Also--SHIT! What if you rename something that is a value in something else?
     like we add another value to blue, so now blue+color needs to be re-written errywhere. Nah. for now don't care about it. Especially since we'll be grabbing and returning the BaseNode/Header anyway! It's up to PA to think about if we're pulling out something metaphorical or a literal value.
      Yeah. Let's let it slide for now. Hm.
      DB Should really be handling the insert parent and the rename stuff to consider the hashbrowns it's controlling.
      */


    //-----------------Interesting/newest methods -----------//

    /**
     * INSERT PARENT
     */
    public boolean insertParent(TreeNode n) {
        if (n == null) {    //n is the new parent of current
            return false;
        }

        parent.addChildNode(n);    //make n a child of current's parent.
        parent.removeChild(this); //n's parent removes the old child.
        n.addChildNode(this);    //n gets current as a child
        this.parent = n;
        return true;
    }

    /**
     * BASE NODE
     *
     * @param n - TreeNode
     * @return n - TreeNode set to Parent.
     */
    public TreeNode getBaseNode(TreeNode n) {
        if (n.getParent().getParent().getName() == null)
            return null;
        while (!(n.getParent().getParent().getName().equals("R"))) {
            n = n.getParent();
        }
        return n;
    }

    /**
     * CONTAINS
     *
     * @param name
     * @return Boolean
     */
    public boolean containsAnyChildWithName(String name) {
        ArrayList<String> children = this.getAllNames();
        return children.contains(name);
    }

    public boolean containsImmediateChildWithName(String name) {
        ArrayList<String> a = new ArrayList<String>();
        for (int i = 0; i < this.children.size(); i++) {
            a.add(this.children.get(i).getName());    //get all names of children
        }
        return a.contains(name);
    }

    @Override
    public int compareTo(TreeNode n) {
        return this.address.compareTo(n.address);
    }

    //----------------- General Tree stuff ---------------//
    //REMOVE Wrapper
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address + "/";
    }

    public void updateAddress() {
        address = parent.getAddress() + name + "/";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeNode getParent() {
        return parent;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void remove() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    public void removeChild(TreeNode child) {
        if (children.contains(child))
            children.remove(child);

    }

    /**
     * ADD a child, update its address.
     *
     * @param child
     */
    public void addChildNode(TreeNode child) {
        child.parent = this;
        child.updateAddress();
        if (!children.contains(child))
            children.add(child);
    }

    //----------------END General Tree stuff --------------//


    //------------------------- Recursive Methods --------------------------//

    /**
     * LS
     */
    public void printChildren() {
        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);
            System.out.print(tmp.name + "     ");
        }
        System.out.print("\n");
    }

    /**
     * LS RECURSIVE
     */
    public void printAll() {
        System.out.println(this.name);
        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);
            tmp.printAll();
        }
    }

    /**
     * ADDRESSES Wrapper, used for first iteration then handed over to getAllAddressesRec.
     *
     * @return
     */
    public ArrayList<String> getAllAddresses() {
        ArrayList<String> a = new ArrayList<String>();
        a.add(this.address);
        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);
            a = getAllAddressesRec(tmp, a);
        }
        return a;
    }

    /**
     * ADDRESSES Wrapper Recursive
     *
     * @param n
     * @param a
     * @return
     */
    private ArrayList<String> getAllAddressesRec(TreeNode n, ArrayList<String> a) {
        a.add(n.address);
        for (int i = 0; i < n.children.size(); i++) {
            tmp = n.children.get(i);
            a = getAllAddressesRec(tmp, a);
        }
        System.out.print("\n");
        return a;
    }

    /**
     * NAMES Wrapper, used for first iteration then handed over to getAllNamesRec.
     *
     * @return a String ArrayList composed of the titles of the children nodes.
     * @devinmcgloin TODO: Doesn't setting a to getAllNamesRec overwrite for each iteration of the loop?
     */
    public ArrayList<String> getAllNames() {
        ArrayList<String> a = new ArrayList<String>();
        a.add(this.name);
        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);
            a = getAllNamesRec(tmp, a);
        }
        return a;
    }

    /**
     * NAMES Wrapper Recursive
     *
     * @param n
     * @param a
     * @return
     */
    private ArrayList<String> getAllNamesRec(TreeNode n, ArrayList<String> a) {
        a.add(n.name);
        for (int i = 0; i < n.children.size(); i++) {
            tmp = n.children.get(i);
            a = getAllNamesRec(tmp, a);
        }
        return a;
    }

    /**
     * EXPORT Wrapper, used for first iteration then handed over to PrettyPrintRec.
     *
     * @return
     */
    public String PrettyPrint() {
        String DBout = "";
        String buffer = "";
        //TODO: Organize children alphabetically on PrettyPrint. (Check if already sorted, duh).
        Collections.sort(children);
        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);  //get next child
            DBout += buffer + tmp.name + "\r\n";
            DBout += tmp.PrettyPrintRec(buffer);
        }
        return DBout;
    }

    /**
     * EXPORT RECURSION
     *
     * @param buffer
     * @return
     */
    private String PrettyPrintRec(String buffer) {
        String DBout = "";
        buffer += "    ";
        //TODO: Organize children alphabetically on PrettyPrint. (Check if already sorted, duh).
        Collections.sort(children);

        for (int i = 0; i < this.children.size(); i++) {
            tmp = this.children.get(i);  //get next child
            DBout += buffer + tmp.name + "\r\n";
            DBout += tmp.PrettyPrintRec(buffer);
        }
        return DBout;
    }
    //------------------------END Recursive Methods ------------------------//


    //------------------------Devins New Methods ------------------------//

    public void changeDir(String name){
        if(!containsImmediateChildWithName(name)){
            addChildNode(new TreeNode(name));
        }
        setName(name);
        updateAddress();
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "name='" + name + '\'' +
                '}';
    }


}


