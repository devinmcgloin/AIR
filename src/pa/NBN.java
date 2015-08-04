package pa;

import r.TreeNode;

import java.util.ArrayList;

/**
 * This class is never to be used outside of the static Noun class.
 */
public final class NBN {

    private final TreeNode TN;
    private final ArrayList<Tuple> record;


    public NBN(TreeNode TN) {
        //Do a full copy on instantiation.
        this.TN = fullCopyNode(TN);
        this.record = new ArrayList<>();
    }

    //Used in adding more shit it needs to change.
    public NBN(TreeNode TN, ArrayList<Tuple> record){
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

    public ArrayList<Tuple> getRecord(){
        return record;
    }

    /*
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an NBN it's final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
     *
     */

    public NBN add(String key){
        //First and foremost, we need a new root:
        TreeNode newNode = copyNode(TN);

        add(newNode, key);

        return new NBN(newNode, copyRecordAddContent(this.record, new Tuple("add", key)));
    }

    /**
     * See private add method for implementation.
     * @param Key
     * @param Val
     * @return
     */
    public NBN add(String Key, String Val) {

        //First and foremost, we need a new root:
        TreeNode newNode = copyNode(TN);

        add(newNode, Key, Val);

        return new NBN(newNode, copyRecordAddContent(this.record, new Tuple("add", Key, Val)));
    }

    /**
     *
     * @param Key
     * @return
     */
    public NBN rm(String Key) {
        TreeNode newNode = copyNode(TN);
        removeChild(newNode, Key);
        return new NBN(newNode, copyRecordAddContent(this.record, new Tuple("rm", Key)));

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

        return new NBN(newNode, copyRecordAddContent(record, new Tuple("rm", Key, Val)));

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

        return new NBN(newNode, copyRecordAddContent(record, new Tuple("update", key, oldVal, newVal)));
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
        ArrayList<Tuple> additions = new ArrayList<Tuple>();
        for (int i = 0; i < keys.size(); i++) {
            add(newNode, keys.get(i), vals.get(i));
            additions.add(new Tuple("add", keys.get(i), vals.get(i)));
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
        ArrayList<Tuple> additions = new ArrayList<Tuple>();

        for (String key : keys) {
            removeChild(newNode, key);
            additions.add(new Tuple("rm", key));
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
        ArrayList<Tuple> additions = new ArrayList<Tuple>();

        for (int i = 0; i < keys.size(); i++) {
            rm(newNode, keys.get(i), vals.get(i));
            additions.add(new Tuple("rm", keys.get(i), vals.get(i)));

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
        ArrayList<Tuple> additions = new ArrayList<Tuple>();

        for (int i = 0; i < keys.size(); i++) {
            update(newNode, keys.get(i), oldVals.get(i), newVals.get(i));
            additions.add(new Tuple("update", keys.get(i), oldVals.get(i), newVals.get(i)));
        }
        return new NBN(newNode);

    }

//    /*
//     * Copying nodes isnt as bad as I was thinking. You basically make a full copy of the root, and copy references to the keys while its in treenode form, you can then remove or add values. Removal works as you're only removing the reference from this new root, not the old one. Changes still have to be copied over into new nodes. The penaltity for small changes is minimal.
//     * CopyNode only works on one individual node. You first do it to the root, then whatever nodes you're changing. May be best to put this in side TreeNode, its going to get called on a alot of levels.
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
////            //I would MUCH much MUCH rather do a full copy of the node as soon as you get it.
////            //Actually i just tried that too, still no dice. Seriously. Devin, you're right, we shouldn't mess with the actual DB, cause then you get these logical errors that no one wants
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

    private ArrayList<Tuple> copyRecordAddContent(ArrayList<Tuple> record, Tuple addition){
        ArrayList<Tuple> newRecord = new ArrayList<Tuple>(); //I don't think you actually have to do this. I'll think it through later.
        for(Tuple entry : record){
            newRecord.add(entry);
        }
        newRecord.add(addition);
        return newRecord;
    }

    //Used in batch add?
    private ArrayList<Tuple> copyRecordAddContent(ArrayList<Tuple> record, ArrayList<Tuple> addition){
        ArrayList<Tuple> newRecord = new ArrayList<Tuple>();
        for(Tuple entry : record){
            newRecord.add(entry);
        }
        for(Tuple entry : addition){
            newRecord.add(entry);
        }
        return newRecord;
    }

    static class Tuple {
        final String first;
        final String second;
        final String third;
        final String forth;


        public Tuple(String first, String second, String third) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.forth = null;
        }
        public Tuple(String first, String second) {
            this.first = first;
            this.second = second;
            this.third = null;
            this.forth = null;
        }
        public Tuple(String first, String second, String third, String forth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.forth = forth;
        }

        public String fst(){
            return first != null ? first : "[first null]";
        }
        public String snd(){
            return second != null ? second : "[second null]";
        }
        public String thrd(){
            return third != null ? third : "[third null]";
        }
        public String frth(){
            return forth != null ? forth : "[forth null]";
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "first='" + first + '\'' +
                    ", second='" + second + '\'' +
                    ", third='" + third + '\'' +
                    ", forth='" + forth + '\'' +
                    '}';
        }
    }
}

