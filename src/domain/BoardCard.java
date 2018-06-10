package domain;

/**
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class BoardCard implements Card {

    private int value;

    /**
     * Constructor to make a card to play.
     * it has a value.
     * 
     * @param value int with numeric value
     */
    BoardCard(int value) {
        this.value = value;
    }

    /**
     * Grabs the description of the BoardCard
     * 
     * @return String with description
     */
    @Override
    public String giveDescription() {
        return toString();
    }

    /**
     * Grab the type of the BoardCard. it's Board 
     * 
     * @return CardType.BOARD
     */
    @Override
    public CardType getType() {
        return CardType.BOARD;
    }

    /**
     * Grabs value of the BoardCard
     * 
     * @return int numeric value of the card
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * set the value with a numeric value
     * 
     * @param value int with numeric value
     */
    @Override
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Grabs information of the card
     * 
     * @return String with type;value
     */
    @Override
    public String toString() {
        return "B*" + getValue();
    }

    /**
     * replaces ; in toSting to *
     * 
     * @return String type*value
     */
    @Override
    public String toBoardString() {
        return toString();
    }
}
