package funct;

/**
 * @author devinmcgloin
 * @version 10/2/15.
 */
public enum Const {
    STRING_REP("string representation"),
    NAME("^name"),
    LOGICAL_P("^logicalParents"),
    LOGICAL_C("^logicalChildren"),
    NOT_KEY("^notkey");

    private final String representation;

    Const(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return getRepresentation();
    }

    private String getRepresentation() {
        return representation;
    }
}
