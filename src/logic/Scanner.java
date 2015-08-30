package logic;

import memory.Whiteboard;
import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/25/15.
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
    public static void restructuring(Node LP, Node LC) {
        ArrayList<Node> LPAppearances = PA.generalSearch(LP.toString() + "`" + LC.toString());
        for (Node n : LPAppearances) {
            for (String firstKey : Node.getKeys(n)) {
                for (String secondKey : Node.getKeys(n)) {
                    if (!firstKey.equals(secondKey) && SetLogic.xISyP(Whiteboard.searchByTitle(firstKey), Whiteboard.searchByTitle(secondKey))) {
                        n = Node.rm(n, secondKey);
                        n = Node.add(n, firstKey, secondKey);
                    }
                }
            }
        }
    }
}
