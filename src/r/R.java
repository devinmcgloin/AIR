package r;

/**
 * Created by Blazej on 6/1/2015.
 */

import org.apache.log4j.Logger;

import java.util.ArrayList;


/*
 * Interprets commands, navigates logic.
 *
 */
public class R {

    static Logger logger = Logger.getLogger(R.class);
    String name;
    GeneralTree genTree;
    ArrayList<TreeNodeBase> hits;
    TreeNode current;


    //Start R Interface
    public R(String name) {
        this.name = name;
        genTree = new GeneralTree();
        hits = new ArrayList<TreeNodeBase>();
        current = genTree.getCurrent();
        current = get("R/" + name);
    }

    public String getName() {
        return name;
    }

    public void rename(String nodeName, String rAddress) {
        current = get(rAddress);
        genTree.rename(nodeName);
        genTree.current.updateAddress();
        //R.rename(nodeName, rAdress);
    }

    public void del(String nodeName, String rAddress) {

        current = get(rAddress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.delNode(nodeName);

    }

    public void add(String nodeName, String rAddress) {
        //System.out.println( "oh shit  " + rAddress);


        current = get(rAddress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.addNode(nodeName);

    }

    //INSERT parent for node
    public void addParent(String nodeName, String rAddress) {
        // R.addParent(nodeName, rAddress);
        current = get(rAddress);
        genTree.addParent(nodeName);

    }

    public TreeNode get(String rAddress) {
        return genTree.getNodeByAddress(rAddress);

    }

    public ArrayList<String> getChildren(String rAddress) {
        return genTree.getNodeByAddress(rAddress).getChildrenString();
    }

    public ArrayList<TreeNodeBase> rFullHashSearch(String terms) {
        return genTree.fullHashSearch(terms);
    }

    public void save() {
        genTree.exportDB();
    }

    public TreeNode getCurrent() {
        return genTree.getCurrent();
    }

}
