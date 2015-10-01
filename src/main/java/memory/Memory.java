package memory;

import pa.Node;

import java.util.ArrayList;

/**
 * @author devinmcgloin
 * @version 8/28/15
 */
public class Memory implements Comparable<Memory> {

    private final double DECAY_CONSTANT = -.10;
    private final Node node;
    private double decay;
    private double time = 1;

    public Memory(Node node) {
        this.node = node;
        cycle();
    }

    public Node getNode() {
        return node;
    }

    public double getDecay() {
        return decay;
    }

    public void cycle() {
        decay = 1 * Math.pow(Math.E, DECAY_CONSTANT * time);
        time = time++;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int compareTo(Memory m) {
        if (decay == m.getDecay())
            return 0;
        else if (decay > m.getDecay())
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "Memory{" + "DECAY_CONSTANT=" + DECAY_CONSTANT + ", node=" + node + ", decay=" + decay + ", time=" + time + '}';
    }

    public boolean nodeEquals(Node n) {
        return Node.getTitle(node).equals(Node.getTitle(n));
    }

    public boolean titleEquals(Memory m) {
        return (Node.getTitle(node)).equals(Node.getTitle(m.getNode()));
    }

    public boolean nameEquals(String nodeName) {
        ArrayList<String> thisNames = Node.getName(node);
        for (String name : thisNames) {
            if (name.equals(nodeName))
                return true;
        }
        return false;
    }

    public boolean titleEquals(String title) {
        return Node.getTitle(node).equals(title);
    }
}
