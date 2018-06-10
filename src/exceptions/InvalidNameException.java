package exceptions;


public class InvalidNameException extends RuntimeException {
    /**
     * Constructs a InvalidNameException with no detail message.
      * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
     */
    public InvalidNameException() {
        super("ERROR_NOPLAYERFORGIVENNAME");
    }

    /**
     * Constructs a GameIsTiedException with the specified detail message.
     * 
     * @param message the specified detail message
     */
    public InvalidNameException(String message) {
        super(message);
    }
}
