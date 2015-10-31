package molecule;

import pa.Node;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class Molecule {
    final Node subject;
    final Node verb;
    final Node object;

    public Molecule(final Node subject, final Node verb) {
        this.subject = subject;
        this.verb = verb;
        this.object = null;
    }

    public Molecule(final Node subject, final Node verb, final Node object) {
        this.subject = subject;
        this.verb = verb;
        this.object = object;
    }
}
