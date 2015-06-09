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
     * HashSearch (Get set -> set logic???)
     *
     * user defined matrices --> export set of BN in a csv file?
     * header logic --> failed add: create header, in all set logic, make sure to
     *      reanalyze headers into their true base nodes and re-rank them.
     * merging nodes --> tricky, idk how that function would look like
     */

    R R;
    public PA(){
        R = new R();

    }

    public void devintest(){

    }

    public void blazetest(){

        //System.out.println(R.rFullHashSearch("1`a").get(0).getOrigin().getName());

    }

    //---------------------------------R WRAPPERS---------------------------------//

    public void del(String nodeName, String rAddress ){
        R.del(nodeName, rAddress);
    }

    public ArrayList<String> getChildren( String rAddress ){
        return R.getChildren(rAddress);
    }

    public void rename(String nodeName, String rAddress ){
        R.rename(nodeName, rAddress);
    }

    public void add(String nodeName, String rAddress ){
        R.add(nodeName, rAddress);
    }


    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    public ArrayList<TreeNode> hashSearch(String terms){
        ArrayList<TreeNodeBase> baseNodes = R.rFullHashSearch(terms);
        ArrayList<TreeNode> treeNodes = new ArrayList<TreeNode>();

        //Check size
        int termSize = terms.split("`").length;

        if(baseNodes.size() == 0){
            return treeNodes; // no dice
        }

        //Number of terms matches baseNode hits
        if(baseNodes.get(0).getRank() == termSize){
            int i = 0;
            while(baseNodes.get(i).getRank() == termSize){
                treeNodes.add(baseNodes.get(i).getOrigin());
            }
        }
        //Use the next highest number of matched terms
        else{
            termSize = baseNodes.get(0).getRank();
            int i =0;
            while(baseNodes.get(i).getRank() == termSize){
                treeNodes.add(baseNodes.get(i).getOrigin());
            }
        }
        

        return treeNodes;

    }

    public TreeNode get(String rAddress) {
        return R.get(rAddress);
    }

    public void save(){
        R.save();
    }

    public void addParent(String nodeName, String rAddress){
        R.addParent(nodeName, rAddress);
    }
    // ----------------------------- END R WRAPPERS ----------------------------------//

}
