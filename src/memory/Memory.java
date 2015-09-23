package memory;

import pa.Node;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/28/15.
 */
public class Memory implements Comparable<Memory> {

    private final double DECAY_CONSTANT = -.10;
    private Node node;
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
        final StringBuilder sb = new StringBuilder("Memory{");
        sb.append("DECAY_CONSTANT=").append(DECAY_CONSTANT);
        sb.append(", node=").append(node);
        sb.append(", decay=").append(decay);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }

    public boolean nodeEquals(Node n) {
        return Node.getTitle(node).equals(Node.getTitle(n));
    }

    public boolean equals(Memory m) {
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
