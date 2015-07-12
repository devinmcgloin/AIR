package pa;

import r.TreeNode;
import r.R;
import r.TreeNodeBase;

import java.io.File;
import java.lang.reflect.Array;
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

    public PA() {

        if (rFolder.length() >= 1) {
            for (File fileEntry : rFolder.listFiles()) {
                if (fileEntry.isDirectory()) {

                } else {
                    rDB.add(new R(fileEntry.getName()));
                }
            }
        }

    }

    public void test(){
        for( NBN node : getNouns("ferrari", "car")){
            System.out.println(node.getName());
            for(String entry : node.getKeys()){
                System.out.println("   " + entry);
                System.out.println("       " + node.get(entry));

            }

            System.out.println();

            System.out.println(node.getName());
            node = node.rm("^names");
            node = node.add("^logicalChild", "Childnode");
            for(String entry : node.getKeys()){
                System.out.println("   " + entry);
                System.out.println("       " + node.get(entry));

            }

            System.out.println();

            System.out.println(node.getName());
            node = node.add("^names", "ferrari Autos");
            node = node.update("^names", "Automalia Ferrari", "Automalia");
            for(String entry : node.getKeys()){
                System.out.println("   " + entry);
                ArrayList<String> nodes = node.get(entry);
                System.out.println("       " + nodes);
            }
        }
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
            while (i < baseNodes.size() && baseNodes.get(i).getRank() == termSize) {
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
            nounBaseNodes.add(new NBN(node));
        }
        return nounBaseNodes;
    }

    public ArrayList<NBN> getNouns(String name, String filter) {
        if(rDBexists("noun"))
            return nounHashSearch(name + "`" + filter);
        return null;
    }

    public void save() {
        for(R r : rDB)
            r.save();
    }

    /**
     * This needs to be changed to get the address not name.
     * @param node
     */
    public void put(NBN node){
        TreeNode tn = getRb("noun").get("R/noun/" + node.getName());
    }

}
