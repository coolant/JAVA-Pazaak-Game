package exceptions;

/**
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class GameIsTiedException extends RuntimeException {
    /**
     * Constructs a GameIsTiedException with no detail message.
     */
    public GameIsTiedException() {
        this("ERROR_GAME_IS_TIED");
    }

    /**
     * Constructs a GameIsTiedException with the specified detail message.
     * @param error specified detail message.
     */
    public GameIsTiedException(String error) {
        super(error);
    }
}
