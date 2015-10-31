package memory;

import executor.Pauser;
import pa.Node;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This is for short term intercycle memory. Memories that need to be kept longer than one cycle need to be added to the Whiteboard.
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

    public static void addNodes(ArrayList<Node> nodes) {
        nodes.forEach(Notepad::addNode);
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

        Node n = Whiteboard.searchByTitle(title);
        if (n != null)
            return n;

        return null;
    }

    public static ArrayList<Node> searchByTitles(ArrayList<String> titles) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (String title : titles) {
            nodes.add(searchByTitle(title));
        }
        return nodes.stream()
                .distinct()
                .filter(n -> n != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Node search(String name) {

        for (Node mem : workingNodes)
            if (Node.nameEquals(mem, name))
                return mem;

        Node n = Whiteboard.search(name);

        return n != null ? n : null;
    }

    public static Node createNode(String title) {
        if (searchByTitle(title) == null) {
            return new Node(title);
        } else {
            for (int i = 1; ; i++) {
                if (searchByTitle(title) == null) {
                    if (Pauser.trueFalse(String.format("Calling this %s", title + i)))
                        return new Node(title + i);
                }
            }
        }
    }

    public static boolean containsInMem(Node node) {
        for (Node n : workingNodes) {
            if (n.toString().equals(node.toString())) {
                return true;
            }
        }
        return false;
    }

    protected static void erasePage() {
        workingNodes.clear();
    }
}
