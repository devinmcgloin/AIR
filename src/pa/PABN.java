package pa;

import r.R;
import r.TreeNode;
import pa.LDATA;

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
    private LDATA ldata = new LDATA();

    public PABN( TreeNode n ){
        BN = n;
    }

    public TreeNode getOrigin(){
        return BN;
    }

    public boolean isFilter(String searchTerm){
        return checkSecondDimension("^is", searchTerm);

    }

    public boolean hasFilter(String searchTerm){
        return checkSecondDimension("^has", searchTerm);
    }

    /**
     * TODO: Needs to be implemented in LDATA
     * needs to be sent to LDATA
     * @return
     */
    public boolean hasValue(String keyVal){
        return ldata.evaluate(keyVal, this);
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



}
