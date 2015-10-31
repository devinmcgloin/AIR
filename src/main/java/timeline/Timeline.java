package timeline;

import executor.MethodPerceptron;
import molecule.Molecule;

/**
 * Need to be able to index the timeline by any relationship present in a molcule. This tends to make me think of a graph, so thats probably the implementation we're going for.
 * @author devinmcgloin
 * @version 10/31/15.
 */
public class Timeline {
    static MethodPerceptron methodPerception = new MethodPerceptron();

    private Timeline() {

    }

    public static void addEvent(Molecule molecule) {
        methodPerception.check(molecule);
        Event e = new Event(molecule);
    }

}
