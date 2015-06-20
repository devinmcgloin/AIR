package pa;

import r.TreeNode;
import r.R;
import r.TreeNodeBase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 * This is the only class in PA that deals with actual tree nodes, everything as it comes out is wrapped into NBN.
 *
 */

//External Methods of PA
public class PA {


    /**
     * HashSearch (Get set -> set logic???)
     *
     * user defined matrices --> export set of BN in a csv file?
     * header logic --> failed add: create header, in all set logic, make sure to
     * reanalyze headers into their true base nodes and re-rank them.
     * merging nodes --> tricky, idk how that function would look like
     */

    protected File rFolder = new File("./R/");
    protected ArrayList<R> rDB = new ArrayList<R>();
    protected Inheritance inherit;
    protected SetLogic setLogic;
    protected LDATA ldata = new LDATA(this);

    public PA() {

        if (rFolder.length() >= 1) {
            for (File fileEntry : rFolder.listFiles()) {
                if (fileEntry.isDirectory()) {

                } else {
                    rDB.add(new R(fileEntry.getName()));
                }
            }
        }


        inherit = new Inheritance(this);
        setLogic = new SetLogic(this);

    }

    public void getset(String query) {
        ArrayList<NBN> nodes = setLogic.genSet(query);
        if(nodes.size() > 0) {
            for (NBN node : nodes) {
                System.out.println(node.getOrigin().getName());
            }
        }else
            System.out.println("No nodes found! Try loosening your search parameters.");
    }

    public void devintest(){

    }

    public void blazetest() {
        //inherit.xISy("ferrari", "car");

        //NBN test = getNoun("R/noun/ferrari");
        //System.out.println(test.isFilter("car"));

        long start = System.currentTimeMillis();

        for(int i =0; i<16000; i++){
            add("test", "abca" +i, "R/test");
            //add("test", "foo" , "R/test/"+"aaaa"+i);
        }
//        for(int i =0; i<1000; i++){
//            add("test", "foo" +i, "R/test/");
//
//        }

//        NBN test = getNoun("R/noun/");
//        test.getOrigin().contains("blazej gawlik");

        long end = System.currentTimeMillis();

        System.out.println("Writing took: " + (end-start)/1000 + "."+ (end-start)%1000);
    }

    public R getRb(String db){
        for(R database : rDB){
            if(database.getName().equals(db))
                return database;
        }
        return null;
    }

    public boolean rDBexists(String db){
        for(R database : rDB){
            if(database.getName().equals(db))
                return true;
        }
        return false;
    }

    //---------------------------------R WRAPPERS---------------------------------//

    public void del(String db, String nodeName, String rAddress) {
        if(rDBexists(db))
            getRb(db).del(nodeName, rAddress);
    }

    public ArrayList<String> getChildren(String db, String rAddress) {
        if(rDBexists(db))
            return getRb(db).getChildren(rAddress);
        return null;
    }

    public void rename(String db, String nodeName, String rAddress) {
        if(rDBexists(db))
            getRb(db).rename(nodeName, rAddress);
    }

    public void add(String db, String nodeName, String rAddress) {
        if(rDBexists(db))
            getRb(db).add(nodeName, rAddress);
    }


    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    public ArrayList<TreeNode> hashSearch(String db, String terms) {
        ArrayList<TreeNodeBase> baseNodes = getRb(db).rFullHashSearch(terms);
        ArrayList<TreeNode> treeNodeBase = new ArrayList<TreeNode>();

        //Check size
        int termSize = terms.split("`").length;

        if (baseNodes.size() == 0) {
            return treeNodeBase; // no dice
        }

        //Number of terms matches baseNode hits
        if (baseNodes.get(0).getRank() == termSize) {
            int i = 0;
            //TODO assumes there will be values that dont match. This isnt always true. Fixed with for each loop.

            for (TreeNodeBase node : baseNodes) {
                if (node.getRank() == termSize) {
                    treeNodeBase.add(node.getOrigin());
                }else{
                    break;
                }
            }
        }
        //Use the next highest number of matched terms
        else {
            termSize = baseNodes.get(0).getRank();
            int i = 0;
            while (baseNodes.get(i).getRank() == termSize) {
                treeNodeBase.add(baseNodes.get(i).getOrigin());
                i += 1;
            }
        }
        return treeNodeBase;
    }

    public ArrayList<NBN> nounHashSearch(String terms){
        ArrayList<NBN> nounBaseNodes = new ArrayList<>();
        ArrayList<TreeNode> nodes = hashSearch("noun", terms);
        for (TreeNode node : nodes){
            nounBaseNodes.add(new NBN(node, this));
        }
        return nounBaseNodes;
    }

    public TreeNode get(String DB, String rAddress) {
        if(rDBexists(DB))
            return getRb(DB).get(rAddress);
        return null;
    }

    public NBN getNoun(String noun){
        TreeNode tmp = get("noun", "R/noun/" + noun);
        if(!tmp.getName().equals( noun ))
            return null;
        else
            return new NBN(tmp, this);
    }

    public LDBN getLDATA(String ldata){
        TreeNode tmp = get("ldata","R/ldata/" + ldata);
        if(!tmp.getName().equals(ldata))
            return null;
        else
            return new LDBN(tmp);
    }

    public void save() {
        for(R r : rDB)
            r.save();
    }

    public void addParent(String db, String nodeName, String rAddress) {
        if(rDBexists(db))
            getRb(db).addParent(nodeName, rAddress);
    }

    // ----------------------------- END R WRAPPERS ----------------------------------//

    public boolean evaluate(String keyVal, NBN node){
        return ldata.evaluate(keyVal, node);
    }

    public boolean isNounBase(String x){
        if (getNoun(x) != null)
            return true;
        else
            return false;
    }

    public void addBaseNode(String type, String name){
        if(type.equals("ldata")){

        }
        else if(type.equals("noun")){
            add("noun", name, "R/noun/");
            add("noun", "^has","R/noun/" + name);
            add("noun", "^is", "R/noun/" + name);
            add("noun", "^v1", "R/noun/" + name);
            add("noun", "^v2", "R/noun/" + name);
            add("noun", "^adj","R/noun/" + name);
            add("noun", "^logicalchild","R/noun/" + name);
            System.out.println(name + " added as basenode in nouns.");
        }
    }
}
