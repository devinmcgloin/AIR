package r;

/**
 * Created by Blazej on 6/1/2015.
 */
import java.util.ArrayList;



/*
 * Interprets commands, navigates logic.
 *
 */
public class R {

    GeneralTree genTree;
    ArrayList<TreeNodeBase> hits;
    TreeNode current;

    //Start R Interface
    public R(){
        genTree = new GeneralTree();
        hits = new ArrayList<TreeNodeBase>();
        current = genTree.getCurrent();
    }

    public void rename(String nodeName, String rAddress){
        current = get(rAddress);
        genTree.rename(nodeName);
        genTree.current.updateAddress();
        //R.rename(nodeName, rAdress);
    }
    public void del(String nodeName, String rAddress){

        current = get(rAddress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.delNode(nodeName);

    }

    public void add(String nodeName, String rAddress){
        current = get(rAddress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.addNode(nodeName);

    }

    //INSERT parent for node
    public void addParent(String nodeName, String rAddress){
       // R.addParent(nodeName, rAddress);
        current = get(rAddress);
        genTree.addParent(nodeName);

    }

    public TreeNode get(String rAddress) {
        return genTree.getNodeByAddress(rAddress);

    }

    public ArrayList<String> getChildren(String rAddress){
        return genTree.getNodeByAddress(rAddress).getChildrenString();
    }

    public ArrayList<TreeNodeBase> rFullHashSearch(String terms){
        return genTree.fullHashSearch(terms);
    }




    public void save(){
        genTree.exportDB();
    }

    public TreeNode getCurrent(){
        return genTree.getCurrent();
    }

    public String getAddress(){
        return current.getAddress();
    }




}
