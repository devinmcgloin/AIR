package pa;

import r.TreeNode;
import r.TreeNodeBase;

/**
 * Created by devinmcgloin on 6/3/15.
 * Inheritance is going to be needed to create new nodes that pull in the HAS qualities only.
 */
public class Inheritance {

    /**
     * TODO: Implement recursive get children function in TreeNode.
     * TODO: Implement CD function inside TreeNode.
     * Adds only immidiate children, not recursive.
     * @param from - node that to is inheriting from.
     * @param to
     */
   public void inherit(TreeNode from, TreeNode to){
       if(from.contains("has"){
         from = from.getChild("has")
         for (String child: from.getChildrenString){
           to.addChild(child)
         }
       }
   }
}
