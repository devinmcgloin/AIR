package pa;

import r.TreeNode;

import java.util.ArrayList;

/**
 * This class is never to be used outside of the static Noun class.
 */
public final class NBN {

    private final TreeNode TN;
    private final ArrayList<pa.Tuple> record;

    public NBN(TreeNode TN) {
        this.TN = TN;
        this.record = new ArrayList<>();
    }

    public NBN(TreeNode TN, ArrayList<pa.Tuple> record){
        this.TN = TN;
        this.record = record;
    }

    /**
     * For making a new node to think about and put back later.
     *
     * @param title
     */
    public NBN(String title) {
        TN = new TreeNode(title);
        this.record = new ArrayList<>();
    }

    public String getTitle() {
        return TN.getTitle();
    }

    public ArrayList<String> getKeys() {
        return TN.getChildrenString();
    }

    public ArrayList<String> get(String Key) {
        return TN.getChild(Key).getChildrenString();
    }

    public ArrayList<pa.Tuple> getRecord(){
        return record;
    }

    /*
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an NBN it's final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
     *
     */

    /**
     * See private add method for implementation.
     * @param Key
     * @param Val
     * @return
     */
    public NBN add(String Key, String Val) {

        TreeNode newNode = copyNode(TN);

        add(newNode, Key, Val);

        return new NBN(newNode, copyRecordAddContent(this.record, new pa.Tuple("add", Key, Val)));
    }

    /**
     *
     * @param Key
     * @return
     */
    public NBN rm(String Key) {
        TreeNode newNode = copyNode(TN);
        removeChild(newNode, Key);
        return new NBN(newNode, copyRecordAddContent(this.record, new pa.Tuple("rm", Key)));

    }

    /**
     * See private rm method for implementation.
     * @param Key
     * @param Val
     * @return
     */
    public NBN rm(String Key, String Val) {
        TreeNode newNode = copyNode(TN);
        rm(newNode, Key, Val);

        return new NBN(newNode, copyRecordAddContent(record, new pa.Tuple("rm", Key, Val)));

    }

    /**
     * See private update method for implementation.
     * @param key
     * @param oldVal
     * @param newVal
     * @return
     */

    public NBN update(String key, String oldVal, String newVal) {
        TreeNode newNode = copyNode(TN);

        update(newNode, key, oldVal, newVal);

        return new NBN(newNode, copyRecordAddContent(record, new pa.Tuple("update", key, oldVal, newVal)));
    }

    // Batch updates below

    /**
     * See private add method for implementation.
     * @param keys
     * @param vals
     * @return
     */
    public NBN batchAdd(ArrayList<String> keys, ArrayList<String> vals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<pa.Tuple> additions = new ArrayList<pa.Tuple>();
        for (int i = 0; i < keys.size(); i++) {
            add(newNode, keys.get(i), vals.get(i));
            additions.add(new pa.Tuple("add", keys.get(i), vals.get(i)));
        }

        return new NBN(newNode, copyRecordAddContent(record, additions));
    }

    /**
     *
     * @param keys
     * @return
     */
    public NBN batchRM(ArrayList<String> keys) {
        TreeNode newNode = copyNode(TN);
        ArrayList<pa.Tuple> additions = new ArrayList<pa.Tuple>();

        for (String key : keys) {
            removeChild(newNode, key);
            additions.add(new pa.Tuple("rm", key));
        }
        return new NBN(newNode, copyRecordAddContent(record, additions));
    }

    /**
     * See private rm method for implementation.
     * @param keys
     * @param vals
     * @return
     */
    public NBN batchRM(ArrayList<String> keys, ArrayList<String> vals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<pa.Tuple> additions = new ArrayList<pa.Tuple>();

        for (int i = 0; i < keys.size(); i++) {
            rm(newNode, keys.get(i), vals.get(i));
            additions.add(new pa.Tuple("rm", keys.get(i), vals.get(i)));

        }
        return new NBN(newNode, copyRecordAddContent(record, additions));
    }

    /**
     * See private update method for implementation.
     *
     * @param keys
     * @param oldVals
     * @param newVals
     * @return
     */
    public NBN batchUpdate(ArrayList<String> keys, ArrayList<String> oldVals, ArrayList<String> newVals) {
        TreeNode newNode = copyNode(TN);
        ArrayList<pa.Tuple> additions = new ArrayList<pa.Tuple>();

        for (int i = 0; i < keys.size(); i++) {
            update(newNode, keys.get(i), oldVals.get(i), newVals.get(i));
            additions.add(new pa.Tuple("update", keys.get(i), oldVals.get(i), newVals.get(i)));
        }
        return new NBN(newNode);

    }

    /*
     * Copying nodes isnt as bad as I was thinking. You basically make a full copy of the root, and copy references to the keys while its in treenode form, you can then remove or add values. Removal works as you're only removing the reference from this new root, not the old one. Changes still have to be copied over into new nodes. The penaltity for small changes is minimal.
     * CopyNode only works on one individual node. You first do it to the root, then whatever nodes you're changing. May be best to put this in side TreeNode, its going to get called on a alot of levels.
     */

    /**
     * NOTE: Does not copy parent references. Any copies have to be reattached to the baseNode.
     * @param oldNode - node to be copied.
     * @return a copy of the node passed in with the same child references
     */
    private TreeNode copyNode(TreeNode oldNode) {
        //old code:
//        TreeNode newNode = new TreeNode(oldNode.getTitle());
//        newNode.setAddress(oldNode.getAddress());
//        for (TreeNode child : oldNode.getChildren()) {
//            newNode.addChildBlind(child);   //Hm. THis was never intended for adding entire branches.
//        }
//        return newNode;

        TreeNode newNode = new TreeNode(oldNode.getTitle());

        //Recursively copy the whole branch
        newNode.setAddress(oldNode.getAddress());
        for (TreeNode child : oldNode.getChildren()) {
            TreeNode tmp = new TreeNode(child.getTitle());
//            tmp.setAddress(child.getAddress()); //No need to set this address. Check out how add child blind works
            newNode.addChildBlind(tmp);
        }
        return newNode;

    }

//    private TreeNode copyTheRestToo(TreeNode old){
//
//        return
//    }


    /**
     * Sinful state function. acts upon the node passed in.
     * @param node - Node you want to insert a value to
     * @param value - treeNode you want to insert
     */
    private void insertNode(TreeNode node, TreeNode value) {
        int index = node.binarySearch(value.getTitle());

        if (index >= 0) {
            System.out.printf("Dimension: %s already exists.\n", value.getTitle());
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
    private void removeChild(TreeNode node, String child) { //Fine since it operates on the BN. Should check that.
        node.removeChild(node.getChild(child));
    }

    /**
     * Sinful state function.
     * @param newNode - node to be added to
     * @param Key - key to add. Will work if key already exists or not.
     * @param Val - value to add to the key. Added to key before key joins new Node
     */
    private void add(TreeNode newNode, String Key, String Val) {
        TreeNode key = newNode.getChild(Key);
        if (key == null) {
            //Create a new treeNode for the key, which obviously doesn't exist.
            key = new TreeNode(Key);
//              //Earlier code added the value first to the key. This would fuck up naming convention since we look at the parent's address.
            //So old code means that first we add a value to the key (making the value's address: "Key/Value" aka "^logicalChild/childNode" which GenTree would automatically change to "R/^logicalChild/childNode"
            //since it was built to prevent something like, oh i don't know, trying to create a new root. Which is what this code below is doing.
//            insertNode(key, new TreeNode(Val)); //Let me think this thru...no yeah, this doesn't work, might be behind Speed and ^logicalChild and ^name showing up as databases. Yup. I was right.
//            insertNode(newNode, key);
            insertNode(newNode, key);
            insertNode(key, new TreeNode(Val));

        } else {
            removeChild(newNode, Key);
            insertNode(key, new TreeNode(Val));
            insertNode(newNode, key);
        }
    }

    /**
     * Sinful state function.
     * @param newNode - Node to remove from
     * @param Key - Does not remove the key, but only the value that the key takes on. Keys can be removed using the public method rm.
     * @param Val - values to remove from the key node.
     */
    private void rm(TreeNode newNode, String Key, String Val) {
        TreeNode keyNode = copyNode(newNode.getChild(Key));     //this whole copying (withblindchild) might be what's fucking up the db
        removeChild(newNode, Key);  //what?
        removeChild(keyNode, Val);  //??
        insertNode(newNode, keyNode);   //???
    }

    /**
     * Sinful state function.
     * @param newNode - node to be updated.
     * @param key - key that is acted upon. Key value does not change, if key does not exist no values are changed.
     * @param oldVal - if old value does not exist no update is made
     * @param newVal -  values to replace old values.
     */
    private void update(TreeNode newNode, String key, String oldVal, String newVal) {
        TreeNode child = copyNode(newNode.getChild(key));

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

    private ArrayList<pa.Tuple> copyRecordAddContent(ArrayList<pa.Tuple> record, pa.Tuple addition){
        ArrayList<pa.Tuple> newRecord = new ArrayList<pa.Tuple>();
        for(pa.Tuple entry : record){
            newRecord.add(entry);
        }
        newRecord.add(addition);
        return newRecord;
    }

    private ArrayList<pa.Tuple> copyRecordAddContent(ArrayList<pa.Tuple> record, ArrayList<pa.Tuple> addition){
        ArrayList<pa.Tuple> newRecord = new ArrayList<pa.Tuple>();
        for(pa.Tuple entry : record){
            newRecord.add(entry);
        }
        for(pa.Tuple entry : addition){
            newRecord.add(entry);
        }
        return newRecord;
    }

}

