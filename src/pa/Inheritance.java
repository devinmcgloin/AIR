package pa;

import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/3/15.
 * Inheritance is going to be needed to create new nodes that pull in the HAS qualities only.
 */
public class Inheritance {

    /**
     * TODO: Implement recursive get children function in TreeNode.
     * TODO: Implement CD function inside TreeNode.
     * TODO: Implement combined check if node exists, and if so CD into that node.
     * @param from - node that to is inheriting from.
     * @param to - node that only has characteristics are copied to
     */
    public void inherit(TreeNode from, TreeNode to){
        if(from.containsImmediateChildWithName("has")){
            from.changeDir("has");
            ArrayList<TreeNode> children = from.getChildren();


        }
    }
}
