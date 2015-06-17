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
    PA pa;


    public Inheritance(PA pa){
        this.pa = pa;
    }
    /**
     *
     * @param from - Node the current node is getting its has attributes from.
     */
    public void inherit(PABN to, PABN from){
        ArrayList<String> children;
        TreeNode tmp = from.getOrigin().getChild("^has");

        if(tmp != null && to.getOrigin().getChild("^has") != null){
            children = tmp.getChildrenString();
            for (String child : children)
                pa.add(child, to.getOrigin().getAddress() + "^has/");
        }else{
            pa.add("^has", to.getOrigin().getAddress());
            pa.add("^has", from.getOrigin().getAddress());
            inherit(to, from);
        }
    }
}
