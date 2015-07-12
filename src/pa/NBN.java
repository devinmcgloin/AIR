package pa;

import r.TreeNode;
import r.TreeNodeBase;

import java.util.ArrayList;

public class NBN{

    private final TreeNode TN;

    public NBN(TreeNode TN){
        this.TN = TN;
    }

    /**
     * For making a new node to think about.
     * @param title
     */
    public NBN(String title){
        TN = new TreeNode(title);
    }

    public String getName(){
        return TN.getTitle();
    }
    public ArrayList<String> getKeys(){
        return TN.getChildrenString();
    }

    public ArrayList<String> get(String Key){
        return TN.getChild(Key).getChildrenString();
    }

    /*
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an NBN its final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
     *
     */

    /**
     *
     * @param Key
     * @param Val
     * @return
     */
    public NBN add(String Key, String Val){

        TreeNode newNode = copyNode(TN);

        add(newNode, Key, Val);

        return new NBN(newNode);
    }

    /**
     *
     * @param Key
     * @return
     */
    public NBN rm(String Key){
        TreeNode newNode = copyNode(TN);
        removeChild(newNode, Key);
        return new NBN(newNode);

    }

    /**
     *
     * @param Key
     * @param Val
     * @return
     */
    public NBN rm(String Key, String Val){
        TreeNode newNode = copyNode(TN);
        rm(newNode, Key, Val);

        return new NBN(newNode);

    }

    /**
     *
     * @param key
     * @param oldVal
     * @param newVal
     * @return
     */

    public NBN update(String key, String oldVal, String newVal){
        TreeNode newNode = copyNode(TN);

        update(newNode, key, oldVal, newVal);

        return new NBN(newNode);
    }

    // Batch updates below

    /**
     *
     * @param keys
     * @param vals
     * @return
     */
    public NBN batchAdd(ArrayList<String > keys, ArrayList<String> vals){
        TreeNode newNode = copyNode(TN);
        for (int i = 0; i < keys.size(); i++){
            add(newNode, keys.get(i), vals.get(i));
        }
        return new NBN(newNode);
    }

    /**
     *
     * @param keys
     * @return
     */
    public NBN batchRM(ArrayList<String> keys){
        TreeNode newNode = copyNode(TN);
        for(String key : keys) {
            removeChild(newNode, key);
        }
        return new NBN(newNode);
    }

    /**
     *
     * @param keys
     * @param vals
     * @return
     */
    public NBN batchRM(ArrayList<String> keys, ArrayList<String> vals){
        TreeNode newNode = copyNode(TN);
        for (int i = 0; i < keys.size(); i++){
            rm(newNode, keys.get(i), vals.get(i));
        }
        return new NBN(newNode);
    }

    /**
     * TODO this is modifying the actual node. DO NOT USE.
     * @param keys
     * @param oldVals
     * @param newVals
     * @return
     */
    public NBN batchUpdate(ArrayList<String> keys, ArrayList<String> oldVals, ArrayList<String> newVals){
        TreeNode newNode = copyNode(TN);
        for (int i = 0; i < keys.size(); i++) {
            update(newNode, keys.get(i), oldVals.get(i), newVals.get(i));
        }
        return new NBN(newNode);

    }

    /**
     /* Copying nodes isnt as bad as I was thinking. You basically make a full copy of the root, and copy references to the keys while its in treenode form, you can then remove or add values. Removal works as you're only removing the reference from this new root, not the old one. Changes still have to be copied over into new nodes. The penaltity for small changes is minimal.
     /* CopyNode only works on one individual node. You first do it to the root, then whatever nodes you're changing. May be best to put this in side TreeNode, its going to get called on a alot of levels.
     */

    private TreeNode copyNode(TreeNode oldNode){
        TreeNode newNode = new TreeNode(oldNode.getTitle());
        newNode.setAddress(oldNode.getAddress());
        for(TreeNode child : oldNode.getChildren()){
            newNode.addChildBlind(child);
        }
        return newNode;
    }

    private void insertNode(TreeNode node, TreeNode value){
        int index = node.binarySearch(value.getTitle());
        if(index < 0) {
            node.insertChild(value, (index * -1) - 1);
        }
    }

    private void removeChild(TreeNode node, String child){
        node.removeChild(node.getChild(child));
    }

    private void add(TreeNode newNode, String Key, String Val){
        TreeNode key = newNode.getChild(Key);

        if(key == null){
            key = new TreeNode(Key);
            insertNode(key, new TreeNode(Val));
            insertNode(newNode, key);
        }else{
            removeChild(newNode, Key);
            insertNode(key, new TreeNode(Val));
            insertNode(newNode, key);
        }
    }

    private void rm (TreeNode newNode, String Key, String Val){
        TreeNode keyNode = copyNode(newNode.getChild(Key));
        removeChild(newNode, Key);
        removeChild(keyNode, Val);
        insertNode(newNode, keyNode);
    }

    private void update(TreeNode newNode, String key, String oldVal, String newVal){
        TreeNode child = copyNode(newNode.getChild(key));

        if(child != null) {
            removeChild(newNode, key);
            TreeNode oldValue = child.getChild(oldVal);
            if (oldValue != null) {
                removeChild(child, oldVal);
                insertNode(child, new TreeNode(newVal));

            }
            insertNode(newNode, child);
        }

    }

}
