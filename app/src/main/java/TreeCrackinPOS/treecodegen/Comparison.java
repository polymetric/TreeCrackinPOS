package TreeCrackinPOS.treecodegen;

public enum Comparison {
    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    GREATER_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN_OR_EQUAL;

    public Comparison getInversion() {
        switch (this) {
            case EQUAL:
                return NOT_EQUAL;
            case NOT_EQUAL:
                return EQUAL;
            case LESS_THAN:
                return GREATER_THAN_OR_EQUAL;
            case GREATER_THAN:
                return LESS_THAN_OR_EQUAL;
            case LESS_THAN_OR_EQUAL:
                return GREATER_THAN;
            case GREATER_THAN_OR_EQUAL:
                return LESS_THAN;
        }
        return null;
    }

    public String toString() {
        switch (this) {
            case EQUAL:
                return "==";
            case NOT_EQUAL:
                return "!=";
            case LESS_THAN:
                return "<";
            case GREATER_THAN:
                return ">";
            case LESS_THAN_OR_EQUAL:
                return "<=";
            case GREATER_THAN_OR_EQUAL:
                return ">=";
        }
        return null;
    }
}