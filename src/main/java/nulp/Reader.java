package nulp;

import executor.Parser;
import molecule.Molecule;

/**
 * @author devinmcgloin
 * @version 10/1/15.
 */
public class Reader {
    private Reader() {
    }

    public static Molecule parse(String msg) {
        if (Parser.isDefault(msg))
            return Parser.parseDefault(msg);
        else
            return null;
    }
}

