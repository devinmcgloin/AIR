package memory;

import pa.Node;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/26/15.
 */
public class Whiteboard {

    private static ArrayList<Node> workingMem = new ArrayList<>();

    private Whiteboard() {
    }

    public static void addNode(Node node) {
        for (Node term : workingMem) {
            if (Node.getTitle(term).equals(Node.getTitle(node))) {
                int index = workingMem.indexOf(term);
                workingMem.remove(term);
                workingMem.add(index, node);
                return;
            }
        }
        workingMem.add(node);
    }

    public static void addNodes(ArrayList<Node> nodes) {
        for (Node n : nodes) {
            addNode(n);
        }
    }

}
