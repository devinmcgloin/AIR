package nulp;

import molecule.Molecule;

/**
 * @author devinmcgloin
 * @version 10/1/15.
 */
public class NlpReader {
    private NlpReader() {
    }

    public static Molecule parse(String msg) {
        if (funct.Parser.isDefault(msg))
            return funct.Parser.parseDefault(msg);
        else
            return null;
    }
}
