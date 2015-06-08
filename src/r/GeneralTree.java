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

    TreeNode root;
    TreeNode current;
    TreeNode tmp;
    final String FILEEXTENSION = "./R/";
    File rFolder = new File(FILEEXTENSION);
    HashBrowns hash;


    GeneralTree(){
        //Start the R/ node.
        root = new TreeNode("R");
        root.setAddress("R");
        current = root;

        //Add the possible files it could have. (DBs)
        //TODO: Could be a null pointer...do i throw an error? jeez. it's such a big program.
        //maybe we could have an error log for the program as a whole!
        for (File fileEntry : rFolder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                tmp = new TreeNode(fileEntry.getName());
                root.addChildNode(tmp);
            }
        }

        //Start a new hashmap.
        hash = new HashBrowns();
    }

    //LOAD DB
    public void loadDB(String dbName){

        FileReader in = null;
        BufferedReader br = null;
        String line = "";
        String name = "";
        String lastAdded = "";
        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;

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
                    current.addChildNode(tmp);
                    hash.add(tmp);
                }
                //Up a level (always increments by 1)
                else if(prevTabs < curTabs){
                    childTraverse(lastAdded);
                    tmp = new TreeNode(name);
                    current.addChildNode(tmp);
                    hash.add(tmp);
                }
                //Most complex. Going backwards by some number of levels in the tree.
                else{
                    for(i = prevTabs-curTabs; i>0; i--){
                        goBack();
                    }
                    tmp = new TreeNode(name);
                    current.addChildNode(tmp);
                    hash.add(tmp);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Loop back to R/
        while(current.getParent().getParent()!= null){
            goBack();
        }

    }

    //Rename attempt
    public void rename(String newName){
        String oldName = current.getName();
        goBack();
        if(hasChild(newName)==true){
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
        if(current.getParent().containsImmediateChildWithName(name)==true){
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
    //TODO: Change search
    //Essentially the same function as above. Marginally faster.
    //OH LAWD CHANGE THIS TOO (to a binary search)
    public boolean hasChild(String next){
        for(int i = 0; i<current.children.size(); i++){
            String childName = current.children.get(i).getName();
            if(next.equals(childName)){
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
        if(current==root)
            return null;
        while(current.getParent()!=root){
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

    //SAVE
    public void exportDB(){
        //Go back until we're at Foo.txt
        if(current==root)
            return;
        while(current.getParent()!=root){
            goBack();
        }

        String DBout = current.prettyPrint();

        //Save file to the DB name
        try{
            String name = "";
            name = current.getName();
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
    //GO BACK
    public void goBack(){
        if(current.getParent()!= null){
            //Check if backing out of DB (triggers save)
            if(current.getParent()==root){
                exportDB();
            }
            current = current.getParent();
        }
    }
    //ADD NODE --HOPEFULLY HASH WORKS
    public void addNode(String name){
        if(hasChild(name)==true){
            System.out.printf("Dimension: %s already exists.\n", name);
            return;
        }
        tmp = new TreeNode(name);
        current.addChildNode(tmp);

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

}
