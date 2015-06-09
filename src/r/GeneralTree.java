package r;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: navigate by address!
//could just create a func(addressOfNodeToDoFuncOn, valueOfNameFuncShouldTake)
//Then rrename genTree to R
/*
 * Thinks of the General tree as a whole.
 * Makes all changes and everything to the tree. TODO: is that true? yeah.
 * Keeps last used node in memory.
 *
 * This is also what will handle loading in databases to populate a tree.
 * This is basically the tree.
 * Will also handle saving it.
 */
public class GeneralTree {

    TreeNode current;
    TreeNode tmp;
    final String FILEEXTENSION = "./R/";
    File rFolder = new File(FILEEXTENSION);
    HashBrowns hash;


    public GeneralTree(){
        //Start the R/ node.
        current = new TreeNode("R");
        current.setAddress("R");

        //Add the possible files it could have. (DBs)
        //TODO: Could be a null pointer...do i throw an error? jeez. it's such a big program.
        //maybe we could have an error log for the program as a whole!
        if(rFolder.length() >= 1) {
            for (File fileEntry : rFolder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    continue;
                } else {
                    tmp = new TreeNode(fileEntry.getName());
                    current.addChild(tmp);
                }
            }
        }

        //Start a new hashmap.
        hash = new HashBrowns();
    }

    /**
     * Returns the current node to root.
     */
    public void toRoot() {
        while (!current.isRoot())
            toParent();

    }

    /**
     * Sets current to its parent.
     */
    public void toParent() {
        if(!current.isRoot()) {
            current = current.getParent();
        }
    }

    public boolean isRoot(){
        return current.isRoot();
    }
    public boolean isLeaf(){
        return current.isLeaf();
    }

    /**
     * QA on isKeyVal method.
     * @return
     */
    public boolean isKeyVal(){
        List<TreeNode> children = current.getChildren();
        if(children.size() == 1){
            for (TreeNode child : children){
                if(child.isLeaf())
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


    public int getLevel(){
        return current.getLevel();
    }

    /**
     * gets all children from current's path.
     *
     * @return
     */
    public ArrayList<String> getChildren() {
        ArrayList<String> children = new ArrayList<String>();
        for (TreeNode child : current.getChildren()) {
            children.add(child.getName());
        }
        return children;
    }


    /**
     * checks if current has children with the given term.
     *
     * @param searchTerm
     * @return
     */
    public boolean contains(String searchTerm) {
        if (current.contains(searchTerm)) //TODO : re-write in treenode with a BS
            return true;
        return false;
    }

    /**
     * checks if current has children with the given term all the way to leaf.
     *
     * @param searchTerm
     * @return
     */
    public boolean containsAll(String searchTerm) {
        if (current.containsAll(searchTerm))
            return true;
        return false;
    }

    //FUCK: MAKE SURE contains replace this correctly
    //Essentially the same function as above. Marginally faster.
    //OH LAWD CHANGE THIS TOO (to a binary search)
//    public boolean contains(String next){
//        for(int i = 0; i<current.children.size(); i++){
//            String childName = current.children.get(i).getName();
//            if(next.equals(childName)){
//                return true;
//            }
//        }
//        return false;
//    }




    /**
     * No negative levels, you cant navigate to a higher level here, wouldn't
     * know what tree to choose.
     * @param level
     */
    public void upLevel(int level){
        if(level < 0 )
            return;
        if(level > current.getLevel())
            return;
        while(current.getLevel() != level)
            toParent();
    }




    //-----------------------------OUT


    //SAVE
    public void exportDB(){
        //Go back until we're at Foo.txt
        if(current.isRoot())
            return;
        while(!current.getParent().isRoot()){
            goBack();
        }

        String DBout = export(current).toString();//current.prettyPrint();

        //Save file to the DB name
        try{
            String name = "";
            name = current.getName(); //gets the .txt file nam
            if(!name.endsWith(".txt")){
                name += ".txt";
            }
            BufferedWriter out = new BufferedWriter( new FileWriter(FILEEXTENSION + name) );
            out.write(DBout);
            out.close();
        } catch (Exception e){
            System.out.println("You suck at writing to files");
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
     * recursive export.
     *
     * @param node
     * @return StringBuilder --> call toString on it for export.
     */
    public StringBuilder export(TreeNode node) {
        upLevel(1);
        if (node.getLevel() == 1) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode child : current.getChildren()) {
                returnStatement.append(child.toString() + "\n");
                returnStatement.append(export(child));
            }
            return returnStatement;
        }

        else if (!node.isLeaf()) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode child : node.getChildren()) {
                if (!child.isLeaf())
                    returnStatement.append(lvlSpacing(child.getLevel() - 1) + child.toString() + "\n");
                returnStatement.append(export(child));
            }
            return returnStatement;
        }

        else if (node.isLeaf()) {
            StringBuilder returnStatement = new StringBuilder("");
            returnStatement.append(lvlSpacing(node.getLevel() - 1) + node.toString() + "\n");
            return returnStatement;
        }


        return new StringBuilder("");
    }

    ///---------------------------------OUT END





    //------------------------------------blaze's methods ---------------------------------//


    //LOAD DB
    public void loadDB(String dbName){

        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;
        FileReader in = null;
        BufferedReader br = null;
        String line = "";
        String name = "";
        String lastAdded = "";


        //Open File
        try {
            in = new FileReader(FILEEXTENSION + dbName);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid database name.");
            return;
        }

        //Create a reader
        br = new BufferedReader(in);
        try {
            //int tst = 0;
            //LOOP: Read line by line. "name" of node starts at first character, ends at "\r\n"
            while ( (line = br.readLine()) != null ) {
                //Break line into characters to count number of tabs. (Four spaces per tab).
                i = 0;

                while(line.charAt(i) == ' '){
                    i++;
                }
                //tst++;
                //System.out.println(tst);

                prevTabs = curTabs;
                curTabs = i/4;
                lastAdded = name;
                name = line.trim();

                //Adding to the same level.
                if(prevTabs == curTabs){
                    tmp = new TreeNode(name);
                    current.addChild(tmp);
                    hash.add(tmp);
                }
                //Up a level (always increments by 1)
                else if(prevTabs < curTabs){
                    childTraverse(lastAdded);
                    tmp = new TreeNode(name);
                    current.addChild(tmp);
                    hash.add(tmp);
                }
                //Most complex. Going backwards by some number of levels in the tree.
                else{
                    for(i = prevTabs-curTabs; i>0; i--){
                        goBack();
                    }
                    tmp = new TreeNode(name);
                    current.addChild(tmp);
                    hash.add(tmp);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Loop back to R/foo.txt
        while(current.getParent().getParent()!= null){
            goBack();
        }


    }



    /**
     * Handles exiting DB and larger bits of logic for the getNode() function.
     * @param address
     * @return
     */
    public TreeNode getNodeByAddress(String address) {


        //TODO: Make sure you have the right directory and GenTree has files right.
        address = address.trim();
        String[] tmpS = address.split("/");

        // System.out.println("address: "+address);

        if(!address.contains("/")){
            System.out.println("GenTree -- Incorrect format for address: " + address);
            return current;
        }
        //We are sending a command to "R/" directory.
        if(tmpS.length==1 && address.equals("R/")){
            System.out.println("GenTree -- root operation triggered!!!");
            return getRoot();
        }
        String dbName = address.split("/")[1];

        //Options, we're in R, or we're at the db.
        //Wrong db or no DB or right db.

        //NO DB loaded
        if( current.isRoot() /*&& current.contains(dbName)*/){
            childTraverse(dbName);
            loadDB(dbName);
        }
//        else{
//            // FUCK FUCK FUCK
//            System.out.println("GenTree -- No db with name: " + dbName);
//            return current;
//        }

        //Wrong DB loaded
        if( !current.getAddress().split("/")[1].equals(dbName) ){
            while(!current.isRoot()){
                goBack();
            }
            //Now load db of the address we were given.
            childTraverse(dbName);
            loadDB(dbName);
        }


        //TODO: holla at yo boy. Y'all gotta know, you gotta keep your currents your currents.
        //Earlier I had TreeNode tmp = genTree.getNode(address).
        //bruh, that's like have two nodes to the same tree in the same db. ain't worth it. not cool.
        //That's why we was getting a doubling up on the db.
        current =  getNode(address);




        return current;
    }

    //Rename attempt
    public void rename(String newName){
        String oldName = current.getName();
        goBack(); //TODO: null pointer
        if(contains(newName)){
            System.out.printf("Dimension: %s already exists.\n", newName);
            return;
        }
        childTraverse(oldName);
        current.setName(newName);
        hash.add(current); //just add new name to hash. old string reference will either still work with header, or just
        //get deleted if it fails on search. We already know this.

    }

    //ADD PARENT
    public void addParent(String name){
        //Please keep in mind it is impossible to change the text file or "R"
        //Nada				//R								//foo.txt
        if(current == null || current.getParent() == null || current.getParent().getParent() == null ){
            return;
        }
        //Check if that name is already being used. (Implies you must rename the node you want to add first)...
        if(current.getParent().contains(name)){
            System.out.printf("Add Parent Dimension: %s already exists.\n", name);
            return;
        }
        System.out.println("Hey this is where we are: " +current.getAddress());
        //Create the new node
        tmp = new TreeNode(name);
        current.insertParent(tmp);
        System.out.println("Hey this is where we are: " +current.getAddress());

        hash.add(tmp);
    }


    //TODO: Change search  -AT LEAST BE ALPHABETIC. Implement a BST search.
    //Check if children have this.
    public boolean childTraverse(String next){
        for(int i = 0; i<current.children.size(); i++){
            String childName = current.children.get(i).getName();
            if(next.equals(childName)){
                current = current.children.get(i);
                return true;
            }
        }
        return false;
    }




    public TreeNode getNode(String address){

        String[] nodeNames = address.split("/");
        //(genTree will need to split up the address on "/", go to root, and then as long as the
        //childTraverse function returns true, it should childTraverse the next name in the list.
        //e.g. [R, foo.txt, people, George Clooney, Pets, Oscar]

        //Go back till R is two away.
        if(current.isRoot())
            return null;
        while(!current.getParent().isRoot()){
            goBack();
        }

        //Start at second dimension
        for(int i = 2; i< nodeNames.length; i++){
            //System.out.println("smkemltm:  " + nodeNames[i] + tabs);
            boolean foundNextNode = childTraverse(nodeNames[i]);

            //Deletes from Hashmap if it couldn't find the node name.
            String delAddress = nodeNames[0] + "/" + nodeNames[1] + "/";
            if( !foundNextNode ){
                //Iterate over all the words that contain that address.
                for(int j = 2; j<nodeNames.length; j++){
                    delAddress += nodeNames[j];
                    hash.del(nodeNames[j], delAddress);
                }
                return null;
            }
        }

        return current;
    }


    //Returns list of all addresses that contain that node name.
    public ArrayList<String> hashSearch(String input){
        ArrayList<String> addresses = new ArrayList<String>();
        addresses = hash.search(input);
        return addresses;
    }

    public ArrayList<TreeNodeBase> fullHashSearch(String terms){
        //TERMs must be separated by `
        //Then try hash searching.
        TreeNode tmp = getCurrent();
        ArrayList<TreeNodeBase> hits = hash.fullHashSearch(terms, this);
        setCurrent(tmp); //i honestly only need this to guarentee we get back to where we were.
        return hits;
    }

//    //SAVE
//    public void exportDB(){
//        //Go back until we're at Foo.txt
//        if(current==root)
//            return;
//        while(current.getParent()!=root){
//            goBack();
//        }
//
//        String DBout = "";//current.prettyPrint();
//
//        //Save file to the DB name
//        try{
//            String name = "";
//            name = current.getName();
//            if(!name.endsWith(".txt")){
//                name += ".txt";
//            }
//            BufferedWriter out = new BufferedWriter( new FileWriter(FILEEXTENSION + name) );
//            out.write(DBout);
//            out.close();
//        } catch (Exception e){
//            System.out.println("You suck at writing to files");
//        }
//    }
    //GO BACK
    public void goBack(){
        if(current.getParent()!= null){
            //Check if backing out of DB (triggers save)
            if(current.getParent().isRoot()){
                exportDB();
            }
            current = current.getParent();
        }
    }
    //ADD NODE --HOPEFULLY HASH WORKS //FUCK Change to addChild
    public void addNode(String name){
        if(contains(name)){
            System.out.printf("Dimension: %s already exists.\n", name);
            return;
        }
        tmp = new TreeNode(name);
        current.addChild(tmp);
        hash.add(tmp);

    }
    //DEL NODE
    public void delNode(String name){
        List<TreeNode> children = current.children;
        //COULD REPLACE WITH A GETNODE() func
        for(int i = 0; i<children.size(); i++){
            String childName = children.get(i).getName();
            if(name.equals(childName)){
                //Go into that node, go into all it's children, delete everything.
                current.removeChild(children.get(i));
                //Delete for HashMap happens on a failed search for a specific node address.
                //in the getNode() function where it searches by address for a node.
            }
        }
    }
    public TreeNode getCurrent(){
        return current;
    }
    public void setCurrent(TreeNode lol){ this.current = lol;}

    public TreeNode getRoot() {
        tmp = current;
        TreeNode temp2;
        toRoot();
        temp2 = current;
        current = tmp;
        return temp2;
    }
}
