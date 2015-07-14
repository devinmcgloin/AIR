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

    public void blaze(){
        TreeNode k = getRb("noun").get("R/noun/ferrari/^logicalChild");


        for( NBN node : getNouns("ferrari", "car")) {
            if (node.getTitle().equals("ferrari")) {

                //Earlier, adding the Key Value pair of logical child, child node was causing changes in the actual DB
                //This was fixed by cloning the first level of children as well as the root.
                node = node.add("^logicalChild", "Childnode");
                for (String entry : node.getKeys()) {
                    System.out.println("   " + entry);
                    System.out.println("       " + node.get(entry));
                }


            }
        }
        k = getRb("noun").get("R/noun/ferrari/");
        System.out.println("Boop:  " + k.getChildrenString() );


    }

    public void test(){



        for( NBN node : getNouns("ferrari", "car")){
            if(node.getTitle().equals("ferrari")) {
                System.out.println(node.getTitle());
                for (String entry : node.getKeys()) {


                    System.out.println("   " + entry);
                    System.out.println("       " + node.get(entry));

                }

                System.out.println();





                System.out.println(node.getTitle());
                node = node.rm("^name");
                node = node.add("^logicalChild", "Childnode");
                for (String entry : node.getKeys()) {
                    System.out.println("   " + entry);
                    System.out.println("       " + node.get(entry));
                }

//                System.out.println();



                System.out.println(node.getTitle());
                node = node.add("^name", "ferrari Autos");
                node = node.add("^name", "ferrari Autosss");
                NBN node2 = node.update("^name", "ferrari Autosss", "Automalia");
                for (String entry : node.getKeys()) {
                    System.out.println("   " + entry);
                    ArrayList<String> nodes = node.get(entry);
                    System.out.println("       " + nodes);
                }


                ArrayList<String> toAddKeys = new ArrayList<>();
                ArrayList<String> toAddVals = new ArrayList<>();
                toAddKeys.add("Speed");
                toAddVals.add("fast");
                toAddKeys.add("cost");
                toAddVals.add("expensive");
                node2 = node2.batchAdd(toAddKeys, toAddVals);

                System.out.println("Node 1:");
                for(NBN.Tuple add : node.getRecord()){
                    System.out.println(add.toString());
                }



                System.out.println("Node 2:");
                for(NBN.Tuple add : node2.getRecord()){
                    System.out.println(add.toString());
                }









                put(node2);



                save();



            }

        }
    }

    /**
     * TODO implement log walker.
     * @param node
     */
    public void put(NBN node){



        for(NBN.Tuple record : node.getRecord()){

//            TreeNode k = getRb("noun").get("R/");
//            for(String name: k.getChildrenString()){
//                System.out.println("ROOT: "+name);
//            }



//            System.out.println(record);

            if(record.fst().equals("add")){
                if(rDBexists("noun")){

                    System.out.println("\n\n\n\n");

                    getRb("noun").add(record.snd(), "R/noun/" + node.getTitle() );
                    getRb("noun").add(record.thrd(), "R/noun/" + node.getTitle() + "/" + record.snd() );


                    //HOW R'S ADD WORKS:
                    //add("nodeName", "R/noun")
                    //add, this node name, to this address.

//                    System.out.println("ADDING: " + "R/noun/" + node.getTitle() + "/" + record.snd());
//                    System.out.println("ADDING: " + "R/noun/" + node.getTitle() );
                }
            }else if(record.fst().equals("rm")) {


                if(rDBexists("noun")){



                    if(record.thrd().equals("[third null]") ){

                        getRb("noun").del(record.snd(), "R/noun/" + node.getTitle());


                    }else{

                        System.out.println("hm2");


                        //FUCK this is where we were getting the original ^name and now Speed db issue.
                        System.out.println("R/noun/" + node.getTitle() + "/" + record.snd());
                        getRb("noun").del(record.thrd(), "R/noun/" + node.getTitle() + "/" + record.snd());

                    }
                }
            }

            else if(record.fst().equals("update")){
                if(rDBexists("noun")){
                    getRb("noun").del(record.thrd(), "R/noun/" + node.getTitle() + "/" + record.snd());
                    getRb("noun").add(record.frth(), "R/noun/" + node.getTitle() + "/" + record.snd());

                }
            }else{
                System.out.println("Record: " + record.toString() + "\n Is not a valid record");
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
            nounBaseNodes.add(new NBN(node)); //let's see how we're instantiating these NBNs
        }
        return nounBaseNodes;
    }

    public ArrayList<NBN> getNouns(String name, String filter) {
        if(rDBexists("noun"))
            return nounHashSearch(name + "`" + filter); //Hopefully this filter has ` in them.
        return null;
    }

    public void save() {
        for(R r : rDB)
            r.save();
    }



}
