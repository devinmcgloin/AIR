package pa;

import r.R;
import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 */

//External Methods of PA
public class PA {


    /**
     * TODO: HashSearch (Get set -> set logic???)
     *
     * TODO: user defined matrices --> export set of BN in a csv file?
     * TODO: header logic --> failed add: create header, in all set logic, make sure to reanalyze headers into their true base nodes and re-rank them.
     * TODO: merging nodes --> tricky, idk how that function would look like
     */

    R R;

    public PA(){
        R = new R();
    }

    //---------------------------------R WRAPPERS---------------------------------//
    public void save(){
        R.save();
    }

    public void toDir(String rAddress){
        R.toDir(rAddress);
    }

    public void toParent(){
        R.toParent();
    }

    public void toChild(String child){
        R.toChild(child);
    }

    public void del(String rAddress){
        R.del(rAddress);
    }

    public void addChild(String child){
        R.addChild(child);
    }

    public void add(String rAddress, String term){
        R.add(rAddress, term);
    }

    public boolean contains(String searchTerm){
        return R.contains(searchTerm);
    }

    public TreeNode<String> get(String rAddress){
        return R.get(rAddress);
    }

    public void toRoot(){
        R.toRoot();
    }

    public ArrayList<String> getChildren(){
        return R.getChildren();
    }

    public ArrayList<String> getPath(){
        return R.getCurrentPath();
    }

    public void rename(String rAddress, String newName){
        R.rename(rAddress, newName);
    }

    // ----------------------------- END R WRAPPERS ----------------------------------//

}
