package pa;

import r.TreeNode;

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

    /**
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an NBN its final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
     *
     */
    public NBN add(String Key, String Val){

        TreeNode newNode = copyNode(TN);
        TreeNode key = newNode.getChild(Key);

        if(key == null){
            key = new TreeNode(Key);
            key.addChildBlind(new TreeNode(Val));
            int index = newNode.binarySearch(key.getTitle());
            newNode.insertChild(key, (index * -1) - 1);
        }else{
            key = copyNode(key);
            key.addChildBlind(new TreeNode(Val));
            newNode.removeChild(newNode.getChild(Key));
            int index = newNode.binarySearch(key.getTitle());
            newNode.insertChild(key, (index*-1)-1);
        }

        return new NBN(newNode);
    }

    public NBN rm(String Key){
        TreeNode newNode = copyNode(TN);
        newNode.removeChild(newNode.getChild(Key));
        return new NBN(newNode);

    }

    public NBN rm(String Key, String Val){
        TreeNode newNode = copyNode(TN);
        newNode.getChild(Key).removeChild(newNode.getChild(Key).getChild(Val));
        return new NBN(newNode);

    }

    /**
     * TODO this is not creating a new child. Modifying the original. DO NOT USE.
     * @param key
     * @param oldVal
     * @param newVal
     * @return
     */

    public NBN update(String key, String oldVal, String newVal){
        TreeNode newNode = copyNode(TN);
        TreeNode child = newNode.getChild(key);
        if(child != null) {
            TreeNode oldValue = child.getChild(oldVal);
            if (oldValue != null)
                oldValue.setTitle(newVal);
        }
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
            TreeNode key = newNode.getChild(keys.get(i));

            if(key == null){
                key = new TreeNode(keys.get(i));
                key.addChildBlind(new TreeNode(vals.get(i)));
                int index = newNode.binarySearch(key.getTitle());
                newNode.insertChild(key, (index * -1) - 1);
            }else{
                key = copyNode(key);
                key.addChildBlind(new TreeNode(vals.get(i)));
                newNode.removeChild(newNode.getChild(keys.get(i)));
                int index = newNode.binarySearch(key.getTitle());
                newNode.insertChild(key, (index*-1)-1);
            }
        }
        return new NBN(newNode);
    }

    public NBN batchRM(ArrayList<String> keys){
        TreeNode newNode = copyNode(TN);
        for(String key : keys) {
            newNode.removeChild(newNode.getChild(key));
        }
        return new NBN(newNode);
    }

    public NBN batchRM(ArrayList<String> keys, ArrayList<String> vals){
        TreeNode newNode = copyNode(TN);
        for (int i = 0; i < keys.size(); i++){
            newNode.getChild(keys.get(i)).removeChild(newNode.getChild(keys.get(i)).getChild(vals.get(i)));
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
            TreeNode child = newNode.getChild(keys.get(i));
            if(child != null) {
                TreeNode oldValue = child.getChild(oldVals.get(i));
                if(oldValue != null)
                    oldValue.setTitle(newVals.get(i));
            }
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

}
