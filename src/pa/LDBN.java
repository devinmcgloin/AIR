package pa;

import r.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devinmcgloin on 6/17/15.
 */
public class LDBN {

    private TreeNode BN;
    private TreeNode tmp;

    public LDBN(TreeNode n){
        BN = n;
    }

    /**
     * TODO need to check this before you can just compare the way Im doing it now in LDATA.
     * @return
     */
    public String getComp(){
        if(BN.getChild("^comparison").contains("ordered")){
            return "ordered";
        }else if(BN.getChild("^comparison").contains("count")){
            return "count";
        }else{
            //TODO more complex logic containers/Time etc. Dont know how to do yet.
            return "Nothing";
        }
    }

    public String getConversion(String unitFrom, String unitTo){
        tmp = BN.getChild("^conversions");

        List<TreeNode> conversions = tmp.getChildren();

        for(TreeNode conversion : conversions){
            String [] types = conversion.getName().split("->");
            if(types[0].equals(unitFrom) && types[1].equals(unitTo)){
                //TODO assumes that there is only one conversion grouping and that it's in the first postion.
                return conversion.getChildrenString().get(0);
            }
        }
        return "Nothing";
    }

    public String getValRanges(){
        tmp = BN.getChild("^valRange");
        ArrayList<String> children = tmp.getChildrenString();
        if(children.size() ==0) {
            System.out.println("LDBN: GetValRanges: No ranges.");
            return "Nothing";
        }
        else if(children.size() == 1)
            return children.get(0); //TODO assumes value is always in the first position and that there is only one.
        else{
            System.out.println("LDBN: GetValRanges: To many ranges.");
            return "Too many Ranges";
        }

    }
}
