package timeline;

import molecule.Molecule;

import java.util.Date;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class Event {
    final Molecule m;
    final Date timestamp;

    Event(Molecule molecule) {
        m = molecule;
        timestamp = new Date();
    }
}
