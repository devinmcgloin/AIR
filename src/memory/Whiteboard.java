package memory;

import pa.Node;

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

    public static void addNodes(ArrayList<Node> nodes) {
        for (Node n : nodes) {
            addNode(n);
        }
    }

    public static Node search(String nodeName) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.nameEquals(nodeName)) {
                mem.setTime(1.0);
                return mem.getNode();
            }
        }
        return null;
    }

    public static Node searchByTitle(String title) {
        Collections.sort(workingMem);
        for (Memory mem : workingMem) {
            if (mem.titleEquals(title)) {
                mem.setTime(1.0);
                return mem.getNode();
            }
        }
        return null;
    }

    public static void cycle() {
        for (Memory mem : workingMem)
            mem.cycle();
    }

}
