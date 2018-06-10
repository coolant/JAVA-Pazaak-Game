package exceptions;

/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class InvalidPlayerCountException extends RuntimeException {
    /**
     * Constructs a InvalidPlayerCountException with no detail message.
     */
    public InvalidPlayerCountException() {
        super("ERROR_NOT_ENOUGH_PLAYERS");
    }
}
