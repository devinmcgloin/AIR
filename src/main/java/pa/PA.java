package pa;

import logic.SetLogic;
import org.apache.log4j.Logger;
import r.R;
import r.TreeNode;
import r.TreeNodeBase;
import util.Record;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static funct.Const.DB_TITLE;


/**
 * This is the only class in PA that deals with actual tree nodes, everything as it comes out is wrapped into NBN.
 *
 * @author Blazej
 * @version 6/3/2015.
 */

public final class PA {


    /**
     * HashSearch (Get set -> set logic???)
     * <p/>
     * user defined matrices --> export set of BN in a csv file? header logic --> failed add: create header, in all set
     * logic, make sure to reanalyze headers into their true base nodes and re-rank them. merging nodes --> tricky, idk
     * how that function would look like
     */

    protected static File rFolder = null;
    protected static ArrayList<R> rDB = new ArrayList<>();
    static Logger logger = Logger.getLogger(PA.class);
    private static boolean started = false;


    private PA() {
    }


    public static void blaze() {

        start();

//        TreeNode k = getRb("noun").getCarrot("R/noun/ferrari/^logicalChild");
//
//
//        for( NBN node : getNouns("ferrari", "car")) {
//            if (node.getTitle().titleEquals("ferrari")) {
//
//                //Earlier, adding the Key Value pair of logical child, child node was causing changes in the actual DB
//                //This was fixed by cloning the operation level of children as well as the root.
//                node = node.add("^logicalChild", "Childnode");
//                for (String entry : node.getKeys()) {
//                    System.out.println("   " + entry);
//                    System.out.println("       " + node.getCarrot(entry));
//                }
//
//
//            }
//        }
//        k = getRb("noun").getCarrot("R/noun/ferrari/");
//        System.out.println("Boop:  " + k.getChildrenString() );

//        TreeNode ba = new TreeNode("bmw^wheel");
//        NBN ta = new NBN(ba);
//
//        NBN x = getNoun("bmw");
//        NBN y = getNoun("car");
////        x = SetLogic.xINHERITy(x, y);
//        System.out.println( Noun.getLogicalParents(x).getCarrot(0).getTitle() );
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


    public static void test() {
        start();

        Node bmw = memory.Notepad.searchByTitle("bmw");
        Node door = memory.Notepad.searchByTitle("door");
        Node handle = memory.Notepad.searchByTitle("handle");
        Node color = memory.Notepad.searchByTitle("color");
        Node blue = memory.Notepad.searchByTitle("blue");

        ArrayList<Node> mahNodes = new ArrayList<Node>();
        mahNodes.add(bmw);
        mahNodes.add(door);
        mahNodes.add(color);


    }

    public static void setDB(String file) {
        rFolder = new File(file);
    }

    public static void start() {
        if (rFolder == null) {
            rFolder = new File("./src/main/resources/R");
        }
        if (!started) {
            if (rFolder.length() >= 1) {
                for (File fileEntry : rFolder.listFiles()) {
                    if (fileEntry.isDirectory()) {

                    } else {
                        try {
                            rDB.add(new R(fileEntry.getName(), rFolder.getCanonicalPath()));
                        } catch (IOException e) {
                            System.err.print("Error reading database");
                        }
                    }
                }
            }
            started = true;
        }
    }

    public static void put(Node node) {
        start();

        put(node, DB_TITLE.toString());
    }

    /**
     * implemented log walker.
     *
     * @param node
     */
    private static void put(Node node, String db) {

        start();
        if (!rDBexists(db)) {
            logger.error(String.format("Database: %s does not exist. Failed to put.", db));
            return;
        }

        R database = getRb(db);
        String nodeTitle = Node.getTitle(node);
        //Contextualy creating new Nodes:
        //-node you wanted doesn't exist, New TreeNode, New NBN
        //-put it back when done
        //-PA checks if doesn't exist, create a new node R level from here.
        //-then continue the regular adding methods PA has.

        //Check if node already exists in DB, if not, add it. Then continue regular put.
        TreeNode x = database.get("R/" + db + "/" + nodeTitle);
        if (x == null) {
            database.add(Node.getTitle(node), "R/" + db + "/" + nodeTitle);
        }

//        node.getRecord().stream()
//                .forEach(record -> recordMapper(record,database,db,nodeTitle));


        for (Record record : node.getRecord()) {
            recordMapper(record, database, db, nodeTitle);
        }


    }

    /**
     * HOW R'S ADD WORKS:
     * add("nodeName", "R/noun")
     * add, this node name, to this address.
     *
     * @param record
     * @param database
     * @param db
     * @param nodeTitle
     */
    private static void recordMapper(Record record, R database, String db, String nodeTitle) {
        switch (record.getOperation()) {
            case "add":
                if (record.getVal() == null) {
                    database.add(record.getKey(), "R/" + db + "/" + nodeTitle);
                } else {
                    database.add(record.getKey(), "R/" + db + "/" + nodeTitle);
                    database.add(record.getVal(), "R/" + db + "/" + nodeTitle + "/" + record.getKey());
                }
                break;
            case "rm":
                if (record.getVal() == null) {
                    database.del(record.getKey(), "R/" + db + "/" + nodeTitle);
                } else {
                    database.del(record.getVal(), "R/" + db + "/" + nodeTitle + "/" + record.getKey());
                }
                break;
            case "update":
                database.del(record.getVal(), "R/" + db + "/" + nodeTitle + "/" + record.getKey());
                database.add(record.getNewVal(), "R/" + db + "/" + nodeTitle + "/" + record.getKey());
                break;
            default:
                logger.error("Record: " + record.toString() + "\n Is not a valid record");
                break;
        }
    }
    private static R getRb(String db) {

        start();

        for (R database : rDB) {
            if (database.getName().equals(db))
                return database;
        }
        return null;
    }

    private static boolean rDBexists(String db) {

        start();

        for (R database : rDB) {
            if (database.getName().equals(db))
                return true;
        }
        return false;
    }


    //TODO: has to count if base nodes returned match the number of terms being asked for.
    //if not, PA needs to flag it's about to return the highest number of matched terms it could.
    private static ArrayList<TreeNode> hashSearch(String db, String terms) {

        start();

        ArrayList<TreeNodeBase> baseNodes = getRb(db).rFullHashSearch(terms);
        ArrayList<TreeNode> treeNodeBase = new ArrayList<>();

        //Check size
        int termSize = terms.split("`").length;

        if (baseNodes.size() == 0) {
            return treeNodeBase; // no dice
        }

        //Number of terms matches baseNode hits
        if (baseNodes.get(0).getRank() == termSize) {
            int i = 0;
            // assumes there will be values that dont match. This isnt always true. Fixed with for each loop.

            for (TreeNodeBase node : baseNodes) {
                if (node.getRank() == termSize) {
                    treeNodeBase.add(node.getOrigin());
                } else {
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


    public static ArrayList<Node> generalSearch(String terms) {
        start();

        ArrayList<Node> nounBaseNodes = new ArrayList<>();
        ArrayList<TreeNode> nodes = hashSearch("noun", terms);
        for (TreeNode node : nodes) {
            nounBaseNodes.add(new Node(node));
        }
        return nounBaseNodes;
    }

    public static Node searchExactTitle(String title) {
        start();

        //I mirrored the logic you used in your nounHashSearch method.
        if (rDBexists(DB_TITLE.toString())) {
            TreeNode node = getRb(DB_TITLE.toString()).get("R/" + DB_TITLE.toString() + "/" + title);
            if (node == null)
                return null;
            Node nounBase = new Node(node);
            if (Node.getTitle(nounBase).equals(title))
                return nounBase;
            else
                return null;
        }
        return null;
    }

    public static ArrayList<Node> searchName(String name) {
        start();
        ArrayList<Node> options = generalSearch(name);
        options = SetLogic.nameFilter(options, name);
        return options;
    }

    public static ArrayList<Node> getByTitle(String title) {
        start();
        Node n = searchExactTitle(title);
        ArrayList<Node> nodes = new ArrayList<>();
        if (n != null) {
            nodes.add(n);
        } else {
            int i = 0;
            while (searchExactTitle(title + i) != null) {
                i = i + 1;
                nodes.add(searchExactTitle(title + i));
            }
        }
        return nodes;
    }


    public static void save() {

        start();

        for (R r : rDB)
            r.save();
    }

    /**
     * automatically add all the ^ headers that NBN's normally have
     * todo maybe need to check later if there are nodes without children in the db and delete them.
     *
     * @param title
     *
     * @return
     */
    public static Node createNode(String title) {
        start();
        R r = getRb("noun");
        //I mirrored the logic you used in your nounHashSearch method.
        if (r != null) {
            r.add(title, "R/" + DB_TITLE.toString() + "/");
            Node n = searchExactTitle(title);
            n = Node.add(n, "^name", title);
            n = Node.add(n, "^logicalParents");
            n = Node.add(n, "^notKey");
            n = Node.add(n, "^logicalChildren");
            put(n);
            return searchExactTitle(title);

        }
        return null;
    }


}
