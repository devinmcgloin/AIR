package r;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO: navigate by address!
//could just create a func(addressOfNodeToDoFuncOn, valueOfNameFuncShouldTake)
//Then rrename genTree to R

/**
 * Thinks of the General tree as a whole.
 * Makes all changes and everything to the tree. TODO: is that true? yeah.
 * Keeps last used node in memory.
 * <p/>
 * This is also what will handle loading in databases to populate a tree.
 * This is basically the tree.
 * Will also handle saving it.
 */
public class GeneralTree {

    protected final String FILEEXTENSION = "./R/";
    protected TreeNode current;
    protected TreeNode tmp;
    protected File rFolder = new File(FILEEXTENSION);
    protected HashBrowns hash;

    static Logger logger = Logger.getLogger(GeneralTree.class);



    protected GeneralTree() {
        //Start the R/ node.
        current = new TreeNode("R");
        current.setAddress("R");

        //Add the possible files it could have. (DBs)
        //TODO: Could be a null pointer...do i throw an error? jeez. it's such a big program.
        //maybe we could have an error log for the program as a whole!
        if (rFolder.length() >= 1) {
            for (File fileEntry : rFolder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    continue;
                } else {
                    tmp = new TreeNode(fileEntry.getName());
                    current.addChildBlind(tmp);
                }
            }
            //Sort database nodes so they work with BS.
            current.sortChildren();
        }

        //Start a new hashmap.
        hash = new HashBrowns();
    }

    /**
     * Returns the current node to root.
     */
    protected void toRoot() {
        while (!current.isRoot())
            toParent();

    }



    /**
     * Sets current to its parent.
     */
    protected void toParent() {
        if (!current.isRoot()) {
            current = current.getParent();
        }
    }


    /**
     * SAVE
     */
    protected void exportDB() {
        logger.info("export called");
        //Go back until we're at Foo.txt
        if (current.isRoot())
            return;
        while (!current.getParent().isRoot()) {
            goBack();
        }

        String DBout = export(current).toString();

        //System.out.println(DBout);

        //Save file to the DB name
        try {
            String name = "";
            name = current.getTitle(); //gets the .txt file nam
//            if (!name.endsWith(".txt")) {
//                name += ".txt";
//            }
            BufferedWriter out = new BufferedWriter(new FileWriter(FILEEXTENSION + name));
            out.write(DBout);
            out.close();
        } catch (Exception e) {
            logger.fatal("You suck at writing to files");
        }
    }


    /**
     * utility for proper spacing on output.
     *
     * @param level
     * @return
     */
    private String lvlSpacing(int level) {
        return new String(new char[level]).replace("\0", "    ");
    }

    /**
     * recursive export calls export recursive.
     *
     * @param node - .txt node
     * @return StringBuilder --> call toString on it for export.
     */
    protected StringBuilder export(TreeNode node) {
        StringBuilder DBout = new StringBuilder();
        if (node.getLevel() == 1) {
            String buffer = "";
            //TODO: Organize children alphabetically on export. (Check if already sorted, duh).
            Collections.sort(node.getChildren());
            TreeNode tmp;
            for (TreeNode child : node.getChildren()) {
                DBout.append(buffer + child.getTitle() + "\n");
                DBout.append(exportRec(child, buffer));
            }
            return DBout;
        }
        return DBout;
    }

    /**
     *
     * @param node
     * @param buffer
     * @return
     */
    private StringBuilder exportRec(TreeNode node, String buffer) {
        StringBuilder DBout = new StringBuilder();
        buffer += "    ";
        Collections.sort(node.getChildren());
        for (TreeNode child : node.getChildren()) {
            DBout.append(buffer + child.getTitle() + "\n");
            DBout.append(exportRec(child, buffer));
        }
        return DBout;
    }

    /**
     *
     * @param dbName
     */
    protected void loadDB(String dbName) {

        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;
        FileReader in = null;
        BufferedReader br = null;
        String line = "";
        String name = "";
        String lastAdded = "";

        //FOR OPTIMIZATION:
        //Replace the current "children" with a NEW longer initial ArrayList
        if(current.getTitle().equals("test")) {
            //current.setChildrenSize(120000);
        }


        //Open File
        try {
            in = new FileReader(FILEEXTENSION + dbName);
        } catch (FileNotFoundException e) {
            logger.fatal("Invalid database name.");
            return;
        }

        //Create a reader
        br = new BufferedReader(in);
        try {
            //LOOP: Read line by line. "name" of node starts at first character, ends at "\r\n"
            while ((line = br.readLine()) != null) {
                //Break line into characters to count number of tabs. (Four spaces per tab).
                i = 0;


                while (line.charAt(i) == ' ') {
                    i++;
                }

                prevTabs = curTabs;
                curTabs = i / 4;
                lastAdded = name;
                name = line.trim();

                //Adding to the same level.
                if (prevTabs == curTabs) {
                    tmp = new TreeNode(name);
                    current.addChildBlind(tmp);
                    hash.add(tmp);
                }
                //Up a level (always increments by 1)
                else if (prevTabs < curTabs) {
                    childTraverse(lastAdded);       //FUCK make into BST
                    tmp = new TreeNode(name);
                    current.addChildBlind(tmp);
                    hash.add(tmp);
                }
                //Most complex. Going backwards by some number of levels in the tree.
                else {
                    for (i = prevTabs - curTabs; i > 0; i--) {
                        goBack();
                    }
                    tmp = new TreeNode(name);
                    current.addChildBlind(tmp);
                    hash.add(tmp);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Loop back to R/foo.txt
        while (current.getParent().getParent() != null) {
            goBack();
        }


    }

    /**
     * Handles exiting DB and larger bits of logic for the getNode() function.
     *
     * @param address
     * @return
     */
    protected TreeNode getNodeByAddress(String address) {




        //TODO: Make sure you have the right directory and GenTree has files right.
        address = address.trim();
        String[] tmpS = address.split("/");

        // System.out.println("address: "+address);

        if (!address.contains("/")) {
            logger.error("GenTree -- Incorrect format for address: " + address);
            return current;
        }
        //We are sending a command to "R/" directory.
        if (tmpS.length == 1 && address.equals("R/")) {
            //System.out.println("GenTree -- root operation triggered!!!");
            return getRoot();
        }
        String dbName = address.split("/")[1];
        if (dbName.equals("") || dbName.equals("\n")) {
            logger.error("No DB Name provided.");
            return current;
        }

        //Options, we're in R, or we're at the db.
        //Wrong db or no DB or right db.

        //NO DB loaded
        if (current.isRoot() && current.contains(dbName)) {
            childTraverse(dbName);
            loadDB(dbName);
        }
        //NO DB loaded and asking for incorrect DB
        if (!current.contains(dbName) && current.isRoot()) {
            logger.error("No DB by name: " + dbName);
            return current;
        }


        //FUCK

        //Wrong DB loaded
        if (!current.getAddress().split("/")[1].equals(dbName)) {
            while (!current.isRoot()) {
                goBack();
            }
            //Now check if that DB exists
            if (!current.contains(dbName) && current.isRoot()) {
                logger.error("No DB by name: " + dbName);
                return current;
            }
            //Now load db of the address we were given.
            childTraverse(dbName);
            //Check if DB is already in memory. If so, just traverse into it.
            //TODO QA this fix
            if(current.getChildren() != null) {
                //We good.
            } else{
                loadDB(dbName); //This was where where export doubling.
            }


        }


        //TODO: holla at yo boy. Y'all gotta know, you gotta keep your currents your currents.
        //Earlier I had TreeNode tmp = genTree.getNode(address).
        //bruh, that's like have two nodes to the same tree in the same db. ain't worth it. not cool.
        //That's why we was getting a doubling up on the db.
        current = getNode(address);


        return current;
    }

    /**
     *
     * @param newName
     */
    protected void rename(String newName) {
        String oldName = current.getTitle();
        goBack(); //TODO: null pointer
        if (contains(newName)) {
            //System.out.printf("Dimension: %s already exists.\n", newName);
            return;
        }
        childTraverse(oldName);
        current.setTitle(newName);
        hash.add(current); //just add new name to hash. old string reference will either still work with header, or just
        //getNoun deleted if it fails on search. We already know this.

    }

    /**
     *
     * @param name
     */
    protected void addParent(String name) {
        //Please keep in mind it is impossible to change the text file or "R"
        //Nada				//R								//foo.txt
        if (current == null || current.getParent() == null || current.getParent().getParent() == null) {
            return;
        }
        //Check if that name is already being used. (Implies you must rename the node you want to add first)...
        if (current.getParent().contains(name)) {
            logger.debug("Add Parent Dimension: " + name + " already exists.\n");
            return;
        }
        logger.debug("Hey this is where we are: " + current.getAddress());
        //Create the new node
        tmp = new TreeNode(name);
        current.insertParent(tmp);
        logger.debug("Hey this is where we are: " + current.getAddress());

        hash.add(tmp);
    }


    protected boolean isRoot() {
        return current.isRoot();
    }

    protected boolean isLeaf() {
        return current.isLeaf();
    }

    /**
     * QA on isKeyVal method.
     *
     * @return
     */
    protected boolean isKeyVal() {
        List<TreeNode> children = current.getChildren();
        if (children.size() == 1) {
            for (TreeNode child : children) {
                if (child.isLeaf())
                    return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Format address utility
     *
     * @param path
     * @return - tring array
     */
    private String[] formatRAddress(String path) {
        return path.split("/");
    }

    protected int getLevel() {
        return current.getLevel();
    }

//    /**
//     * gets all children from current's path.
//     *
//     * @return
//     */
//    protected ArrayList<String> getChildren() {
//        ArrayList<String> children = new ArrayList<String>();
//        for (TreeNode child : current.getChildren()) {
//            children.add(child.getTitle());
//        }
//        return children;
//    }

    /**
     * checks if current has children with the given term all the way to leaf.
     *
     * @param searchTerm
     * @return
     */
    protected boolean containsAll(String searchTerm) {
        if (current.containsAll(searchTerm))
            return true;
        return false;
    }

    /**
     * No negative levels, you cant navigate to a higher level here, wouldn't
     * know what tree to choose.
     *
     * @param level
     */
    protected void upLevel(int level) {
        if (level < 0)
            return;
        if (level > current.getLevel())
            return;
        while (current.getLevel() != level)
            toParent();
    }

    /**
     * TODO: Change search  -AT LEAST BE ALPHABETIC. Implement a BST search.
     * Takes the name of the next node you want to go to within current's children.
     * @param next
     * @return
     */
    protected boolean childTraverse(String next) {
        //Implementing using BS.
        int index = current.binarySearch(next);
        if(index >= 0){
            current = current.getChildren().get(index);
            return true;
        }
        //Why is this retuning a boolean. Who uses this?
        //Hope the binarySearch method is working well.
        return false;


    }

    /**
     *
     * @param address
     * @return
     */
    protected TreeNode getNode(String address) {


        String[] nodeNames = address.split("/");
        //(genTree will need to split up the address on "/", go to root, and then as long as the
        //childTraverse function returns true, it should childTraverse the next name in the list.
        //e.g. [R, foo.txt, people, George Clooney, Pets, Oscar]


        if (current.isRoot())
            return null;
        //Go back till R is two away.
        while (!current.getParent().isRoot()) {
            goBack();
        }

        //Start at second dimension
        for (int i = 2; i < nodeNames.length; i++) {
            //System.out.println("smkemltm:  " + nodeNames[i] + tabs);
            boolean foundNextNode = childTraverse(nodeNames[i]);

           // System.out.println("Entereed");
            //System.out.println(foundNextNode + ":  "  +nodeNames[i]);

            //Deletes from Hashmap if it couldn't find the node name.
            String delAddress = nodeNames[0] + "/" + nodeNames[1] + "/";
            if (!foundNextNode) {
                //Iterate over all the words that contain that address.
                for (int j = 2; j < nodeNames.length; j++) {
                    delAddress += nodeNames[j] + "/";
                    hash.del(nodeNames[j], delAddress);
                }
                return current; //FUCK //TODO:: DONT KNOW WHY THIS WAS REUTURNING THE CURRENT. HAVE NO WAY TO KNOW IF IT FAILS.
            }
        }


        return current;
    }

    /**
     *
     * @param input
     * @return - list of all addresses that contain that node name.
     */
    protected ArrayList<String> hashSearch(String input) {
        ArrayList<String> addresses = new ArrayList<String>();
        addresses = hash.search(input);
        return addresses;
    }

    /**
     *
     * @param terms
     * @return
     */
    protected ArrayList<TreeNodeBase> fullHashSearch(String terms) {
        //TERMs must be separated by `
        //Then try hash searching.
        TreeNode tmp = getCurrent();
        ArrayList<TreeNodeBase> hits = hash.fullHashSearch(terms, this);
        setCurrent(tmp); //i honestly only need this to guarentee we get back to where we were.
        return hits;
    }

    protected TreeNode getCurrent() {
        return current;
    }

    protected void setCurrent(TreeNode lol) {
        this.current = lol;
    }

    /**
     * Sets current to the parent node.
     */
    protected void goBack() {
        if (current.getParent() != null) {
            //Check if backing out of DB (triggers save)
            if (current.getParent().isRoot()) {
                logger.debug("goBack() -- > Is this why exporting twice? ");
                exportDB();
            }
            current = current.getParent();
        }
    }



    /**
     *
     * @param name
     */
    protected void addNode(String name) {
        //Implementing using BS.
        int index = current.binarySearch(name);

        if (index >= 0) {
            logger.error(String.format("Dimension: %s already exists.\n", name));
            return;
        }
        tmp = new TreeNode(name);

        current.insertChild(tmp, (index*-1)-1);
        //We now use insertChild as oppose to addChildBlind
        //current.addChildBlind(tmp);
        hash.add(tmp);

    }



    /**
     * checks if current has children with the given term.
     *
     * @param searchTerm
     * @return
     */
    protected boolean contains(String searchTerm) {
        if (current.contains(searchTerm)) //TODO : re-write in treenode with a BS
            return true;
        return false;
    }

    /**
     * DEL NODE
     * @param name
     */
    protected void delNode(String name) {
        int index = current.binarySearch(name);

        if(index>=0){
            current.removeChild(index);
        }

//        List<TreeNode> children = current.getChildren();
//        //COULD REPLACE WITH A GETNODE() func
//        for (int i = 0; i < children.size(); i++) {
//            String childName = children.get(i).getTitle();
//            if (name.equals(childName)) {
//                //Go into that node, go into all it's children, delete everything.
//                current.removeChild(children.get(i));
//                //Delete for HashMap happens on a failed search for a specific node address.
//                //in the getNode() function where it searches by address for a node.
//            }
//        }
    }

    /**
     *
     * @return - root of current node.
     */
    protected TreeNode getRoot() {
        tmp = current;
        TreeNode temp2;
        toRoot();
        temp2 = current;
        current = tmp;
        return temp2;
    }
}
