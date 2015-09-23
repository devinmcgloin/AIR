package memory;

import funct.Core;
import funct.Formatter;
import funct.Pauser;
import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by devinmcgloin on 8/26/15.
 * This will be where all items that the system is thinking about will reside. They hold memories which is a Node, plus a double which is updated with a decay function to ascertain relevance. Items are searched thu while ordered by relevance in order to give the most recently referenced result if you are searching by name.
 *
 */
public class Whiteboard {

    private static final double PROMINENCE_THRESHOLD = .6;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Whiteboard.class);
    private static ArrayList<Memory> workingMem = new ArrayList<>();

    private Whiteboard() {
    }

    protected static void addNode(Node node) {
        Memory mem = new Memory(node);
        for (Memory term : workingMem) {
            logger.debug(Node.getTitle(term.getNode()));
            logger.debug(Node.getTitle(mem.getNode()));
            logger.debug(term.equals(mem));
            if (term.equals(mem)) {
                workingMem.remove(term);
                workingMem.add(mem);
                return;
            }
        }
        workingMem.add(mem);
    }

    protected static void addNodeTime(Node node, double time) {
        Memory mem = new Memory(node);
        mem.setTime(time);
        for (Memory term : workingMem) {
            if (term.equals(mem)) {
                workingMem.remove(term);
                workingMem.add(mem);
                return;
            }
        }
        workingMem.add(mem);
    }

    protected static void addNodes(ArrayList<Node> nodes) {
        for (Node n : nodes) {
            addNode(n);
        }
    }

    /**
     * TODO May want to create a node on failed search
     *
     * @param nodeName
     * @return
     */
    protected static Node search(String nodeName) {
        nodeName = nodeName.trim();
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.nameEquals(nodeName)) {
                return mem.getNode();
            }
        }

        ArrayList<Node> nodes = PA.searchName(nodeName);
        if (!nodes.isEmpty()) {
            Node n;
            if (nodes.size() == 1) {
                n = nodes.get(0);
            } else {
                int i = Pauser.whichOne(nodes);
                n = nodes.get(i);
            }
            if (n != null) {
                return n;
            }
        } else {
            logger.warn(String.format("Node %s not found.", nodeName));
            return null;
        }
        return null;
    }

    /**
     * TODO May want to create a node on failed search (Perhaps this is more applicable to search by name)
     * @param title
     * @return
     */
    protected static Node searchByTitle(String title) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.titleEquals(title)) {
                return mem.getNode();
            }
        }

        Node n = PA.searchExactTitle(title);
        if (n != null) {
            return n;
        } else {
            return null;
        }
    }

    public static void cycle() {
        for (Memory mem : workingMem)
            mem.cycle();
    }

    public static void putAll() {
        for (Memory mem : workingMem) {
            PA.put(mem.getNode());
        }
    }

    public static ArrayList<Node> getProminentNodes() {
        Collections.sort(workingMem);
        ArrayList<Node> prominentNodes = new ArrayList<>();
        for (Memory mem : workingMem) {
            if (mem.getDecay() > PROMINENCE_THRESHOLD) {
                prominentNodes.add(mem.getNode());
            } else {
                return prominentNodes;
            }
        }
        return prominentNodes;
    }

    public static void clearAll() {
        workingMem.clear();
    }

    public static void addAllNotepadNodes() {
        ArrayList<Node> nodes = Notepad.getWorkingNodes();
        for (Node n : nodes) {
            Core.println(Formatter.quickView(n));
        }
        boolean cont = Pauser.trueFalse("Can these be added to whiteboard? (t|f)");
        if (cont)
            addNodes(nodes);
    }

}
