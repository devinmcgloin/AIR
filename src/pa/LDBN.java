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
     * TODO Not sure if needed.
     * need to check this before you can just compare the way Im doing it now in LDATA.
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

    /**
     *
     * @param unitFrom
     * @param unitTo
     * @return
     */
    public String getConversion(String unitFrom, String unitTo){
        tmp = BN.getChild("^conversions");

        List<TreeNode> conversions = tmp.getChildren();

        for(TreeNode conversion : conversions){
            String [] types = conversion.getTitle().split("->");
            if(types[0].equals(unitFrom) && types[1].equals(unitTo)){
                //assumes that there is only one conversion grouping and that it's in the first postion.
                return conversion.getChildrenString().get(0);
            }
        }
        return "Nothing";
    }

    /**
     *
     * @return
     */
    public ArrayList<LDATA.Expression> getValRanges(){
        tmp = BN.getChild("^value_ranges");
        ArrayList<String> children = tmp.getChildrenString();
        if(children.size() == 0) {
            System.out.println("LDBN: GetValRanges: No ranges.");
            return null;
        }
        else{
            ArrayList<LDATA.Expression> expressions = new ArrayList<>();
            //assumes value is always in the first position and that there is only one.
            for(String range : children) {
                String[] terms = range.trim().split(" ");
                // [ 12 - 324 ft ]
                if(range.startsWith("[") || range.startsWith("(")) {
                    if(terms.length == 6) {
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), ">", terms[1], terms[4]));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), ">=", terms[1], terms[4]));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), "<", terms[3], terms[4]));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), "<=", terms[3], terms[4]));
                        }
                    }else{
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), ">", terms[1], "count"));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), ">=", terms[1], "count"));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), "<", terms[3], "count"));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new LDATA.Expression(BN.getTitle(), "<=", terms[3], "count"));
                        }
                    }
                }else{
                    if(terms.length == 5)
                        expressions.add(new LDATA.Expression(terms[0],terms[1],terms[2], terms[3] ));
                    else
                        expressions.add(new LDATA.Expression(terms[0],terms[1],terms[2], "count" ));
                }
            }
            return expressions;
        }


    }

    /**
     *
     * @return
     */
    public ArrayList<String> getUnits(){
        return BN.getChild("^unit").getChildrenString();
    }
}
