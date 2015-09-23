package memory;

import pa.Node;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 9/23/15
 */
public class ShortTermMemory {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ShortTermMemory.class);
    private static ArrayList<Node> workingNodes = new ArrayList<>();

    private ShortTermMemory() {
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
}
