package pa;

import r.TreeNode;

/**
 * Created by devinmcgloin on 6/16/15.
 * BN is never changed after instatiation. NEVER.
 *
 * PA should only be dealing with PABN.
 */
public class PABN {

    TreeNode BN;
    TreeNode tmp;

    public PABN( TreeNode n ){
        BN = n;
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
        TreeNode tmp = BN.getChild(dimension);
        if(tmp != null){
            return tmp.contains(searchTerm);
        }else
            return false;
    }

}
