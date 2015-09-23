package pa;

import logic.LDATA;
import logic.SetLogic;
import org.apache.log4j.Logger;
import r.TreeNode;
import util.Record;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/17/15.
 * This class is basically locked.
 */
public class Node {

    static Logger logger = Logger.getLogger(Node.class);
    private final TreeNode TN;
    private final ArrayList<Record> record;


    public Node(TreeNode TN) {
        //Do a full copy on instantiation.
        this.TN = fullCopyNode(TN);
        this.record = new ArrayList<>();
    }

    //Used in adding more shit it needs to change.
    public Node(TreeNode TN, ArrayList<Record> record){
        this.TN = TN;
        this.record = record;
    }

    /**
     * For making a new node to think about and put back later.
     *
     * @param title
     */
    public Node(String title) {
        TN = new TreeNode(title);
        this.record = new ArrayList<>();
    }

    public static Node add(Node node, String key){ return node.add(key); }

    public static Node add(Node node, String key, String val ){
        if (!SetLogic.isValid(key, val) || !LDATA.isValid(key, val))
            return node.add(key, val);
        logger.warn("Value not verified, original node returned.");
        return node;
    }

    public static Node rm(Node node, String key){
        node = add(node,"^notKey", key);
        return node.rm(key);
    }

    public static Node rm(Node node, String key, String val ){
        return node.rm(key, val);
    }

//    public static ArrayList<Node> getLogicalParent(Node node) {
//        if(node == null){
//            return null;
//        }
//
//        ArrayList<Node> parents = new ArrayList<Node>();
//        ArrayList<String> tmp = node.getCarrot("^logicalParents");
//        if(tmp ==null)
//            return null;
//        for(String title: tmp){
//            Node foo = PA.getByExactTitle(title);
//            if(foo == null) {
//                logger.error("NOUN: Couldn't find node: " + title);
//                continue;
//            }
//            parents.add( foo );
//        }
//        return parents;
//    }

//    public static ArrayList<Node> getLogicalChildren(Node node){
//        if(node == null){
//            return null;
//        }
//
//        ArrayList<Node> children = new ArrayList<Node>();
//        ArrayList<String> tmp = node.getCarrot("^logicalChildren");
//        if(tmp ==null)
//            return null;
//        for(String title: tmp){
//            Node foo = PA.getByExactTitle(title);
//            if(foo == null) {
//                logger.error("NODE: Couldn't find node: " + title);
//                continue;
//            }
//            children.add(foo);
//        }
//        return children;
//    }

    /**
     *
     * @param node
     * @return
     */
    public static ArrayList<String> getName(Node node){
        return node.getCarrot("^name");
    }

    public static ArrayList<String> getKeys(Node node){
        return node.getKeys();
    }

    /**
     * @param node
     * @param key
     * @return
     */
    public static String get(Node node, String key) {
        ArrayList<String> t = node.getCarrot(key);

        if (t == null || t.isEmpty()) {
            logger.warn("Throwing a null.");
            return null;
        }
        return node.getCarrot(key).get(0);
    }

    public static String getStringRep(Node node) {
        return Node.get(node, "^stringRepresentation");
    }

    public static ArrayList<String> getCarrot(Node node, String key) {

        return node.getCarrot(key);
    }

    public static Node update(Node node, String key, String oldVal, String newVal){
        return node.update(key, oldVal, newVal);
    }

//    /**
//     * @param node
//     * @param key
//     * @return
//     */
//    public static boolean isP(Node node, String key){
//        for (String entry : getCarrot(node, "^logicalParent")) {
//            if(entry.equals(key))
//                return true;
//        }
//        return false;
//    }

    public static String getTitle(Node node) {
        return node.getTitle();
    }

    public static boolean titleEquals(Node nodeA, Node nodeB) {
        return getTitle(nodeA).equals(getTitle(nodeB));
    }

    public static boolean nameEquals(Node nodeA, String name) {
        for (String nameA : Node.getName(nodeA)) {
            if (nameA.equals(name))
                return true;

        }
        return false;
    }

    private String getTitle() {
        return TN.getTitle();
    }

    public String toString() {
        return TN.getTitle();
    }

    private ArrayList<String> getKeys() {
        return TN.getChildrenString();
    }


    private ArrayList<String> getName(){
        return getCarrot("^name");
    }

    private ArrayList<String> getCarrot(String Key) {
        TreeNode kid = TN.getChild(Key);
        if(kid==null) {
            logger.warn("Did not have the Key: "+Key);
            return null;
        }
        else
            return kid.getChildrenString();
    }

    public ArrayList<Record> getRecord(){
        return record;
    }

    /*
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an Node it's final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
     *
     */

    private Node add(String key){
        //First and foremost, we need a new root:
        TreeNode newNode = copyNode(TN);

        add(newNode, key);

        return new Node(newNode, copyRecordAddContent(this.record, new Record("add", key)));
    }

    /**
     * See private add method for implementation.
     * @param Key
     * @param Val
     * @return
     */
    private Node add(String Key, String Val) {

        //First and foremost, we need a new root:
        TreeNode newNode = copyNode(TN);

        add(newNode, Key, Val);
        return new Node(newNode, copyRecordAddContent(this.record, new Record("add", Key, Val)));
    }

    /**
     *
     * @param Key
     * @return
     */
    private Node rm(String Key) {
        TreeNode newNode = copyNode(TN);
        removeChild(newNode, Key);
        return new Node(newNode, copyRecordAddContent(this.record, new Record("rm", Key)));
    }

    /**
     * See private rm method for implementation.
     * @param Key
     * @param Val
     * @return
     */
    private Node rm(String Key, String Val) {
        TreeNode newNode = copyNode(TN);
        rm(newNode, Key, Val);
        return new Node(newNode, copyRecordAddContent(record, new Record("rm", Key, Val)));
    }

    /**
     * See private update method for implementation.
     * @param key
     * @param oldVal
     * @param newVal
     * @return
     */

    private Node update(String key, String oldVal, String newVal) {
        TreeNode newNode = copyNode(TN);

        update(newNode, key, oldVal, newVal);
        return new Node(newNode, copyRecordAddContent(record, new Record("update", key, oldVal, newVal)));
    }

    // Batch updates below

    /**
     * See private add method for implementation.
     * @param keys
     * @param vals
     * @return
     */
    private Node batchAdd(ArrayList<String> keys, ArrayList<String> vals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<Record> additions = new ArrayList<Record>();

        for (int i = 0; i < keys.size(); i++) {
            add(newNode, keys.get(i), vals.get(i));
            additions.add(new Record("add", keys.get(i), vals.get(i)));
        }

        return new Node(newNode, copyRecordAddContent(record, additions));
    }

    /**
     *
     * @param keys
     * @return
     */
    private Node batchRM(ArrayList<String> keys) {
        TreeNode newNode = copyNode(TN);
        ArrayList<Record> additions = new ArrayList<Record>();

        for (String key : keys) {
            removeChild(newNode, key);
            additions.add(new Record("rm", key));
        }
        return new Node(newNode, copyRecordAddContent(record, additions));
    }

    /**
     * See private rm method for implementation.
     * @param keys
     * @param vals
     * @return
     */
    private Node batchRM(ArrayList<String> keys, ArrayList<String> vals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<Record> additions = new ArrayList<Record>();


        for (int i = 0; i < keys.size(); i++) {
            rm(newNode, keys.get(i), vals.get(i));
            additions.add(new Record("rm", keys.get(i), vals.get(i)));

        }
        return new Node(newNode, copyRecordAddContent(record, additions));
    }

    /**
     * See private update method for implementation.
     *
     * @param keys
     * @param oldVals
     * @param newVals
     * @return
     */
    private Node batchUpdate(ArrayList<String> keys, ArrayList<String> oldVals, ArrayList<String> newVals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<Record> additions = new ArrayList<Record>();


        for (int i = 0; i < keys.size(); i++) {
            update(newNode, keys.get(i), oldVals.get(i), newVals.get(i));
            additions.add(new Record("update", keys.get(i), oldVals.get(i), newVals.get(i)));
        }
        return new Node(newNode);

    }

//    /*
//     * Copying nodes isnt as bad as I was thinking. You basically make a full copy of the root, and copy references to the keys while its in treenode form, you can then remove or add values. Removal works as you're only removing the reference from this new root, not the old one. Changes still have to be copied over into new nodes. The penaltity for small changes is minimal.
//     * CopyNode only works on one individual node. You operation do it to the root, then whatever nodes you're changing. May be best to put this in side TreeNode, its going to getCarrot called on a alot of levels.
//     */
//
//    /**
//     * NOTE: Does not copy parent references. Any copies have to be reattached to the baseNode.
//     * @param oldNode - node to be copied.
//     * @return a copy of the node passed in with the same child references
//     */
//    private TreeNode copyNode(TreeNode oldNode) {
//        //old code:
////        TreeNode newNode = new TreeNode(oldNode.getTitle());
////        newNode.setAddress(oldNode.getAddress());
////        for (TreeNode child : oldNode.getChildren()) {
////            newNode.addChildBlindWithNoAddressUpdate(child);   //Hm. THis was never intended for adding entire branches. Yeah, so the address here is what's actually getting fucked.
////            //There are two possible solutions. One is to add a method in Tree node: addChildBlindWithNoAddressUpdate(child)
////            //But since I REEAAAAAALLLLLLLLLYYYYYYY don't want to go all the way down and fuck around with having LITERAL references to the DB in here and thinking thru all that logic,
////            //I would MUCH much MUCH rather do a full copy of the node as soon as you getCarrot it.
////            //Actually i just tried that too, still no dice. Seriously. Devin, you're right, we shouldn't mess with the actual DB, cause then you getCarrot these logical errors that no one wants
////            //to fucking trace from A-Z. And it's not any line you can point to so it makes debugging harder since it's usually a few functions working together. I'm going to do a deep copy.
////            //Then, there should be no reason to copy again and again and again in here, it also allows us to think in a term of a cache system later.
////        }
////        return newNode;
//
//        TreeNode newNode = new TreeNode(oldNode.getTitle());
//
//        //Recursively copy the whole branch
//        newNode.setAddress(oldNode.getAddress());
//        for (TreeNode child : oldNode.getChildren()) {
//            TreeNode tmp = new TreeNode(child.getTitle());
////            tmp.setAddress(child.getAddress()); //No need to set this address. Check out how add child blind works
//            newNode.addChildBlind(tmp);
//        }
//        return newNode;
//
//    }

    private TreeNode copyNode(TreeNode oldNode){
        TreeNode newNode = new TreeNode(oldNode.getTitle());
        newNode.setAddress(oldNode.getAddress());
        //Add copy to children.
        for (TreeNode child : oldNode.getChildren()) {

            //No need to set this address. Look into addChildBlind.
            newNode.addChildBlindWithNoAddressUpdate(child);
        }
        return newNode;
    }


    private TreeNode fullCopyNode(TreeNode oldNode){
        //First, copy the root of the BN
        TreeNode newNode = new TreeNode(oldNode.getTitle());
        newNode.setAddress(oldNode.getAddress());

        //Recursively copy the whole branch
        for (TreeNode child : oldNode.getChildren()) {

            //Copy that child recursively so we can reach the bottom of the tree.
            TreeNode tmp = fullRecCopy(child);

            //No need to set this address. Look into addChildBlind.
            newNode.addChildBlindWithNoAddressUpdate(tmp);
        }
        return newNode;

    }

    private TreeNode fullRecCopy(TreeNode oldNode){
        TreeNode newNode = new TreeNode(oldNode.getTitle());
        newNode.setAddress(oldNode.getAddress());

        //Recursively copy the whole branch
        for (TreeNode child : oldNode.getChildren()) {

            //Copy that child recursively so we can reach the bottom of the tree.
            TreeNode tmp = fullRecCopy(child);

            //No need to set this address. Look into addChildBlind.
            newNode.addChildBlindWithNoAddressUpdate(tmp);
        }

        return newNode;
    }


    /**
     * Sinful state function. acts upon the node passed in.
     * @param node - Node you want to insert a value to
     * @param value - treeNode you want to insert
     */
    private void insertNode(TreeNode node, TreeNode value) {
        int index = node.binarySearch(value.getTitle());

        if (index >= 0) {
            logger.warn("Dimension: " + value.getTitle() + " already exists.\n");
            return;
        }

        if (index < 0) {
            node.insertChild(value, (index * -1) - 1);  //HANDLES ALL NAMING CONVENTION
        }
    }

    /**
     * Sinful state function.
     * @param node - node to remove the child from
     * @param child - string to indicate which child.
     */
    private void removeChild(TreeNode node, String child) { //Removes the child from the copied node.
        node.removeChild(node.getChild(child));
    }

    /**
     * Sinful state function.
     * @param newNode - node to be added to
     * @param Key - key to add. Will work if key already exists or not.
     */
    private void add(TreeNode newNode, String Key){
        //Root (newNode) is a copy. The any other changes must be copied up to the root.
        TreeNode key = newNode.getChild(Key);
        if (key == null) {
            //Create a new treeNode for the key, which obviously doesn't exist.
            key = new TreeNode(Key);
            insertNode(newNode, key); //UPDATES THE NAME.
        } else {
            //We already have the key...so don't do anything.
        }
    }

    /**
     * Sinful state function.
     * @param newNode - node to be added to
     * @param Key - key to add. Will work if key already exists or not.
     * @param Val - value to add to the key. Added to key before key joins new Node
     */
    private void add(TreeNode newNode, String Key, String Val) {
        //Root (newNode) is a copy. The any other changes must be copied up to the root.
        TreeNode key = newNode.getChild(Key);
        if (key == null) {
            //Create a new treeNode for the key, which obviously doesn't exist.
            key = new TreeNode(Key);
            insertNode(newNode, key); //UPDATES THE NAME.
            insertNode(key, new TreeNode(Val)); //UPDATES THE NAME. (which is okay since both didn't exist).

        } else {
            //Remove the old Key Node (child) since it exists.
            removeChild(newNode, Key); //Removes "key" from newNode, eliminating that whole branch.
            insertNode(newNode, key);
            insertNode(key, new TreeNode(Val));
        }
    }

    /**
     * Removes VALUE
     * Sinful state function.
     * @param newNode - Node to remove from
     * @param Key - Does not remove the key, but only the value that the key takes on. Keys can be removed using the public method rm.
     * @param Val - values to remove from the key node.
     */
    private void rm(TreeNode newNode, String Key, String Val) {
        TreeNode keyNode = copyNode(newNode.getChild(Key)); //Make a copy of the new keynode (whose value will be removed)
        removeChild(newNode, Key);  //remove the old keyNode from the new Node
        removeChild(keyNode, Val);  //remove the old value from the new keynode
        insertNode(newNode, keyNode);   //give the new node root the new keynode
    }

    /**
     * Sinful state function.
     * @param newNode - node to be updated.
     * @param key - key that is acted upon. Key value does not change, if key does not exist no values are changed.
     * @param oldVal - if old value does not exist no update is made
     * @param newVal -  values to replace old values.
     */
    private void update(TreeNode newNode, String key, String oldVal, String newVal) {
        TreeNode child = copyNode(newNode.getChild(key));   //copy of the key child

        if (child != null) {
            removeChild(newNode, key);
            TreeNode oldValue = child.getChild(oldVal);
            if (oldValue != null) {
                removeChild(child, oldVal);
                insertNode(child, new TreeNode(newVal));

            }
            insertNode(newNode, child);
        }

    }

    private ArrayList<Record> copyRecordAddContent(ArrayList<Record> record, Record addition){
        ArrayList<Record> newRecord = new ArrayList<Record>(); //I don't think you actually have to do this. I'll think it through later.
        for(Record entry : record){
            newRecord.add(entry);
        }
        newRecord.add(addition);
        return newRecord;
    }

    //Used in batch add?
    private ArrayList<Record> copyRecordAddContent(ArrayList<Record> record, ArrayList<Record> addition){
        ArrayList<Record> newRecord = new ArrayList<Record>();
        for(Record entry : record){
            newRecord.add(entry);
        }
        for(Record entry : addition){
            newRecord.add(entry);
        }
        return newRecord;
    }
}
