package molecule;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Molecule molecule = (Molecule) o;

        return new EqualsBuilder()
                .append(subject, molecule.subject)
                .append(verb, molecule.verb)
                .append(object, molecule.object)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(subject)
                .append(verb)
                .append(object)
                .toHashCode();
    }

    public Node getSubject() {

        return subject;
    }

    public Node getVerb() {
        return verb;
    }

    public Node getObject() {
        return object;
    }
}
