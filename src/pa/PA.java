package pa;

import r.TreeNode;
import r.R;
import r.TreeNodeBase;

import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 */

//External Methods of PA
public class PA {


    /**
     * Add
     * Del
     * HashSearch (Get set -> set logic???)
     * addParent
     * rename
     *
     * user defined matrices --> export set of BN in a csv file?
     * header logic --> failed add: create header, in all set logic, make sure to
     *      reanalyze headers into their true base nodes and re-rank them.
     * merging nodes --> tricky, idk how that function would look like
     */

    R R = new R();
    public PA(){

    }

    public void rename(String nodeName, String rAdress){
        R.rename(nodeName, rAdress);
    }
    public void del(String nodeName, String rAdress){
        R.del(nodeName, rAdress);

    }

    public void add(String nodeName, String rAdress){
        R.add(nodeName, rAdress);

    }

    public void addParent(String nodeName, String rAdress){
        R.addParent(nodeName, rAdress);

    }

    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    public ArrayList<TreeNode> hashSearch(String terms){
        ArrayList<TreeNodeBase> baseNodes = R.hashSearch(terms);
        ArrayList<TreeNode> treeNodes = new ArrayList<TreeNode>();

        //Check size


        for(int i =0; i<baseNodes.size(); i++){
            System.out.println(baseNodes.get(i).getOrigin().getName());
        }

        return treeNodes;

    }

    public TreeNode get(String address) {
        return R.get(address);
    }
    public void save(){
        R.save();
    }

}
