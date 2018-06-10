package domain;

import java.util.List;

/**
 * Data triplet used in Game objects. This class couples information for a given player
 * Namely their game deck and their overall score
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class PlayerDataTriplet {

    // WAAROM BESTAAT DIT NIET IN JAVA???? New Triplet<F,S,T> ????????????????????????
    // JAVA PLS

    private final Player player;
    private List<Card> gameDeck;
    private int score;

    /**
     * Constructor to create a PlayerDataTriplet. It contains a name, gamedeck and score
     * 
     * @param player player who owns the class
     * @param gameDeck list of cards with contains the gameDeck
     * @param score int with the score of a player
     */
    public PlayerDataTriplet(Player player, List<Card> gameDeck, int score) {
        this.player = player;
        this.gameDeck = gameDeck;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDataTriplet that = (PlayerDataTriplet) o;

        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    /**
     * Grabs the player of the triplet
     * 
     * @return player of the triplet
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Grabs gameDeck of the Triplet
     * 
     * @return list of cards
     */
    public List<Card> getGameDeck() {
        return gameDeck;
    }

    /**
     * Set the gameDeck with a list of cards
     * 
     * @param gameDeck list of cards
     */
    public void setGameDeck(List<Card> gameDeck) {
        this.gameDeck = gameDeck;
    }

    /**
     * Grabs score of the Triplet
     * 
     * @return int with score of the triplet
     */
    public int getScore() {
        return score;
    }

    /**
     * check if the PlayerDataTriplet has a gameDeck
     * 
     * @return true when Triplet has gameDecks
     */
    public boolean hasGameDeck() {
        return gameDeck != null;
    }

    /**
     * Increment score with 1
     * 
     */
    public void incrementScore() {
        if (score < 3)
            this.score = this.score + 1;
    }

    /**
     * Grabs all the information of the Triplet
     * 
     * @return String with 'TRIPLET player, gameDeck, score'
     */
    @Override
    public String toString() {
        return "TRIPLET<" +
                "" + player +
                ", " + gameDeck +
                ", " + score +
                '>';
    }
}
