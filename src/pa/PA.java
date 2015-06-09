package pa;

import r.GeneralTree;
import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 * PACKAGE GOD.
 */

//External Methods of PA
public class PA {


    /**
     *
     * TODO: Implement recursive get all children function in GeneralTree. - Not sure if needed.
     *
     * TODO: HashSearch (Get set -> set logic???)
     * TODO: user defined matrices --> export set of BN in a csv file?
     * TODO: header logic --> failed add: create header, in all set logic, make sure to reanalyze headers into their true base nodes and re-rank them.
     * TODO: merging nodes --> tricky, idk how that function would look like
     */

    GeneralTree GeneralTree;

    public PA(){
        GeneralTree = new GeneralTree();
    }

    //---------------------------------GeneralTree WRAPPERS---------------------------------//
    public void save(){
        GeneralTree.save();
    }

    public void toDir(String rAddress){
        GeneralTree.toDir(rAddress);
    }

    public void toParent(){
        GeneralTree.toParent();
    }

    public void toChild(String child){
        GeneralTree.toChild(child);
    }

    public void del(String rAddress){
        GeneralTree.del(rAddress);
    }

    public void addChild(String child){
        GeneralTree.addChild(child);
    }

    public void add(String rAddress, String term){
        GeneralTree.add(rAddress, term);
    }

    public boolean contains(String searchTerm){
        return GeneralTree.contains(searchTerm);
    }

    public boolean containsAll(String searchTerm) {
        return GeneralTree.containsAll(searchTerm);
    }

    public TreeNode get(String rAddress){
        return GeneralTree.get(rAddress);
    }

    public void toRoot(){
        GeneralTree.toRoot();
    }

    public ArrayList<String> getChildren(){
        return GeneralTree.getChildren();
    }

    public String getPath(){
        return GeneralTree.getCurrentPath();
    }

    public void rename(String rAddress, String newName){
        GeneralTree.rename(rAddress, newName);
    }

    public boolean isRoot(){
        return GeneralTree.isRoot();
    }

    public boolean isLeaf(){
        return GeneralTree.isLeaf();
    }

    public boolean isKeyVal(){
        return GeneralTree.isKeyVal();
    }

    public boolean ifExistsCD(String node){
        return GeneralTree.ifExistsCD(node);
    }

    // ----------------------------- END GeneralTree WRAPPERS ----------------------------------//






}
