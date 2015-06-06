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
import java.util.Scanner;

import r.TreeNode;

/**
 * Created by devinmcgloin on 6/5/15.
 * PATHS EXCLUDE BASE NODE R.
 * ALL FUNCTIONS EXCEPT THOSE THAT BEGIN WITH TO, RETURN TO THEIR ORGINAL rAddress.
 *
 */
public class R {

    ArrayList<String> currentPath = new ArrayList<String>();
    ArrayList<String> tempPath = new ArrayList<String>();
    TreeNode<String> current;
    TreeNode<String> tmp = new TreeNode<String>("");

    public R(){
        current = new TreeNode<String>("R");
        populate(new File("./R/R.txt"));
        tempPath = copyCurrentPath();
        tmp = current;
    }

    public void setCurrent(TreeNode<String> current) {
        this.current = current;
    }

    /**
     * Returns the current node to root.
     */
    public void toRoot(){
        while(!current.isRoot())
            toParent();
        currentPath.clear();
        currentPath.add("R");
    }

    public ArrayList<String> copyCurrentPath(){
        return (ArrayList<String>) currentPath.clone();
    }
    /**
     *
     * @return
     */
    public ArrayList<String> getCurrentPath(){
        return currentPath;

    }

    /**
     * PATH NAME EXCLUDING THE BASE NODE R.
     * @param path
     */
    public void toDir(String path){
        toRoot();
        String[] rAddress = formatRAddress(path);

        for(String address : rAddress) {
//            System.out.print("["+address+"]");
            currentPath.add(address);
            toChild(address);
        }

    }

    /**
     * Format address utility
     * @param path
     * @return - tring array
     */
    private String[] formatRAddress(String path){
        return path.split("/");
    }

    /**
     * Sets current to its parent.
     */
    public void toParent(){
        int index = currentPath.lastIndexOf(current.data);
        if(index > 0)
            currentPath.remove(index);
        current = current.getParent();

    }

    /**
     * sets current to the specified child node.
     * @param specifiedChild
     */
    public void toChild(String specifiedChild){
        current = current.getChild(specifiedChild);
        currentPath.add(specifiedChild);
    }

    /**
     * adds a child at the current's path.
     * @param child
     */
    public void addChild(String child){
        current.addChild(child);
    }

    /**
     * gets all children from current's path.
     * @return
     */
    public ArrayList<String> getChildren(){
        ArrayList<String> children = new ArrayList<String>();
        for(TreeNode<String> child : current.getChildren()){
            children.add(child.getData());
        }
        return children;
    }

    /**
     * checks if current has children with the given term.
     * @param searchTerm
     * @return
     */
    public boolean contains(String searchTerm){
        if(current.contains(searchTerm) != null)
            return true;
        return false;
    }

    /**
     * deletes the specified node, including its children.
     * @param rAddress
     * @return
     */
    public void del(String rAddress){
        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        toParent();
        String[] address = formatRAddress(rAddress);
        current.delChild(address[address.length - 1]);
        current = tmp;
        currentPath = tempPath;

    }

    /**
     * renames the node at the specified address.
     * @param rAddress - address at which you want to change to take effect.
     *                 NOTE, IT WILL CHANGE THE GIVEN ADDRESS'S NAME, Not its child.
     * @param newName -  new data field. has to be a string.
     */
    public void rename(String rAddress, String newName){
        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        current.setData(newName);
        current = tmp;
        currentPath = tempPath;

    }

    /**
     * adds a child at the given address.
     * @param rAddress
     * @param term
     */
    public void add(String rAddress, String term){
        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        current.addChild(term);
        current = tmp;
        currentPath = tempPath;

    }

    /**
     * returns the node at the given address.
     * @param rAddress
     * @return
     */
    public TreeNode<String> get(String rAddress){

        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        TreeNode<String> returnVar = current;
        current = tmp;
        currentPath = tempPath;
        return returnVar;

    }


    /**
     * recursive export.
     * @param node
     * @return
     */
    public String export(TreeNode<String> node){
        toRoot();
        if(node.isRoot()){
            String returnStatement = ""; //current.toString() + "\n";
            for(TreeNode<String> child : current.getChildren()){
                if(!child.isLeaf())
                    returnStatement += (child.toString() + "\n");
                returnStatement += export(child);
            }
            return returnStatement;
        }
        else if(!node.isLeaf()){
            String returnStatement = "";
            for(TreeNode<String> child : node.getChildren()){
                if(!child.isLeaf())
                    returnStatement += (lvlSpacing(child.getLevel() - 1)+ child.toString() + "\n");
                returnStatement += export(child);
            }
            return returnStatement;
        }
        else if(node.isLeaf()){
            String returnStatement = "";
            returnStatement += (lvlSpacing(node.getLevel() - 1)+ node.toString() + "\n");
            return returnStatement;
        }


        return "";
    }

    /**
     * utility for proper spacing on output.
     * @param level
     * @return
     */
    private String lvlSpacing(int level){
        return new String(new char[level]).replace("\0", "    ");
    }

    /**
     * saves the whole tree.
     */
    public void save(){
        toRoot();
        try{
            String name = "";
            name = "R.txt";
            if(!name.endsWith(".txt")){
                name += ".txt";
            }
            BufferedWriter out = new BufferedWriter( new FileWriter("./R/" + name) );
            out.write(export(current));
            out.close();
        } catch (Exception e){
            System.out.println("You suck at writing to files");
        }

    }


    /**
     * Pulls all the database info out of the text file.
     * @param f - file to load
     */
    public void populate(File f){
        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;
        String term = "";
        String preName;
        Scanner db;

        try{
            db = new Scanner(f);
        }catch (FileNotFoundException e) {
            System.out.println("Invalid database name.");
            return;
        }
        //TODO: Wtf why arent you sticking?

        while(db.hasNextLine()) {
            i = 0;
            String input = db.nextLine();

            while (input.charAt(i) == ' ') {
                i++;
            }
            preName = term;
            prevTabs = curTabs;
            curTabs = i / 4;
            term = input.trim();

            //Adding to the same level
            if(prevTabs == curTabs){
                addChild(term);
            }

            //Adding Child
            else if(prevTabs < curTabs){
                toChild(preName);
                addChild(term);
            }

            //going up
            else{
                for(i = prevTabs-curTabs; i>0; i--){
                    toParent();
                }
                addChild(term);
            }

        }
        db.close();
        toRoot();
    }

    /**
     * prints all children to the standard console. Not used for exports.
     */
    public void printChildren(){
        for(String data : getChildren()){
            System.out.print(data + "    ");
        }
        System.out.print("\n");
    }

}
