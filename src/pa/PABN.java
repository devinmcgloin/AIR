package pa;

import r.TreeNode;

/**
 * Created by devinmcgloin on 6/16/15.
 * BN is never changed after instatiation. NEVER.
 */
public class PABN {

    TreeNode BN;

    public PABN( TreeNode n ){
        BN = n;
    }

    public boolean isFilter(String searchTerm){
        if(BN.getChild("^is").contains(searchTerm))
            return true;
        else
            return false;
    }

    public boolean hasFilter(String searchTerm){
        if(BN.getChild("^has").contains(searchTerm))
            return true;
        else
            return false;
    }

    /**
     * needs to be sent to LDATA
     * @return
     */
    public boolean hasValue(){

    }

    public boolean hasAdj(String searchTerm){
        if(BN.getChild("^adj").contains(searchTerm))
            return true;
        else
            return false;
    }

    public boolean canVerb(String searchTerm){
        if(BN.getChild("^v1").contains(searchTerm))
            return true;
        else
            return false;
    }

    public boolean hasVerb(String searchTerm){
        if(BN.getChild("^v2").contains(searchTerm))
            return true;
        else
            return false;
    }

}
