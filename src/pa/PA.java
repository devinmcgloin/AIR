package pa;

import r.TreeNode;
import r.R;
import r.TreeNodeBase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 * This is the only class in PA that deals with actual tree nodes, everything as it comes out is wrapped into PABN.
 * TODO: where is header logic info stored? If we use what are current base nodes that qould require a rewriting of lower R functions.
 */

//External Methods of PA
public class PA {


    /**
     * HashSearch (Get set -> set logic???)
     * <p/>
     * user defined matrices --> export set of BN in a csv file?
     * header logic --> failed add: create header, in all set logic, make sure to
     * reanalyze headers into their true base nodes and re-rank them.
     * merging nodes --> tricky, idk how that function would look like
     */

    protected File rFolder = new File("./R/");
    protected ArrayList<R> rDB = new ArrayList<R>();
    protected R currentR = null;
    protected Inheritance inherit;

    public PA(String user) {
        if(!user.equals("Terminal")) {
            if (rFolder.length() >= 1) {
                for (File fileEntry : rFolder.listFiles()) {
                    if (fileEntry.isDirectory()) {
                        continue;
                    } else {
                        rDB.add(new R(fileEntry.getName()));
                    }
                }
            }
        }
        currentR = new R();
        inherit = new Inheritance(this);

    }

    public void devintest() {
        inherit.inherit(get("R/Noun.txt/town"), get("R/Noun.txt/cities"));
    }

    public void blazetest() {
        TreeNode curr = currentR.getCurrent(); //Wrapper for GenTree.getCurrent()


        System.out.println("GenTree's Current: " + curr.getName() + " -  " + curr.getAddress());
        //System.out.println(R.rFullHashSearch("1`a").get(0).getOrigin().getName());
        //curr = R.get("R/0.txt/");

        //System.out.println(curr.prettyPrint());

    }

    //---------------------------------R WRAPPERS---------------------------------//

    public void del(String nodeName, String rAddress) {
        currentR.del(nodeName, rAddress);
    }

    public ArrayList<String> getChildren(String rAddress) {
        return currentR.getChildren(rAddress);
    }

    public void rename(String nodeName, String rAddress) {
        currentR.rename(nodeName, rAddress);
    }

    public void add(String nodeName, String rAddress) {
        currentR.add(nodeName, rAddress);
    }


    //Moved to set logic
    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    public ArrayList<PABN> hashSearch(String terms) {
        ArrayList<TreeNodeBase> baseNodes = currentR.rFullHashSearch(terms);
        ArrayList<PABN> paBaseNodes = new ArrayList<PABN>();

        //Check size
        int termSize = terms.split("`").length;

        if (baseNodes.size() == 0) {
            return paBaseNodes; // no dice
        }

        //Number of terms matches baseNode hits
        if (baseNodes.get(0).getRank() == termSize) {
            int i = 0;
            while (baseNodes.get(i).getRank() == termSize) {
                paBaseNodes.add(new PABN(baseNodes.get(i).getOrigin(), currentR));
            }
        }
        //Use the next highest number of matched terms
        else {
            termSize = baseNodes.get(0).getRank();
            int i = 0;
            while (baseNodes.get(i).getRank() == termSize) {
                paBaseNodes.add(new PABN(baseNodes.get(i).getOrigin(), currentR));
            }
        }


        return paBaseNodes;

    }

    public PABN get(String rAddress) {

        return new PABN(currentR.get(rAddress), currentR);
    }

    public TreeNode getTreeNode(String rAddress) {
        return currentR.get(rAddress);
    }

    public void save() {
        for(R r : rDB)
            r.save();

        //Terminal Only
        currentR.save();
    }

    public void addParent(String nodeName, String rAddress) {
        currentR.addParent(nodeName, rAddress);
    }

    public void goToDB(String rAddress){
        for(R r : rDB){
            if(r.getName() == rAddress.split("/")[1])
                currentR = r;
        }
    }
    // ----------------------------- END R WRAPPERS ----------------------------------//

}
