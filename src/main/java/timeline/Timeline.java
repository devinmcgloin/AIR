package timeline;

import executor.MethodPerception;
import molecule.Molecule;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class Timeline {
    static MethodPerception methodPerception = new MethodPerception();

    private Timeline() {

    }

    public static void addEvent(Molecule molecule) {
        methodPerception.check(molecule);
        Event e = new Event(molecule);
    }

}
