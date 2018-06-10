package domain;

/**
 * Enumeration containing literals for all possible card types
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public enum CardType {
    POSITIVE,
    NEGATIVE,
    SWAPPABLE,
    POSITIVESWAPPABLE,
    NEGATIVESWAPPABLE,
    EMPTY, BOARD;

    @Override
    public String toString() {
        switch (this) {
            case POSITIVE:
                return "+";
            case NEGATIVE:
                return "-";
            case SWAPPABLE:
                return "±";
            case POSITIVESWAPPABLE:
                return "+";
            case NEGATIVESWAPPABLE:
                return "-";
            case EMPTY:
                return "E";
            case BOARD:
                return "B";
            default:
                return "ERROR";
        }
    }
}
