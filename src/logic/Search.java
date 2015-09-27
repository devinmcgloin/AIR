package logic;

import org.apache.log4j.Logger;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * Controls TreeNodeHash and is present in R.
 * <p>
 * CONSIDER the Following:
 * <p>
 * ------- BASIC SEARCH ----------
 * SEARCH FORM: "tree`leaves`green"
 * First word is the primary term.
 * Other words are secondary, with the 2nd having more weight than the valOne.
 * <p>
 * Steps:
 * locate operation node. in this case "tree"
 * Find distance of the 2nd term from "tree", remove nodes that are further than say 3 steps away.
 * Do the same for the valOne search and so on.
 * Always keep nodes that do not meet the all search terms, but push them to the end of the list.
 * <p>
 * ------- FUZZY SEARCH ----------
 * What happens when Basic search cant locate a node?
 * <p>
 * ------- SET SEARCH ----------
 * There will be situations in which a special search is needed, this is mostly for set logic.
 * <p>
 * First:
 * What are sets? And what kinds of things do we want from them?
 * Sets are
 * <p>
 * ------- KEY VAL SEARCH ----------
 * This type of search should only return key value pairs.
 * EG "How tall is the Empire State Building" etc etc ad infinitum.
 * <p>
 * Steps:
 * Goto Empire State Building node, hash search for tall or synonyms.
 * Pull out key value pair related to query.
 *
 * @author devinmcgloin
 * @version 6/6/15.
 *
 */
public final class Search {

    static Logger logger = Logger.getLogger(Search.class);


    public static ArrayList<String> search(Node node, String key) {
        return null;
    }

    public static String simpleSearch(Node node, String key) {
        //Have to make sure it accounts for overflow.

        if (node == null)
            return "^Null Node";

        if (key == null || key.equals("^N/A"))
            return "^No Key";   //if no key, try triggering overflowSearch and filtering on context.


        ArrayList<String> x = Node.getCarrot(node, key);
        if (x == null) {
            return "^No Value"; //if no value, try tracing up to logical parents to getCarrot a spread of likelihood.
            //TODO (best guess search)
        } else {
            return x.get(0);
        }

    }

    //Returns an arraylist of OFlowed Nodes that contain the key.
    public static ArrayList<Node> overflowSearch(Node node, String key) {
        //Couldn't find Value, must account for overflow.
        ArrayList<String> keys = Node.getKeys(node);
        ArrayList<String> tmpOF = new ArrayList<>();
        ArrayList<Node> OFlows = new ArrayList<>();

        //Look into all current keys for overflow nodes
        for (String k : keys) {
            if (k.startsWith("^"))
                continue;
            String value = simpleSearch(node, k);
            if (value.contains("+")) {
                tmpOF.add(value);
            }
        }

        //Search the overflown nodes for the key.
        for (String OFTitle : tmpOF) {
            Node tmp = PA.searchExactTitle(OFTitle);
            String val = simpleSearch(tmp, key);
            if (!val.startsWith("^")) {
                //Found a valid value for the key!
                OFlows.add(tmp);
            }
            if (val.equals("^No Value")) {
                //Key was still found and a success
                OFlows.add(tmp);
            }

            //Otherwise, try finding deeper levels!
            ArrayList<Node> otherSuccesses = overflowSearch(tmp, key);
            for (Node success : otherSuccesses) {
                OFlows.add(success);
            }
        }
        return OFlows;
    }
}
