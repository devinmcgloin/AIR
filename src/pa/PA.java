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

    //---------------------------------R WRAPPERS---------------------------------//
    public void rename(String nodeName, String rAddress){
        R.rename(nodeName, rAddress);
    }

    public void del(String nodeName, String rAddress){
        R.del(nodeName, rAddress);

    }
    public void add(String nodeName, String rAddress){
        R.add(nodeName, rAddress);

    }
    public void addParent(String nodeName, String rAddress){
        R.addParent(nodeName, rAddress);

    }

    /**
     * TODO: has to count if base nodes returned match the number of terms being asked for.
     * if not, PA needs to flag it's about to return the highest number of matched terms it could.
     * TODO: Dont know where its coming from but trying to move into a child node that doesnt exist causes "Trying hash del for first time! Only on failed search." to be printed to the console. followed by a nullPointer.
     * @param terms
     * @return
     */
    public ArrayList<TreeNode> hashSearch(String terms){
        ArrayList<TreeNodeBase> baseNodes = R.hashSearch(terms);
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

    // ----------------------------- END R WRAPPERS ----------------------------------//

    /**
     * TODO: Implement recursive get children function in TreeNode.
     * TODO: Implement CD function inside TreeNode.
     * TODO: Implement combined check if node exists, and if so CD into that node.
     * @param from - node that to is inheriting from.
     * @param to - node that only has characteristics are copied to
     */
    public void inherit(String rAddressA, String rAddressB, String qualifiers){
        R.copyContents(rAddressA, rAddressB, qualifiers);
    }
}
