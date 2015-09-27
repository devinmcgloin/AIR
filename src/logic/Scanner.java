package logic;

import memory.Notepad;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 8/25/15
 */
public class Scanner {

    private Scanner() {
    }

    /**
     * any node that contain the logical parent, or the logical parent / logical child may need restructuring. Make sure LC is strctured under LP.
     * Has to go thru each key val pair to ensure that all the keys match up with proper values or are left blank.
     *
     * @param LP
     * @param LC
     */
    public static void restructure(Node LP, Node LC) {
        ArrayList<Node> LPAppearances = PA.generalSearch(LP.toString() + "`" + LC.toString());
        for (Node n : LPAppearances) {
            for (String firstKey : Node.getKeys(n)) {
                for (String secondKey : Node.getKeys(n)) {
                    if (!firstKey.equals(secondKey) && SetLogic.xISyP(Notepad.searchByTitle(firstKey), Notepad.searchByTitle(secondKey))) {
                        //TODO this will add it to ^notkey, is that alright?
                        n = Node.rm(n, secondKey);
                        n = Node.add(n, firstKey, secondKey);
                    }
                }
            }
        }
    }
}
