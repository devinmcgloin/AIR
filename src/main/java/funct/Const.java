package funct;

/**
 * @author devinmcgloin
 * @version 10/2/15.
 */
public enum Const {
    STRING_REP("string representation"),
    NAME("^name"),
    LOGICAL_P("^logicalParents"),
    LOGICAL_C("^logicalChildren");

    private final String representation;

    Const(String representation) {
        this.representation = representation;
    }

    public static void main(String[] args) {
        Core.print(STRING_REP.toString());
    }

    @Override
    public String toString() {
        return getRepresentation();
    }

    private String getRepresentation() {
        return representation;
    }
}
