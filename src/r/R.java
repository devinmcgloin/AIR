package r;

/**
 * Created by Blazej on 6/1/2015.
 */
import java.util.ArrayList;



/**
 * Interprets commands, navigates logic.
 *
 */
public class R {

    GeneralTree genTree;
    ArrayList<TreeNodeBase> hits;
    TreeNode current;
    HashSearch cmdHash = new HashSearch();

    //Start R Interface
    public R(){
        genTree = new GeneralTree();
        hits = new ArrayList<TreeNodeBase>();
        current = genTree.getCurrent();
    }

    public void rename(String nodeName, String rAdress){
        current = get(rAdress);
        genTree.rename(nodeName);
        genTree.current.updateAddress();
        //R.rename(nodeName, rAdress);
    }
    public void del(String nodeName, String rAdress){

        current = get(rAdress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.delNode(nodeName);

    }

    public void add(String nodeName, String rAdress){
        current = get(rAdress);
        //System.out.println("did that work: "+current.getAddress());
        genTree.addNode(nodeName);

    }

    public void addParent(String nodeName, String rAdress){
       // R.addParent(nodeName, rAdress);
        current = get(rAdress);
        genTree.addParent(nodeName);

    }

    public TreeNode get(String address) {
        //TODO: Make sure you have the right directory and GenTree has files right.
        address = address.trim();
        String[] tmpS = address.split("/");

       // System.out.println("address: "+address);


        if(!address.contains("/")){
            System.out.println("R -- Incorrect format: " + address);
            return null;
        }
        //We are sending a command to "R/" directory.
        if(tmpS.length==1 && address.equals("R/")){
            System.out.println("R -- root operation triggered!!!");
            return genTree.root;
        }
        String dbName = address.split("/")[1];

        //Options, we're in R, or we're at the db.
        //Wrong db or no DB or right db.

        //NO DB loaded
        if( current.getAddress().equals("R/")){

            genTree.childTraverse(dbName);
            genTree.loadDB(dbName);
        }


        //TODO: holla at yo boy. Y'all gotta know, you gotta keep your currents your currents.
        //Earlier I had TreeNode tmp = genTree.getNode(address).
        //bruh, that's like have two nodes to the same tree in the same db. ain't worth it. not cool.
        //That's why we was getting a doubling up on the db.
        current =  genTree.getNode(address);




        return current;
    }

    public ArrayList<TreeNodeBase> hashSearch(String terms){
        //TERMs must be separated by `

        //Then try hash searching.
        TreeNode tmp = getCurrent();
        hits = cmdHash.hashSearch(terms, genTree);
        genTree.setCurrent(tmp); //i honestly only need this to guarentee we get back to where we were.
        return hits;
    }




    public void save(){
        genTree.exportDB();
    }

    public TreeNode getCurrent(){
        return genTree.getCurrent();
    }




}
