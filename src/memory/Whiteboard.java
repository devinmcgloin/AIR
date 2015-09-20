package memory;

import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by devinmcgloin on 8/26/15.
 * This will be where all items that the system is thinking about will reside. They hold memories which is a Node, plus a double which is updated with a decay function to ascertain relevance. Items are searched thu while ordered by relevance in order to give the most recently referenced result if you are searching by name.
 *
 * TODO add whiteboard delete so the whiteboard isn't flooded with ghost tree nodes that were looking at, but never changed.
 *
 * TODO add temp staging area for addnode, then ask to commit after each cycle.
 */
public class Whiteboard {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Whiteboard.class);
    private static ArrayList<Memory> workingMem = new ArrayList<>();
    private Whiteboard() {
    }

    public static void addNode(Node node) {
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

    public static void addNodeTime(Node node, double time) {
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

    public static void addNodes(ArrayList<Node> nodes) {
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
    public static Node search(String nodeName) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.titleEquals(nodeName)) {
                mem.setTime(0.0);
                return mem.getNode();
            }
        }
        //TODO may need to update this search system to accomodate blaze.
        Node n = PA.searchExactTitle(nodeName);
        if (n != null) {
            addNodeTime(n, 0.0);
            return n;
        } else {
            n = PA.searchName(nodeName).get(0);
            if (n != null) {
                addNodeTime(n, 0.0);
                return n;
            }
            return null;
        }
    }

    /**
     * TODO May want to create a node on failed search (Perhaps this is more applicable to search by name)
     * @param title
     * @return
     */
    public static Node searchByTitle(String title) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.titleEquals(title)) {
                mem.setTime(0.0);
                return mem.getNode();
            }
        }
        //TODO may need to update this search system to accomodate blaze.

        Node n = PA.searchExactTitle(title);
        if (n != null) {
            addNodeTime(n, 0.0);
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
            if (mem.getDecay() > .6) {
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

}
