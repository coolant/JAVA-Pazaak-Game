package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Board is a container class that houses the data for each player's play area during a set
 * It contains a list of cards that were played on the table, as well as a set score
 * which is recalculated after every card is played.
 */

public class Board {

    private List<Card> cards;
    private int score;

    /**
     * The constructor assures that the list is filled with empty slots
     */
    public Board() {
        cards = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            cards.add(null);
        }
    }

    /**
     * the placeCard() method determines whether a card is fit to be placed on the table
     * and then calculates the combined set score of all the cards on the table
     *
     * @param c The card that is to be placed on the table
     */
    void placeCard(Card c) {
        int counter;
        for (counter = 0; counter < 9; counter++) {
            if (cards.get(counter) == null) {
                cards.set(counter, c);
                break;
            }
        }

        if (c instanceof SimpleCard) {
            CardType type = c.getType();
            switch (type) {
                case POSITIVE:
                case POSITIVESWAPPABLE:
                    this.score += c.getValue();
                    break;
                case NEGATIVE:
                case NEGATIVESWAPPABLE:
                    this.score -= c.getValue();
            }
        } else { // No complex cards so else is when a boardcard is added
            this.score += c.getValue();
        }

        if (this.score < 0) {
            this.score = 0;
        }

        // if adding complex cards that change score depending on previous cards
        // be sure to iterate over the cards list and check how many instances of the matching
        // card occur and adjust the score correctly
    }

    /**
     * displayBoard() ensures the visual integrity of the table side with cards on it.
     * <p>
     * Empty card spots are marked by the 'E*0' card string
     *
     * @return returns an array of Strings for each card placed on the table in order
     */
    public String[] displayBoard() {
        String[] output = new String[9];
        for (int i = 0; i < 9; i++) {
            if (cards.get(i) == null)
                output[i] = "E*0";
            else
                output[i] = cards.get(i).toBoardString();
        }
        return output;
    }

    public int getSize() {
        return cards.size();
    }

    public Integer getScore() {
        return score;
    }

    /**
     * Helps reset a player's score
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Determines how many cards are already on the table
     *
     * @return Returns a positive integer between 0 and the maximum allowed card size (9)
     */
    public int getCardCount() {
        return 9 - ((int) this.cards.stream().filter(Objects::isNull).count());
    }

    @Override
    public String toString() {
        return "Board{" +
                "score=" + score +
                '}';
    }
}
