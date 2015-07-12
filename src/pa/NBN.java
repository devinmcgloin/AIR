package pa;

import r.TreeNode;

import java.util.ArrayList;

/**
 * This class is never to be used outside of the static Noun class.
 */
public class NBN {

    private final TreeNode TN;

    public NBN(TreeNode TN) {
        this.TN = TN;
    }

    /**
     * For making a new node to think about and put back later.
     *
     * @param title
     */
    public NBN(String title) {
        TN = new TreeNode(title);
    }

    public String getName() {
        return TN.getTitle();
    }

    public ArrayList<String> getKeys() {
        return TN.getChildrenString();
    }

    public ArrayList<String> get(String Key) {
        return TN.getChild(Key).getChildrenString();
    }

    /*
     * Tricky adding stuff below.
     * In order to make a change you have to make a new tree node, and copy content over. Once you turn it into an NBN its final. This means we have to have methods that are singular such as add or rm, and ones that apply these updates in batches.
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

        return new NBN(newNode);
    }

    /**
     *
     * @param Key
     * @return
     */
    public NBN rm(String Key) {
        TreeNode newNode = copyNode(TN);
        removeChild(newNode, Key);
        return new NBN(newNode);

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

        return new NBN(newNode);

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

        return new NBN(newNode);
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
        for (int i = 0; i < keys.size(); i++) {
            add(newNode, keys.get(i), vals.get(i));
        }
        return new NBN(newNode);
    }

    /**
     *
     * @param keys
     * @return
     */
    public NBN batchRM(ArrayList<String> keys) {
        TreeNode newNode = copyNode(TN);
        for (String key : keys) {
            removeChild(newNode, key);
        }
        return new NBN(newNode);
    }

    /**
     * See private rm method for implementation.
     * @param keys
     * @param vals
     * @return
     */
    public NBN batchRM(ArrayList<String> keys, ArrayList<String> vals) {
        TreeNode newNode = copyNode(TN);
        for (int i = 0; i < keys.size(); i++) {
            rm(newNode, keys.get(i), vals.get(i));
        }
        return new NBN(newNode);
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
        for (int i = 0; i < keys.size(); i++) {
            update(newNode, keys.get(i), oldVals.get(i), newVals.get(i));
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
        TreeNode newNode = new TreeNode(oldNode.getTitle());
        newNode.setAddress(oldNode.getAddress());
        for (TreeNode child : oldNode.getChildren()) {
            newNode.addChildBlind(child);
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
        if (index < 0) {
            node.insertChild(value, (index * -1) - 1);
        }
    }

    /**
     * Sinful state function.
     * @param node - node to remove the child from
     * @param child - string to indicate which child.
     */
    private void removeChild(TreeNode node, String child) {
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
            key = new TreeNode(Key);
            insertNode(key, new TreeNode(Val));
            insertNode(newNode, key);
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
        TreeNode keyNode = copyNode(newNode.getChild(Key));
        removeChild(newNode, Key);
        removeChild(keyNode, Val);
        insertNode(newNode, keyNode);
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

}
