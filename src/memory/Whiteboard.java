package memory;

import pa.Node;
import pa.PA;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by devinmcgloin on 8/26/15.
 */
public class Whiteboard {

    private static ArrayList<Memory> workingMem = new ArrayList<>();

    private Whiteboard() {
    }

    public static void addNode(Node node) {
        Memory mem = new Memory(node);
        for (Memory term : workingMem) {
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

    public static Node search(String nodeName) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.nameEquals(nodeName)) {
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
            addNodeTime(n, 0.0);
            return n;
        }
    }

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
        addNodeTime(n, 0.0);
        return n;
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
