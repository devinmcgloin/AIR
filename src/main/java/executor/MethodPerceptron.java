package executor;

import molecule.Molecule;
import pa.Node;

import java.util.HashSet;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class MethodPerceptron {
    HashSet<String> actionableVerbs = new HashSet<>();

    public MethodPerceptron() {
        actionableVerbs.add("has");
        actionableVerbs.add("is");
    }

    public void check(Molecule m) {
        if (canAct(m))
            act(m);
    }

    private boolean canAct(Molecule m) {
        return Node.getName(m.getVerb()).stream()
                .anyMatch(s -> actionableVerbs.contains(s));
    }

    private void act(Molecule m) {
        Invoker.doThis(m);
    }
}
