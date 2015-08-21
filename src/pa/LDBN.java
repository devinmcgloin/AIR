package pa;

import org.apache.log4j.Logger;
import r.TreeNode;
import util.Expression;
import util.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devinmcgloin on 6/17/15.
 */
public final class LDBN {

    static Logger logger = Logger.getLogger(LDBN.class);
    private final BaseNode TN;


    public LDBN(BaseNode TN) {
        this.TN = TN;
    }

    public LDBN(TreeNode TN) {
        this.TN = new BaseNode(TN);
    }
    //Used in adding more shit it needs to change.

    public LDBN(TreeNode TN, ArrayList<Record> record) {
        this.TN = new BaseNode(TN, record);
    }

    public BaseNode getBaseNode() {
        return TN;
    }

    /**
     * TODO Not sure if needed.
     * need to check this before you can just compare the way Im doing it now in LDATA.
     *
     * @return
     */
    public String getComp() {
        if (TN.get("^comparison").contains("ordered")) {
            return "ordered";
        } else if (TN.get("^comparison").contains("count")) {
            return "count";
        } else {
            //TODO more complex logic containers/Time etc. Dont know how to do yet.
            return "Nothing";
        }
    }


    /**
     * @param unitFrom
     * @param unitTo
     * @return
     */
    public String getConversion(String unitFrom, String unitTo) {
        List<String> conversions = TN.get("^conversions");

        for (String conversion : conversions) {
            String[] types = conversion.split("->");
            if (types[0].equals(unitFrom) && types[1].equals(unitTo)) {
                //assumes that there is only one conversion grouping and that it's in the operation postion.
                return TN.get("^conversions", conversion).get(0);
            }
        }
        return "Nothing";
    }

    /**
     * @return
     */
    public ArrayList<Expression> getValRanges() {
        ArrayList<String> children = TN.get("^value_ranges");
        if (children.size() == 0) {
            logger.error("LDBN: GetValRanges: No ranges.");
            return null;
        } else {
            ArrayList<Expression> expressions = new ArrayList<>();
            //assumes value is always in the operation position and that there is only one.
            for (String range : children) {
                String[] terms = range.trim().split(" ");
                // [ 12 - 324 ft ]
                if (range.startsWith("[") || range.startsWith("(")) {
                    if (terms.length == 6) {
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new Expression(TN.getTitle(), ">", terms[1], terms[4]));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new Expression(TN.getTitle(), ">=", terms[1], terms[4]));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new Expression(TN.getTitle(), "<", terms[3], terms[4]));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new Expression(TN.getTitle(), "<=", terms[3], terms[4]));
                        }
                    } else {
                        //Opening paren
                        if (terms[0].equals("(")) {
                            expressions.add(new Expression(TN.getTitle(), ">", terms[1], "count"));
                        } else if (terms[0].equals("[")) {
                            expressions.add(new Expression(TN.getTitle(), ">=", terms[1], "count"));
                        }
                        //closing paren
                        if (terms[5].equals(")")) {
                            expressions.add(new Expression(TN.getTitle(), "<", terms[3], "count"));
                        } else if (terms[5].equals("]")) {
                            expressions.add(new Expression(TN.getTitle(), "<=", terms[3], "count"));
                        }
                    }
                } else {
                    if (terms.length == 5)
                        expressions.add(new Expression(terms[0], terms[1], terms[2], terms[3]));
                    else
                        expressions.add(new Expression(terms[0], terms[1], terms[2], "count"));
                }
            }
            return expressions;
        }


    }

    /**
     * @return
     */
    public ArrayList<String> getUnits() {
        return TN.get("^unit");
    }

    public String getTitle() {
        return TN.getTitle();
    }

    public String toString() {
        return TN.toString();
    }

    public ArrayList<String> getKeys() {
        return TN.getKeys();
    }

    public ArrayList<String> get(String key) {
        return TN.get(key);
    }

    public ArrayList<String> get(String firstKey, String secondKey) {
        return TN.get(firstKey, secondKey);
    }

    public ArrayList<Record> getRecord() {
        return TN.getRecord();
    }

    public LDBN add(String key) {
        return new LDBN(TN.add(key));
    }

    public LDBN add(String key, String val) {
        return new LDBN(TN.add(key, val));
    }

    public LDBN rm(String key) {
        return new LDBN(TN.rm(key));
    }

    public LDBN rm(String key, String val) {
        return new LDBN(TN.rm(key, val));
    }

    public LDBN update(String key, String oldVal, String newVal) {
        return new LDBN(TN.update(key, oldVal, newVal));
    }


}
