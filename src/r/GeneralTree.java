package r;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 6/5/15.
 *
 * PACKAGE GOD.
 *
 * PATHS EXCLUDE BASE NODE GeneralTree. are of the form "nouns/places/nations"
 * ALL FUNCTIONS EXCEPT THOSE THAT BEGIN WITH TO, RETURN TO THEIR ORGINAL rAddress.
 *
 * CONSIDER HashNode Responsibilities:
 *
 * GeneralTree contains hash for base nodes
 * base nodes contain hash for all their children.
 *
 * GeneralTree contains a hashMap of all of the base nodes, the assumption being that questions are generally about something.
 * First we try to find the specific base node as outlined in search.java, then if it is not found then we ove on to
 * fuzzy search.
 *
 * CONSIDER: Maybe able to navigate by hash not path names? Do we really even need paths at all?
 */
public class GeneralTree {

    TreeNode current;
    TreeNode tmp;
    HashBrowns hash;
    final String FILEEXTENSION = "./R/";
    File rFolder = new File(FILEEXTENSION);

    public GeneralTree() {
        tmp = current;
        current = new TreeNode("R");
        current.setAddress("R");

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

    public void setCurrent(TreeNode current) {
        this.current = current;
    }

    public String getCurrentPath(){
        return current.getAddress();
    }
    /**
     * Returns the current node to root.
     */
    public void toRoot() {
        while (!current.isRoot())
            toParent();

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
     * PATH NAME EXCLUDING THE BASE NODE GeneralTree.
     *
     * @param path
     */
    public void toDir(String path) {
        toRoot();
        String[] rAddress = formatRAddress(path);

        for (String address : rAddress) {
//            System.out.print("[" + address + "]");

            toChild(address);
        }

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

    /**
     * Sets current to its parent.
     */
    public void toParent() {
        if(!current.isRoot()) {
            if(current.getParent().isRoot()){
                export(current);
            }
            current = current.getParent();
        }
    }

    /**
     * sets current to the specified child node.
     *
     * @param specifiedChild
     */
    public void toChild(String specifiedChild) {
        current = current.getChild(specifiedChild);

    }

    /**
     * adds a child at the current's path.
     *
     *
     * @param child
     */
    public void addChild(String child) {
        current.addChild(child);
    }

    /**
     * Here for move function. Dont use for anything else.
     *
     * @param node
     */
    public void addChild(TreeNode node){
        current.addChild(node);
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
        if (current.contains(searchTerm) != null)
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
        if (current.containsAll(searchTerm) != null)
            return true;
        return false;
    }

    /**
     * deletes the specified node, including its children.
     *
     * QA - del with hashMap
     *
     * @param rAddress
     * @return
     */
    public void del(String rAddress) {
        tmp = current;
        toDir(rAddress);
        toParent();
        String[] address = formatRAddress(rAddress);
        current.delChild(address[address.length - 1]);
        current = tmp;

    }

    /**
     *
     * renames the node at the specified address.
     *
     * QA - rename with hashmap
     *
     * @param rAddress - address at which you want to change to take effect.
     *                 NOTE, IT WILL CHANGE THE GIVEN ADDRESS'S NAME, Not its child.
     * @param newName  -  new name field. has to be a string.
     */
    public void rename(String rAddress, String newName) {
        tmp = current;
        toDir(rAddress);
        current.setName(newName);
        current = tmp;

    }

    /**
     * adds a child at the given address.
     *
     * @param rAddress
     * @param term
     */
    public void add(String rAddress, String term) {
        tmp = current;
        toDir(rAddress);
        if(contains(term)){
            System.out.printf("Dimension: %s already exists.\n", term);
            return;
        }
        current.addChild(term);
        current = tmp;

    }

    /**
     * returns the node at the given address.
     * TODO
     * @param rAddress
     * @return
     */
    public TreeNode get(String rAddress) {

        rAddress = rAddress.trim();
        String[] tmpS = rAddress.split("/");

        if(!rAddress.contains("R/")){
            System.out.println("GenTree -- Incorrect format for address: " + rAddress);
            return current;
        }

        if(tmpS.length==1 && rAddress.equals("R/")){
            System.out.println("GenTree -- root operation triggered!!!");
            return getRoot();
        }

        String dbName = rAddress.split("/")[1];

        if(current.isRoot()){
            toChild(dbName);
            populate(dbName);
        }

        if(!current.getAddress().split("/")[1].equals(dbName)){
            while(!current.isRoot()){
                toParent();
            }
            //Now load db of the address we were given.
            toChild(dbName);
            populate(dbName);
        }

//        rAddress = rAddress.replace(current.getParent().getAddress(), "");
        String[] nodeNames = rAddress.split("/");

        for(String name : nodeNames){
            if(current.contains(name) != null)
                toChild(name);
//            else{
//                String delAddress = nodeNames[0] + "/" + nodeNames[1] + "/";
//                for(int j = 2; j<nodeNames.length; j++){
//                    delAddress += nodeNames[j];
//                    hash.del(nodeNames[j], delAddress);
//                }
//            }

        }
        return current;
    }

    public ArrayList<String> hashSearch(String input){
        ArrayList<String> addresses = new ArrayList<String>();
        addresses = hash.search(input);
        return addresses;
    }

    public ArrayList<TreeNodeBase> fullHashSearch(String terms){
        //TERMs must be separated by `
        //Then try hash searching.
        TreeNode tmp = current;
        ArrayList<TreeNodeBase> hits = hash.fullHashSearch(terms, this);
        setCurrent(tmp); //i honestly only need this to guarentee we get back to where we were.
        return hits;
    }


    /**
     * recursive export.
     *
     * @param node
     * @return
     */
    public StringBuilder export(TreeNode node) {
        if (node.getParent().isRoot()) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode child : current.getChildren()) {
                if (!child.isLeaf())
                    returnStatement.append(child.toString() + "\n");
                returnStatement.append(export(child));
            }
            return returnStatement;
        } else if (!node.isLeaf()) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode child : node.getChildren()) {
                if (!child.isLeaf())
                    returnStatement.append(lvlSpacing(child.getLevel() - 1) + child.toString() + "\n");
                returnStatement.append(export(child));
            }
            return returnStatement;
        } else if (node.isLeaf()) {
            StringBuilder returnStatement = new StringBuilder("");
            returnStatement.append(lvlSpacing(node.getLevel() - 1) + node.toString() + "\n");
            return returnStatement;
        }


        return new StringBuilder("");
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
     * saves the whole tree.
     */
    public void save() {
        //Go back until we're at Foo.txt
        if(current.isRoot())
            return;
        while(!current.getParent().isRoot()){
            toParent();
        }

        String DBout = export(current).toString();

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
     * Pulls all the database info out of the text file.
     *
     * TODO: Fix db loading speed, ~7 seconds currently.
     *
     * @param f - file to load
     */
    public void populate(String f) {
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
            in = new FileReader(FILEEXTENSION + f);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid database name.");
            return;
        }

        //Create a reader
        br = new BufferedReader(in);
        try {
            System.out.println("FILE READ INITALIZED");
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
                    toChild(lastAdded);
                    tmp = new TreeNode(name);
                    current.addChild(tmp);
                    hash.add(tmp);
                }
                //Most complex. Going backwards by some number of levels in the tree.
                else{
                    for(i = prevTabs-curTabs; i>0; i--){
                        toParent();
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
        toDBLevel();


    }


    public int getLevel(){
        return current.getLevel();
    }

    public TreeNode getRoot() {
        tmp = current;
        TreeNode temp2;
        toRoot();
        temp2 = current;
        current = tmp;
        return temp2;
    }

    /**
     * QA on ifExistsCD method.
     * @param node
     * @return
     */
    public boolean ifExistsCD(String node){
        if(contains(node)) {
            get(current.getAddress() + node + "/");
            return true;
        }
        return false;
    }

    public void toDBLevel(){
        if(current.getLevel() >= 1)
            return;
        while(current.getLevel() >= 2)
            toParent();

    }



}
