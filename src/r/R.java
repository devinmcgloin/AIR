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
import r.TreeNode;

/**
 * Created by devinmcgloin on 6/5/15.
 * PATHS EXCLUDE BASE NODE R.
 * ALL FUNCTIONS EXCEPT THOSE THAT BEGIN WITH TO, RETURN TO THEIR ORGINAL rAddress.
 *
 */
public class R {

    TreeNode<String> current;
    TreeNode<String> tmp = current;

    public R(){
        current = new TreeNode<String>("R");
    }

    public void toRoot(){
        while(!current.isRoot())
            toParent();
    }

    /**
     * PATH NAME EXCLUDING THE BASE NODE R.
     * @param path
     */
    public void toDir(String path){
        toRoot();
        String[] rAddress = formatRAddress(path);
        for(String address : rAddress)
            toChild(address);

    }

    public String[] formatRAddress(String path){
        return path.split("/");
    }
    public void toParent(){
        current = current.getParent();
    }

    public void toChild(String specifiedChild){
        current = current.getChild(specifiedChild);
    }

    public void addChild(String child){
        current.addChild(child);
    }

    public ArrayList<String> getChildren(){
        ArrayList<String> children = new ArrayList<String>();
        for(TreeNode<String> child : current.getChildren()){
            children.add(child.getData());
        }
        return children;
    }

    public boolean contains(String searchTerm){
        if(current.contains(searchTerm) != null)
            return true;
        return false;
    }

//    public boolean del(String rAddress){
//
//    }

    public void rename(String rAddress, String newName){
        tmp = current;
        toDir(rAddress);
        current.setData(newName);
        current = tmp;

    }

    public void add(String rAddress, String term){
        tmp = current;
        toDir(rAddress);
        current.addChild(term);
        current = tmp;

    }

    public TreeNode<String> get(String rAddress){
        tmp = current;
        toDir(rAddress);
        TreeNode<String> returnVar = current;
        current = tmp;
        return returnVar;

    }

//    public ArrayList<TreeNode<String>> search(String terms){
//
//    }

    public void save(){

    }

    public void populate(){

    }

    public void printChildren(){
        for(String data : getChildren()){
            System.out.print(data + "    ");
        }
        System.out.print("\n");
    }


    public static void main(String[] args ){
        R r = new R();

        System.out.println(r.current.toString());
        r.addChild("has");
        r.addChild("is");
        r.printChildren();

        r.toChild("has");

        r.addChild("car");
        r.addChild("home");
        r.addChild("bag");
        r.addChild("dog");
        r.addChild("cat");

        r.printChildren();

        r.toChild("car");
        r.addChild("has");
        System.out.println(r.current.getParent().toString());
        r.toRoot();
        System.out.println(r.current.toString());
        r.printChildren();






//        System.out.println("INSTANTIATION TEST: ");
//        System.out.println("R" == current.toString());
//        current = current.getParent();
//
//        System.out.println("\nCHILDREN TEST: ");
//        current.addChild("chair");
//        current.addChild("desk");
//        current.addChild("stool");
//        current.printChildren();
//        System.out.println(current.contains("desk"));
//
//
//        System.out.println("\ntoChild TEST: ");
//        current = current.getChild("stool");
//        System.out.println("stool" == current.toString());
//
//        System.out.println("\ncontains TEST: ");
//        current.addChild("has");
//        System.out.println(current.contains("has"));
//
//        System.out.println("\ntoParent TEST: ");
//        current = current.getParent();
//        System.out.println(current.toString());
//        System.out.println("R" == current.toString());

    }
}
