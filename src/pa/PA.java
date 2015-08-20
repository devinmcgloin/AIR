package pa;

import org.apache.log4j.Logger;
import r.TreeNode;
import r.R;
import r.TreeNodeBase;
import util.Record;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Blazej on 6/3/2015.
 * This is the only class in PA that deals with actual tree nodes, everything as it comes out is wrapped into NBN.
 *
 */

//External Methods of PA
public final class PA {


    /**
     * HashSearch (Get set -> set logic???)
     *
     * user defined matrices --> export set of BN in a csv file?
     * header logic --> failed add: create header, in all set logic, make sure to
     * reanalyze headers into their true base nodes and re-rank them.
     * merging nodes --> tricky, idk how that function would look like
     */

    protected static File rFolder = new File("./R/");
    protected static ArrayList<R>  rDB = new ArrayList<R>();
    private static boolean started = false;

    static Logger logger = Logger.getLogger(PA.class);


    private PA(){}

    private static void startLibraries(){
        if (rFolder.length() >= 1) {
            for (File fileEntry : rFolder.listFiles()) {
                if (fileEntry.isDirectory()) {

                } else {
                    rDB.add(new R(fileEntry.getName()));
                }
            }
        }
        started = true;
    }


    public static void blaze(){

        if(!started){
            startLibraries();
        }

//        TreeNode k = getRb("noun").get("R/noun/ferrari/^logicalChild");
//
//
//        for( NBN node : getNouns("ferrari", "car")) {
//            if (node.getTitle().equals("ferrari")) {
//
//                //Earlier, adding the Key Value pair of logical child, child node was causing changes in the actual DB
//                //This was fixed by cloning the operation level of children as well as the root.
//                node = node.add("^logicalChild", "Childnode");
//                for (String entry : node.getKeys()) {
//                    System.out.println("   " + entry);
//                    System.out.println("       " + node.get(entry));
//                }
//
//
//            }
//        }
//        k = getRb("noun").get("R/noun/ferrari/");
//        System.out.println("Boop:  " + k.getChildrenString() );

//        TreeNode ba = new TreeNode("bmw^wheel");
//        NBN ta = new NBN(ba);
//
//        NBN x = getNoun("bmw");
//        NBN y = getNoun("car");
////        x = SetLogic.xINHERITy(x, y);
//        System.out.println( Noun.getLogicalParents(x).get(0).getTitle() );
//
//        y = Noun.add(y, "wheel" );
//        x = Noun.add(x, "wheel", "bmw^wheel");
//
//        ta = Noun.add(ta, "NumberOf", "4");
//
////        System.out.println( Noun.nonCarrotSearch(x, "^logicalParents") );
//
//
//        put(x);
//        put(y);
//        put(ta);




    }




    public static void test(){

        if(!started){
            startLibraries();
        }


    }

    public static void put(NBN node){
        put(node.getBaseNode(), "noun");
    }

    public static void put(LDBN node){
        put(node.getBaseNode(), "ldata");
    }
    /**
     * TODO implement log walker.
     * @param node
     */
    public static void put(BaseNode node, String db){

        if(!started){
            startLibraries();
        }
        //Contextualy creating new Nodes:
        //-node you wanted doesn't exist, New TreeNode, New NBN
        //-put it back when done
        //-PA checks if doesn't exist, create a new node R level from here.
        //-then continue the regular adding methods PA has.

        //Check if node already exists in DB, if not, add it. Then continue regular put.
        TreeNode x = getRb(db).get("R/" + db + "/" + node.getTitle());
        if( !x.getTitle().equals(node.getTitle()) ){
            getRb(db).add(node.getTitle(), "R/" + db + "/" + node.getTitle());
        }


        for(Record record : node.getRecord()){

           //TreeNode k = getRb("noun").get("R/");
//            for(String name: k.getChildrenString()){
//                System.out.println("ROOT: "+name);
//            }
//            System.out.println(record);

            if(record.getOperation().equals("add")){
                if(rDBexists(db)){
                    if(record.getVal() == null ){
                        //getRb("noun").del(record.getKey(), "R/noun/" + node.getTitle());
                        //System.out.println("\n\nyup\n\n");
                        getRb(db).add(record.getKey(), "R/" + db + "/" + node.getTitle() );
                    }else{
                        getRb(db).add(record.getKey(),"R/" + db + "/" + node.getTitle() );
                        getRb(db).add(record.getVal(), "R/" + db + "/" + node.getTitle() + "/" + record.getKey() );

                    }
                    //HOW R'S ADD WORKS:
                    //add("nodeName", "R/noun")
                    //add, this node name, to this address.

//                    System.out.println("ADDING: " + "R/noun/" + node.getTitle() + "/" + record.getKey());
//                    System.out.println("ADDING: " + "R/noun/" + node.getTitle() );
                }
            }else if(record.getOperation().equals("rm")) {
                if(rDBexists(db)){
                    if(record.getVal() == null ){
                        getRb(db).del(record.getKey(), "R/" + db + "/" + node.getTitle());
                    }else{
                        //System.out.println("R/noun/" + node.getTitle() + "/" + record.getKey());
                        getRb(db).del(record.getVal(), "R/" + db + "/" + node.getTitle() + "/" + record.getKey());
                    }
                }
            }
            else if(record.getOperation().equals("update")){
                if(rDBexists(db)){
                    getRb(db).del(record.getVal(), "R/" + db + "/" + node.getTitle() + "/" + record.getKey());
                    getRb(db).add(record.getNewVal(), "R/" + db + "/" + node.getTitle() + "/" + record.getKey());
                }
            }else{
                logger.error("Record: " + record.toString() + "\n Is not a valid record");
            }
        }


    }

    public static R getRb(String db){

        if(!started){
            startLibraries();
        }

        for(R database : rDB){
            if(database.getName().equals(db))
                return database;
        }
        return null;
    }

    public static boolean rDBexists(String db){

        if(!started){
            startLibraries();
        }

        for(R database : rDB){
            if(database.getName().equals(db))
                return true;
        }
        return false;
    }



    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    private static ArrayList<TreeNode> hashSearch(String db, String terms) {

        if(!started){
            startLibraries();
        }

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

    public static ArrayList<NBN> nounHashSearch(String terms){

        if(!started){
            startLibraries();
        }

        ArrayList<NBN> nounBaseNodes = new ArrayList<>();
        ArrayList<TreeNode> nodes = hashSearch("noun", terms);
        for (TreeNode node : nodes){
            nounBaseNodes.add(new NBN(node)); //let's see how we're instantiating these NBNs
        }
        return nounBaseNodes;
    }

    public static ArrayList<LDBN> ldataHashSearch(String terms){

        if(!started){
            startLibraries();
        }

        ArrayList<LDBN> ldataBaseNodes = new ArrayList<>();
        ArrayList<TreeNode> nodes = hashSearch("ldata", terms);
        for (TreeNode node : nodes){
            ldataBaseNodes.add(new LDBN(node)); //let's see how we're instantiating these NBNs
        }
        return ldataBaseNodes;
    }

    public static ArrayList<NBN>  getNouns(String name, String filter) {

        if(!started){
            startLibraries();
        }

        if(rDBexists("noun"))
            return nounHashSearch(name + "`" + filter); //Hopefully this filter has ` in them.
        return null;
    }

    public static NBN getNoun(String title){
        if(!started){
            startLibraries();
        }

        //I mirrored the logic you used in your nounHashSearch method.
        if(rDBexists("noun")){
            TreeNode node = getRb("noun").get("R/noun/" + title);
            NBN nounBase = new NBN(node);
            if(nounBase.getTitle().equals(title))
                return nounBase;
            else
                return null;
        }
        return null;
    }

    /**
     * TODO may be good to automatically add all the ^ headers that NBN's normally have
     * @param title
     * @return
     */
    public static NBN createNBN(String title){
        if(!started){
            startLibraries();
        }

        //I mirrored the logic you used in your nounHashSearch method.
        if(rDBexists("noun")){
            getRb("noun").add(title, "R/noun/");
            return getNoun(title);
        }
        return null;
    }

    public static LDBN getLDATA(String title){
        if(!started){
            startLibraries();
        }

        //I mirrored the logic you used in your nounHashSearch method.
        if(rDBexists("ldata")){
            TreeNode node = getRb("ldata").get("R/ldata/" + title);
            LDBN ldataBase = new LDBN(node);
            if(ldataBase.getTitle().equals(title))
                return ldataBase;
            else
                return null;
        }
        return null;
    }

    public static LDBN createLDBN(String title){
        if(!started){
            startLibraries();
        }

        //I mirrored the logic you used in your nounHashSearch method.
        if(rDBexists("ldata")){
            getRb("ldata").add(title, "R/ldata/");
            return getLDATA(title);
        }
        return null;
    }

    public static void save() {

        if(!started){
            startLibraries();
        }

        for(R r : rDB)
            r.save();
    }



}
