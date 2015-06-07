package r;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by devinmcgloin on 6/5/15.
 *
 * PACKAGE GOD.
 *
 * PATHS EXCLUDE BASE NODE R. are of the form "nouns/places/nations"
 * ALL FUNCTIONS EXCEPT THOSE THAT BEGIN WITH TO, RETURN TO THEIR ORGINAL rAddress.
 */
public class R {

    ArrayList<String> currentPath = new ArrayList<String>();
    ArrayList<String> tempPath = new ArrayList<String>();
    TreeNode<String> current;
    TreeNode<String> tmp = new TreeNode<String>("");

    public R() {
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
    public void toRoot() {
        while (!current.isRoot())
            toParent();
        currentPath.clear();
        currentPath.add("R");
    }

    public boolean isRoot(){
        return current.isRoot();
    }

    public boolean isLeaf(){
        return current.isLeaf();
    }

    /**
     * TODO: QA on isKeyVal method.
     * @return
     */
    public boolean isKeyVal(){
        List<TreeNode<String>> children = current.getChildren();
        if(children.size() == 1){
            for (TreeNode<String> child : children){
                if(child.isLeaf())
                    return true;
            }
            return false;
        }
        return false;

    }

    public ArrayList<String> copyCurrentPath() {
        return (ArrayList<String>) currentPath.clone();
    }

    /**
     * @return
     */
    public ArrayList<String> getCurrentPath() {
        return currentPath;

    }

    /**
     * PATH NAME EXCLUDING THE BASE NODE R.
     *
     * @param path
     */
    public void toDir(String path) {
        toRoot();
        String[] rAddress = formatRAddress(path);

        for (String address : rAddress) {
//            System.out.print("[" + address + "]");
            currentPath.add(address);
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
            int index = currentPath.lastIndexOf(current.data);
            if (index > 0)
                currentPath.remove(index);
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
        currentPath.add(specifiedChild);
    }

    /**
     * adds a child at the current's path.
     *
     * @param child
     */
    public void addChild(String child) {
        current.addChild(child);
    }

    /**
     * Here for move function. Dont use for anything else.
     * @param node
     */
    public void addChild(TreeNode<String> node){
        current.addChild(node);
    }

    /**
     * gets all children from current's path.
     *
     * @return
     */
    public ArrayList<String> getChildren() {
        ArrayList<String> children = new ArrayList<String>();
        for (TreeNode<String> child : current.getChildren()) {
            children.add(child.getData());
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
     * @param rAddress
     * @return
     */
    public void del(String rAddress) {
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
     *
     * renames the node at the specified address.
     *
     * @param rAddress - address at which you want to change to take effect.
     *                 NOTE, IT WILL CHANGE THE GIVEN ADDRESS'S NAME, Not its child.
     * @param newName  -  new data field. has to be a string.
     */
    public void rename(String rAddress, String newName) {
        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        current.setData(newName);
        current = tmp;
        currentPath = tempPath;

    }

    /**
     * adds a child at the given address.
     *
     * @param rAddress
     * @param term
     */
    public void add(String rAddress, String term) {
        tmp = current;
        tempPath = copyCurrentPath();
        toDir(rAddress);
        current.addChild(term);
        current = tmp;
        currentPath = tempPath;

    }

    /**
     * returns the node at the given address.
     *
     * @param rAddress
     * @return
     */
    public TreeNode<String> get(String rAddress) {

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
     *
     * @param node
     * @return
     */
    public StringBuilder export(TreeNode<String> node) {
        toRoot();
        if (node.isRoot()) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode<String> child : current.getChildren()) {
                if (!child.isLeaf())
                    returnStatement.append(child.toString() + "\n");
                returnStatement.append(export(child));
            }
            return returnStatement;
        } else if (!node.isLeaf()) {
            StringBuilder returnStatement = new StringBuilder("");
            for (TreeNode<String> child : node.getChildren()) {
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
        toRoot();
        try {
            String name = "";
            name = "R.txt";
            if (!name.endsWith(".txt")) {
                name += ".txt";
            }
            BufferedWriter out = new BufferedWriter(new FileWriter("./R/" + name));
            out.write(export(current).toString());
            out.close();
        } catch (Exception e) {
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
    public void populate(File f) {
        int i = 0;
        int curTabs = 0;
        int prevTabs = 0;
        String term = "";
        String preName;
        Scanner db;
        String input;

        try {
            db = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid database name.");
            return;
        }
        while (db.hasNextLine()) {
            i = 0;
            input = db.nextLine();

            while (input.charAt(i) == ' ') {
                i++;
            }
            preName = term;
            prevTabs = curTabs;
            curTabs = i / 4;
            term = input.trim();

            //Adding to the same level
            if (prevTabs == curTabs) {
                addChild(term);
            }

            //Adding Child
            else if (prevTabs < curTabs) {
                toChild(preName);
                addChild(term);
            }

            //going up
            else {
                for (i = prevTabs - curTabs; i > 0; i--) {
                    toParent();
                }
                addChild(term);
            }

        }
        db.close();
        toRoot();
    }

    /**
     * TODO: QA on move method
     * @param rAddressFrom
     * @param rAddressTo
     */
    public void move(String rAddressFrom, String rAddressTo){
        TreeNode<String> nodeA = get(rAddressFrom);
        toDir(rAddressFrom);
        del(rAddressFrom);
        toDir(rAddressTo);
        addChild(nodeA);

    }

    public int getLevel(){
        return current.getLevel();
    }



}
