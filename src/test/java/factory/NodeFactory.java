package factory;

import pa.Node;

import static funct.Const.*;


/**
 * @author devinmcgloin
 * @version 10/3/15.
 */
public class NodeFactory {
    private Node n;

    public NodeFactory() {
    }

    public NodeFactory init(String title) {
        n = new Node(title);
        n = Node.add(n, NAME.toString(), title);
        n = Node.add(n, NOT_KEY.toString());
        n = Node.add(n, LOGICAL_P.toString());
        n = Node.add(n, LOGICAL_C.toString());
        return this;
    }

    public NodeFactory setFields(String key, String... values) {
        for (String value : values) {
            n = Node.add(n, key, value);
        }
        return this;
    }

    public NodeFactory setFields(String[] keys, String[] values) {
        if (keys.length == values.length) {
            for (int i = 0; i < keys.length; i++) {
                n = Node.add(n, keys[i], values[i]);
            }
        }
        return this;
    }

    public NodeFactory addLP(String lp) {
        n = Node.add(n, LOGICAL_P.toString(), lp);
        return this;
    }

    public NodeFactory addLC(String lp) {
        n = Node.add(n, LOGICAL_C.toString(), lp);
        return this;
    }

    public NodeFactory addName(String lp) {
        n = Node.add(n, NAME.toString(), lp);
        return this;
    }

    public Node build() {
        return n;
    }
}
