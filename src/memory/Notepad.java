package memory;

import pa.Node;
import pa.PA;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 9/23/15
 */
public class Notepad {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Notepad.class);
    private static ArrayList<Node> workingNodes = new ArrayList<>();

    private Notepad() {
    }

    public static void addNode(Node node) {
        for (Node term : workingNodes) {
            if (Node.getTitle(node).equals(Node.getTitle(term))) {
                workingNodes.remove(term);
                workingNodes.add(node);
                return;
            }
        }
        workingNodes.add(node);
    }

    public static void delNode(Node node) {
        for (Node term : workingNodes) {
            if (Node.getTitle(node).equals(Node.getTitle(term))) {
                workingNodes.remove(term);
                return;
            }
        }
    }

    public static ArrayList<Node> getWorkingNodes() {
        return workingNodes;
    }

    public static Node searchByTitle(String title) {

        for (Node mem : workingNodes)
            if (Node.getTitle(mem).equals(title))
                return mem;


        Node n = PA.searchExactTitle(title);

        return n != null ? n : null;
    }
}
