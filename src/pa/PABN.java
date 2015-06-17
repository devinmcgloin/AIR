package pa;

import r.R;
import r.TreeNode;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 6/16/15.
 * BN is never changed after instatiation. NEVER.
 *
 * PA should only be dealing with PABN.
 */
public class PABN {

    private TreeNode BN;
    private TreeNode tmp;
    private R currentR;

    public PABN( TreeNode n, R r ){
        BN = n;
        currentR = r;
    }

    public boolean isFilter(String searchTerm){
        return checkSecondDimension("^is", searchTerm);

    }

    public boolean hasFilter(String searchTerm){
        return checkSecondDimension("^has", searchTerm);
    }

    /**
     * needs to be sent to LDATA
     * @return
     */
    public boolean hasValue(){
        return false;
    }

    public boolean hasAdj(String searchTerm){
        return checkSecondDimension("^adj", searchTerm);
    }

    public boolean canVerb(String searchTerm){
        return checkSecondDimension("^v1", searchTerm);
    }

    public boolean hasVerb(String searchTerm){
        return checkSecondDimension("^v2", searchTerm);
    }

    private boolean checkSecondDimension(String dimension, String searchTerm){
        tmp = BN.getChild(dimension);
        if(tmp != null){
            return tmp.contains(searchTerm);
        }else
            return false;
    }

    /**
     * Node that the method is called on is the one that inherits from the parameter.
     * @param from - Node the current node is getting its has attributes from.
     */
    public void inherit(PABN from){
        ArrayList<String> children;
        tmp = from.BN.getChild("^has");

        if(tmp != null){
            children = tmp.getChildrenString();
            for (String child : children)
                currentR.add(BN.getAddress() + "^has/", child);
        }
    }

}
