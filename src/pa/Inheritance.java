package pa;

import r.R;
import r.TreeNode;
import r.TreeNodeBase;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/3/15.
 * Inheritance is going to be needed to create new nodes that pull in the HAS qualities only.
 */
public class Inheritance {
    R currentR;


    public Inheritance(R r){
        currentR = r;
    }
    /**
     *
     * @param from - Node the current node is getting its has attributes from.
     */
    public void inherit(PABN to, PABN from){
        ArrayList<String> children;
        TreeNode tmp = from.getOrigin().getChild("^has");

        if(tmp != null){
            children = tmp.getChildrenString();
            for (String child : children)
                currentR.add(to.getOrigin().getAddress() + "^has/", child);
        }
    }
}
