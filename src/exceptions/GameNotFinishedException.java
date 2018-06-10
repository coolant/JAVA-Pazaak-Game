package exceptions;

/**
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class GameNotFinishedException extends RuntimeException {
    /**
     * Constructs a GameNotFinishedException with no detail message.
     */
    public GameNotFinishedException() {
        this("ERROR_GAME_NOT_FINISHED");
    }

    /**
     * Constructs a GameNotFinishedException with the specified detail message.
     * 
     * @param message specified detail message.
     */
    public GameNotFinishedException(String message) {
        super(message);
    }
}
